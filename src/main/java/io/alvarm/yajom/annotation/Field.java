package io.alvarm.yajom.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation refers to a field member of an entity that has to be stored.
 * @author Àlvar Mercadé Ibáñez
 * @version 0.1
 * @since 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
    /**
     * The entity's field name where the instance member has to be stored.
     * @return The name of the entity's field
     * @see Entity
     */
    String value();
}
