package io.alvarm.yajom.persistance;

import java.util.List;
import io.alvarm.yajom.annotation.*;

/**
 * 
 * @author Àlvar Mercadé Ibáñez
 * @version 0.1
 * @since 0.1
 */
public interface EntityManager {

    /**
     * Persist an object into its class resource.
     * @param obj An entity instance to be persisted.
     * @see Entity
     */
    public <T> void persist(T obj);

    /**
     * Removes the specified entity instance from the entity resource.
     * @param obj Entity instance to be erased from resource.
     */
    public <T> void remove(T obj);

    /**
     * Gets all persisted entity instances from the entity resource.
     * @param clazz The entity class object we want to retrieve all persisted instances.
     * @return A Java stream of persisted entity instances.
     * @see List
     */
    public <T> List<T> retrieveAll(Class<T> clazz);
}
