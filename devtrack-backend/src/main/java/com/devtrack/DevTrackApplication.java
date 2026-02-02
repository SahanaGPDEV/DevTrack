package com.devtrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevTrackApplication {
    public static void main(String[] args) {
        SpringApplication.run(DevTrackApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  DevTrack Backend Started Successfully!");
        System.out.println("  API: http://localhost:8080/api");
        System.out.println("========================================\n");
    }
}
