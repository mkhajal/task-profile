package com.luxoft.ctask.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.luxoft.ctask.entity.FlightStatus;

public class CSVUtility {
	static final Logger log = LoggerFactory.getLogger(CSVUtility.class);
	
	public static final String FILE_TYPE = "text/csv";
	
	public static boolean isCSVFormat(MultipartFile file) {
		log.info("File uploaded with content type {}", file.getContentType());
		if(!FILE_TYPE.equals(file.getContentType())) {
			log.error("Invalid File type, Expected: {} Provided: {}",FILE_TYPE, file.getContentType());
			return false;
		}
		return true;
	}
	
	public static List<FlightStatus> readCSVFile(InputStream is) {
		List<FlightStatus> flightData = new LinkedList<>();
		try {
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			CSVFormat csvFormat = CSVFormat.Builder
											.create(CSVFormat.DEFAULT)
											.setHeader(FLIGHT_FEED_STATUS_HEADERS.class).setSkipHeaderRecord(true)
											.build();
				
			CSVParser csvParser = csvFormat.parse(fileReader);
			List<CSVRecord> csvLineData = csvParser.getRecords();
			log.info("A total of {} records found in the uploaded CSV file for processing.", csvLineData.size() );
			for(CSVRecord csvLine : csvLineData) {
				
				boolean validData = validateCSVData(csvLine);
				if(!validData) {
					log.error("Invalid record identifed, skipping record number {}", csvLine.getRecordNumber());
					continue;
				}
				FlightStatus flight = new FlightStatus();
				
				String csvFlightId = csvLine.get(FLIGHT_FEED_STATUS_HEADERS.FLIGHT_ID);
				flight.setFlightId(csvFlightId);
				String csvFlightAirlines = csvLine.get(FLIGHT_FEED_STATUS_HEADERS.AIRLINES);
				flight.setAirlines(csvFlightAirlines);
				String csvFlightDesc = csvLine.get(FLIGHT_FEED_STATUS_HEADERS.FLIGHT_DESCRIPTION);
				flight.setFlightDesc(csvFlightDesc);
				OffsetDateTime csvFlightTime = OffsetDateTime.parse(csvLine.get(FLIGHT_FEED_STATUS_HEADERS.LAST_REFRESHED_TIMESTAMP));
				flight.setUpdatedTimestamp(csvFlightTime);
				
				flightData.add(flight);
			}
		} catch(IOException e) {
			log.error("Error while reading uploaded CSV File", e.getMessage());
			return new LinkedList<>();
		}
		return flightData;
	}
	
	public static boolean validateCSVData(CSVRecord csvLine) {
		String csvFlightId = csvLine.get(FLIGHT_FEED_STATUS_HEADERS.FLIGHT_ID);
		log.info("flight id {}",csvFlightId);
		if(csvFlightId.isBlank()) {
			log.error("Invalid data for flight id {}", csvFlightId);
			return false;
		}
		String csvFlightTime = csvLine.get(FLIGHT_FEED_STATUS_HEADERS.LAST_REFRESHED_TIMESTAMP);
		if(csvFlightTime.isEmpty()) {
			log.error("Empty timestamp data for flight timestamp {} with id {}",csvFlightTime, csvFlightId);
			return false;
		}
		try {
			OffsetDateTime.parse(csvFlightTime);
		} catch(DateTimeParseException e) {
			log.error("Unparseable timestamp data for flight timestamp {} with id {}",csvFlightTime, csvFlightId);
			return false;
		}
		return true;
	}

	
	public enum FLIGHT_FEED_STATUS_HEADERS {
		FLIGHT_ID,AIRLINES,FLIGHT_DESCRIPTION,LAST_REFRESHED_TIMESTAMP
	}
	
	/*
	public static void main(String[] abc) throws IOException {
		String filename = "C:\\Work\\flight_status_feed.csv";
		InputStream is = new FileInputStream(new File(filename));
		List<FlightStatus> flights = readCSVFile(is);
		System.out.println("number of valid records {}"+ flights.size());
		flights.forEach(f -> System.out.println(f));
	}
	*/
}
