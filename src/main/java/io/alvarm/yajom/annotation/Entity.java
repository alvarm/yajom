package io.alvarm.yajom.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** 
 * This annotations indicates this is class is an entity that has to be persisted.
 * @author Àlvar Mercadé Ibáñez
 * @version 0.1
 * @since 0.1
*/
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    /**
     * The resource name where instances of this entity have to be stored.
     * @return The name of the resource.
     */
    String value();
}
