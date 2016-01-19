package org.dbrinker.dataCleaner.model;


import java.util.Objects;

/**
 * Defines a category/subcategory pair to be processed by the data cleaner.
 * Note that no relationship is assumed between subcategories of different
 * categories, even if the names are the same.  In other words, if categories
 * A and B have subcategories with the same name, they still represent distinct
 * entities.
 *
 * @author Don Brinker
 */
public class CategoryAndSubcat {
    private String category;
    private String subcategory;

    /**
     * Creates a new instance of this class
     */
    public CategoryAndSubcat() {
    }

    /**
     * Creates a new instance of this class
     *
     * @param category      The category represented in the pair
     * @param subcategory   The subcategory in the pair
     */
    public CategoryAndSubcat(String category, String subcategory) {
        this.category = category;
        this.subcategory = subcategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CategoryAndSubcat that = (CategoryAndSubcat) obj;
        return Objects.equals(category, that.category) &&
               Objects.equals(subcategory, that.subcategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, subcategory);
    }
}
