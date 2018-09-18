package entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Flight {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="from")
	private String from;
	
	@Column(name="destination")
	private String destination;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="departure_time")
	private Date departure;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="arrival_time")
	private Date arrival;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getDeparture() {
		return departure;
	}

	public void setDeparture(Date departure) {
		this.departure = departure;
	}

	public Date getArrival() {
		return arrival;
	}

	public void setArrival(Date arrival) {
		this.arrival = arrival;
	}
	
	
}
