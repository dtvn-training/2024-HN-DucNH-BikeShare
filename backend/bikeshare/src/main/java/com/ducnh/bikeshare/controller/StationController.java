package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.model.Station;
import com.ducnh.bikeshare.model.StationHolder;
import com.ducnh.bikeshare.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.arrow.flatbuf.Int;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/stations")
@CrossOrigin(origins = "http://localhost:3000")
public class StationController {
    @Autowired
    StationService stationService;

    @GetMapping()
    public StationHolder getStations(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "address", required = false) String address,
                                     @RequestParam(value = "power_type", required = false) String power_type,
                                     @RequestParam(value = "property_type", required = false) String property_type,
                                     @RequestParam(value = "min_number_of_docks", required = false) Integer min_docks,
                                     @RequestParam(value = "max_number_of_docks", required = false) Integer max_docks,
                                     @RequestParam(value = "min_footprint_length", required = false) Integer min_length,
                                     @RequestParam(value = "max_footprint_length", required = false) Integer max_length,
                                     @RequestParam(value = "min_footprint_width", required = false) Float min_width,
                                     @RequestParam(value = "max_footprint_width", required = false) Float max_width
                                    ) {
        StringBuilder query = new StringBuilder("SELECT * "
                + "FROM `springboot-bq.bikeshare.stations`");

        String clause = " WHERE";
        String query_format = "";

        // Could be null if param is not specified - solved
        Map<String, String> like_params = Map.of("name", (name == null ? "" : name),
                "address", (address == null ? "" : address));

        Map<String, String> equal_params = Map.of("status", (status == null ? "" : status),
                "power_type", (power_type == null ? "" : power_type),
                "property_type", (property_type == null ? "" : property_type)
                );

        Map<String, String> min_number_params = Map.of("min_number_of_docks", (min_docks == null ? "" : min_docks.toString()),
                "min_footprint_length", (min_length == null ? "" : min_length.toString()),
                "min_footprint_width", (min_width == null ? "" : min_width.toString()));

        Map<String, String> max_number_params = Map.of("max_number_of_docks", (max_docks == null ? "" : max_docks.toString()),
                "max_footprint_length", (max_length == null ? "" : max_length.toString()),
                "max_footprint_width", (max_width == null ? "" : max_width.toString()));

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

        log.info(query.toString());
        return stationService.getStations(query.toString());
    }
}