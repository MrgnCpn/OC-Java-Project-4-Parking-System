package com.parkit.parkingsystem.unit.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class ParkingSpotDAOTest {

    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    public static void setUp(){
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest(){
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.setDataBaseConfig(new DataBaseTestConfig());
    }

    @Test
    void getNextAvailableSlotTest() throws SQLException {
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isInstanceOf(Integer.class);
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(1);
        assertThat(parkingSpotDAO.getNextAvailableSlot(null)).isEqualTo(-1);
    }

    @Test
    void updateParkingTest() throws SQLException {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        assertThat(parkingSpotDAO.updateParking(parkingSpot)).isEqualTo(true);
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);

        parkingSpot = null;
        assertThat(parkingSpotDAO.updateParking(parkingSpot)).isEqualTo(false);
    }

    @AfterEach
    public void tearDownPerTest(){
        parkingSpotDAO = null;
    }

    @AfterAll
    public static void tearDown(){
        dataBasePrepareService.clearDataBaseEntries();
        dataBasePrepareService = null;
    }
}