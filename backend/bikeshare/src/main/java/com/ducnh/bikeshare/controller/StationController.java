package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.model.Station;
import com.ducnh.bikeshare.model.StationHolder;
import com.ducnh.bikeshare.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StationController {
    @Autowired
    StationService stationService;

    @GetMapping("/")
    public StationHolder getStations() {
        return stationService.getStations();
    }
}
