DROP TABLE if exists FLIGHT_STATUS;

CREATE TABLE flight_status (
	FLIGHT_ID VARCHAR(10) NOT NULL,
	AIRLINES VARCHAR(50) NOT NULL,
	FLIGHT_DESCRIPTION VARCHAR(255),
	LAST_REFRESHED_TIMESTAMP TIMESTAMP,
	PRIMARY KEY (FLIGHT_ID)
);