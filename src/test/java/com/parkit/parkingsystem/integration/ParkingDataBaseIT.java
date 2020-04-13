package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(inputReaderUtil.readSelection()).thenReturn(1);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar() throws Exception {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        // TODO : Check that a ticket is actualy saved in DB and Parking table is updated with availability

        verify(inputReaderUtil, times(1)).readSelection();
        verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        assertThat(ticket).isInstanceOf(Ticket.class);
        assertThat(ticket.getId()).isPositive();
        assertThat(ticket.getParkingSpot()).isEqualTo(new ParkingSpot(1, ParkingType.CAR, false));
        assertThat(ticket.getInTime()).isToday();
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);
    }

    @Test
    public void testParkingLotExit() throws Exception {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        Thread.sleep(500); // User input
        parkingService.processExitingVehicle();

        // TODO : Check that the fare generated and out time are populated correctly in the database

        verify(inputReaderUtil, times(1)).readSelection();
        verify(inputReaderUtil, times(2)).readVehicleRegistrationNumber();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        assertThat(ticket.getOutTime()).isAfterOrEqualTo(ticket.getInTime());

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        double duration = (double) (outHour - inHour) / (1000 * 3600);
        duration = (Math.round(duration * 100)) / (double) 100;

        assertThat(ticket.getPrice()).isEqualTo(duration * Fare.CAR_RATE_PER_HOUR);
    }

    @AfterAll
    public static void tearDown(){
        parkingSpotDAO = null;
        ticketDAO = null;
        dataBasePrepareService.clearDataBaseEntries();
        dataBasePrepareService = null;
    }
}

