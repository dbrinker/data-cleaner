package org.dbrinker.dataCleaner.model;

import java.util.Objects;

/**
 * Defines a count of occurrences of a single category
 *
 * @author Don Brinker
 */
public class CategoryCount {
    private String category;
    private long numOccurrences;

    public CategoryCount() {
    }

    public CategoryCount(String category, long numOccurrences) {
        this.category = category;
        this.numOccurrences = numOccurrences;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getNumOccurrences() {
        return numOccurrences;
    }

    public void setNumOccurrences(long numOccurrences) {
        this.numOccurrences = numOccurrences;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CategoryCount that = (CategoryCount) obj;
        return Objects.equals(numOccurrences, that.numOccurrences) &&
               Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, numOccurrences);
    }
}
