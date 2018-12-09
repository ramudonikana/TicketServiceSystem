package com.wm.ts;

import java.util.HashMap;
import java.util.Map;


public class Venue {

    private Map<Integer, VenueLevel> venue = new HashMap<>();

    /**
     * constrcutor to initialize the venue.
     */
    public Venue() {

        for(VenueLevel venueLevel: VenueLevel.values()){
            venue.put(venueLevel.getLevel(), venueLevel);
        }

    }


    /**
     * getter for Level to VenueLevel map
     * @return returns the map
     */
    public Map<Integer, VenueLevel> getLevelToVenueLevelHashMap() {
        return venue;
    }


    /**
     * Finds the number of seats available between two levels at the venue.
     * @param minLevel minLevel for checking the availability
     * @param maxLevel maxLevel for checking the availability
     * @return number of seats available between minLevel and maxLevel
     */
    public int maxSeatsBetweenTwoLevels(VenueLevel minLevel, VenueLevel maxLevel){
        Map<Integer, VenueLevel> hashMap = this.getLevelToVenueLevelHashMap();
        int totalPossibleSeats = 0;
        for (Integer level : hashMap.keySet()) {
            if ((level >= minLevel.getLevel()) && level <= maxLevel.getLevel()) {
                totalPossibleSeats += hashMap.get(level).getAvailableSeats();
            }
        }
        return totalPossibleSeats;
    }


}
