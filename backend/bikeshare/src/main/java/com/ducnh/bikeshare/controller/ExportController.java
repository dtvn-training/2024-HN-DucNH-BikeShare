package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.constant.Constant;
import com.ducnh.bikeshare.dto.TripParamDTO;
import com.ducnh.bikeshare.model.Trip;
import com.ducnh.bikeshare.service.ExportService;
import com.ducnh.bikeshare.service.TripService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/export")
@CrossOrigin(origins = Constant.FRONTEND_URL)
public class ExportController {
    @Autowired
    ExportService exportService;

    @Autowired
    TripService tripService;

    // Export API for retrieving data from frontend response
    @PostMapping("/{table}")
    public void export(@RequestBody String json,
                       @PathVariable String table,
                       HttpServletResponse response) throws IOException {
        json = exportService.editJSONInput(json);
        exportService.exportQueryResult(json, response, table);
    }

    // Export API for retrieving data from BigQuery
    @PostMapping("/trips/getAll")
    public void export(@RequestBody TripParamDTO params,
                       HttpServletResponse response) throws IOException {
        List<Trip> trips = tripService.getTrips(params).getTrips();
        exportService.exportToExcel(trips, response, params);
    }
}
