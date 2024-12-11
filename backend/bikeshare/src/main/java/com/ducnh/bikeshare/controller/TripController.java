package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.constant.Constant;
import com.ducnh.bikeshare.dto.TripParamDTO;
import com.ducnh.bikeshare.model.TripHolder;
import com.ducnh.bikeshare.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/trips")
@CrossOrigin(origins = Constant.FRONTEND_URL)
public class TripController {
    @Autowired
    TripService tripService;

    @PostMapping()
    public TripHolder getTrips(@RequestBody TripParamDTO params) throws IOException {
        return tripService.getTrips(params);
    }
}
