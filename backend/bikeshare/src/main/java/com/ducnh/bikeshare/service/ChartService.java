package com.ducnh.bikeshare.service;

import com.ducnh.bikeshare.model.chart.Chart;
import com.ducnh.bikeshare.model.chart.ChartHolder;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.TableResult;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ChartService {
    private static final Logger log = LoggerFactory.getLogger(ChartService.class);

    public ChartHolder getChartData(String nestedQuery, String chartName) {
        log.info("query started");

        ChartHolder chartHolder = new ChartHolder();
        List<Chart> charts = new ArrayList<>();
        chartHolder.setCharts(charts);
        String query = "";
        String attribute = "";

        switch (chartName) {
            case "Top 10 Start Stations":
                query = queryTop10StartStations(nestedQuery);
                attribute = "start_station_name";
                break;
            case "Top 10 End Stations":
                query = queryTop10EndStations(nestedQuery);
                attribute = "end_station_name";
        };

        log.info(query);

        Job queryJob = TableService.createJob(query);

        getResponseData(queryJob, chartHolder, attribute);

        return chartHolder;
    }

    private void getResponseData(Job queryJob, ChartHolder chartHolder, String attribute) {
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
                String name = row.get(attribute).getStringValue();
                Integer amount = row.get("count").getNumericValue().intValue();

                Chart chart = new Chart(name, amount);
                chartHolder.getCharts().add(chart);
            }

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            log.error("Error: {}", e.getMessage());
        }
    }

    private String queryTop10StartStations(String nestedQuery) {
        return new SQL(){{
            SELECT("start_station_name", "COUNT(start_station_name) AS count");
            FROM("(" + nestedQuery + ")");
            GROUP_BY("start_station_name");
            ORDER_BY("COUNT(start_station_name) DESC");
            LIMIT(10);
        }}.toString();
    }

    private String queryTop10EndStations(String nestedQuery) {
        return new SQL(){{
            SELECT("end_station_name", "COUNT(end_station_name) AS count");
            FROM("(" + nestedQuery + ")");
            GROUP_BY("end_station_name");
            ORDER_BY("COUNT(end_station_name) DESC");
            LIMIT(10);
        }}.toString();
    }
}
