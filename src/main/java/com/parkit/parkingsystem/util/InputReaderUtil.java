package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Input Reader
 * @author OpenClassrooms
 * @author MrgnCpn
 */
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings("DM_DEFAULT_ENCODING")
public class InputReaderUtil {
    /**
     * Logger log4j2
     */
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    /**
     * Input scanner
     */
    private static Scanner scan = new Scanner(System.in);

    /**
     * Read input in console for actions
     * @return action number or -1 if error
     */
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("DM_DEFAULT_ENCODING")
    public int readSelection() {
        try {
            int input = Integer.parseInt(scan.nextLine());
            return input;
        } catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**
     * Read VehicleRegNumber in console
     * @return Vehicle register number
     */
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("DM_DEFAULT_ENCODING")
    public String readVehicleRegistrationNumber() {
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        } catch (Exception e) {
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }
}
