package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

/**
 * @author OpenClassrooms
 * @author MrgnCpn
 */
public class DataBasePrepareService {

    /**
     * Logger log4j2
     */
    private static final Logger logger = LogManager.getLogger("DataBasePrepareService");

    /**
     * Database configuration
     */
    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    /**
     * Set up database for test
     * Clean all tickets
     * Set all parking slots available
     * Insert Test ticket
     */
    public void clearDataBaseEntries(){
        Connection connection = null;

        try {
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
            connection.prepareStatement("UPDATE parking SET available = 1").execute();

            //clear ticket entries;
            connection.prepareStatement("TRUNCATE TABLE ticket").execute();

            //set TEST ticket
            connection.prepareStatement("INSERT INTO ticket VALUES (1, 1, \"TST_TCKT\", 10, CAST(\"2020-01-01 10:00:00\" AS DATETIME), CAST(\"2020-01-01 10:10:00\" AS DATETIME))").execute();

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }


}
