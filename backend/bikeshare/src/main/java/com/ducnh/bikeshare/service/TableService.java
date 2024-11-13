package com.ducnh.bikeshare.service;

import com.google.cloud.bigquery.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class TableService {
    private static final Logger log = LoggerFactory.getLogger(TableService.class);

    public static String validateString(String param, FieldValueList row) {
        return !row.get(param).isNull() ? row.get(param).getStringValue() : "";
    }

    public static int validateInteger(String param, FieldValueList row) {
        return !row.get(param).isNull() ? row.get(param).getNumericValue().intValue() : 0;
    }

    public static float validateFloat(String param, FieldValueList row) {
        return !row.get(param).isNull() ? row.get(param).getNumericValue().floatValue() : 0f;
    }

    public static LocalDateTime validateTime(String param, FieldValueList row) {
        return !row.get(param).isNull() ? row.get(param).getTimestampInstant().atZone(ZoneId.of("UTC")).toLocalDateTime() : null;
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
}
