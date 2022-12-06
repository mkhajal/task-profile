package com.luxoft.ctask.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "flight_status")
public class FlightStatus {
	@Id
	@Column(name = "FLIGHT_ID")
	private String flightId;
	
	@Column(name = "AIRLINES")
	private String airlines;
	
	@Column(name = "FLIGHT_DESCRIPTION")
	private String flightDesc;
	
	@Column(name = "LAST_REFRESHED_TIMESTAMP")
	private OffsetDateTime updatedTimestamp;

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public String getAirlines() {
		return airlines;
	}

	public void setAirlines(String airlines) {
		this.airlines = airlines;
	}

	public String getFlightDesc() {
		return flightDesc;
	}

	public void setFlightDesc(String flightDesc) {
		this.flightDesc = flightDesc;
	}

	public OffsetDateTime getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	public void setUpdatedTimestamp(OffsetDateTime updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}

	@Override
	public String toString() {
		return "FlightStatus [flightId=" + flightId + ", airlines=" + airlines + ", flightDesc=" + flightDesc
				+ ", updatedTimestamp=" + updatedTimestamp + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(airlines, flightDesc, flightId, updatedTimestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlightStatus other = (FlightStatus) obj;
		return Objects.equals(airlines, other.airlines) && Objects.equals(flightDesc, other.flightDesc)
				&& Objects.equals(flightId, other.flightId) && Objects.equals(updatedTimestamp, other.updatedTimestamp);
	}

}
