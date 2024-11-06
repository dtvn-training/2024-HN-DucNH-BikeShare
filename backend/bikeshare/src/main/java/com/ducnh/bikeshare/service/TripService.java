package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.constant.Constant;
import com.ducnh.bikeshare.dto.TripParamDTO;
import com.ducnh.bikeshare.model.Trip;
import com.ducnh.bikeshare.model.TripHolder;
import com.google.cloud.bigquery.*;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TripService implements ITripService{
    private static final Logger log = LoggerFactory.getLogger(TripService.class);

    @Override
    public TripHolder getTrips(TripParamDTO params) {
        log.info("query started");

        TripHolder tripHolder = new TripHolder();
        List<Trip> trips = new ArrayList<>();
        tripHolder.setTrips(trips);
        tripHolder.setStatus("Failed");
        tripHolder.setCount(0);

        String query = parsedQuery(params);
        log.info(query);

        Job queryJob = StationService.createJob(query);

        getResponseData(queryJob, tripHolder);

        return tripHolder;
    }

    private void getResponseData(Job queryJob, TripHolder tripHolder) {
        try {
            queryJob = queryJob.waitFor();
            if (queryJob == null) {
                log.info("queryJob is null");
                return;
            }

            if (queryJob.getStatus().getError() != null) {
                log.info("queryJob status: {}", queryJob.getStatus().getError());
                return;
            }

            TableResult result = queryJob.getQueryResults();

            for (FieldValueList row : result.iterateAll()) {
                String trip_id = !row.get("trip_id").isNull() ? row.get("trip_id").getStringValue() : "";
                String subscriber_type = !row.get("subscriber_type").isNull() ? row.get("subscriber_type").getStringValue() : "";
                String bike_id = !row.get("bike_id").isNull() ? row.get("bike_id").getStringValue() : "";
                String bike_type = !row.get("bike_type").isNull() ? row.get("bike_type").getStringValue() : "";
                ZoneId zoneId = ZoneId.of("UTC");
                LocalDateTime start_time = !row.get("start_time").isNull() ? row.get("start_time").getTimestampInstant().atZone(zoneId).toLocalDateTime() : null;
                String start_station_name = !row.get("start_station_name").isNull() ? row.get("start_station_name").getStringValue() : "";
                String end_station_name = !row.get("end_station_name").isNull() ? row.get("end_station_name").getStringValue() : "";
                int duration_minutes = !row.get("duration_minutes").isNull() ? row.get("duration_minutes").getNumericValue().intValue() : 0;

                Trip trip = new Trip(trip_id, subscriber_type, bike_id, bike_type, start_time, start_station_name, end_station_name, duration_minutes);
                tripHolder.getTrips().add(trip);
            }

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            log.error("Error: {}", e.getMessage());
            return;
        }

        tripHolder.setCount(tripHolder.getTrips().size());
        tripHolder.setStatus("Success");
    }

    public String convertTime(String time) {
        String modified = time.substring(0, 10) + ' ' + time.substring(11);
        modified = modified.concat(":00");
        return modified;
    }

    private String parsedQuery(TripParamDTO params) {
        return new SQL(){{
            SELECT("trip_id, subscriber_type, bike_id, bike_type, start_time, start_station_name, end_station_name, duration_minutes");
            FROM(Constant.TABLE_TRIPS);
            if (!Objects.equals(params.getTrip_id(), "")) WHERE(String.format("REGEXP_CONTAINS(trip_id,r'(?i)%s')", params.getTrip_id()));
            if (!Objects.equals(params.getSubscriber_type(), "")) WHERE(String.format("REGEXP_CONTAINS(subscriber_type,r'(?i)%s')", params.getSubscriber_type()));
            if (!Objects.equals(params.getBike_id(), "")) WHERE(String.format("REGEXP_CONTAINS(bike_id,r'(?i)%s')", params.getBike_id()));
            if (!Objects.equals(params.getStart_station_name(), "")) WHERE(String.format("REGEXP_CONTAINS(start_station_name,r'(?i)%s')", params.getStart_station_name()));
            if (!Objects.equals(params.getEnd_station_name(), "")) WHERE(String.format("REGEXP_CONTAINS(end_station_name,r'(?i)%s')", params.getEnd_station_name()));
            if (!Objects.equals(params.getBike_type(), "")) WHERE(String.format("bike_type = '%s'", params.getBike_type()));
            if (params.getMin_duration() != 0) WHERE(String.format("duration_minutes >= %s", params.getMin_duration()));
            if (params.getMax_duration() != 0) WHERE(String.format("duration_minutes <= %s", params.getMax_duration()));
            if (!Objects.equals(params.getMin_start_time(), "")) WHERE(String.format("start_time >= TIMESTAMP('%s')", convertTime(params.getMin_start_time())));
            if (!Objects.equals(params.getMax_start_time(), "")) WHERE(String.format("start_time <= TIMESTAMP('%s')", convertTime(params.getMax_start_time())));
            LIMIT(200);
            OFFSET(params.getOffset());
        }}.toString();
    }
}
