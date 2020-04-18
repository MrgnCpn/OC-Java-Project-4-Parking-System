package com.parkit.parkingsystem.unit.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class TicketDAOTest {
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    public static void setUp(){
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest(){
        ticketDAO = new TicketDAO();
        ticketDAO.setDataBaseConfig(new DataBaseTestConfig());
    }

    @Test
    void getTicketUnknown() throws SQLException {
        assertThat(ticketDAO.getTicket("UNKNOW")).isNull();
    }

    @Test
    void getTicketTest() throws SQLException {
        Ticket ticket = ticketDAO.getTicket("TST_TCKT");

        assertThat(ticket.getId()).isEqualTo(1);
        assertThat(ticket.getPrice()).isEqualTo(10);
        assertThat(ticket.getInTime().getTime()).isEqualTo(Timestamp.valueOf("2020-01-01 10:00:00").getTime());
        assertThat(ticket.getOutTime().getTime()).isEqualTo(Timestamp.valueOf("2020-01-01 10:10:00").getTime());
    }

    @Test
    void saveTicketTest() throws SQLException {
        Date inTime = new Date();
        inTime.setTime(Timestamp.valueOf("2020-01-01 10:00:00").getTime());
        Date outTime = new Date();
        outTime.setTime(Timestamp.valueOf("2020-01-01 10:10:00").getTime());

        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("NEW_TCKT");
        ticket.setPrice(10);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, false));

        ticketDAO.saveTicket(ticket);

        Ticket ticketDB = ticketDAO.getTicket("NEW_TCKT");

        assertThat(ticketDB.getPrice()).isEqualTo(10);
        assertThat(ticketDB.getParkingSpot()).isEqualTo(new ParkingSpot(2, ParkingType.CAR, false));
        assertThat(ticketDB.getInTime().getTime()).isEqualTo(inTime.getTime());
        assertThat(ticketDB.getOutTime().getTime()).isEqualTo(outTime.getTime());
    }

    @Test
    void updateTicketReturnBoolean() throws SQLException {
        Date outTime = new Date();
        outTime.setTime(Timestamp.valueOf("2020-02-02 20:20:00").getTime());

        Ticket ticket = ticketDAO.getTicket("TST_TCKT");
        ticket.setPrice(30);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));
        ticket.setOutTime(outTime);

        ticketDAO.updateTicket(ticket);
        assertThat(ticketDAO.updateTicket(ticket)).isInstanceOf(Boolean.class);
        assertThat(ticketDAO.getTicket("TST_TCKT").getPrice()).isEqualTo(30);
        assertThat(ticketDAO.getTicket("TST_TCKT").getParkingSpot()).isEqualTo(new ParkingSpot(1, ParkingType.BIKE, false));
        assertThat(ticketDAO.getTicket("TST_TCKT").getInTime().getTime()).isEqualTo(Timestamp.valueOf("2020-01-01 10:00:00").getTime());
        assertThat(ticketDAO.getTicket("TST_TCKT").getOutTime().getTime()).isEqualTo(outTime.getTime());
    }

    @Test
    void updateTicketWithFidelityDiscount() throws SQLException {
        Ticket ticket = ticketDAO.getTicket("TST_TCKT");
        for (int i = 0; i < 3; i ++) {
            ticketDAO.saveTicket(ticket);
        }
        assertThat(ticketDAO.updateTicket(ticket)).isInstanceOf(Boolean.class);
        assertThat(ticketDAO.getTicket("TST_TCKT").getPrice()).isEqualTo(9.5);
    }

    @AfterEach
    public void tearDownPerTest(){
        ticketDAO = null;
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown(){
        dataBasePrepareService = null;
    }
}