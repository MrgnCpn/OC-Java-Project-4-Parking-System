package com.parkit.parkingsystem.unit.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


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
        parkingSpotDAO.dataBaseConfig = new DataBaseTestConfig();
    }

    @Test
    void getNextAvailableSlotTest() {
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isInstanceOf(Integer.class);
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(1);
    }

    @Test
    void updateParkingTest() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        assertThat(parkingSpotDAO.updateParking(parkingSpot)).isInstanceOf(Boolean.class);
        parkingSpotDAO.updateParking(parkingSpot);
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);
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