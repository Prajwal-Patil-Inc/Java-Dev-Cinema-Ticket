package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;

    private final int ADULT_TICKET_PRICE = 25;
    private final int CHILD_TICKET_PRICE = 15;
    //private final int INFANT_TICKET_PRICE = 0;

    //Constructor
    //Made public to access in the test file
    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService seatReservationService){
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
    }
 
    //Check if account ID is valid
    private boolean isAccountIDValid(Long accountId){
        if(accountId == null || accountId <= 0){
            return false; //not valid
        }
        return true; //valid
    }

    //Check ticket limit
    private void checkTicketLimit(int totalTickets) throws InvalidPurchaseException{
        if(totalTickets > 25){
            throw new InvalidPurchaseException();
        }
    }

    //Check if no adult ticket is booked
    private void checkAdultTicketRequirement(int adultTickets, int childTickets, int infantTickets) throws InvalidPurchaseException{
        if(adultTickets == 0 && (childTickets > 0 || infantTickets > 0)){
            throw new InvalidPurchaseException();
        }
    }

    //Calculate total ticket amount
    private int calculateTotalAmount(int adultTickets, int childTickets){
        int totalAmount = (adultTickets * ADULT_TICKET_PRICE) + (childTickets * CHILD_TICKET_PRICE);
        return totalAmount;
    }

    //Calculate total seats to allocate
    private int calculateTotalSeats(int adultTickets, int childTickets){
        int totalSeats = (adultTickets + childTickets);
        return totalSeats;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if(!isAccountIDValid(accountId)){ //Account ID is not valid
            throw new InvalidPurchaseException();
        }

        int adultTickets = 0;
        int childTickets = 0;
        int infantTickets = 0;

        for(TicketTypeRequest request : ticketTypeRequests){
            switch (request.getTicketType()) {
                case ADULT:
                    adultTickets += request.getNoOfTickets();
                    break;
                case CHILD: 
                    childTickets += request.getNoOfTickets();
                    break;
                case INFANT:
                    infantTickets += request.getNoOfTickets();
                    break;
            }
        }

        int totalTickets = adultTickets + childTickets + infantTickets;

        checkTicketLimit(totalTickets); //Check if total tickets exceed 25

        checkAdultTicketRequirement(adultTickets, childTickets, infantTickets); //Check adult ticket requirement is satisfied or not

        int totalAmount = calculateTotalAmount(adultTickets, childTickets); 
        int totalSeats = calculateTotalSeats(adultTickets, childTickets); 

        paymentService.makePayment(accountId, totalAmount);
        seatReservationService.reserveSeat(accountId, totalSeats);
    }

}
