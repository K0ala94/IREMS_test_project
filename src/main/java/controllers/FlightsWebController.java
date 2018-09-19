package controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

import entities.Flight;
import interceptors.Logged;
import services.FlightService;
import utility.DateTimeUtil;

@Named
@ApplicationScoped
public class FlightsWebController {
	
	//provides business logic that manages flights
	@EJB
	private FlightService flightService;
	
	private List<Flight> allFlightsList;
	private Flight newFlight;
	private Flight flightToEdit;
	//indicates wether the user is editing a flight (used in getNewArrivalTime for date conversion)
	private boolean edit;
	private long biggestDelayInMins;
	
	public double getAverageDelay(){
		return flightService.getAverageDelayInMinutesParalell();
	}
	
	@Logged
	public Flight getFlightWithBiggestDelay(){
		
		Flight lateFlight = flightService.getFlightWithBiggestDelay();
		if(lateFlight != null){
			biggestDelayInMins = DateTimeUtil.millisecondsBetween(LocalDateTime.now(), lateFlight.getArrival()) / DateTimeUtil.MILIS_IN_MIN;
		}
		else{
			lateFlight = new Flight();
		}
		return lateFlight;
	}
	
	//determines whether the flight has surpassed its arrival time and has not yet landed
	public boolean isFlightDelayed(Flight flight){
		
		return flightService.isFlightDelayed(flight);
	}
	
	@Logged
	public String createNewFlight(){
		
		flightService.createFlight(newFlight);
		
		return "flight_browser?faces-redirect=true";
	}
	
	//initialized if null, JSF calls getters on it so it must not be null
	public String preCreate(){
		
		newFlight = new Flight();
		return "new_flight?faces-redirect=true";
	}
	
	//initializes the variable that is backing input fields
	public String preEdit(Flight flight){
		
		edit = true;
		flightToEdit = flight;
		
		return "edit_flight?faces-redirect=true";
	}
	
	@Logged
	public String editFlight(){
		
		flightService.updateFlight(flightToEdit);
		//flightToEdit = null;
		edit = false;
		return "flight_browser?faces-redirect=true";
	}
	
	@Logged
	public String deleteFlight(Flight flightToDelete){
		
		flightService.deleteFlight(flightToDelete);
		return "flight_browser?faces-redirect=true";
	}

	//loads flight data from backend service
	@Logged
	public List<Flight> getAllFlightsList() {
		
		allFlightsList = flightService.getAllFlightsList();
		return allFlightsList;
	}

	public void setAllFlightsList(List<Flight> allFlightsList) {
		this.allFlightsList = allFlightsList;
	}

	public Flight getNewFlight() {
		if(newFlight == null){
			newFlight = new Flight();
		}
		return newFlight;
	}

	public void setNewFlight(Flight newFlight) {
		this.newFlight = newFlight;
	}

	//depending on whether we are editing, converts arrival time to string to be displayed
	public String getNewArrivalTime() {
		if(!edit){
			return "";
		}
		else{
			return DateTimeUtil.convertLocalDateTimeToString(flightToEdit.getArrival());
		}
	}

	//JSF sends the input as String so it needs to be converted to LocalDateTime
	public void setNewArrivalTime(String newArrivalTime) {
		if(!edit){
			newFlight.setArrival(DateTimeUtil.convertStringtoLocalDateTime(newArrivalTime));
		}
		else{
			flightToEdit.setArrival(DateTimeUtil.convertStringtoLocalDateTime(newArrivalTime));
		}
	}

	//depending on whether we are editing, converts departure time to string to be displayed
	public String getNewDepartureTime() {
		
		if(!edit){
			return "";
		}
		else{
			return DateTimeUtil.convertLocalDateTimeToString(flightToEdit.getDeparture());
		}
		
	}

	//JSF sends the input as String so it needs to be converted to LocalDateTime
	public void setNewDepartureTime(String newDepartureTime) {
		
		if(!edit){
			newFlight.setDeparture(DateTimeUtil.convertStringtoLocalDateTime(newDepartureTime));
		}
		else{
			flightToEdit.setDeparture(DateTimeUtil.convertStringtoLocalDateTime(newDepartureTime));
		}
	}

	public Flight getFlightToEdit() {
		return flightToEdit;
	}

	public void setFlightToEdit(Flight flightToEdit) {
		this.flightToEdit = flightToEdit;
	}

	public long getBiggestDelayInMins() {
		return biggestDelayInMins;
	}

	public void setBiggestDelayInMins(long biggestDelayInMins) {
		this.biggestDelayInMins = biggestDelayInMins;
	}
	
}
