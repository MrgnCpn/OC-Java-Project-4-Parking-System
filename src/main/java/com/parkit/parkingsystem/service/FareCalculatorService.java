package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Price calculating service
 */
public class FareCalculatorService {

    /**
     * Calculate price of ticket by type of vehicle
     * Free first half hour if firstHalfHourFree = true
     * @param ticket
     * @param firstHalfHourFree
     */
    public void calculateFare(Ticket ticket, boolean firstHalfHourFree){
        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        if ((outHour == 0) || (outHour < inHour) ){
            throw new IllegalArgumentException("Out time provided is incorrect : " + ticket.getOutTime().toString());
        }

        double duration = (double) (outHour - inHour) / (1000 * 3600);
        duration = (Math.round(duration * 100)) / (double) 100;

        // First Half Hour free
        if (firstHalfHourFree) {
            duration -= 0.5;
            if (duration < 0) duration = 0;
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR : {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE : {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default : throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}