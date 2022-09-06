package io.alvarm.yajom.tests.annotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.alvarm.yajom.annotation.Field;
import io.alvarm.yajom.annotation.Key;

public class KeyAnnotationTest {
    
    class Example {
        @Key @Field("UID")
        long id;
    }

    @Test void keyAnnotationStoredInClass() {
        java.lang.reflect.Field field = 
            Arrays.stream(Example.class.getDeclaredFields())
            .filter(f -> f.getAnnotation(Key.class) != null)
            .findFirst().orElse(null);
        
        assertNotNull(field);

        assertEquals("id", field.getName());

        
    }
}
