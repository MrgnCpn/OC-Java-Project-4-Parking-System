package com.parkit.parkingsystem.unit.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ParkingSpotDAOTest {

    private static ParkingSpotDAO parkingSpotDAO;

    @BeforeEach
    public void setUpPerTest(){
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = new DataBaseTestConfig();
    }

    @Test
    void getNextAvailableSlotTest() {
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isInstanceOf(Integer.class);
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);
    }

    @Test
    void updateParkingTest() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        assertThat(parkingSpotDAO.updateParking(parkingSpot)).isInstanceOf(Boolean.class);
    }

    @AfterEach
    public void tearDownPerTest(){
        parkingSpotDAO = null;
    }
}