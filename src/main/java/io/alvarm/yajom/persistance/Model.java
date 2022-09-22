package io.alvarm.yajom.persistance;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.alvarm.yajom.annotation.Entity;
import io.alvarm.yajom.annotation.Field;
import io.alvarm.yajom.annotation.Key;

/**
 * Class that represents the persistance model of an entity.
 * @author Àlvar Mercadé Ibáñez
 * @version 0.1
 * @since 0.1
 * @see Entity
 */
public class Model {

    private Class<?> clazz;

    private Model(Class<?> objClass) {
        clazz = objClass;
    }

    /**
     * Factory method that generates a Model for the specified class
     * @param objClass The Class we request a Model for.
     * @return A persistance model related to the argument class.
     * @throws IllegalArgumentException In case the class is not a persistance entity.
     */
    public static Model of(Class<?> objClass) throws IllegalArgumentException {

        Optional.ofNullable(objClass.getDeclaredAnnotation(Entity.class))
                .orElseThrow(() -> new IllegalArgumentException("Entity %s not properly annotated".formatted(objClass.getCanonicalName())));
        
        Arrays.stream(objClass.getDeclaredFields())
                .filter(f -> (f.getDeclaredAnnotation(Field.class) != null))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Entity %s has not fields to persist".formatted(objClass.getCanonicalName())));

        Arrays.stream(objClass.getDeclaredFields())
                .filter(f -> (f.getDeclaredAnnotation(Field.class) != null))
                .filter(f -> (f.getDeclaredAnnotation(Key.class) != null))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Entity %s has not Key field.".formatted(objClass.getCanonicalName())));

        return new Model(objClass);
    }

    /**
     * Factory method that generates a persistance model for a class given an instance of the same class 
     * @param o A class instance.
     * @return A persistance model for the instance class.
     * @throws IllegalArgumentException In case the class is not a persistance entity.
     */
    public static Model of(Object o) throws IllegalArgumentException{
        return of(o.getClass());
    }

    /**
     * Returns the resource name for this persistance model.
     * @return The resource name for this persistance model.
     * @see Entity
     */
    public String getResourceName() {
        return clazz.getAnnotation(Entity.class).value();
    }

    /**
     * Returns the full name of persistance entity for this model.
     * @return The full name of persistance entity for this model.
     */
    public String getEntityName() {
        return clazz.getCanonicalName();
    }

    /**
     * Returns the persistance model key field.
     * @return A pair formed by the persistance field name and the java object field.
     * @see Map.Entry
     * @see java.lang.reflect.Field
     */
    public Map.Entry<String, java.lang.reflect.Field> getKeyField() {
        java.lang.reflect.Field fld = Arrays.stream(clazz.getDeclaredFields())
        .filter(f -> (f.getAnnotation(Field.class) != null))
        .filter(f -> (f.getAnnotation(Key.class) != null))
        .findFirst().get();

        return Map.entry(fld.getAnnotation(Field.class).value(), fld);
    }

    /**
     * Returns a list of persistance fields for this model.
     * @return a list of field names and Java object fields.
     * @see List
     * @see Map.Entry
     * @see java.lang.reflect.Field
     */
    public List<Map.Entry<String, java.lang.reflect.Field>> getFields() {
        return Arrays.stream(clazz.getDeclaredFields())
                        .filter(f -> (f.getDeclaredAnnotation(Field.class) != null))
                        .map(f -> Map.entry(f.getDeclaredAnnotation(Field.class).value(), f))
                        .toList();
    }

    /**
     * 
     * @return
     */
    public List<Map.Entry<String, java.lang.reflect.Field>> getNonKeyFields() {
        return Arrays.stream(clazz.getDeclaredFields())
                        .filter(f -> (f.getDeclaredAnnotation(Field.class) != null))
                        .filter(f -> (f.getDeclaredAnnotation(Key.class) == null))
                        .map(f -> Map.entry(f.getDeclaredAnnotation(Field.class).value(), f))
                        .toList();

    }
}
