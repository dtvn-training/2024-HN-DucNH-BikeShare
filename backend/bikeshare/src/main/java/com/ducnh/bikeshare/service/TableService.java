package com.ducnh.bikeshare.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.UUID;

@Service
public class TableService {
    @Value("${GCP_SERVICE_ACCOUNT_KEY}")
    public static String GCP_SERVICE_ACCOUNT_KEY;

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

    public static Job createJob(String query) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ByteArrayInputStream(GCP_SERVICE_ACCOUNT_KEY.getBytes(StandardCharsets.UTF_8))
        ).createScoped(Arrays.asList(
                                "https://www.googleapis.com/auth/cloud-platform",
                                "https://www.googleapis.com/auth/cloudplatformprojects.readonly"
                        ));

        BigQuery bigQuery = BigQueryOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId("springboot-bq")
                .build()
                .getService();

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query)
                .setUseLegacySql(false)
                .build();

        String jobIdStr = UUID.randomUUID().toString();
        log.info("jobIdStr: {}", jobIdStr);

        JobId jobId = JobId.of(jobIdStr);

        return bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
    }
}
