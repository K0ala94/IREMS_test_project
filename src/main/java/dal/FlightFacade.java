package dal;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entities.Flight;

@Dependent
public class FlightFacade extends DBFacade<Flight> {
	
	private static final String ALL_FLIGHTS_QUERY = "SELECT flight FROM Flight flight";

	@PersistenceContext
	private EntityManager em;
	
	public FlightFacade(){
		super(Flight.class);
	};
	
	public FlightFacade(Class<Flight> entityClass) {
		super(entityClass);
	}

	@Override
	protected EntityManager getEm() {
		return em;
	}

	public List findAllFlights() {
		
		Query query = em.createQuery(ALL_FLIGHTS_QUERY);
		return query.getResultList();
	}
	
	

}
