package com.parkit.parkingsystem.constants;

/**
 * Database SQL requests
 * @author OpenClassrooms
 * @author MrgnCpn
 */
public class DBConstants {
    /**
     * Class constructor
     */
    private DBConstants(){ }

    /**
     * Query to get the next parking spot available by vehivle type
     */
    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";

    /**
     * Update availability of parking spot when vehicle in or out of the parking
     */
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    /**
     * Save new ticket when vehicle in of the parking
     */
    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";

    /**
     * Update ticket when vehicule out of the parking, set up ticket price and out time
     */
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";

    /**
     * Get ticket by vehicle register number
     */
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";

    /**
     * Get ticket count for a same user
     */
    public static final String GET_TICKET_COUNT = "select count(ID) from ticket where VEHICLE_REG_NUMBER = ?";
}
