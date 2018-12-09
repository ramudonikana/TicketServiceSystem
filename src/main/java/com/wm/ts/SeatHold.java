package com.wm.ts;

import java.util.HashSet;
import java.util.Set;



public class SeatHold {

    private Set<SeatLevelWrapper> holdSet = new HashSet<SeatLevelWrapper>();
    private int holdId;
    private Long timeOfHold;
    private String customerEmail;

    public SeatHold(Set<SeatLevelWrapper> holdSet, int holdId, Long timeOfHold, String customerEmail){

        this.holdSet = holdSet;
        this.holdId = holdId;
        this.timeOfHold = timeOfHold;
        this.customerEmail = customerEmail;

    }

    public Long getTimeOfHold(){
        return timeOfHold;
    }

    public void setTimeStamp(Long timeOfHold){
        this.timeOfHold = timeOfHold;
    }

    public int getHoldId(){
        return holdId;
    }

    public void setHoldId(int holdId){
        this.holdId = holdId;
    }

    public Set<SeatLevelWrapper> getHoldSet() {
        return holdSet;
    }

    public void setHoldSet(Set<SeatLevelWrapper> holdSet) {
        this.holdSet = holdSet;
    }

    public void setTimeOfHold(Long timeOfHold) {
        this.timeOfHold = timeOfHold;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

}
