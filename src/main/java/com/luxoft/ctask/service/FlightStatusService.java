package com.luxoft.ctask.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.luxoft.ctask.entity.FlightStatus;
import com.luxoft.ctask.repository.FlightStatusRepository;
import com.luxoft.ctask.util.CSVUtility;

@Service
public class FlightStatusService {
	
	static final Logger log = LoggerFactory.getLogger(FlightStatusService.class);
	
	@Autowired
	private FlightStatusRepository flightStatusRepository;
	
	public List<FlightStatus> processCSVFile(MultipartFile file) {
		List<FlightStatus> flightData = new LinkedList<>();
		try {
			flightData = CSVUtility.readCSVFile(file.getInputStream());
			if(flightData.isEmpty()) {
				log.error("Failed to read flight status from uploaded CSV file, empty records processed");
			}
			log.info("Flight Status count to be updated in the database is {}", flightData.size());
			flightStatusRepository.saveAll(flightData);
			
		} catch(IOException e) {
			log.error("Error while processing Flight status data from CSV file", e.getMessage());
			throw new RuntimeException("Failed to prcess CSV file" + e.getMessage());
		}
		return flightData;
	}
	
	@Cacheable("flightid")
	public Optional<FlightStatus> fetchSpecificFlightStatus(String flightId) {
		return flightStatusRepository.findById(flightId);
	}
	
	public List<FlightStatus> fetchFlightStatusData() {
		return flightStatusRepository.findAll();
	}
	
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public int deleteSpecificFlightStatus(String flightId) {
		return flightStatusRepository.deleteFlightStatusById(flightId);
	}
}
