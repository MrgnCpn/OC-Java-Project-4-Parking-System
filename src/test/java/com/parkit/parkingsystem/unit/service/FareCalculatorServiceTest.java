package com.parkit.parkingsystem.unit.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.sql.Timestamp;
import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void initTestClass() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void initTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,false));
        fareCalculatorService.calculateFare(ticket, false);

        assertThat(Fare.CAR_RATE_PER_HOUR).isEqualTo(ticket.getPrice());
    }

    @Test
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE,false));
        fareCalculatorService.calculateFare(ticket, false);

        assertThat(Fare.BIKE_RATE_PER_HOUR).isEqualTo(ticket.getPrice());
    }

    @Test
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, null,false));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE,false));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000)); // 45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE,false));
        fareCalculatorService.calculateFare(ticket, false);

        assertThat(ticket.getPrice()).isEqualTo(0.75 * Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000)); // 45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,false));
        fareCalculatorService.calculateFare(ticket, false);

        assertThat(ticket.getPrice()).isEqualTo(0.75 * Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,false));
        fareCalculatorService.calculateFare(ticket, false);
        assertThat(ticket.getPrice()).isEqualTo((24 * Fare.CAR_RATE_PER_HOUR));
    }

    @Test
    public void calculateFareBikeWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE,false));
        fareCalculatorService.calculateFare(ticket, false);
        assertThat(ticket.getPrice()).isEqualTo((24 * Fare.BIKE_RATE_PER_HOUR));
    }

    @Tag("FreeFirstHalfHour")
    @Test
    public void calculateFareCar_WithFreeFirstHalfHour() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000)); // 45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        fareCalculatorService.calculateFare(ticket, true);

        assertThat(ticket.getPrice()).isEqualTo(0.5 * Fare.CAR_RATE_PER_HOUR);
    }

    @Tag("FreeFirstHalfHour")
    @Test
    public void calculateFareBike_WithFreeFirstHalfHour() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000)); // 45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));
        fareCalculatorService.calculateFare(ticket, true);

        assertThat(ticket.getPrice()).isEqualTo(0.5 * Fare.BIKE_RATE_PER_HOUR);
    }

    @Tag("FreeFirstHalfHour")
    @Test
    public void calculateFareCar_WithLessThanOneHourParkingTime_WithFreeFirstHalfHour() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000)); // 45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        fareCalculatorService.calculateFare(ticket, true);

        assertThat(ticket.getPrice()).isEqualTo(0.25 * Fare.CAR_RATE_PER_HOUR);
    }

    @Tag("FreeFirstHalfHour")
    @Test
    public void calculateFareBike_WithLessThanOneHourParkingTime_WithFreeFirstHalfHour() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000)); // 45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));
        fareCalculatorService.calculateFare(ticket, true);

        assertThat(ticket.getPrice()).isEqualTo(0.25 * Fare.BIKE_RATE_PER_HOUR);
    }

    @Tag("FreeFirstHalfHour")
    @Test
    public void calculateFareCar_WithMoreThanADayParkingTime_WithFreeFirstHalfHour(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,false));
        fareCalculatorService.calculateFare(ticket, true);
        assertThat(ticket.getPrice()).isEqualTo((23.5 * Fare.CAR_RATE_PER_HOUR));
    }

    @Tag("FreeFirstHalfHour")
    @Test
    public void calculateFareBike_WithMoreThanADayParkingTime_WithFreeFirstHalfHour(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE,false));
        fareCalculatorService.calculateFare(ticket, true);
        assertThat(ticket.getPrice()).isEqualTo((23.5 * Fare.BIKE_RATE_PER_HOUR));
    }

    @Test
    public void calculateFareBike_errorDateTime_outTimeBeforeInTime(){
        Date inTime = new Date();
        inTime.setTime(Timestamp.valueOf("2020-01-01 10:10:00").getTime());
        Date outTime = new Date();
        outTime.setTime(Timestamp.valueOf("2020-01-01 10:00:00").getTime());

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBike_errorDateTime_outTimeIsEqualToZero(){
        Date inTime = new Date();
        inTime.setTime(Timestamp.valueOf("2020-01-01 10:10:00").getTime());

        ticket.setInTime(inTime);

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBike_errorParkingTypeIsNull(){
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(null);

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> fareCalculatorService.calculateFare(ticket, false));
    }


    @AfterEach
    private void undefTest(){
        ticket = null;
    }

    @AfterAll
    private static void undefTestClass(){
        fareCalculatorService = null;
    }

}
