package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.model.TripHolder;
import com.ducnh.bikeshare.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/trips")
@CrossOrigin(origins = "http://localhost:3000")
public class TripController {
    @Autowired
    TripService tripService;

    @GetMapping()
    public TripHolder getTrips() {
        StringBuilder query = new StringBuilder("SELECT trip_id "
                + "FROM `springboot-bq.bikeshare.trips`");

        String clause = " WHERE";
        String query_format = "";




        log.info(query.toString());
        return tripService.getTrips(query.toString());
    }
}
