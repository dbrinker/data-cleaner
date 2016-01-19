package org.dbrinker.dataCleaner.service;

import org.dbrinker.dataCleaner.model.CategoryAndSubcat;
import org.dbrinker.dataCleaner.model.CategoryCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * A simple implementation of the Data Cleaner API
 *
 * @author Don Brinker
 */
@Service
public class DataCleanerImpl implements DataCleaner {
    private CategoryService categoryService;

    @Autowired
    public DataCleanerImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Cleans the input, stripping out duplicates and invalid categories
     *
     * @param input     The collection of category/subcategory pairs.  Presumed
     *                  to be non-null.
     *
     * @return The cleaned input
     */
    @Override
    public List<CategoryAndSubcat> cleanData(List<CategoryAndSubcat> input) {
        Objects.requireNonNull(input, "Input must not be null");

        // Get our valid categories.
        Set<String> validCategories = categoryService.getCategories();

        // Okay, stripping out duplicates can be done with Java 8 streams.
        // Filtering out invalid categories will require actively checking the
        // set of valid categories
        return input.stream()
                    .distinct()
                    .filter(cat -> validCategories.contains(cat.getCategory()))
                    .collect(Collectors.toList());

    }

    /**
     * Retrieves the categories known by the system, along with the number of
     * occurrences of each in the given collection.
     *
     * @param input     The collection of category/subcategory pairs.  Presumed
     *                  to be non-null.
     *
     * @return  The categories and counts, ordered by counts.
     */
    @Override
    public List<CategoryCount> getCategoryCounts(List<CategoryAndSubcat> input) {
        Objects.requireNonNull(input, "Input must not be null");

        // All in all, this is pretty simple.  A Java 8 stream can group and
        // count the results
        Map<String, Long> countingResult
            = input.stream()
                   .collect(groupingBy(CategoryAndSubcat::getCategory,
                                       counting()));

        // Now convert the results to a collection of CategoryCounts.
        List<CategoryCount> counts
            = countingResult.entrySet()
                            .stream()
                            .map(e -> new CategoryCount(e.getKey(),
                                                        e.getValue()))
                            .collect(Collectors.toList());

        // Sort the resulting mess by number of occurrences.  Note that for
        // whatever reason, the numerical count is ordered in INCREASING order.
        // Make sure we flip it to decreasing.
        Comparator<CategoryCount> countComparator
            = (p1, p2) -> Long.compare(p1.getNumOccurrences(),
                                       p2.getNumOccurrences());
        counts.sort(countComparator.reversed());

        // One more thing: we need all the OTHER categories as well.  Just add
        // them to the results with a zero count.  We don't have to worry about
        // ordering, since by definition add() adds the element to the end of
        // the list
        categoryService.getCategories()
                       .stream()
                       .filter(cat->!countingResult.containsKey(cat))
                       .forEach(cat -> counts.add(new CategoryCount(cat, 0L)));

        return counts;
    }
}
