package controllers;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

import entities.Flight;
import services.FlightService;

@Named
@RequestScoped
public class FlightsWebController {
	
	@EJB
	private FlightService flightService;
	
	private DataModel<Flight> allFlightsDataModel;
	private List<Flight> allFlightsList;
	
	public String test(){
		return "hello irems";
	}

	//loads data from service if not initialized
	public DataModel<Flight> getAllFlightsDataModel() {
		
		if(allFlightsDataModel == null){
			List<Flight> allFlights = flightService.getAllFlightsList();
			allFlightsDataModel = new ListDataModel<>(allFlights);
		}
		return allFlightsDataModel;
	}

	public void setAllFlightsDataModel(DataModel<Flight> allFlightsData) {
		this.allFlightsDataModel = allFlightsData;
	}

	public List<Flight> getAllFlightsList() {
		if(allFlightsList == null){
			allFlightsList =flightService.getAllFlightsList();
		}
		return allFlightsList;
	}

	public void setAllFlightsList(List<Flight> allFlightsList) {
		this.allFlightsList = allFlightsList;
	}
	

}
