package com.wm.ts;


public class SeatLevelWrapper {

    Seat seat;
    VenueLevel venueLevel;

    SeatLevelWrapper(VenueLevel venueLevel, Seat seat){
        this.venueLevel = venueLevel;
        this.seat = seat;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public VenueLevel getVenueLevel() {
        return venueLevel;
    }

    public void setVenueLevel(VenueLevel venueLevel) {
        this.venueLevel = venueLevel;
    }
}
