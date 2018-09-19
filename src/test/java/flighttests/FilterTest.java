package flighttests;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.concurrent.ManagedExecutorService;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import dal.FlightFacade;
import entities.Flight;
import services.FlightService;

public class FilterTest {

	FlightFacade flightRepository = mock(FlightFacade.class) ;
	FlightService flightService = new FlightService();
	
	@Test
	public void testAmountFlightListFiltering(){
		//mocking the db query
		List<Flight> flights = new ArrayList<>();
		Flight flight = new Flight();
		flight.setDeparture(LocalDateTime.now().minus(2, ChronoUnit.HOURS));
		flights.add(flight);
		
		flight = new Flight();
		flight.setDeparture(LocalDateTime.now().minus(1, ChronoUnit.HOURS));
		flights.add(flight);
		
		flight = new Flight();
		flight.setDeparture(LocalDateTime.now().plus(1, ChronoUnit.HOURS));
		flights.add(flight);
		
		flight = new Flight();
		flight.setDeparture(LocalDateTime.now().plus(4, ChronoUnit.HOURS));
		flights.add(flight);
		
		flight = new Flight();
		flight.setDeparture(LocalDateTime.now().minus(6, ChronoUnit.HOURS));
		flights.add(flight);
		
		flight = new Flight();
		flight.setDeparture(LocalDateTime.now().plus(2, ChronoUnit.HOURS));
		flights.add(flight);
		
		when(flightRepository.findAllFlights()).thenReturn(flights);
		
		flightService.setFlightRepo(flightRepository);
		
		assertEquals(flightService.filterFlightsByDepartureTime(LocalDateTime.now(), 3).size(), 4);
	}
	
	@Test
	public void testContainsFlightListFiltering(){
		//mocking the db query
		List<Flight> flights = new ArrayList<>();
		Flight flight0 = new Flight();
		flight0.setDeparture(LocalDateTime.now().minus(2, ChronoUnit.HOURS));
		flights.add(flight0);
		
		Flight flight1 = new Flight();
		flight1.setDeparture(LocalDateTime.now().minus(1, ChronoUnit.HOURS));
		flights.add(flight1);
		
		Flight flight3 = new Flight();
		flight3.setDeparture(LocalDateTime.now().plus(1, ChronoUnit.HOURS));
		flights.add(flight3);
		
		Flight flight = new Flight();
		flight.setDeparture(LocalDateTime.now().plus(4, ChronoUnit.HOURS));
		flights.add(flight);
		
		flight = new Flight();
		flight.setDeparture(LocalDateTime.now().minus(6, ChronoUnit.HOURS));
		flights.add(flight);
		
		flight = new Flight();
		flight.setDeparture(LocalDateTime.now().plus(2, ChronoUnit.HOURS));
		flights.add(flight);
		
		when(flightRepository.findAllFlights()).thenReturn(flights);
		
		flightService.setFlightRepo(flightRepository);
		
		assertEquals(flightService.filterFlightsByDepartureTime(LocalDateTime.now(), 3).contains(flight), true);
		assertEquals(flightService.filterFlightsByDepartureTime(LocalDateTime.now(), 3).contains(flight1), true);
		assertEquals(flightService.filterFlightsByDepartureTime(LocalDateTime.now(), 3).contains(flight0), true);
		assertEquals(flightService.filterFlightsByDepartureTime(LocalDateTime.now(), 3).contains(flight3), true);
	}
}
