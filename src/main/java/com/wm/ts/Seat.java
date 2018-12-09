package com.wm.ts;



public class Seat {

    private SeatState available;
    private SeatState reserved;
    private SeatState held;

    private SeatState seatState;



    /**
     * constructor for seat class.
     * Each seat is set to Available state initially.
     */
    public Seat(){

        available = new IsAvailable(this);
        reserved = new IsReserved(this);
        held = new IsHeld(this);

        seatState = available;
    }

    /**
     * getter for seatState
     * @return seatState which represents the current state of seat
     */
    public SeatState getSeatState() {
        return seatState;
    }

    /**
     * set seatState
     * @param newSeatState set state of seat
     */
    void setSeatState(SeatState newSeatState){
        this.seatState = newSeatState;
    }

    /**
     * reserve the seat thus making it unavailable.
     */
    public void reserveSeat(){
        seatState.reserve();
    }

    /**
     * hold the seat for a particular time
     */
    public void hold(){
        seatState.hold();
    }

    /**
     * release the seat
     */
    public void releaseSeat(){
        seatState.releaseSeat();
    }

    /**
     * returns the state reserved.
     * @return reserved state
     */
    public SeatState getSeatReserved(){
        return reserved;
    }

    /**
     * returns the state held
     * @return held state
     */
    public SeatState getSeatHeld(){
        return held;
    }

    /**
     * returns the available state
     * @return available state
     */
    public SeatState getSeatAvailable(){
        return available;
    }


}
