package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.constant.Constant;
import com.ducnh.bikeshare.dto.StationParamDTO;
import com.ducnh.bikeshare.model.Station;
import com.ducnh.bikeshare.model.StationHolder;
import com.google.cloud.bigquery.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class StationService implements IStationService{
    private static final Logger log = LoggerFactory.getLogger(StationService.class);

    @Override
    public StationHolder getStations(StationParamDTO params) {
        log.info("query started");

        StationHolder stationHolder = new StationHolder();
        List<Station> stations = new ArrayList<>();
        stationHolder.setStations(stations);
        stationHolder.setStatus("Failed");
        stationHolder.setCount(0);

        String query = parsedQuery(params);
        log.info(query);

        Job queryJob = createJob(query);

        getResponseData(queryJob, stationHolder);

        return stationHolder;
    }

    public static Job createJob(String query) {
        BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                .setUseLegacySql(false)
                .build();

        String jobIdStr = UUID.randomUUID().toString();
        log.info("jobIdStr: {}", jobIdStr);

        JobId jobId = JobId.of(jobIdStr);

        return bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
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
                int station_id = !row.get("station_id").isNull() ? row.get("station_id").getNumericValue().intValue() : 0;
                String name = !row.get("name").isNull() ? row.get("name").getStringValue() : "";
                String status = !row.get("status").isNull() ? row.get("status").getStringValue() : "";
                String location = !row.get("location").isNull() ? row.get("location").getStringValue() : "";
                String address = !row.get("address").isNull() ? row.get("address").getStringValue() : "";
                String alternate_name = !row.get("alternate_name").isNull() ? row.get("alternate_name").getStringValue() : "";
                int city_asset_number = !row.get("city_asset_number").isNull() ? row.get("city_asset_number").getNumericValue().intValue() : 0;
                String property_type = !row.get("property_type").isNull() ? row.get("property_type").getStringValue() : "";
                int number_of_docks = !row.get("number_of_docks").isNull() ? row.get("number_of_docks").getNumericValue().intValue() : 0;
                String power_type = !row.get("power_type").isNull() ? row.get("power_type").getStringValue() : "";
                int footprint_length = !row.get("footprint_length").isNull() ? row.get("footprint_length").getNumericValue().intValue() : 0;
                float footprint_width = !row.get("footprint_width").isNull() ? row.get("footprint_width").getNumericValue().floatValue() : 0f;
                String notes = !row.get("notes").isNull() ? row.get("notes").getStringValue() : "";
                int council_district = !row.get("council_district").isNull() ? row.get("council_district").getNumericValue().intValue() : 0;
                ZoneId zoneId = ZoneId.of("UTC");
                LocalDateTime modified_date = !row.get("modified_date").isNull() ? row.get("modified_date").getTimestampInstant().atZone(zoneId).toLocalDateTime() : null;

                Station station = new Station(station_id, name, status, location, address, alternate_name, city_asset_number, property_type, number_of_docks, power_type, footprint_length,
                        footprint_width, notes, council_district, modified_date);

                stationHolder.getStations().add(station);

            }

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
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