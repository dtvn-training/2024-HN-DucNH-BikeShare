//package com.ducnh.bikeshare.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StringUtils;
//
//import java.io.ByteArrayInputStream;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//public class GoogleCloudConfig {
//    @Value("${GCP_SERVICE_ACCOUNT_KEY}")
//    private String serviceAccountKey;
//
//    @Bean
//    public GoogleCredentials credentials() throws IOException {
//        if (StringUtils.hasText(serviceAccountKey)) {
//            try (InputStream credentialsStream = new ByteArrayInputStream(serviceAccountKey.getBytes())) {
//                return GoogleCredentials.fromStream(credentialsStream)
//                        .createScoped(Arrays.asList(
//                                "https://www.googleapis.com/auth/cloud-platform",
//                                "https://www.googleapis.com/auth/cloudplatformprojects.readonly"
//                        ));
//            }
//        }
//        throw new IllegalStateException("Error");
//    }
//
//    @Bean
//    public Storage storage(GoogleCredentials credentials) throws IOException {
//        return StorageOptions.newBuilder()
//                .setCredentials(credentials)
//                .build()
//                .getService();
//    }
//}
