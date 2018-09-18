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
	
	public void createFlight(Flight flight){
		
		flight.setArrived(false);
		flightRepository.create(flight);
	}
	
	public boolean isFlightDelayed(Flight flight){
		
		if(flight.getArrival().isBefore(LocalDateTime.now()) && !flight.isArrived()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void updateFlight(Flight flightToUpdate){
		
		flightRepository.update(flightToUpdate);
	}
	
	public void deleteFlight(Flight flightToDelet){
		flightRepository.remove(flightToDelet);
	}
	
	public int calculateAverageDelayInMinutes(){
		
		return 0;
	}
}
