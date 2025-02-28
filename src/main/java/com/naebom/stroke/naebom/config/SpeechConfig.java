package com.naebom.stroke.naebom.config;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class SpeechConfig {

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        return GoogleCredentials.fromStream(new FileInputStream("src/main/resources/google-cloud-key.json"));
    }
}
