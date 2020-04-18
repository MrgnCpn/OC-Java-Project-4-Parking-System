package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Configuration and action of DataBase
 */
public class DataBaseConfig {

    /**
     * Logger log4j2
     */
    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    /**
     * Open Connection on OC_parkingSystem_p4_prod DB
     * @return Connection
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("DMI_CONSTANT_DB_PASSWORD")
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/OC_parkingSystem_p4_prod", "root", "password");
    }

    /**
     * Close Connection
     * @param con Connection
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
     * @param ps PreparedStatement
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
     * @param rs ResultSet
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
