package com.alcatrazescapee.notreepunching.platform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Like {@link Override} for methods where the parent method is not present at compile time, but is at runtime.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface PlatformOverride
{
    /**
     * Which platform this method's parent is present on.
     */
    Platform value();
}
