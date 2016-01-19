package org.dbrinker.dataCleaner.endpoint;

import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.dbrinker.dataCleaner.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.MediaType;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

/**
 * REST-based integration test of the category endpoint
 *
 * @author Don Brinker
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class CategoryEndpointIT  {
    // Set automagically by Spring when the server starts
    @Value("${local.server.port}")
    protected int port;

    @Before
    public void initialize() {
        RestAssured.port = port;
    }

    @Test
    public void canGetCategories() {
        when().
            get("/category").
        then().
            statusCode(HttpStatus.SC_OK).
            contentType(MediaType.APPLICATION_JSON).
            body("$", containsInAnyOrder("PERSON",
                                         "PLACE",
                                         "ANIMAL",
                                         "COMPUTER",
                                         "OTHER"));

    }

    // For what it's worth, Spring will use the same server between tests UNLESS
    // we annotate a method with DirtiesContext.  In that case, it'll restart
    // the server following the marked method.  This way, we can make sure that
    // changes in one test method won't affect another.
    @Test
    @DirtiesContext
    public void canAddCategory() {
        // First, add the new category
        String newCategory = "MUPPET";
        given().
            body(newCategory).
            contentType(MediaType.APPLICATION_JSON).
        when().
            post("/category").
        then().
            statusCode(HttpStatus.SC_CREATED).
            // Hardcoding the URI is a bit brittle, but we don't have a good way
            // handy to get the outgoing URI.
            header("location",
                   new URIBuilder().setScheme("http")
                                   .setHost("localhost")
                                   .setPort(port)
                                   .setPath("/category/" + newCategory)
                                   .toString());

        // Now query the categories and make sure the added category exists
        when().
            get("/category").
        then().
            statusCode(HttpStatus.SC_OK).
            contentType(MediaType.APPLICATION_JSON).
            body("$", hasItem(newCategory));

    }

    @Test
    @DirtiesContext
    public void canDeleteCategory() {
        // Delete a category we know exists.  We can't delete the one we
        // created in the add test since the methods may not be executed in the
        // order listed (and a good test driver allows each method to be
        // independent)
        String deletedCategory = "PLACE";
        given().
            pathParameter("category", deletedCategory).
        when().
            delete("/category/{category}").
        then().
            statusCode(HttpStatus.SC_NO_CONTENT);

        // Now query the categories and make sure the deleted category doesn't
        // exist
        when().
            get("/category").
        then().
            statusCode(HttpStatus.SC_OK).
            contentType(MediaType.APPLICATION_JSON).
            body("$", not(hasItem("PLACE")));

    }

}
