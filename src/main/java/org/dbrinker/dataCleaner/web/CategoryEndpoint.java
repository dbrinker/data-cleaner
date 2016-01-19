package org.dbrinker.dataCleaner.web;

import org.dbrinker.dataCleaner.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Set;

/**
 * JAX-RS Endpoint for accessing and changing Categories via REST
 *
 * @author Don Brinker
 */
@Component
@Path("/category")
public class CategoryEndpoint extends AbstractEndpoint {
    private static final String SERVICE_NAME = "Category";

    @Context
    private UriInfo uriInfo;

    private CategoryService service;

    @Autowired
    public CategoryEndpoint(CategoryService service) {
        super(SERVICE_NAME);
        this.service = service;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getAll() {
        return service.getCategories();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(String category) {
        // Add the category
        service.addCategory(category);

        // And return a response indicating the value was added.
        URI location = uriInfo.getAbsolutePathBuilder()
                              .path("{category}")
                              .resolveTemplate("category", category)
        	                  .build();

        return Response.created(location).build();
    }

    @DELETE
    @Path("{category}")
    public void delete(@PathParam("category") String category) {
        service.deleteCategory(category);
    }

}
