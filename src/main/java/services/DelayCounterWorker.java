package services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.interceptor.Interceptors;

import entities.Flight;
import interceptors.Logged;
import interceptors.LoggingInterceptor;
import utility.DateTimeUtil;


public class DelayCounterWorker implements Callable<Double> {
	
	private final Logger logger = Logger.getLogger(DelayCounterWorker.class.getName());
	
	private List<Flight> flightsToProcess;
	
	public DelayCounterWorker(List<Flight> flights) {
		this.flightsToProcess = flights;
	}
	
	//it works on the recieved list
	private boolean isFlightDelayed(Flight flight){
		
		if(flight.getArrival().isBefore(LocalDateTime.now()) && !flight.isArrived()){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public Double call() throws Exception {
		logger.log(Level.INFO, "delayCounterWorker started running");
		
		double averageDelay;
		
		try{
			averageDelay = flightsToProcess.stream()
							      .filter( flight -> isFlightDelayed(flight))
							      .mapToLong( flight -> DateTimeUtil.millisecondsBetween(LocalDateTime.now(), flight.getArrival()))
							      .average()
							      .getAsDouble();
		}catch (NoSuchElementException e) {
			e.printStackTrace();
			averageDelay = 0;
		}
		
		logger.log(Level.INFO, "delayCounterWorker done");
		return averageDelay;
	}

}
