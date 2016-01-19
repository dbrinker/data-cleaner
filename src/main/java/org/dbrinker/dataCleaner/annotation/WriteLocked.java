package org.dbrinker.dataCleaner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define a method that should be write locked.  Methods of this type
 * will block if one or more read locked methods (annotated with @ReadLocked)
 * or another write locked method is currently being executed against the given
 * resource.
 *
 * @see ReadLocked
 *
 * @author Don Brinker
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteLocked {
    String value();
}
