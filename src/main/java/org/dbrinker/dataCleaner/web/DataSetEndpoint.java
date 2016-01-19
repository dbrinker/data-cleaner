package org.dbrinker.dataCleaner.web;

// Note that this metaphor is a wee bit tortured.   "Pass in data, process it,
// and return it, is far more a RPC-based approach than you normally see in
// REST APIs.  We'll go ahead and look at it in terms of processing data sets.
//
// One way to extend this if data sources (and/or processing times) grew large
// would be to make the process asynchronous by saving the data set with a POST,
// adding an attribute indicating if the data is clean, and retrieving the
// (possibly cleaned) data on a GET.  We're not going to do that now, since the
// requirements describe a synchronous return, but we'll still use a POST for
// lack of a better action

import org.dbrinker.dataCleaner.model.CategoryAndSubcat;
import org.dbrinker.dataCleaner.model.CategoryCleanResponse;
import org.dbrinker.dataCleaner.model.CategoryCount;
import org.dbrinker.dataCleaner.service.DataCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

/**
 * REST endpoint for processing data sets
 *
 * @author Don Brinker
 */
@Component
@Path("/data")
public class DataSetEndpoint extends AbstractEndpoint {
    private static final String SERVICE_NAME = "Data";

    private DataCleaner cleaner;

    /**
     * Creates a new instance of this class
     *
     * @param cleaner   A service which can clean and otherwise process data
     *                  sets
     */
    @Autowired
    public DataSetEndpoint(DataCleaner cleaner) {
        super(SERVICE_NAME);
        this.cleaner = cleaner;
    }

    /**
     * Cleans the given data of both invalid categories and duplicate
     * category/subcategory pairs.  The result is returned, along with a count
     * of each category found.
     *
     * @param data  The input category/subcategory pairs
     *
     * @return  The cleaned pairs (in the same order as the input data) and
     *          counts (ordered by frequency of occurrences in the data)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CategoryCleanResponse clean(List<CategoryAndSubcat> data) {
        List<CategoryAndSubcat> cleanedData = cleaner.cleanData(data);
        List<CategoryCount> counts = cleaner.getCategoryCounts(cleanedData);

        return new CategoryCleanResponse(cleanedData, counts);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CategoryAndSubcat> test() {
        return Arrays.asList(new CategoryAndSubcat("CATEGORY 1",
                                                   "SUBCATEGORY 1"),
                             new CategoryAndSubcat("CATEGORY 2",
                                                   "SUBCATEGORY 2"),
                             new CategoryAndSubcat("CATEGORY 3",
                                                   "SUBCATEGORY 3"));
    }
}
