package io.alvarm.yajom.tests.annotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.alvarm.yajom.annotation.Field;

public class FieldAnnotationTest {

    Stream<java.lang.reflect.Field> selectedFields;

    class Example {
        @Field("UID")
        long id;

        boolean flag;
    }

    @BeforeEach void setUpStream() {
        selectedFields = Arrays.stream(Example.class.getDeclaredFields())
                                .filter(f -> (f.getDeclaredAnnotation(Field.class) != null));
    }

    @Test void fieldAnnotationStoredInFields() {
        java.lang.reflect.Field f = selectedFields.findFirst()
                                    .orElse(null);

        assertNotNull(f);

        assertEquals("id", f.getName());
    }


    @Test void fieldAnnotationHasValue() {
        java.lang.reflect.Field f = selectedFields.findFirst()
                                    .orElse(null);

        String str = f.getDeclaredAnnotation(Field.class).value();

        assertEquals("UID", str);

    }
}
