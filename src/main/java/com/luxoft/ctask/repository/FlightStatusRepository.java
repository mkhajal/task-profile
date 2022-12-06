package com.luxoft.ctask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.luxoft.ctask.entity.FlightStatus;

public interface FlightStatusRepository extends JpaRepository<FlightStatus, String> {
	@Modifying
	@Query(value = "DELETE FROM FlightStatus fs WHERE fs.flightId = ?1 ")
	int deleteFlightStatusById(String id); // To get deleted record
}
