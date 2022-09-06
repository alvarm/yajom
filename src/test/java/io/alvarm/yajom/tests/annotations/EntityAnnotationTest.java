package io.alvarm.yajom.tests.annotations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.alvarm.yajom.annotation.Entity;

public class EntityAnnotationTest {

    @Entity("my-resource")
    class Example {}

    @Test void entityAnnotationPersistsOnClass() {
        assertNotNull(Example.class.getDeclaredAnnotation(Entity.class));
    }

    @Test void entityAnnotationHasValue() {
        String str = Example.class.getDeclaredAnnotation(Entity.class).value();
        assertEquals("my-resource", str);
    }
}
