package com.parkit.parkingsystem.unit.dao;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;

import java.sql.Timestamp;

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
        assertThat(ticket.getPrice()).isEqualTo(0.5);
        assertThat(ticket.getInTime().getTime()).isEqualTo(Timestamp.valueOf("2020-01-01 10:00:00").getTime());
        assertThat(ticket.getOutTime().getTime()).isEqualTo(Timestamp.valueOf("2020-01-01 10:10:00").getTime());
    }

    @Test
    void saveTicketReturnBoolean() {
        Ticket ticket = new Ticket();
        assertThat(ticketDAO.saveTicket(ticket)).isInstanceOf(Boolean.class);
    }

    @Test
    void updateTicketReturnBoolean() {
        Ticket ticket = new Ticket();
        assertThat(ticketDAO.updateTicket(ticket)).isInstanceOf(Boolean.class);
    }

    @AfterEach
    public void tearDownPerTest(){
        ticketDAO = null;
    }

    @AfterAll
    public static void tearDown(){
        dataBasePrepareService.clearDataBaseEntries();
        dataBasePrepareService = null;
    }
}