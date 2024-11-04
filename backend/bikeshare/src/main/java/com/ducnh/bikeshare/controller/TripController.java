package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.model.TripHolder;
import com.ducnh.bikeshare.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/trips")
@CrossOrigin(origins = "http://localhost:3000")
public class TripController {
    @Autowired
    TripService tripService;

    @GetMapping()
    public TripHolder getTrips(@RequestParam(name = "offset", defaultValue = "0") Integer offset) {
        StringBuilder query = new StringBuilder("SELECT trip_id "
                + "FROM `springboot-bq.bikeshare.trips` LIMIT 200 OFFSET " + offset);

        String clause = " WHERE";
        String query_format = "";




        log.info(query.toString());
        return tripService.getTrips(query.toString());
    }
}
