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

public class DelayTest {

	FlightFacade flightRepository = mock(FlightFacade.class) ;
	FlightService flightService = new FlightService();
	
	
	@Test
	public void averageWithZeroFlights(){
		
		when(flightRepository.findAllFlights()).thenReturn(new ArrayList<Flight>());
		
		flightService.setFlightRepo(flightRepository);
		assertEquals( flightService.getAverageDelayInMinutes(), 0, 0.1);
	}
	
	@Test
	public void averageWithOneDelayedFlight(){
		
		List<Flight> flights = new ArrayList<>();
		Flight lateFlight = new Flight();
		lateFlight.setArrival(LocalDateTime.now().minus(2, ChronoUnit.HOURS));
		flights.add(lateFlight);
		when(flightRepository.findAllFlights()).thenReturn(flights);
		
		flightService.setFlightRepo(flightRepository);
		assertEquals( flightService.getAverageDelayInMinutes(), 120, 0.1);
	}
	
	@Test
	public void averageWithMultipleDelayedFlight(){
		
		//mocking data from db
		List<Flight> flights = new ArrayList<>();
		Flight lateFlight = new Flight();
		lateFlight.setArrival(LocalDateTime.now().minus(2, ChronoUnit.HOURS));
		flights.add(lateFlight);
		
		lateFlight = new Flight();
		lateFlight.setArrival(LocalDateTime.now().minus(6, ChronoUnit.HOURS));
		flights.add(lateFlight);
		
		lateFlight = new Flight();
		lateFlight.setArrival(LocalDateTime.now().minus(4, ChronoUnit.HOURS));
		flights.add(lateFlight);
		
		when(flightRepository.findAllFlights()).thenReturn(flights);
		
		flightService.setFlightRepo(flightRepository);
		//240 = is the average of the 2,4 and 6 hour delays in mins
		assertEquals( flightService.getAverageDelayInMinutes(), 240, 0.1);
	}
	
	@Test
	public void findMostDelayedWithMultipleDelayedFlight(){
		
		//mocking data from db
		List<Flight> flights = new ArrayList<>();
		Flight lateFlight = new Flight();
		lateFlight.setArrival(LocalDateTime.now().minus(2, ChronoUnit.HOURS));
		flights.add(lateFlight);
		
		lateFlight = new Flight();
		lateFlight.setArrival(LocalDateTime.now().minus(4, ChronoUnit.HOURS));
		flights.add(lateFlight);
		
		lateFlight = new Flight();
		lateFlight.setArrival(LocalDateTime.now().minus(6, ChronoUnit.HOURS));
		flights.add(lateFlight);
		
		when(flightRepository.findAllFlights()).thenReturn(flights);
		
		flightService.setFlightRepo(flightRepository);
		assertEquals( flightService.getFlightWithBiggestDelay(), lateFlight);
	}
}
