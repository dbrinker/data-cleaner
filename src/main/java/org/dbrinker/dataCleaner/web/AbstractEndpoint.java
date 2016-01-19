package org.dbrinker.dataCleaner.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Defines an abstract base of all REST endpoints
 *
 * @author Don Brinker
 */
public abstract class AbstractEndpoint {
    private String serviceName;

    public AbstractEndpoint(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Performs a health check of the service
     *
     * @return  A response string indicating the service is live
     */
    @Path("/health")
    @GET
    @Produces("application/json")
    public String healthCheck() {
        return new StringBuilder().append(serviceName)
                                  .append(" service up and running")
                                  .toString();
    }
}
