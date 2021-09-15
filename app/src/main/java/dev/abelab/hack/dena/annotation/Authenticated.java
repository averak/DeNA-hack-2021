package dev.abelab.hack.dena.annotation;

import java.lang.annotation.*;

/**
 * Authenticated Interface
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Authenticated {
}
