package org.dbrinker.dataCleaner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define a method that should be read locked.  Methods of this type
 * will block if a write locked method (annotated with @WriteLocked) is
 * currently being executed against the given resource.  Multiple calls to this
 * method (and/or to other  read locked methods on the same resource) can be
 * triggered simultaneously with no ill effect.
 *
 * @see WriteLocked
 *
 * @author Don Brinker
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadLocked {
    String value();
}
