package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.model.Station;
import com.ducnh.bikeshare.model.StationHolder;
import com.ducnh.bikeshare.service.StationService;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/")
    public StationHolder getStations(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "address", required = false) String address) {
        StringBuilder query = new StringBuilder("SELECT * "
                + "FROM `springboot-bq.bikeshare.stations`");

        String clause = " WHERE";

        Map<String, String> params= Map.of("name", name, "status", status, "address", address);

        for (var entry : params.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                query.append(clause);
                query.append(" REGEXP_CONTAINS(").append(entry.getKey()).append(", r'(?i)").append(entry.getValue()).append("')");
                clause = " AND";
            }
        }

        log.info(query.toString());
        return stationService.getStations(query.toString());
    }
}
