package services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import dal.FlightFacade;
import entities.Flight;
import interceptors.LoggingInterceptor;
import utility.DateTimeUtil;

@Stateless
public class FlightService {
	
	private final Logger logger = Logger.getLogger(FlightService.class.getName());
	
	@Inject
	private FlightFacade flightRepository;
	@Resource
	private ManagedExecutorService executor;
	
	
	@Interceptors(LoggingInterceptor.class)
	public List<Flight> getAllFlightsList(){
		
		List<Flight> flights = flightRepository.findAllFlights();
		
		flights = flights.stream()
			   .sorted( (f1,f2) -> (f1.getDeparture().isBefore(f2.getDeparture()) ? -1 : 1))
			   .collect(Collectors.toList());
		
		flights.stream().forEach(this::logFlight);
			   
		return flights;
	}
	
	@Interceptors(LoggingInterceptor.class)
	public void createFlight(Flight flight){
		
		flight.setArrived(false);
		flightRepository.create(flight);
		
		logger.log(Level.INFO, "flight created");
		logFlight(flight);
	}
	
	public boolean isFlightDelayed(Flight flight){
		
		if(flight.getArrival().isBefore(LocalDateTime.now()) && !flight.isArrived()){
			return true;
		}
		else{
			return false;
		}
	}
	
	@Interceptors(LoggingInterceptor.class)
	public void updateFlight(Flight flightToUpdate){
		
		flightRepository.update(flightToUpdate);
		
		logger.log(Level.INFO, "flight udpated");
		logFlight(flightToUpdate);
	}
	
	@Interceptors(LoggingInterceptor.class)
	public void deleteFlight(Flight flightToDelet){
		
		flightRepository.remove(flightToDelet);
		
		logger.log(Level.INFO, "flight deleted");
		logFlight(flightToDelet);
	}
	
	@Interceptors(LoggingInterceptor.class)
	public double getAverageDelayInMinutes(){
		
		List<Flight> flights = flightRepository.findAllFlights();
		double averageDelay;
		
		try{
			averageDelay = flights.stream()
							      .filter( flight -> isFlightDelayed(flight))
							      .mapToLong( flight -> DateTimeUtil.millisecondsBetween(LocalDateTime.now(), flight.getArrival()))
							      .average()
							      .getAsDouble();
		}catch (NoSuchElementException e) {
			logger.log(Level.INFO, "unable to calculate average delay");
			//e.printStackTrace();
			averageDelay = 0;
		}
		
		logger.log(Level.INFO, "average delay calculated: " + averageDelay);
		
		return averageDelay / DateTimeUtil.MILIS_IN_MIN;
	}
	
	@Interceptors(LoggingInterceptor.class)
	public double getAverageDelayInMinutesParalell(){
		
		double averageDelay = 0;
		List<Flight> flights = flightRepository.findAllFlights();
		
		//separate the flights into two equivalent halves
		List<Flight> flightsPart1 = new ArrayList<>();
		List<Flight> flightsPart2 = new ArrayList<>();
		for(int i = 0; i < flights.size(); ++i){
			if(i < flights.size() /2){
				flightsPart1.add(flights.get(i));
			}
			else{
				flightsPart2.add(flights.get(i));
			}
		}
		
		//executing task in two threads
		Future<Double> avgDelay1 = executor.submit( new DelayCounterWorker(flightsPart1));
		Future<Double> avgDelay2 = executor.submit( new DelayCounterWorker(flightsPart2));
		
		//if any of the averages is zero then the division messes up the result therefore cases are separated
		try {
			if(avgDelay1.get() == 0){
				averageDelay = avgDelay2.get();
			}
			else if(avgDelay2.get() == 0){
				averageDelay = avgDelay1.get();
			}
			else{
				averageDelay = (avgDelay1.get() + avgDelay2.get()) / 2;
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.log(Level.INFO, "Paralell delay calculation was interupted" + e.getMessage());
		}
		
		return averageDelay / DateTimeUtil.MILIS_IN_MIN;
	}
	
	
	@Interceptors(LoggingInterceptor.class)
	public Flight getFlightWithBiggestDelay(){
		
		List<Flight> flights = flightRepository.findAllFlights();
		
		Flight flightWBigestDelay = null;
		try{
			//compares all the delayed flights by their delay  ( using functional interface of Comparator)
			flightWBigestDelay = flights.stream()
				   .filter(flight -> isFlightDelayed(flight))
				   .max((f1,f2) -> (int)(DateTimeUtil.millisecondsBetween(LocalDateTime.now(), f1.getArrival()) 
						   				- DateTimeUtil.millisecondsBetween(LocalDateTime.now(), f2.getArrival())))
				   .get();
			logger.log(Level.INFO, "Found flight with biggest delay, flight id : " + flightWBigestDelay.getId());
		}catch (NoSuchElementException e) {
			logger.log(Level.INFO, "unable to calculate most delayed flight due to lack of delays");
		}
		
		
		
		return flightWBigestDelay;
	}
	
	//Not wired to the frontend, tests available
	public List<Flight> filterFlightsByDepartureTime(LocalDateTime time, int intervalInHours){
		
		List<Flight> allFlights = flightRepository.findAllFlights();
		LocalDateTime intervalBeforeSpecifiedTime = time.minus(intervalInHours, ChronoUnit.HOURS);
		LocalDateTime intervalAfterSpecifiedTime = time.plus(intervalInHours, ChronoUnit.HOURS);
		return allFlights.stream()
				  .filter( flight -> flight.getDeparture().isAfter(intervalBeforeSpecifiedTime)
						  			 && flight.getDeparture().isBefore(intervalAfterSpecifiedTime))
				  .collect(Collectors.toList());
	}
	
	private void logFlight(Flight flight){
		logger.log(Level.INFO, flight.getFrom() + ":"  + flight.getDestination() + ":"  + flight.getId());
	}
	
	//this is solely for testing purposes (couldnt inject the mock)
	public void setFlightRepo(FlightFacade repo){
		flightRepository = repo;
	}
}
