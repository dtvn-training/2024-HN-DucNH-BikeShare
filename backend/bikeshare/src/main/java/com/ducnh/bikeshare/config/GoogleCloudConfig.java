package com.ducnh.bikeshare.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

@Configuration
public class GoogleCloudConfig {
    @Value("${GOOGLE_APPLICATION_CREDENTIALS}")
    private String credentialsJson;

    @Value("${GOOGLE_CLOUD_PROJECT_ID}")
    private String projectId;

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ByteArrayInputStream(credentialsJson.getBytes())
        ).createScoped(
                Collections.singletonList("https://www.googleapis.com/auth/cloud-platform")
        );

        return StorageOptions.newBuilder()
                .setCredentials(googleCredentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }
}
