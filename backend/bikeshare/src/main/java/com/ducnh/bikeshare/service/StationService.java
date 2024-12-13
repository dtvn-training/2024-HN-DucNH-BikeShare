package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.constant.Constant;
import com.ducnh.bikeshare.dto.StationParamDTO;
import com.ducnh.bikeshare.model.Station;
import com.ducnh.bikeshare.model.StationHolder;
import com.google.cloud.bigquery.*;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class StationService implements IStationService{
    private static final Logger log = LoggerFactory.getLogger(StationService.class);

    @Autowired
    TableService tableService;

    @Override
    public StationHolder getStations(StationParamDTO params) throws IOException {
        log.info("query started");

        StationHolder stationHolder = new StationHolder();
        List<Station> stations = new ArrayList<>();
        stationHolder.setStations(stations);
        stationHolder.setStatus("Failed");
        stationHolder.setCount(0);

        String query = parsedQuery(params);
        log.info(query);

        Job queryJob = tableService.createJob(query);

        getResponseData(queryJob, stationHolder);

        return stationHolder;
    }

    private void getResponseData(Job queryJob, StationHolder stationHolder) {
        try {
            queryJob = queryJob.waitFor();
            if (queryJob == null) {
                log.info("queryJob is null");
                return;
            }

            if (queryJob.getStatus().getError() != null) {
                log.info("queryJob status: {}", queryJob.getStatus().getError());
                return;
            }

            TableResult result = queryJob.getQueryResults();

            for (FieldValueList row : result.iterateAll()) {
                int station_id = TableService.validateInteger("station_id", row);
                String name = TableService.validateString("name", row);
                String status = TableService.validateString("status", row);
                String location = TableService.validateString("location", row);
                String address = TableService.validateString("address", row);
                String alternate_name = TableService.validateString("alternate_name", row);
                int city_asset_number = TableService.validateInteger("city_asset_number", row);
                String property_type = TableService.validateString("property_type", row);
                int number_of_docks = TableService.validateInteger("number_of_docks", row);
                String power_type = TableService.validateString("power_type", row);
                int footprint_length = TableService.validateInteger("footprint_length", row);
                float footprint_width = TableService.validateFloat("footprint_width", row);
                String notes = TableService.validateString("notes", row);
                int council_district = TableService.validateInteger("council_district", row);
                LocalDateTime modified_date = TableService.validateTime("modified_date", row);

                Station station = new Station(station_id, name, status, location, address, alternate_name, city_asset_number, property_type, number_of_docks, power_type, footprint_length,
                        footprint_width, notes, council_district, modified_date);

                stationHolder.getStations().add(station);

            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return;
        }

        stationHolder.setCount(stationHolder.getStations().size());
        stationHolder.setStatus("Success");
    }

    private String parsedQuery(StationParamDTO params) {
        return new SQL(){{
            SELECT("*");
            FROM(Constant.TABLE_STATIONS);
            if (!Objects.equals(params.getName(), "")) WHERE(String.format("REGEXP_CONTAINS(name,r'(?i)%s')", params.getName()));
            if (!Objects.equals(params.getAddress(), "")) WHERE(String.format("REGEXP_CONTAINS(address,r'(?i)%s')", params.getAddress()));
            if (!Objects.equals(params.getStatus(), "")) WHERE(String.format("status = '%s'", params.getStatus()));
            if (!Objects.equals(params.getPower_type(), "")) WHERE(String.format("power_type = '%s'", params.getPower_type()));
            if (!Objects.equals(params.getProperty_type(), "")) WHERE(String.format("property_type = '%s'", params.getProperty_type()));
            if (params.getMin_docks() != 0) WHERE(String.format("number_of_docks >= %s", params.getMin_docks()));
            if (params.getMax_docks() != 0) WHERE(String.format("number_of_docks <= %s", params.getMax_docks()));
            if (params.getMin_length() != 0) WHERE(String.format("footprint_length >= %s", params.getMin_length()));
            if (params.getMax_length() != 0) WHERE(String.format("footprint_length <= %s", params.getMax_length()));
            if (params.getMin_width() != 0) WHERE(String.format("footprint_width >= %s", params.getMin_width()));
            if (params.getMax_width() != 0) WHERE(String.format("footprint_width <= %s", params.getMax_width()));
        }}.toString();
    }
}