package io.alvarm.yajom.tests.persistance;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.alvarm.yajom.annotation.Entity;
import io.alvarm.yajom.annotation.Field;
import io.alvarm.yajom.annotation.Key;
import io.alvarm.yajom.persistance.Model;

public class ModelTest {

    @Test void classWithoutFieldsTest() {
        @Entity("bad-resource")
        class BadExample {
            @Key
            long id;
        }

        assertThrows(IllegalArgumentException.class, () -> Model.of(BadExample.class));
        assertThrows(IllegalArgumentException.class, () -> Model.of(new BadExample()));
    }

    @Test void classWithoutKeyFieldTest() {
        @Entity("bad-resource") 
        class BadExample {
            @Field("field")
            String identification;
        }

        assertThrows(IllegalArgumentException.class, () -> Model.of(BadExample.class));
        assertThrows(IllegalArgumentException.class, () -> Model.of(new BadExample()));
    }

    @Test void classWithoutEntityTest() {
        class BadExample {
            @Key @Field("userID")
            String uID;
        }
        assertThrows(IllegalArgumentException.class, () -> Model.of(BadExample.class));
        assertThrows(IllegalArgumentException.class, () -> Model.of(new BadExample()));

    }

    @Test void goodClassModelTest()
    {
        @Entity("my-resource")
        class GoodExample {
            @Key @Field("uid")
            long id;

            @Field("importantValue")
            String value;
        }
        assertDoesNotThrow(() -> Model.of(GoodExample.class));
        assertDoesNotThrow(() -> Model.of(new GoodExample()));

        Model m = Model.of(GoodExample.class);
        assertEquals(GoodExample.class.getCanonicalName(), m.getEntityName());
        assertEquals("my-resource", m.getResourceName());

        assertNotNull(m.getKeyField());
        assertNotNull(m.getKeyField().getKey());
        assertNotNull(m.getKeyField().getValue());
        assertEquals("uid", m.getKeyField().getKey());
        assertEquals("id", m.getKeyField().getValue().getName());

        assertNotNull(m.getFields());
        assertEquals(2, m.getFields().size());
        assertEquals("uid", m.getFields().get(0).getKey());
        assertEquals("id", m.getFields().get(0).getValue().getName());

        assertEquals("importantValue", m.getFields().get(1).getKey());
        assertEquals("value", m.getFields().get(1).getValue().getName());
    }

    @Test void aReallyBadClassTest() {
        assertThrows(IllegalArgumentException.class, () -> Model.of(Boolean.class));
        assertThrows(IllegalArgumentException.class, () -> Model.of(Boolean.TRUE));

    }
}
