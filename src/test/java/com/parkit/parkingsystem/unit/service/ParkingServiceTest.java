package com.parkit.parkingsystem.unit.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;
    private static ParkingSpot parkingSpot;
    private static Ticket ticket;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void initTest() {
        parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket = new Ticket();
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
    }

    @Tag("ParkingServiceTest")
    @Test
    public void processExitingVehicleTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processExitingVehicle();

        verify(inputReaderUtil, Mockito.times(1)).readVehicleRegistrationNumber();
        verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Tag("ParkingServiceTest")
    @Test
    public void getNextParkingNumberIfAvailableTest() throws Exception {
        ParkingSpot parkingSpotTest;
        int availableSpot = 10;
        int parkingTypeChoice = 2; // ParkingType.BIKE
        when(inputReaderUtil.readSelection()).thenReturn(parkingTypeChoice);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(availableSpot);

        parkingSpotTest = parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil, Mockito.times(1)).readSelection();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
        assertThat(parkingSpotTest.isAvailable()).isEqualTo(true);
        assertThat(parkingSpotTest.getId()).isEqualTo(availableSpot);
        assertThat(parkingSpotTest.getParkingType()).isEqualTo(ParkingType.BIKE);
    }

    @Tag("ParkingServiceTest")
    @Test
    public void processIncomingVehicleTest() throws Exception {
        int availableSpot = 10;
        int parkingTypeChoice = 2; // ParkingType.BIKE
        when(inputReaderUtil.readSelection()).thenReturn(parkingTypeChoice);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(availableSpot);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Tag("ParkingServiceTest")
    @Test
    public void processExitingVehicleTestNullException() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> parkingService.processExitingVehicle());
    }

    @Tag("ParkingServiceTest")
    @Test
    public void getNextParkingNumberIfAvailableTest_unknowType() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> parkingService.getNextParkingNumberIfAvailable());
    }

    @Tag("ParkingServiceTest")
    @Test
    public void getNextParkingNumberIfAvailableTest_nullParkingSpotDAO() {
        when(inputReaderUtil.readSelection()).thenReturn(1);

        assertThatExceptionOfType(Exception.class).isThrownBy(() -> parkingService.getNextParkingNumberIfAvailable());
    }

    @Tag("ParkingServiceTest")
    @Test
    public void processIncomingVehicleTest_nullParkingSpotDAO(){
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> parkingService.processIncomingVehicle());
    }

    @AfterEach
    private void undefTest() {
        parkingSpot = null;
        ticket = null;
        parkingService = null;
    }
}
