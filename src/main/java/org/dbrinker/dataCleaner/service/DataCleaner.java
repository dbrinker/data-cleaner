package org.dbrinker.dataCleaner.service;

import org.dbrinker.dataCleaner.model.CategoryAndSubcat;
import org.dbrinker.dataCleaner.model.CategoryCount;

import java.util.List;
import java.util.Map;

/**
 * Defines a service which cleans a set of incoming categories and
 * subcategories.  The cleaning process will remove any and all unrecognized
 * categories, as well as collapse duplicate entries.
 *
 * @author Don Brinker
 */
public interface DataCleaner {
    /**
     * Cleans the input, stripping out duplicates and invalid categories
     *
     * @param input     The collection of category/subcategory pairs.  Presumed
     *                  to be non-null.
     *
     * @return  The cleaned input
     */
    List<CategoryAndSubcat> cleanData(List<CategoryAndSubcat> input);

    /**
     * Retrieves the categories known by the system, along with the number of
     * occurrences of each in the given collection.
     *
     * @param input     The collection of category/subcategory pairs.  Presumed
     *                  to be non-null.
     *
     * @return  The categories and counts
     */
    List<CategoryCount> getCategoryCounts(List<CategoryAndSubcat> input);
}
