package org.dbrinker.dataCleaner.model;

import java.util.List;
import java.util.Objects;

/**
 * Defines the response of a request to clean a set of category data
 *
 * @author Don Brinker
 */
public class CategoryCleanResponse {
    private List<CategoryAndSubcat> categories;
    private List<CategoryCount> counts;

    public CategoryCleanResponse() {
    }

    public CategoryCleanResponse(List<CategoryAndSubcat> categories,
                                 List<CategoryCount> counts) {
        this.categories = categories;
        this.counts = counts;
    }

    public List<CategoryAndSubcat> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryAndSubcat> categories) {
        this.categories = categories;
    }

    public List<CategoryCount> getCounts() {
        return counts;
    }

    public void setCounts(List<CategoryCount> counts) {
        this.counts = counts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CategoryCleanResponse that = (CategoryCleanResponse) o;
        return Objects.equals(categories, that.categories) &&
               Objects.equals(counts, that.counts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categories, counts);
    }
}
