package com.parkit.parkingsystem.unit.model;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;

class TicketTest {
    private Ticket ticket;

    @BeforeEach
    void initTest(){
        ticket = new Ticket();
    }

    @Test
    void testGetterAndSetter(){
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setId(1);
        ticket.setPrice(12.0);
        ticket.setVehicleRegNumber("ABC");
        ticket.setParkingSpot(parkingSpot);
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        outTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);

        assertThat(ticket.getId()).isEqualTo(1);
        assertThat(ticket.getPrice()).isEqualTo(12.0);
        assertThat(ticket.getVehicleRegNumber()).isEqualTo("ABC");
        assertThat(ticket.getParkingSpot()).isEqualTo(parkingSpot);
        assertThat(ticket.getInTime()).isEqualTo(inTime);
        assertThat(ticket.getOutTime()).isEqualTo(outTime);
    }

    @Test
    void testGetterNoSetter(){
        assertThat(ticket.getId()).isEqualTo(0);
        assertThat(ticket.getPrice()).isEqualTo(0);
        assertThat(ticket.getVehicleRegNumber()).isNull();
        assertThat(ticket.getParkingSpot()).isNull();
        assertThat(ticket.getInTime()).isNull();
        assertThat(ticket.getOutTime()).isNull();
    }

    @Test
    void applyDiscountToPrice() throws Exception {
        ticket.setPrice(10);
        assertThat(ticket.getPrice()).isEqualTo(10);

        ticket.applyDiscount(5);
        assertThat(ticket.getPrice()).isEqualTo(9.5);

        ticket.setPrice(10);
        ticket.applyDiscount(50);
        assertThat(ticket.getPrice()).isEqualTo(5);

        ticket.setPrice(10);
        ticket.applyDiscount(90);
        assertThat(ticket.getPrice()).isEqualTo(1);

        assertThatExceptionOfType(Exception.class).isThrownBy(() -> ticket.applyDiscount(200));
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> ticket.applyDiscount(-20));
    }

    @AfterEach
    public void undefTest(){
        ticket = null;
    }
}