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

    public Date getInTime() {
        if (inTime != null) return new Date(inTime.getTime());
        else return null;
    }

    public void setInTime(Date inTime) {
        if (inTime != null) this.inTime = new Date(inTime.getTime());
    }

    public Date getOutTime() {
        if (outTime != null) return new Date(outTime.getTime());
        else return null;
    }

    public void setOutTime(Date outTime) {
        if (outTime != null) this.outTime = new Date(outTime.getTime());
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
