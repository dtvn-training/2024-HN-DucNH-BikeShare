package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.model.Station;
import com.ducnh.bikeshare.model.StationHolder;
import com.ducnh.bikeshare.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stations")
@CrossOrigin(origins = "http://localhost:3000")
public class StationController {
    @Autowired
    StationService stationService;

    @GetMapping("/")
    public StationHolder getStations() {
        return stationService.getStations();
    }
}
