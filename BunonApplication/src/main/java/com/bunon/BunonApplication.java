package com.bunon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * START HERE. Running this class's main() starts the whole website.
 * @SpringBootApplication tells Spring: "scan the com.bunon package and wire everything up for me".
 */
@SpringBootApplication
public class BunonApplication {
    public static void main(String[] args) {
        SpringApplication.run(BunonApplication.class, args);
    }
}
