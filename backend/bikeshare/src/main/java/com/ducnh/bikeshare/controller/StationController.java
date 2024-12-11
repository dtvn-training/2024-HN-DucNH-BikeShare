package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.constant.Constant;
import com.ducnh.bikeshare.dto.StationParamDTO;
import com.ducnh.bikeshare.model.StationHolder;
import com.ducnh.bikeshare.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/stations")
@CrossOrigin(origins = Constant.FRONTEND_URL)
public class StationController {
    @Autowired
    StationService stationService;

    @PostMapping()
    public StationHolder getStations(@RequestBody StationParamDTO params) throws IOException {
        return stationService.getStations(params);
    }
}