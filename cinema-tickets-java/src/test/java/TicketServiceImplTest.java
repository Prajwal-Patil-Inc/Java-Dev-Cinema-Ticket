import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImplTest {
    private final TicketPaymentService paymentService = mock(TicketPaymentService.class);
    private final SeatReservationService reservationService = mock(SeatReservationService.class);
    private final TicketServiceImpl ticketService = new TicketServiceImpl(paymentService, reservationService);
    
    @Test
    public void testValidPurchase1(){
       TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 2);
       TicketTypeRequest childTicket = new TicketTypeRequest(Type.CHILD, 1);

       ticketService.purchaseTickets(1L, adultTicket, childTicket);

       verify(paymentService).makePayment(1L, 65);
       verify(reservationService).reserveSeat(1L,3);
    }

    @Test
    public void testValidPurchase2(){
        TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 12);
       TicketTypeRequest childTicket = new TicketTypeRequest(Type.CHILD, 6);

       ticketService.purchaseTickets(123L, adultTicket, childTicket);

       verify(paymentService).makePayment(123L, 390);
       verify(reservationService).reserveSeat(123L,18);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseMoreThan25Tickets(){
        TicketTypeRequest adultTicket = new TicketTypeRequest(uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.ADULT, 26);
        
        ticketService.purchaseTickets(1L, adultTicket);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsWithoutAdult(){
        TicketTypeRequest child = new TicketTypeRequest(uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.CHILD, 1);
        TicketTypeRequest infant = new TicketTypeRequest(uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.INFANT, 1);

        ticketService.purchaseTickets(1L, child, infant);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testNullAccountID(){
        TicketTypeRequest adult = new TicketTypeRequest(uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.ADULT, 2);

        ticketService.purchaseTickets(null, adult);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testInvalidAccountID(){
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);

        ticketService.purchaseTickets(-1L, adult);
    }
}
