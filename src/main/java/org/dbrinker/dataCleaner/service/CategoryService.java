package org.dbrinker.dataCleaner.service;

import java.util.Set;

/**
 * Defines a basic service for retrieving and modifying categories.  For the
 * sake of this exercise, categories are simple strings, and have no
 * relationships with each other, hierarchical or otherwise.  However,
 * categories *can* be expected to be unique.
 *
 * @author Don Brinker
 */
public interface CategoryService {
    /**
     * Retrieves a collection of all categories known by the system.  If none
     * have been defined, an empty list will be returned
     *
     * @return  The known categories.
     */
    Set<String> getCategories();

    /**
     * Adds the given category to the system's known categories.  If the
     * category already exists, this will essentially be a no-op.
     *
     * @param categoryToAdd     The category in question
     */
    void addCategory(String categoryToAdd);

    /**
     * Deletes the given category from the system.  If it has not been
     * previously defined, this is a no-op.
     *
     * @param categoryToDelete  The category in question
     */
    void deleteCategory(String categoryToDelete);
}
