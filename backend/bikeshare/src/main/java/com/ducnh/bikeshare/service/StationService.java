package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.model.Station;
import com.ducnh.bikeshare.model.StationHolder;
import com.google.cloud.bigquery.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class StationService {
    private static final Logger log = LoggerFactory.getLogger(StationService.class);

    public StationHolder getStations() {
        log.info("query started");

        StationHolder stationHolder = new StationHolder();
        List<Station> stations = new ArrayList<>();
        stationHolder.setStations(stations);
        stationHolder.setStatus("Failed");
        stationHolder.setCount(0);

        BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(
                        "SELECT * "
                                + "FROM `springboot-bq.bikeshare.stations`"
                                + "LIMIT 1000")
                .setUseLegacySql(false)
                .build();

        String jobIdStr = UUID.randomUUID().toString();
        log.info("jobIdStr: {}", jobIdStr);

        JobId jobId = JobId.of(jobIdStr);

        Job queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        try {
            queryJob = queryJob.waitFor();
            if (queryJob == null) {
                log.info("queryJob is null");
                return stationHolder;
            }

            if (queryJob.getStatus().getError() != null) {
                log.info("queryJob status: {}", queryJob.getStatus().getError());
                return stationHolder;
            }

            TableResult result = queryJob.getQueryResults();

            for (FieldValueList row : result.iterateAll()) {
                int station_id = row.get("station_id").getNumericValue().intValue();
                String name = row.get("name").getStringValue();
                String status = row.get("status").getStringValue();
                String location = row.get("location").getStringValue();
                String address = row.get("address").getStringValue();
                String alternate_name = row.get("alternate_name").getStringValue();
                int city_asset_number = row.get("city_asset_number").getNumericValue().intValue();
                String property_type = row.get("property_type").getStringValue();
                int number_of_docks = row.get("number_of_docks").getNumericValue().intValue();
                String power_type = row.get("power_type").getStringValue();
                int footprint_length = row.get("footprint_length").getNumericValue().intValue();
                float footprint_width = row.get("footprint_width").getNumericValue().floatValue();
                String notes = row.get("notes").getStringValue();
                int council_district = row.get("council_district").getNumericValue().intValue();
                // modified date to add later
                log.info(name);
                Station station = new Station(station_id, name, status, location, address, alternate_name, city_asset_number, property_type, number_of_docks, power_type, footprint_length,
                        footprint_width, notes, council_district);
                log.info(station.toString());

                stations.add(station);

            }

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            log.error("Error: {}", e.getMessage());
            return stationHolder;
        }

        stationHolder.setCount(stations.size());
        stationHolder.setStatus("Success");
        return stationHolder;
    }
}
