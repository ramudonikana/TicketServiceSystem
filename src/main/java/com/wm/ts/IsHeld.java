package com.wm.ts;


public class IsHeld extends SeatState {


    private Seat seat;

    /**
     * Constructor
     *
     * @param newSeat seat for which the state has to be set.
     */
    public IsHeld(Seat newSeat){
        seat = newSeat;
    }

    /**
     * reserve a seat when it is held.
     */
    @Override
    public void reserve() {
        seat.setSeatState(seat.getSeatReserved());
    }

    /**
     * hold a seat when it is already held.
     */
    @Override
    public void hold() {
        System.out.println("There already exists a hold on the seat.");
    }

    /**
     * release the held seat.
     */
    @Override
    public void releaseSeat() {
        seat.setSeatState(seat.getSeatAvailable());

    }

}
