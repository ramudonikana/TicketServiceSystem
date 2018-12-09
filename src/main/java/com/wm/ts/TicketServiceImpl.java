package com.wm.ts;

import java.util.*;
import java.util.UUID;


public class TicketServiceImpl implements TicketService{

    private Venue venue;
    private Map<Long, SeatHold> timeStampToSeatHoldMap;
    private Set<Integer> seatHoldIdSet;
    private Map<Integer, SeatHold> holdIdToSeatHoldMap;
    private Set<String> confirmationCodeStringSet;

    /**
     * constructor for TheatreTicketService.
     */
    public TicketServiceImpl() {
        venue = new Venue();
        timeStampToSeatHoldMap = new TreeMap<>();   //keeps record of all the timestamps being generated.
        seatHoldIdSet = new HashSet<>();
        holdIdToSeatHoldMap = new HashMap<>();
        confirmationCodeStringSet = new HashSet<>();
    }


    /**
     * @return returns the number of seats available based on the level entered,
     *         if no level is entered, returns the total number of seats available in the theatre.
     *         if undefined level is passed, the method returns '-1' indicating invalid operation.
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return
     */
    public synchronized int numSeatsAvailable(Optional<Integer> venueLevel) {

        if (venueLevel.isPresent()) {
            if((venueLevel.get() < VenueLevel.values()[0].getLevel()) || (venueLevel.get() > VenueLevel.values()[VenueLevel.values().length - 1].getLevel())){
                return -1;                      //invalid operation
            }
            int venueLevelIndex = venueLevel.get()-1;
            VenueLevel venueLevel1 = VenueLevel.values()[venueLevelIndex];
            return venueLevel1.getAvailableSeats();
        }

        return this.getAvailableSeats(venueLevel);
    }


    /**
     * This method holds seats temporarily for a given threshold time before which they can be reserved.
     * @param numSeats the number of seats to find and hold
     * @param minLevel the minimum venue level
     * @param maxLevel the maximum venue level
     * @param customerEmail unique identifier for the customer
     * @return
     */
    public synchronized SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {

        if(minLevel == null || maxLevel == null || numSeats <0 || customerEmail == ""){
            if(customerEmail == "")
                System.out.println("Please enter a valid email. No seats were reserved.");
            if (numSeats < 0)
                System.out.println("Please enter a positive seat number. No seats were reserved.");
            return null;
        }

        removeOldHolds();

        Optional<Integer> valueOptional = Optional.ofNullable(null);
        if (numSeats > this.numSeatsAvailable(valueOptional)) {
            return null;
        }

        Integer venueLevelMin = minLevel.orElse(VenueLevel.values()[0].getLevel());
        Integer venueLevelMax = maxLevel.orElse(VenueLevel.values()[VenueLevel.values().length - 1].getLevel());

        //swapping max and min levels if max level is smaller than min level.
        if(venueLevelMax < venueLevelMin){
            Integer temp = venueLevelMin;
            venueLevelMin = venueLevelMax;
            venueLevelMax = temp;
        }

        SeatHold seatHold = holdSeats(numSeats, venueLevelMin, venueLevelMax, customerEmail);

        if(seatHold != null) {
            timeStampToSeatHoldMap.put(seatHold.getTimeOfHold(), seatHold);
            Set<SeatLevelWrapper> set = seatHold.getHoldSet();
        }

        return seatHold;

    }

    /**
     * This helper method removes the existing holds based on the threshold value.
     */
    private void removeOldHolds() {

        for (Long t : timeStampToSeatHoldMap.keySet()) {
            if ((System.currentTimeMillis() - t) > 30000) {
                SeatHold releaseSeatHold = timeStampToSeatHoldMap.get(t);
                Set<SeatLevelWrapper> releaseSet = releaseSeatHold.getHoldSet();
                for (SeatLevelWrapper seatLevelWrapper : releaseSet) {
                    VenueLevel venueLevel = seatLevelWrapper.getVenueLevel();
                    Seat seat = seatLevelWrapper.getSeat();
                    venueLevel.releaseSeat(seat);
                }
                timeStampToSeatHoldMap.remove(t);
                holdIdToSeatHoldMap.remove(releaseSeatHold.getHoldId());
            }
            else
                break;
        }

    }

    /**
     * It is a helper function helping findAndHoldSeats in putting holds onto the seats.
     * @param numSeats number of seats to be held.
     * @param minLevel minimum level for reserving seats
     * @param maxLevel max level for reserving the seats
     * @param customerEmail customer email provided.
     * @return
     */
    private SeatHold holdSeats(int numSeats, Integer minLevel, Integer maxLevel, String customerEmail) {

        int totalPossibleSeats = venue.maxSeatsBetweenTwoLevels(VenueLevel.values()[minLevel-1], VenueLevel.values()[maxLevel-1]);
        Set<SeatLevelWrapper> levelSeats = null;
        Set<SeatLevelWrapper> heldSeats = new HashSet<>();
        if (totalPossibleSeats < numSeats) {
            System.out.println("Not enough seats in the levels");
        } else {

            Map<Integer, VenueLevel> levelToVenueMap = venue.getLevelToVenueLevelHashMap();
            for (Integer level : levelToVenueMap.keySet()) {
                if (numSeats == 0) {
                    break;
                }
                if ((level >= minLevel) && level <= maxLevel) {
                    VenueLevel currentLevel = levelToVenueMap.get(level);
                    if (currentLevel.getAvailableSeats() > 0) {
                        levelSeats = currentLevel.allocateSeats(numSeats);
                        numSeats = numSeats - levelSeats.size();
                    }
                    if (levelSeats != null) {
                        for (SeatLevelWrapper seatLevelWrapper : levelSeats) {
                            heldSeats.add(seatLevelWrapper);
                        }
                    }
                }

            }
        }
        Long time = System.currentTimeMillis();

        int holdId = generateHoldId();
        seatHoldIdSet.add(holdId);

        if (heldSeats.size() != 0) {
            SeatHold seatHold = new SeatHold(heldSeats, holdId, time, customerEmail);
            holdIdToSeatHoldMap.put(holdId, seatHold);
            return seatHold;
        }
        return null;
    }


    /**
     * This method reserves the seats which have to be on hold.
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold is assigned
     * @return
     */
    public synchronized String reserveSeats(int seatHoldId, String customerEmail) {

        Integer seatHoldIdInteger = new Integer(seatHoldId);

        if(seatHoldIdInteger == null)
            return null;

        SeatHold seatHold = holdIdToSeatHoldMap.get(seatHoldId);

        if(seatHold != null){
            Set<SeatLevelWrapper> heldSeatsWrapper = seatHold.getHoldSet();
            for (SeatLevelWrapper seatLevelWrapper: heldSeatsWrapper){
                VenueLevel venueLevel = seatLevelWrapper.getVenueLevel();
                Seat seat = seatLevelWrapper.getSeat();
                seat.reserveSeat();
                //venueLevel.setAvailableSeats(venueLevel.getAvailableSeats()-1);
            }
            //removing the hold from the map so that it does not get eliminated later.
            timeStampToSeatHoldMap.remove(seatHold.getTimeOfHold());
            seatHoldIdSet.remove(seatHold.getHoldId());
        }
        else {
            return null;
        }

        //generating confirmation code here.
        return generateConfirmationCode();

    }

    /**
     * this generates a random hold Id created for a SeatHold.
     * @return random hold Id created for a SeatHold
     */
    public  int generateHoldId() {

        Random random = new Random();
        Integer holdId = random.nextInt((Integer.MAX_VALUE));
        while (seatHoldIdSet.contains(holdId)) {
            holdId = random.nextInt((Integer.MAX_VALUE));
        }
        return holdId;
    }

    /**
     * This method generates a reservation confirmation code for the customer
     *
     * @return Sring which is the confirmation code for reservation.
     */
    private String generateConfirmationCode() {

        String confirmationCode = "";
        while(confirmationCodeStringSet.contains(confirmationCode) || confirmationCode.equals("")){
            confirmationCode = UUID.randomUUID().toString();
        }
        confirmationCodeStringSet.add(confirmationCode);
        System.out.println("The confirmation code "+confirmationCode);
        return confirmationCode;

    }


    /**
     * gives the number of available seats in a particular level.
     * @param level the level to look for available seats in.
     * @return number of available seats in the level
     */
    private int getAvailableSeats(Optional<Integer> level) {
        int totalSeats = 0;
        Map<Integer, VenueLevel> hashMap = venue.getLevelToVenueLevelHashMap();
        for (Integer levelNumber : hashMap.keySet()) {
            totalSeats = totalSeats + hashMap.get(levelNumber).getAvailableSeats();
        }
        return totalSeats;
    }


    /**
     * ouputs the number of seats available throughout the venue in respective levels
     * Format - "level ---> number of seats available"
     */
    public void displaySeatsInAllLevels(){
        Map<Integer, VenueLevel> hashMap = venue.getLevelToVenueLevelHashMap();
        for (Integer level : hashMap.keySet()) {
            System.out.println(hashMap.get(level).getLevelName()+ " ----> "+hashMap.get(level).getAvailableSeats());
        }
    }

    /**
     * getter for venue
     * @return returns the venue
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     * sets the venue object
     * @param venue venue
     */
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    /**
     * returns the map of timestamp and corresponding seatHold
     * @return Map of timestamp and corresponding seatHold
     */
    public Map<Long, SeatHold> getTimeStampToSeatHoldMap() {
        return timeStampToSeatHoldMap;
    }


    /**
     * setter for setTimeStampToSeatHoldMap
     * @param timeStampToSeatHoldMap new setTimeStampToSeatHoldMap
     */
    public void setTimeStampToSeatHoldMap(Map<Long, SeatHold> timeStampToSeatHoldMap) {
        this.timeStampToSeatHoldMap = timeStampToSeatHoldMap;
    }

    /**
     * returns the set of HoldId's that have been assigned
     * @return set of HoldId's that have been assigned
     */
    public Set<Integer> getSeatHoldIdSet() {
        return seatHoldIdSet;
    }


    /**
     * setter for SeatHoldIdSet
     * @param seatHoldIdSet new value of seatHoldIdSet
     */
    public void setSeatHoldIdSet(Set<Integer> seatHoldIdSet) {
        this.seatHoldIdSet = seatHoldIdSet;
    }

    /**
     * getter for holdIdToSeatHoldMap
     * @return holdIdToSeatHoldMap
     */
    public Map<Integer, SeatHold> getHoldIdToSeatHoldMap() {
        return holdIdToSeatHoldMap;
    }

    /**
     * setter for holdIdToSeatHoldMap
     * @param holdIdToSeatHoldMap holdIdToSeatHoldMap
     */
    public void setHoldIdToSeatHoldMap(Map<Integer, SeatHold> holdIdToSeatHoldMap) {
        this.holdIdToSeatHoldMap = holdIdToSeatHoldMap;
    }

    /**
     * getter for confirmationCodeStringSet
     * @return confirmationCodeStringSet
     */
    public Set<String> getConfirmationCodeStringSet() {
        return confirmationCodeStringSet;
    }


    /**
     * setter for confirmationCodeStringSet
     * @param confirmationCodeStringSet
     */
    public void setConfirmationCodeStringSet(Set<String> confirmationCodeStringSet) {
        this.confirmationCodeStringSet = confirmationCodeStringSet;
    }

}