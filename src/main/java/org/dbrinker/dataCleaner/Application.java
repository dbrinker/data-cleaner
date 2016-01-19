package org.dbrinker.dataCleaner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Core Spring Boot class for the application.  This will allow it to run as a
 * fully executable JAR, without having to manually load it into an application
 * server or the like
 *
 * @author Don Brinker
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
