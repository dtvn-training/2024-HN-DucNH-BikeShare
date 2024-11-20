package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.constant.Constant;
import com.ducnh.bikeshare.dto.TripParamDTO;
import com.ducnh.bikeshare.model.chart.ChartHolder;
import com.ducnh.bikeshare.service.ChartService;
import com.ducnh.bikeshare.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chart")
@CrossOrigin(origins = Constant.FRONTEND_URL)
public class ChartController {
    @Autowired
    ChartService chartService;

    @Autowired
    TripService tripService;

    @PostMapping("/top10start")
    public ChartHolder getTop10MostStartStations(@RequestBody TripParamDTO params) {
        return chartService.getChartData(tripService.parsedQuery(params), "Top 10 Start Stations");
    }

    @PostMapping("/top10end")
    public ChartHolder getTop10MostEndStations(@RequestBody TripParamDTO params) {
        return chartService.getChartData(tripService.parsedQuery(params), "Top 10 End Stations");
    }
}
