# task-profile

This is a Spring Boot application which handles Multipart File upload using HTTP request which process/validate the CSV data and persist it into the SQL DB (H2)

1. Git Clone and checkout the folder.
2. Requires Java 11 and Maven
3. Run Mvn clean, install and run as a Spring Boot app.
4. Refer H2 console and ensure FLIGHT_SATUS table is created in H2 custom datasource, "flightdb"
5. Execute REST endpoints speciifed in the src/main/resources/rest/commands.rest
6. DB scripts are available in class path src/main/resources
7. A sample CSV file with flight status data is provided in classpath flight_feed_status.csv

Note: Redeploying the application cleans up the persistent store and drops the SQL table FLIGHT_STATUS
