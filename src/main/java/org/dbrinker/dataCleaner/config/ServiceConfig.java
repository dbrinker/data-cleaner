package org.dbrinker.dataCleaner.config;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Set;

/**
 * Spring Configuration for the service layer of the Data Cleaner application.
 * Defined independently from the REST endpoints and such to simplify testing.
 *
 * @author Don Brinker
 */
@Configuration
@ComponentScan("org.dbrinker.dataCleaner")
@EnableAspectJAutoProxy
public class ServiceConfig {
    @Bean
    public Set<String> validCategories() {
        return Sets.newHashSet("PERSON",
                               "PLACE",
                               "ANIMAL",
                               "COMPUTER",
                               "OTHER");
    }
}
