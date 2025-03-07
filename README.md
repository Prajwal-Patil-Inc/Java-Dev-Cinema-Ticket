# Java-Dev-Cinema-Ticket

This repository contains a solution for a cinema ticket purchase service. The `TicketServiceImpl` class handles the purchase of adult, child, and infant tickets while ensuring the business rules are followed. The service uses two external services: `TicketPaymentService` for processing payments and `SeatReservationService` for reserving seats.

## Features
- Validates account ID
- Ensures a maximum of 25 tickets can be purchased at once
- Enforces the rule that child or infant tickets cannot be purchased without an adult ticket
- Calculates total ticket amount and reserves the required number of seats
- Supports adult, child, and infant ticket types

## Requirements
- Java 11 or higher
- Maven for dependency management and building the project

## Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Prajwal-Patil-Inc/Java-Dev-Cinema-Ticket.git

2. **Navigate to the project directory**:
   ```bash
   cd Java-Dev-Cinema-Ticket

3. **Build the project using Maven**:
   ```bash
   mvn clean install

## Testing 
1. **Run the tests using Maven**:
   ```bash
   mvn test
   
This will execute the test cases for the TicketServiceImpl class using JUnit 5 and Mockito to verify valid and invalid ticket purchases.
2. **Test results**: The test results will be shown in the terminal or in the target/surefire-reports directory.

## How to Use the Service
1. **Create an instance of TicketServiceImpl:**
```java
TicketService ticketService = new TicketServiceImpl(paymentService, seatReservationService);

2. **Purchase tickets by calling the purchaseTickets method:**
```java
TicketTypeRequest adultTicket = new TicketTypeRequest(TicketType.ADULT, 2);
TicketTypeRequest childTicket = new TicketTypeRequest(TicketType.CHILD, 1);
ticketService.purchaseTickets(accountId, adultTicket, childTicket);

3. **Test cases are provided for various scenarios, such as:**
- Valid ticket purchases with combinations of adult, child, and infant tickets
- Invalid purchases like exceeding the ticket limit or missing adult tickets
