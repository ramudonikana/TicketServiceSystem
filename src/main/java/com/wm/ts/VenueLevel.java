package com.wm.ts;

import java.util.HashSet;
import java.util.Set;


public enum VenueLevel {

    ORCHESTRA(1, "ORCHESTRA", 100.00, 10, 10),
    MAIN(2, "MAIN", 75.00, 10, 10),
    BALCONY1(3, "BALCONY 1", 50.00, 10, 10),
    BALCONY2(4, "BALCONY 2", 40.00, 10, 10);

    private final int level;
    private final String levelName;
    private final double price;
    private final int rows;
    private final int numberOfSeatsPerRow;
    private int availableSeats;
    Seat[][] seatingArrangement;
    private int firstUnoccupiedSeat;
    private int maximumSeating;


    /**
     * Constructor for the VenueLevel
     *
     * @param level level number in the venue.
     * @param levelName name of the level in the venue.
     * @param price price per ticket for the level.
     * @param rows number of rows in the level.
     * @param numberOfSeatsPerRow number of seats per row in the level.
     */
    VenueLevel(int level, String levelName, double price, int rows, int numberOfSeatsPerRow) {
        this.level = level;
        this.levelName = levelName;
        this.price = price;
        this.rows = rows;
        this.numberOfSeatsPerRow = numberOfSeatsPerRow;
        this.seatingArrangement = new Seat[rows][numberOfSeatsPerRow];
        this.availableSeats = rows * numberOfSeatsPerRow;
        this.firstUnoccupiedSeat = 0;
        this.maximumSeating = rows * numberOfSeatsPerRow;
        for(int i = 0; i < seatingArrangement.length; ++i){
            for (int j = 0; j < seatingArrangement[0].length; j++){
                seatingArrangement[i][j] = new Seat();
            }
        }

    }

    /**
     * Reserve the seat in a level at a row and column in the row.
     *
     * @param row row of the seat in the level to be reserved.
     * @param column column of the seat in the level to be reserved.
     * @return true if the seat was reserved, false otherwise.
     */
    public boolean reserveSeat(int row, int column){
        Seat seat = seatingArrangement[row][column];
        seat.reserveSeat();
        this.availableSeats -= 1;
        return true;
    }

    /**
     * Allocates or holds number of seats specified.
     *
     * @param numSeats number of seats to be allocated in the level.
     * @return set of seats which were allocated
     */
    public Set<SeatLevelWrapper> allocateSeats(int numSeats){
        Set<SeatLevelWrapper> setSeat = new HashSet<SeatLevelWrapper>();
        VenueLevel venueLevel = this;
        int seatsHeld = 0;

        for (int i = 0; i < this.getRows(); ++i){
            for (int j = 0; j < this.getNumberOfSeatsPerRow() && seatsHeld < numSeats; j++) {
                Seat[][] arrangement = (this.getSeatingArrangement());
                if(arrangement[i][j].getSeatState() == arrangement[i][j].getSeatAvailable()){
                    arrangement[i][j].setSeatState(arrangement[i][j].getSeatHeld());
                    SeatLevelWrapper seatLevelWrapper = new SeatLevelWrapper(venueLevel, arrangement[i][j]);
                    setSeat.add(seatLevelWrapper);
                    seatsHeld++;
                }

            }
        }
        this.setAvailableSeats(getAvailableSeats() - setSeat.size());
        return setSeat;
    }

    /**
     * Releases the seat which was put on hold.
     *
     * @param seat seat which is to be released
     */
    public void releaseSeat(Seat seat){
        seat.setSeatState(seat.getSeatAvailable());
        this.setAvailableSeats(this.getAvailableSeats() + 1);
        //availableSeats += 1;
    }


    /**
     * Returns the level number
     * @return level number
     */
    public int getLevel() {
        return level;
    }

    /**
     *
     * @return name of the level
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Returns the price of seat in the level.
     * @return price of seat in the level.
     */
    public double getPrice() {
        return price;
    }


    /**
     * returns the number of seats per row
     * @return the number of seats per row
     */
    public int getNumberOfSeatsPerRow() {
        return numberOfSeatsPerRow;
    }


    /**
     * returns number of rows in the level.
     *
     * @return number of rows in the level.
     */
    public int getRows() {
        return rows;
    }

    /**
     * returns number of available seats in the level.
     *
     * @return number of available seats in the level.
     */
    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats){
        this.availableSeats = availableSeats;
    }

    public Seat[][] getSeatingArrangement() {
        return seatingArrangement;
    }

    public void setSeatingArrangement(Seat[][] seatingArrangement){
        this.seatingArrangement = seatingArrangement;
    }

    public int getFirstUnoccupiedSeat() {
        return firstUnoccupiedSeat;
    }

    public void setFirstUnoccupiedSeat(int firstUnoccupiedSeat) {
        this.firstUnoccupiedSeat = firstUnoccupiedSeat;
    }

    public int getMaximumSeating() {
        return maximumSeating;
    }

}
