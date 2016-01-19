package org.dbrinker.dataCleaner.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.dbrinker.dataCleaner.model.CategoryAndSubcat;
import org.dbrinker.dataCleaner.model.CategoryCount;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit test driver for the DataCleanerImpl class
 *
 * @author Don Brinker
 */
@RunWith(MockitoJUnitRunner.class)
public class DataCleanerImplTest {
    private static final String CATEGORY_1 = "Category 1";
    private static final String CATEGORY_2 = "Category 2";
    private static final String CATEGORY_3 = "Category 3";
    private static final String CATEGORY_4 = "Category 4";

    private static final String SUBCATEGORY_1 = "Subcategory 1";
    private static final String SUBCATEGORY_2 = "Subcategory 2";
    private static final String SUBCATEGORY_3 = "Subcategory 3";
    private static final String SUBCATEGORY_4 = "Subcategory 4";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private CategoryService mockCategoryService;

    @InjectMocks
    private DataCleanerImpl cleaner;

    private List<CategoryAndSubcat> testInput;

    private CategoryAndSubcat categoryAndSubcat1;
    private CategoryAndSubcat categoryAndSubcat2;
    private CategoryAndSubcat categoryAndSubcat3;
    private CategoryAndSubcat categoryAndSubcat4;
    private HashSet<String> expectedCategories;

    @Before
    public void initialize() {
        // Set up our expectations
        expectedCategories
            = Sets.newHashSet(CATEGORY_1, CATEGORY_2, CATEGORY_3);
        when(mockCategoryService.getCategories())
            .thenReturn(expectedCategories);

        // And set up test data
        categoryAndSubcat1 = new CategoryAndSubcat(CATEGORY_1,
                                                   SUBCATEGORY_1);
        categoryAndSubcat2 = new CategoryAndSubcat(CATEGORY_2,
                                                   SUBCATEGORY_2);
        categoryAndSubcat3 = new CategoryAndSubcat(CATEGORY_3,
                                                   SUBCATEGORY_3);
        categoryAndSubcat4 = new CategoryAndSubcat(CATEGORY_4,
                                                   SUBCATEGORY_4);

        // Note that we can't use Arrays.asList() to create our expected input,
        // since we'll want to add data to them for various tests, and the
        // Arrays() method returns an immutable list.
        //
        // We're also going to mix up the order of the valid input, to make
        // sure the output isn't ordered or reverse-ordered automagically
        testInput = Lists.newArrayList(categoryAndSubcat3,
                                       categoryAndSubcat1,
                                       categoryAndSubcat2);
    }

    @Test
    public void cleanDataWithValidInput() {
        List<CategoryAndSubcat> output = cleaner.cleanData(testInput);
        validateOutput(output,
                       categoryAndSubcat3,
                       categoryAndSubcat1,
                       categoryAndSubcat2);
    }

    @Test
    public void cleanDataWithDuplicatedEntries() {
        // Add a couple of entries that already exist
        testInput.add(categoryAndSubcat1);
        testInput.add(categoryAndSubcat3);

        // Now make sure those extra entries are removed
        List<CategoryAndSubcat> output = cleaner.cleanData(testInput);
        validateOutput(output,
                       categoryAndSubcat3,
                       categoryAndSubcat1,
                       categoryAndSubcat2);
    }

    @Test
    public void cleanDataWithInvalidCategories() {
        // Now just add an entry to the input that's not in the expected
        // category list
        testInput.add(categoryAndSubcat4);

        // And make sure that entry is removed
        List<CategoryAndSubcat> output = cleaner.cleanData(testInput);
        validateOutput(output,
                       categoryAndSubcat3,
                       categoryAndSubcat1,
                       categoryAndSubcat2);

    }

    @Test
    public void cleanDataWithEmptyInput() {
        List<CategoryAndSubcat> output
            = cleaner.cleanData(Collections.emptyList());
        assertThat(output, notNullValue());
        assertThat(output, emptyCollectionOf(CategoryAndSubcat.class));
    }

    @Test
    public void cleanDataWithNullInput() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Input must not be null");

        cleaner.cleanData(null);
    }

    @Test
    public void getCategoryCountsWithValidInput() {
        // To fully test things, let's remove an entry from our input and add a
        // second subcategory for another.  This will allow us to definitively
        // order the results
        testInput.remove(categoryAndSubcat3);
        testInput.add(new CategoryAndSubcat(CATEGORY_2, SUBCATEGORY_3));

        List<CategoryCount> counts = cleaner.getCategoryCounts(testInput);

        assertThat(counts, notNullValue());
        assertThat(counts.size(), is(expectedCategories.size()));
        assertThat(counts.get(0), is(new CategoryCount(CATEGORY_2, 2L)));
        assertThat(counts.get(1), is(new CategoryCount(CATEGORY_1, 1L)));
        assertThat(counts.get(2), is(new CategoryCount(CATEGORY_3, 0L)));
    }


    @Test
    public void getCategoryCountsWithEmptyInput() {
        List<CategoryCount> counts
            = cleaner.getCategoryCounts(Collections.emptyList());

        assertThat(counts, notNullValue());
        assertThat(counts.size(), is(expectedCategories.size()));
        assertThat(counts, containsInAnyOrder(new CategoryCount(CATEGORY_1, 0L),
                                              new CategoryCount(CATEGORY_2, 0L),
                                              new CategoryCount(CATEGORY_3, 0L)));
    }

    @Test
    public void getCategoryCountsWithNullInput() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Input must not be null");

        cleaner.getCategoryCounts(null);
    }

    private void validateOutput(List<CategoryAndSubcat> output,
                                CategoryAndSubcat... expectedData) {
        assertThat(output, notNullValue());
        assertThat(output, hasSize(expectedData.length));

        // Note the use of contains here - order is VERY important
        assertThat(output, contains(expectedData));
    }
}
