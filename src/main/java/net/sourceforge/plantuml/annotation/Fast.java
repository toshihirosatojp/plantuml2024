package net.sourceforge.plantuml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a calculateDimension() implementation as known to be fast,
 * meaning it does not need memoization via TextBlockMemoized.
 * <p>
 * This annotation is used to track the review progress: every
 * calculateDimension() should eventually be either annotated with
 * {@code @Fast} or moved to a class extending TextBlockMemoized.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Fast {
}
