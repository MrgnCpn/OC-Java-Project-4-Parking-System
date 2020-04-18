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
import java.sql.SQLException;

/**
 * Access of parking table
 */
public class ParkingSpotDAO {

    /**
     * Logger log4j2
     */
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    private static DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public static void setDataBaseConfig(DataBaseConfig dataBaseConfig) {
        ParkingSpotDAO.dataBaseConfig = dataBaseConfig;
    }

    /**
     * Get the next slot available for one parking type CAR / BIKE
     * @param parkingType
     * @return parking number
     */
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
    public int getNextAvailableSlot(ParkingType parkingType) throws SQLException {
        int result =- 1;
        ResultSet rs = null;

        try (Connection con = dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
        ){
            if (parkingType.toString() != null) {
                ps.setString(1, parkingType.toString());
            }
            rs = ps.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex){
            logger.error("Error fetching next available slot", ex);
        } finally {
            if (rs != null) rs.close();
        }
        return result;
    }

    /**
     * Update Availability of parking spot if vehicle in or out by type of vehicle
     * @param parkingSpot
     * @return success execution by boolean
     */
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
    public boolean updateParking(ParkingSpot parkingSpot) {
        //update the availability fo that parking slot
        try (Connection con= dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
        ) {
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            dataBaseConfig.closePreparedStatement(ps);
            return (updateRowCount == 1);
        } catch (Exception ex){
            logger.error("Error updating parking info", ex);
            return false;
        }
    }
}
