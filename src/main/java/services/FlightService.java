package services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import dal.FlightFacade;
import entities.Flight;

@Stateless
public class FlightService {
	
	@Inject
	private FlightFacade flightRepository;

	public List<Flight> getAllFlightsList(){
		
		List<Flight> flights = flightRepository.findAllFlights();
		
		// additional operations may be done on the list like sorting and filtering
		
		return flights;
	}
}
