package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.model.Trip;
import com.ducnh.bikeshare.model.TripHolder;
import com.google.cloud.bigquery.*;
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
public class TripService {
    private static final Logger log = LoggerFactory.getLogger(TripService.class);

    public TripHolder getTrips(String query) {
        log.info("query started");

        TripHolder tripHolder = new TripHolder();
        List<Trip> trips = new ArrayList<>();
        tripHolder.setTrips(trips);
        tripHolder.setStatus("Failed");
        tripHolder.setCount(0);

        BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
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
                return tripHolder;
            }

            if (queryJob.getStatus().getError() != null) {
                log.info("queryJob status: {}", queryJob.getStatus().getError());
                return tripHolder;
            }

//            TableResult result = queryJob.getQueryResults();
            BigQuery.QueryResultsOption queryResultsOption = BigQuery.QueryResultsOption.pageSize(20);

            TableResult result = queryJob.getQueryResults(queryResultsOption);

            for (FieldValueList row : result.iterateAll()) {
                String trip_id = !row.get("trip_id").isNull() ? row.get("trip_id").getStringValue() : "";
                String subscriber_type = !row.get("subscriber_type").isNull() ? row.get("subscriber_type").getStringValue() : "";
                String bike_id = !row.get("bike_id").isNull() ? row.get("bike_id").getStringValue() : "";
                String bike_type = !row.get("bike_type").isNull() ? row.get("bike_type").getStringValue() : "";
                ZoneId zoneId = ZoneId.of("UTC");
                LocalDateTime start_time = !row.get("start_time").isNull() ? row.get("start_time").getTimestampInstant().atZone(zoneId).toLocalDateTime() : null;
//                int start_station_id = !row.get("start_station_id").isNull() ? row.get("start_station_id").getNumericValue().intValue() : 0;
                String start_station_name = !row.get("start_station_name").isNull() ? row.get("start_station_name").getStringValue() : "";
//                String end_station_id = !row.get("end_station_id").isNull() ? row.get("end_station_id").getStringValue() : "";
                String end_station_name = !row.get("end_station_name").isNull() ? row.get("end_station_name").getStringValue() : "";
                int duration_minutes = !row.get("duration_minutes").isNull() ? row.get("duration_minutes").getNumericValue().intValue() : 0;

                Trip trip = new Trip(trip_id, subscriber_type, bike_id, bike_type, start_time, start_station_name, end_station_name, duration_minutes);
                trips.add(trip);
            }

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            log.error("Error: {}", e.getMessage());
            return tripHolder;
        }

        tripHolder.setCount(trips.size());
        tripHolder.setStatus("Success");
        return tripHolder;
    }

    public String convertTime(String time) {
        String modified = time.substring(0, 10) + ' ' + time.substring(11);
        modified = modified.concat(":00");
        return modified;
    }
}
