package org.dbrinker.dataCleaner.service;

import com.google.common.collect.Lists;
import org.dbrinker.dataCleaner.config.ServiceConfig;
import org.dbrinker.dataCleaner.model.CategoryAndSubcat;
import org.dbrinker.dataCleaner.model.CategoryCount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Integration test for the DataCleaner class.  Tests basic object interation
 * and Spring configuration
 *
 * @author Don Brinker
 */
@ContextConfiguration(classes = ServiceConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DataCleanerIT {
    @Autowired
    private DataCleaner cleaner;

    @Autowired
    private CategoryService categoryService;

    private Set<String> validCategories;

    private CategoryAndSubcat categoryAndSubcat1;
    private CategoryAndSubcat categoryAndSubcat2;
    private CategoryAndSubcat categoryAndSubcat3;
    private CategoryAndSubcat categoryAndSubcat4;


    @Before
    public void initialize() {
        validCategories = categoryService.getCategories();

        categoryAndSubcat1 = new CategoryAndSubcat("COMPUTER", "LAPTOP");
        categoryAndSubcat2 = new CategoryAndSubcat("COMPUTER", "TABLET");
        categoryAndSubcat3 = new CategoryAndSubcat("MUPPET", "ANIMAL");
        categoryAndSubcat4 = new CategoryAndSubcat("PLACE", "FAIRFAX");
    }

    // Okay, we've tested most of the DC functionality in the unit test driver.
    // Just make sure it's working with the expected values

    @Test
    public void cleanData() {
        List<CategoryAndSubcat> inputData
            = Lists.newArrayList(categoryAndSubcat1,
                                 categoryAndSubcat2,
                                 categoryAndSubcat3,
                                 categoryAndSubcat4,
                                 categoryAndSubcat2);

        List<CategoryAndSubcat> outputData = cleaner.cleanData(inputData);
        assertThat(outputData, notNullValue());
        assertThat(outputData, hasSize(3));
        assertThat(outputData, contains(categoryAndSubcat1,
                                        categoryAndSubcat2,
                                        categoryAndSubcat4));
    }

    // Same here - all the edge cases of the counter operation should have been
    // tested in the unit test, so we can just test basic functionality here
    @Test
    public void getCategoryCounts() {
        List<CategoryAndSubcat> inputData
            = Lists.newArrayList(categoryAndSubcat1,
                                 categoryAndSubcat2,
                                 categoryAndSubcat4);

        List<CategoryCount> categoryCounts
            = cleaner.getCategoryCounts(inputData);
        assertThat(categoryCounts, notNullValue());
        assertThat(categoryCounts, hasSize(validCategories.size()));

        assertThat(categoryCounts.get(0),
                   is(new CategoryCount("COMPUTER", 2L)));
        assertThat(categoryCounts.get(1),
                   is(new CategoryCount("PLACE", 1L)));

        validCategories.stream()
                       .filter(validCategory ->
                                   (!Objects.equals(validCategory,"COMPUTER")) &&
                                   (!Objects.equals(validCategory, "PLACE")))
                       .forEach(validCategory ->
                                    assertThat(categoryCounts,
                                               hasItem(new CategoryCount(validCategory,
                                                                         0L))));

    }
}
