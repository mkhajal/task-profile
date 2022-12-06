package com.luxoft.ctask.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.luxoft.ctask.data.NormalResult;
import com.luxoft.ctask.entity.FlightStatus;
import com.luxoft.ctask.service.FlightStatusService;
import com.luxoft.ctask.util.CSVUtility;

@RestController
@RequestMapping("flight/api")
public class FlightController {
	static final Logger log = LoggerFactory.getLogger(FlightController.class);
	
	@Autowired
	private FlightStatusService flightService;
	
	@PostMapping("/uploadCSV")
	public ResponseEntity<NormalResult> uploadMultipartFile(@RequestParam("file") MultipartFile file) {
		log.info("CSV File upload, input file type {}", file.getContentType());
		NormalResult result = new NormalResult("Failed", "Unknown Error");
		if(!CSVUtility.isCSVFormat(file)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NormalResult("Failed", "Upload a valid CSV file"));
		}
		try {
			List<FlightStatus> flightData = flightService.processCSVFile(file);
						
			result = new NormalResult("Success", "CSV File upload successful");
			result.setData(flightData);
		} catch(Exception e) {
			result = new NormalResult("Failed", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@GetMapping("status")
	public ResponseEntity<NormalResult> getAllFlightStatus(@RequestParam(value="id",required=false) String id) {
		NormalResult result = new NormalResult("Failed", "Unknown Error");
		try {
			if(null != id) {
				Optional<FlightStatus> flightStatus = flightService.fetchSpecificFlightStatus(id);
				if(flightStatus.isPresent()) {
					result = new NormalResult("Success", "Flight Status found with id ["+id+"]");
					result.setData(flightStatus.get());			
					return ResponseEntity.status(HttpStatus.OK).body(result);
				} else {
					result = new NormalResult("NOT_FOUND", "Flight Status was not found with id ["+id+"]");
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
				}
			}
			List<FlightStatus> flightData = flightService.fetchFlightStatusData();
			
			if(flightData.isEmpty()) {
				result = new NormalResult("Success", "No Content");
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
			}
			result = new NormalResult("Success", "Flight Status results count "+flightData.size());
			result.setData(flightData);			
			return ResponseEntity.status(HttpStatus.OK).body(result);
			//return new ResponseEntity<NormalResult>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<NormalResult>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/status/remove")
	public ResponseEntity<NormalResult> deleteFlightStatus(@RequestParam(value="id",required=true) String id) {
		NormalResult result = new NormalResult("Failed", "Unknown Error");
		
		int count = flightService.deleteSpecificFlightStatus(id);
		if(count > 0) {
			result = new NormalResult("Success", "Flight Status deleted with id ["+id+"]");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			result = new NormalResult("NOT_FOUND", "Flight Status was not found with id ["+id+"]");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		}
	}	
}
