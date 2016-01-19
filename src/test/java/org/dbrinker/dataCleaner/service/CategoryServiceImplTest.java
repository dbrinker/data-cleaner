package org.dbrinker.dataCleaner.service;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Unit test for the CategoryServiceImpl class
 *
 * @author Don Brinker
 */
public class CategoryServiceImplTest {
    private static final String CATEGORY_1 = "Category 1";
    private static final String CATEGORY_2 = "Category 2";
    private static final String CATEGORY_3 = "Category 3";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CategoryServiceImpl service;

    @Before
    public void initialize() {
        service = new CategoryServiceImpl();
        service.setCategories(Sets.newHashSet(CATEGORY_1, CATEGORY_2));
    }

    @Test
    public void getCategories() {
        Set<String> categories = service.getCategories();
        assertThat(categories, notNullValue());
        assertThat(categories, hasSize(2));
        assertThat(categories, containsInAnyOrder(CATEGORY_1, CATEGORY_2));
    }

    @Test
    public void addValidCategory() {
        service.addCategory(CATEGORY_3);
        validateCategories(CATEGORY_1, CATEGORY_2, CATEGORY_3);
    }

    @Test
    public void addDuplicateCategory() {
        service.addCategory(CATEGORY_1);
        validateCategories(CATEGORY_1, CATEGORY_2);
    }

    @Test
    public void addNullCategory() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Category to add must not be null");

        service.addCategory(null);
    }

    @Test
    public void deleteValidCategory() {
        service.deleteCategory(CATEGORY_1);
        validateCategories(CATEGORY_2);
    }

    @Test
    public void deleteNonexistentCategory() {
        service.deleteCategory(CATEGORY_3);
        validateCategories(CATEGORY_1, CATEGORY_2);
    }

    @Test
    public void deleteNullCategory() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Category to delete must not be null");

        service.deleteCategory(null);
    }

    private void validateCategories(String... expectedCategories) {
        Set<String> categories = service.getCategories();
        assertThat(categories, notNullValue());
        assertThat(categories, hasSize(expectedCategories.length));
        assertThat(categories, containsInAnyOrder(expectedCategories));
    }
}
