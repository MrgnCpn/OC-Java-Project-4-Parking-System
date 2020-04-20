package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Access of parking table
 * @author OpenClassrooms
 * @author MrgnCpn
 */
public class ParkingSpotDAO {

    /**
     * Logger log4j2
     */
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    /**
     * Database configuration
     */
    private static DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Setting database configuration PROD / TEST
     * @param dataBaseConfig
     */
    public static void setDataBaseConfig(DataBaseConfig dataBaseConfig) {
        ParkingSpotDAO.dataBaseConfig = dataBaseConfig;
    }

    /**
     * Get the next slot available for one parking type CAR / BIKE
     * @param parkingType
     * @return parking number
     */
    public int getNextAvailableSlot(ParkingType parkingType) {
        int result =- 1;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);
            }
        } catch (Exception e){
            logger.error("Error fetching next available slot", e);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
            dataBaseConfig.closeResultSet(rs);
        }
        return result;
    }

    /**
     * Update Availability of parking spot if vehicle in or out by type of vehicle
     * @param parkingSpot
     * @return success execution by boolean
     */
    public boolean updateParking(ParkingSpot parkingSpot) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        } catch (Exception e){
            logger.error("Error updating parking info", e);
            return false;
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
            if (con != null) con = null;
        }
    }
}
