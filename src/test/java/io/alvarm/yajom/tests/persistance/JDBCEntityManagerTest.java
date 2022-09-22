package io.alvarm.yajom.tests.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.alvarm.yajom.annotation.Entity;
import io.alvarm.yajom.annotation.Field;
import io.alvarm.yajom.annotation.Key;

public class JDBCEntityManagerTest {
    static Connection conn;

    @Entity("PERSONS")
    private class Person {
        @Key @Field("P_UID")
        private long id;

        @Field("P_NAME")
        private String name;

        @Field("P_AGE")
        private byte age;
    }

    @BeforeAll static void setUpTable() {
        long[] uids = {1L, 2L, 3L};
        String[] names = {"John", "Lisa", "Eva"};
        byte[] ages = {30, 25, 40};
        try {
            conn = DriverManager.getConnection("jdbc:h2:mem:test");
            conn.createStatement().execute("CREATE TABLE PERSONS(P_UID BIGINT PRIMARY KEY, NAME VARCHAR(150), AGE TINYINT);");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO PERSONS(P_UID, NAME, AGE) VALUES(?, ?, ?)");
            for(int i = 0; i < uids.length; ++i) {
                ps.setLong(1, uids[i]);
                ps.setString(2, names[i]);
                ps.setByte(3, ages[i]);
                ps.executeUpdate();
            }      
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test void testDataBase(){
        try{
            conn.createStatement().executeQuery("SELECT COUNT(*) FROM PERSONS;");
        } catch(SQLException ex){

        }
    }
}
