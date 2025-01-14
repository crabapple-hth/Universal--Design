package com.example.forum.Configuration;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration

public class MinioConfig {
    @Value("${spring.minio.endpoint}")
    String endpoint;
    @Value("${spring.minio.username}")
    String username;
    @Value("${spring.minio.password}")
    String password;

    @Bean
    public MinioClient client(){
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(username,password)
                .build();
    }
}
