package org.dbrinker.dataCleaner.web;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Defines the configuration required in order for Jersey to function properly.
 *
 * @author Don Brinker
 */
@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(CategoryEndpoint.class);
        register(DataSetEndpoint.class);
    }
}
