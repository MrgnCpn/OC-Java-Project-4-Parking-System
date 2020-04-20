package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Configuration and action of DataBase PROD
 * @author OpenClassrooms
 * @author MrgnCpn
 */
public class DataBaseConfig {

    /**
     * Logger log4j2
     */
    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    /**
     * Database host
     */
    private String host;
    /**
     * Database port
     */
    private String port;
    /**
     * Database database name
     */
    private String database;
    /**
     * Database username
     */
    private String user;
    /**
     * Database password
     */
    private String password;

    /**
     * Open Connection on OC_parkingSystem_p4_prod DB
     * @return Connection
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (InputStream inputStream = new FileInputStream("src/main/resources/dbConfig.properties")){
            Properties properties = new Properties();
            properties.load(inputStream);
            host = properties.getProperty("host");
            port = properties.getProperty("port");
            database = properties.getProperty("database_prod");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        } catch (IOException e) {
            logger.error("Error while read file", e);
        }
        return DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    }

    /**
     * Close Connection
     * @param con Active connection
     */
   public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }

    /**
     * Close Prepared Statement
     * @param ps Open statement
     */
   public void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement", e);
            }
        }
    }


    /**
     * Close ResultSet
     * @param rs Open ResultSet
     */
  public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set", e);
            }
        }
    }
}
