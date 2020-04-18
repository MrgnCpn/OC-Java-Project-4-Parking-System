package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

public class DataBasePrepareService {

    private static final Logger logger = LogManager.getLogger("DataBasePrepareService");
    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries(){
        try (Connection connection = dataBaseTestConfig.getConnection();) {
            //set parking entries to available
            connection.prepareStatement("UPDATE parking SET available = 1").execute();

            //clear ticket entries;
            connection.prepareStatement("TRUNCATE TABLE ticket").execute();

            //set TEST ticket
            connection.prepareStatement("INSERT INTO ticket VALUES (1, 1, \"TST_TCKT\", 10, CAST(\"2020-01-01 10:00:00\" AS DATETIME), CAST(\"2020-01-01 10:10:00\" AS DATETIME))").execute();

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            logger.info("Close DB connection");
        }
    }


}
