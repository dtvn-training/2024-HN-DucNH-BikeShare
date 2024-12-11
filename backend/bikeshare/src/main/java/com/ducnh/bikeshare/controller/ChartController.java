package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.constant.Constant;
import com.ducnh.bikeshare.dto.TripParamDTO;
import com.ducnh.bikeshare.model.chart.ChartHolder;
import com.ducnh.bikeshare.service.ChartService;
import com.ducnh.bikeshare.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public ChartHolder getTop10MostStartStations(@RequestBody TripParamDTO params) throws IOException {
        return chartService.getChartData(tripService.parsedQuery(params), "Top 10 Start Stations");
    }

    @PostMapping("/top10end")
    public ChartHolder getTop10MostEndStations(@RequestBody TripParamDTO params) throws IOException {
        return chartService.getChartData(tripService.parsedQuery(params), "Top 10 End Stations");
    }

    @PostMapping("/subscriber_type")
    public ChartHolder getSubscriberTypes(@RequestBody TripParamDTO params) throws IOException {
        return chartService.getChartData(tripService.parsedQuery(params), "Subscriber Types");
    }

    @PostMapping("/duration")
    public ChartHolder getDuration(@RequestBody TripParamDTO params) throws IOException {
        return chartService.getChartData(tripService.parsedQuery(params), "Duration");
    }

    @PostMapping("/time_period")
    public ChartHolder getTimePeriod(@RequestBody TripParamDTO params) throws IOException {
        return chartService.getChartData(tripService.parsedQuery(params), "Time period");
    }
}
