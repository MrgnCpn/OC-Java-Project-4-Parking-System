package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    public void calculateFare(Ticket ticket, Boolean firstHalfHourFree){
        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        if((outHour == 0) || (outHour < inHour) ){
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
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}