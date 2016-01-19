package org.dbrinker.dataCleaner.endpoint;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.dbrinker.dataCleaner.Application;
import org.dbrinker.dataCleaner.model.CategoryAndSubcat;
import org.dbrinker.dataCleaner.model.CategoryCleanResponse;
import org.dbrinker.dataCleaner.model.CategoryCount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * REST-based integration test of the data set endpoint
 *
 * @author Don Brinker
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class DataSetEndpointIT  {
    // Set automagically by Spring when the server starts
    @Value("${local.server.port}")
    protected int port;

    private CategoryAndSubcat cs1 = new CategoryAndSubcat("PERSON", "Bob Jones");
    private CategoryAndSubcat cs2 = new CategoryAndSubcat("PLACE", "Fairfax, VA");
    private CategoryAndSubcat cs3 = new CategoryAndSubcat("PERSON", "Don Brinker");
    private CategoryAndSubcat cs4 = new CategoryAndSubcat("MUPPET", "Animal");

    @Before
    public void initialize() {
        RestAssured.port = port;
    }

    @Test
    public void canProcessValidData() {
        List<CategoryAndSubcat> input
            = Arrays.asList(cs1, cs2, cs3);

        Response response = given().
            body(input).
            contentType(MediaType.APPLICATION_JSON).
        when().
            post("/data").
        then().
            statusCode(HttpStatus.SC_OK).
            contentType(MediaType.APPLICATION_JSON).
        extract().
            response();

        // For the categories, we know the exact order they should be in
        CategoryCleanResponse cleanResponse
            = response.body().as(CategoryCleanResponse.class);
        assertThat(cleanResponse.getCategories(), notNullValue());
        assertThat(cleanResponse.getCategories(), contains(cs1, cs2, cs3));

        // Okay, for the counts, we know the order of the first two.  The last
        // three will be indeterminate, so we'll just need to check each to
        // make sure the right elements exist
        assertThat(cleanResponse.getCounts(), notNullValue());
        assertThat(cleanResponse.getCounts(), hasSize(5));
        assertThat(cleanResponse.getCounts().get(0),
                   is(new CategoryCount("PERSON", 2)));
        assertThat(cleanResponse.getCounts().get(1),
                   is(new CategoryCount("PLACE", 1)));
        assertThat(cleanResponse.getCounts(),
                   hasItems(new CategoryCount("ANIMAL", 0),
                            new CategoryCount("COMPUTER", 0),
                            new CategoryCount("OTHER", 0)));
    }

    @Test
    public void canProcessDataWithDuplicateEntries() {
        List<CategoryAndSubcat> input
            = Arrays.asList(cs1, cs2, cs3, cs2, cs1);

        Response response = given().
            body(input).
            contentType(MediaType.APPLICATION_JSON).
        when().
            post("/data").
        then().
            statusCode(HttpStatus.SC_OK).
            contentType(MediaType.APPLICATION_JSON).
        extract().
            response();

        // For the categories, we know the exact order they should be in
        CategoryCleanResponse cleanResponse
            = response.body().as(CategoryCleanResponse.class);
        assertThat(cleanResponse.getCategories(), notNullValue());
        assertThat(cleanResponse.getCategories(), contains(cs1, cs2, cs3));

        // Okay, for the counts, we know the order of the first two.  The last
        // three will be indeterminate, so we'll just need to check each to
        // make sure the right elements exist
        assertThat(cleanResponse.getCounts(), notNullValue());
        assertThat(cleanResponse.getCounts(), hasSize(5));
        assertThat(cleanResponse.getCounts().get(0),
                   is(new CategoryCount("PERSON", 2)));
        assertThat(cleanResponse.getCounts().get(1),
                   is(new CategoryCount("PLACE", 1)));
        assertThat(cleanResponse.getCounts(),
                   hasItems(new CategoryCount("ANIMAL", 0),
                            new CategoryCount("COMPUTER", 0),
                            new CategoryCount("OTHER", 0)));
    }

    @Test
    public void canProcessDataWithInvalidCategories() {
        List<CategoryAndSubcat> input
            = Arrays.asList(cs1, cs2, cs3, cs4);

        Response response = given().
            body(input).
            contentType(MediaType.APPLICATION_JSON).
        when().
            post("/data").
        then().
            statusCode(HttpStatus.SC_OK).
            contentType(MediaType.APPLICATION_JSON).
        extract().
            response();

        // For the categories, we know the exact order they should be in
        CategoryCleanResponse cleanResponse
            = response.body().as(CategoryCleanResponse.class);
        assertThat(cleanResponse.getCategories(), notNullValue());
        assertThat(cleanResponse.getCategories(), contains(cs1, cs2, cs3));

        // Okay, for the counts, we know the order of the first two.  The last
        // three will be indeterminate, so we'll just need to check each to
        // make sure the right elements exist
        assertThat(cleanResponse.getCounts(), notNullValue());
        assertThat(cleanResponse.getCounts(), hasSize(5));
        assertThat(cleanResponse.getCounts().get(0),
                   is(new CategoryCount("PERSON", 2)));
        assertThat(cleanResponse.getCounts().get(1),
                   is(new CategoryCount("PLACE", 1)));
        assertThat(cleanResponse.getCounts(),
                   hasItems(new CategoryCount("ANIMAL", 0),
                            new CategoryCount("COMPUTER", 0),
                            new CategoryCount("OTHER", 0)));
    }
}
