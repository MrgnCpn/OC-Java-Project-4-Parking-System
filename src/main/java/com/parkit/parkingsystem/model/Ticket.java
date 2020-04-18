package com.parkit.parkingsystem.model;

import java.util.Date;

/**
 * Parking spot object
 * id : Integer
 * ParkingSpot : ParkingSpot
 * vehicleRegNumber : String
 * price : double
 * inTime : Date
 * outTime : Date
 * @author OpenClassrooms
 * @author MrgnCpn
 */
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP")
    public Date getInTime() {
        return inTime;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP")
    public Date getOutTime() {
        return outTime;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    /**
     * Apply discount to the price of the ticket
     * Discount [0 ; 100]
     * @param discount
     */
    public void applyDiscount(double discount) {
        if ((discount >= 0) && (discount <= 100)) {
            this.price -= this.price * (discount / 100);
        } else {
            throw new IllegalArgumentException("The discount value is incorrect");
        }
    }
}
