package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.model.TripHolder;
import com.ducnh.bikeshare.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.apache.arrow.flatbuf.Int;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/trips")
@CrossOrigin(origins = "http://localhost:3000")
public class TripController {
    @Autowired
    TripService tripService;

    @GetMapping()
    public TripHolder getTrips(@RequestParam(name = "offset", defaultValue = "0") Integer offset,
                               @RequestParam(name = "trip_id", required = false) String trip_id,
                               @RequestParam(name = "subscriber_type", required = false) String subscriber_type,
                               @RequestParam(name = "bike_id", required = false) String bike_id,
                               @RequestParam(name = "bike_type", required = false) String bike_type,
                               @RequestParam(name = "start_station_name", required = false) String start_station_name,
                               @RequestParam(name = "end_station_name", required = false) String end_station_name,
                               @RequestParam(name = "min_duration_minutes", required = false) Integer min_duration_minutes,
                               @RequestParam(name = "max_duration_minutes", required = false) Integer max_duration_minutes,
                               @RequestParam(name = "min_start_time", required = false) String min_start_time,
                               @RequestParam(name = "max_start_time", required = false) String max_start_time
                               ) {
        StringBuilder query = new StringBuilder("SELECT trip_id, subscriber_type, bike_id, bike_type, start_time, start_station_name, end_station_name, duration_minutes "
                + "FROM `springboot-bq.bikeshare.trips_1000`");

        String clause = " WHERE";
        String query_format = "";

        Map<String, String> like_params = Map.of("trip_id", (trip_id == null ? "" : trip_id),
                "subscriber_type", (subscriber_type == null ? "" : subscriber_type),
                "bike_id", (bike_id == null ? "" : bike_id),
                "start_station_name", (start_station_name == null ? "" : start_station_name),
                "end_station_name", (end_station_name == null ? "" : end_station_name)
                );

        Map<String, String> equal_params = Map.of("bike_type", (bike_type == null ? "" : bike_type));

        Map<String, String> min_number_params = Map.of("min_duration_minutes", (min_duration_minutes == null ? "" : min_duration_minutes.toString()),
                "min_start_time", (min_start_time == null ? "" : String.format("TIMESTAMP('%s')", tripService.convertTime(min_start_time))));

        Map<String, String> max_number_params = Map.of("max_duration_minutes", (max_duration_minutes == null ? "" : max_duration_minutes.toString()),
                "max_start_time", (max_start_time == null ? "" : String.format("TIMESTAMP('%s')", tripService.convertTime(max_start_time))));

        for (var entry : like_params.entrySet()) {
            if (entry.getValue().compareTo("") != 0) {
                query.append(clause);
                query_format = String.format(" REGEXP_CONTAINS(%s,r'(?i)%s')", entry.getKey(), entry.getValue());
                query.append(query_format);
                clause = " AND";
            }
        }

        for (var entry : equal_params.entrySet()) {
            if (entry.getValue().compareTo("") != 0) {
                query.append(clause);
                query_format = String.format(" %s = '%s'", entry.getKey(), entry.getValue());
                query.append(query_format);
                clause = " AND";
            }
        }

        for (var entry : min_number_params.entrySet()) {
            if (entry.getValue().compareTo("") != 0) {
                query.append(clause);
                query_format = String.format(" %s >= %s", entry.getKey().substring(4), entry.getValue());
                query.append(query_format);
                clause = " AND";
            }
        }

        for (var entry : max_number_params.entrySet()) {
            if (entry.getValue().compareTo("") != 0) {
                query.append(clause);
                query_format = String.format(" %s <= %s", entry.getKey().substring(4), entry.getValue());
                query.append(query_format);
                clause = " AND";
            }
        }

        query.append(" LIMIT 200 OFFSET ").append(offset);

        log.info(query.toString());
        return tripService.getTrips(query.toString());
    }
}
