package org.dbrinker.dataCleaner.service;

import org.dbrinker.dataCleaner.annotation.ReadLocked;
import org.dbrinker.dataCleaner.annotation.WriteLocked;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Defines a (very) simple in-memory implementation of the Category Service
 * API.  Note that a traditional service would probably delegate to a Data
 * Access Object, but for simplicity and time's sake, that will be skipped here.
 *
 * @author Don Brinker
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    private Set<String> categories;

    // NOTE: Since the categories are stored in memory, and since we could
    // conceivably have requests coming in to both get and change categories
    // simultaneously, we'll need to do some locking of the resource.  Since
    // the majority of our accesses will be reads, we'll use a read-write lock
    // (to allow multiple readers but only one writer).
    //
    // With that said, using a read-write lock is boilerplate city, so we'll
    // use annotations and aspects to simplify the code.
    //
    // If we were to cluster this service but retain the in-memory model for
    // categories, we'd probably need to use something like Zookeeper to
    // implement a global lock model.  That said, we'd arguably be better off
    // in that case just using a RDBMS or similar with a distributed cache in
    // front.

    // Note that if we were to use a RDBMS or other data store to hold
    // categories, we'd DEFINITELY want to have that access encapsulated in a
    // DAO.  This would also result in us starting from scratch with a database
    // intialization script rather than just injecting a collection of
    // categories.

    /**
     * Creates a new instance of this class
     */
    public CategoryServiceImpl() {
    }

    /**
     * Retrieves a collection of all categories known by the system.  If none
     * have been defined, an empty list will be returned
     *
     * @return The known categories.
     */
    @Override
    @ReadLocked("categories")
    public Set<String> getCategories() {
        return categories;
    }

    // Note that we're using @Resource below because it's not possible to
    // inject a predefined set using @Autowired - the latter assumes a set of
    // beans of the element type (in this case String).  Similarly, we can't
    // inject the beans into the constructor because @Resource is broken by
    // design to not allow constructor injection.  Go figure...

    /**
     * Sets the collection of categories associated with this service to the
     * given value.  This will overwrite any previously set categories
     *
     * @param validCategories   The new valid categories
     */
    @Resource(name="validCategories")
    @WriteLocked("categories")
    public void setCategories(Set<String> validCategories) {
        this.categories = validCategories;
    }

    /**
     * Adds the given category to the system's known categories.  If the
     * category already exists, this will essentially be a no-op.
     *
     * @param categoryToAdd     The category in question.  Presumed to be
     *                          non-null.
     */
    @Override
    @WriteLocked("categories")
    public void addCategory(String categoryToAdd) {
        Objects.requireNonNull(categoryToAdd,
                               "Category to add must not be null");
        categories.add(categoryToAdd);
    }

    /**
     * Deletes the given category from the system.  If it has not been
     * previously defined, this is a no-op.
     *
     * @param categoryToDelete  The category in question.  Presumed to be
     *                          non-null.
     */
    @Override
    @WriteLocked("categories")
    public void deleteCategory(String categoryToDelete) {
        Objects.requireNonNull(categoryToDelete,
                               "Category to delete must not be null");
        categories.remove(categoryToDelete);
    }
}
