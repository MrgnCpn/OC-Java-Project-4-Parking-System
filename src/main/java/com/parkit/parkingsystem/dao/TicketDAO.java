package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Access of ticket table
 * @author OpenClassrooms
 * @author MrgnCpn
 */
public class TicketDAO {

    /**
     * Logger log4j2
     */
    private static final Logger logger = LogManager.getLogger("TicketDAO");

    /**
     * Database configuration
     */
    private static DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Setting database configuration PROD / TEST
     * @param dataBaseConfig
     */
    public static void setDataBaseConfig(DataBaseConfig dataBaseConfig) {
        TicketDAO.dataBaseConfig = dataBaseConfig;
    }

    /**
     * Insert new ticket in ticket table
     * @param ticket
     * @return success execution by boolean
     */
    public boolean saveTicket(Ticket ticket){
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            ps.setInt(1,ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
            return ps.execute();
        } catch (Exception ex){
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    /**
     * Get last ticket in ticket table by vehicleRegNumber
     * @param vehicleRegNumber
     * @return ticket
     */
    public Ticket getTicket(String vehicleRegNumber) {
        Ticket ticket = null;
        if (vehicleRegNumber != null) {
            ResultSet rs = null;
            Connection con = null;
            PreparedStatement ps = null;
            try {
                con = dataBaseConfig.getConnection();
                ps = con.prepareStatement(DBConstants.GET_TICKET);
                ps.setString(1, vehicleRegNumber);
                rs = ps.executeQuery();
                if (rs.next()) {
                    ticket = new Ticket();
                    ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
                    ticket.setParkingSpot(parkingSpot);
                    ticket.setId(rs.getInt(2));
                    ticket.setVehicleRegNumber(vehicleRegNumber);
                    ticket.setPrice(rs.getDouble(3));
                    ticket.setInTime(rs.getTimestamp(4));
                    ticket.setOutTime(rs.getTimestamp(5));
                }
            } catch (Exception ex) {
                logger.error("Error fetching next available slot", ex);
            } finally {
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);
                dataBaseConfig.closeResultSet(rs);
            }
        }
        return ticket;
    }

    /**
     * Update price and outTime of ticket in ticket table when vehicle out
     * @param ticket
     * @return success execution by boolean
     */
    public boolean updateTicket(Ticket ticket) {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement psGET = null;
        PreparedStatement psUPDATE = null;
        int userTicketCount = 0;
        try {
            con = dataBaseConfig.getConnection();
            psGET = con.prepareStatement(DBConstants.GET_TICKET_COUNT);
            psGET.setString(1, ticket.getVehicleRegNumber());
            rs = psGET.executeQuery();
            if(rs.next()){
                userTicketCount = rs.getInt(1);
            }

            // Apply Discount if is a loyal client (>= 3 tickets)
            if (userTicketCount >= 3) ticket.applyDiscount(5);
            psUPDATE = con.prepareStatement(DBConstants.UPDATE_TICKET);
            psUPDATE.setDouble(1, ticket.getPrice());
            psUPDATE.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            psUPDATE.setInt(3,ticket.getId());
            psUPDATE.execute();
            return true;
        } catch (Exception ex){
            logger.error("Error saving ticket info", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(psGET);
            dataBaseConfig.closePreparedStatement(psUPDATE);
            dataBaseConfig.closeConnection(con);
            dataBaseConfig.closeResultSet(rs);
        }
        return false;
    }
}
