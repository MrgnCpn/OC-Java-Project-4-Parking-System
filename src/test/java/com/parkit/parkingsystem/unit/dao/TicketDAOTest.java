package com.parkit.parkingsystem.unit.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

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
        ticketDAO.dataBaseConfig = new DataBaseTestConfig();
    }

    @Test
    void getTicketUnknow() {
        assertThat(ticketDAO.getTicket("UNKNOW")).isNull();
    }

    @Test
    void getTicketTest() {
        Ticket ticket = ticketDAO.getTicket("TST_TCKT");

        assertThat(ticket.getId()).isEqualTo(1);
        assertThat(ticket.getPrice()).isEqualTo(10);
        assertThat(ticket.getInTime().getTime()).isEqualTo(Timestamp.valueOf("2020-01-01 10:00:00").getTime());
        assertThat(ticket.getOutTime().getTime()).isEqualTo(Timestamp.valueOf("2020-01-01 10:10:00").getTime());
    }

    @Disabled("Plus de tests")
    @Test
    void saveTicket() {
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("NEW_TCKT");
        ticket.setPrice(10);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, false));
        ticketDAO.saveTicket(ticket);

        Ticket ticketDB = ticketDAO.getTicket("NEW_TCKT");

        assertThat(ticketDB.getPrice()).isEqualTo(10);
        assertThat(ticketDB.getParkingSpot()).isEqualTo(new ParkingSpot(2, ParkingType.CAR, false));
    }

    @Disabled("A réecrire")
    @Test
    void updateTicketReturnBoolean() {
        Ticket ticket = ticketDAO.getTicket("TST_TCKT");
        ticket.setPrice(30);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));
        ticketDAO.updateTicket(ticket);
        assertThat(ticketDAO.updateTicket(ticket)).isInstanceOf(Boolean.class);
        assertThat(ticketDAO.getTicket("TST_TCKT").getPrice()).isEqualTo(30);
        assertThat(ticketDAO.getTicket("TST_TCKT").getParkingSpot()).isEqualTo(new ParkingSpot(1, ParkingType.BIKE, false));
    }

    @Test
    void updateTicketWithFidelityDiscount(){
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