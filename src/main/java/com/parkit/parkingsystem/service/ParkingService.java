package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Date;

/**
 * Main function to input or exit vehicule
 * @author OpenClassrooms
 * @author MrgnCpn
 */
public class ParkingService {

    /**
     * Logger log4j2
     */
    private static final Logger logger = LogManager.getLogger("ParkingService");

    /**
     * Ticket price calculating service
     */
    private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

    /**
     * Input reader
     */
    private InputReaderUtil inputReaderUtil;

    /**
     * Access to Parking table
     */
    private ParkingSpotDAO parkingSpotDAO;

    /**
     * Access to Ticket table
     */
    private TicketDAO ticketDAO;

    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO){
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    /**
     * Update parking slot availability and save new ticket in DB
     */
    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if(parkingSpot != null && parkingSpot.getId() > 0){
                String vehicleRegNumber = getVehicleRegNumber();
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot); //allot this parking space and mark it's availability as false

                Date inTime = new Date();
                Ticket ticket = new Ticket();
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticketDAO.saveTicket(ticket);
                System.out.println("Generated Ticket and saved in DB\nPlease park your vehicle in spot number:" + parkingSpot.getId() + "\nRecorded in-time for vehicle number:" + vehicleRegNumber + " is : " + inTime);
            }
        } catch(Exception e) {
            logger.error("Unable to process incoming vehicle", e);
            throw new IllegalArgumentException();
        }
    }

    /**
     * Get vehicleRegNumber inputted in console by user
     * @return  inputReaderUtil.readVehicleRegistrationNumber();
     */
    private String getVehicleRegNumber() {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * Get the next parking slot available by vehicle type
     * @return slot number or 0 if neither available
     */
    public ParkingSpot getNextParkingNumberIfAvailable(){
        int parkingNumber = 0;
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if(parkingNumber > 0){
                parkingSpot = new ParkingSpot(parkingNumber,parkingType, true);
            }else{
                throw new SQLException("Error fetching parking number from DB. Parking slots might be full");
            }
        } catch (IllegalArgumentException ie) {
            logger.error("Error parsing user input for type of vehicle", ie);
            throw new IllegalArgumentException();
        } catch (Exception e) {
            logger.error("Error fetching next available parking slot", e);
            throw new IllegalArgumentException();
        }
        return parkingSpot;
    }

    /**
     * Get vehicle Type inputted in terminal by user
     * @return ParkingType
     */
    private ParkingType getVehicleType(){
        System.out.println("Please select vehicle type from menu\n1 CAR\n2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch(input){
            case 1 : return ParkingType.CAR;
            case 2 : return ParkingType.BIKE;
            default :
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
        }
    }

    /**
     * Exit vehicle by update parking slot availability and ticket (price / out time)
     */
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket, true);
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare :" + ticket.getPrice() + "\nRecorded out-time for vehicle number :" + ticket.getVehicleRegNumber() + " is :" + outTime);
            } else {
                System.out.println("Unable to update ticket information. Error occurred");
            }
        } catch (Exception e) {
            logger.error("Unable to process exiting vehicle", e);
            throw new NullPointerException();
        }
    }
}