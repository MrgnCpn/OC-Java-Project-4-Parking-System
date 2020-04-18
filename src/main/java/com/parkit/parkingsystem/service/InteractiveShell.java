package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Interface of the App in console
 */
public class InteractiveShell {
    /**
     * Logger log4j2
     */
    private static final Logger logger = LogManager.getLogger("InteractiveShell");

    /**
     * Load interface in terminal
     * @throws Exception
     */
    public static void loadInterface() {
        logger.info("App initialized!!!");
        System.out.println("\n\nWelcome to Parking System!");

        boolean continueApp = true;
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        TicketDAO ticketDAO = new TicketDAO();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        while(continueApp){
            System.out.println("Please select an option. Simply enter the number to choose an action\n1 New Vehicle Entering - Allocate Parking Space\n2 Vehicle Exiting - Generate Ticket Price\n3 Shutdown System");
            int option = inputReaderUtil.readSelection();
            switch(option){
                case 1 :
                    parkingService.processIncomingVehicle();
                    break;
                case 2 :
                    parkingService.processExitingVehicle();
                    break;
                case 3 :
                    System.out.println("Exiting from the system!");
                    continueApp = false;
                    break;
                default : System.out.println("Unsupported option. Please enter a number corresponding to the provided menu");
            }
        }
    }
}
