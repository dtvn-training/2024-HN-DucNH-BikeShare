package com.ducnh.bikeshare.config;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GoogleCloudConfig {
    @Value("${GOOGLE_APPLICATION_CREDENTIALS}")
    private String credentialsJson;

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        return GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes()));
    }
}
