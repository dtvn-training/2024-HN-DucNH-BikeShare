package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.model.Station;
import com.ducnh.bikeshare.model.StationHolder;
import com.google.cloud.bigquery.*;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
                                + "FROM `springboot-bq.bikeshare.stations`")
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
                int station_id = !row.get("station_id").isNull() ? row.get("station_id").getNumericValue().intValue() : 0;
                String name = !row.get("name").isNull() ? row.get("name").getStringValue() : "";
                String status = !row.get("status").isNull() ? row.get("status").getStringValue() : "";
                String location = !row.get("location").isNull() ? row.get("location").getStringValue() : "";
                String address = !row.get("address").isNull() ? row.get("address").getStringValue() : "";
                String alternate_name = !row.get("alternate_name").isNull() ? row.get("alternate_name").getStringValue() : "";
                int city_asset_number = !row.get("city_asset_number").isNull() ? row.get("city_asset_number").getNumericValue().intValue() : 0;
                String property_type = !row.get("property_type").isNull() ? row.get("property_type").getStringValue() : "";
                int number_of_docks = !row.get("number_of_docks").isNull() ? row.get("number_of_docks").getNumericValue().intValue() : 0;
                String power_type = !row.get("power_type").isNull() ? row.get("power_type").getStringValue() : "";
                int footprint_length = !row.get("footprint_length").isNull() ? row.get("footprint_length").getNumericValue().intValue() : 0;
                float footprint_width = !row.get("footprint_width").isNull() ? row.get("footprint_width").getNumericValue().floatValue() : 0f;
                String notes = !row.get("notes").isNull() ? row.get("notes").getStringValue() : "";
                int council_district = !row.get("council_district").isNull() ? row.get("council_district").getNumericValue().intValue() : 0;
                ZoneId zoneId = ZoneId.of("UTC");
                LocalDateTime modified_date = !row.get("modified_date").isNull() ? row.get("modified_date").getTimestampInstant().atZone(zoneId).toLocalDateTime() : null;

                Station station = new Station(station_id, name, status, location, address, alternate_name, city_asset_number, property_type, number_of_docks, power_type, footprint_length,
                        footprint_width, notes, council_district, modified_date);

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