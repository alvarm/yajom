package io.alvarm.yajom.persistance;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.Map;

public class JDBCEntityManager implements EntityManager {

    private Connection conn;

    public JDBCEntityManager(Connection ct) {
        conn = ct;
    }

    @Override
    public <T> void persist(T obj) {
        Model m = Model.of(obj);
        try(PreparedStatement stmnt = conn.prepareStatement(getInsertStatement(m));) {
            IntStream.range(0, m.getFields().size()).boxed()
            .map(i -> Map.entry(i, m.getFields().get(i.intValue()).getKey()))
            .forEach(e -> {
                try {
                    stmnt.setObject(e.getKey(), e.getValue());
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            });
            // TODO: continue 
            ResultSet rs = stmnt.executeQuery();
        } catch(SQLException ex) {
            
        }

    }

    @Override
    public <T> List<T> retrieveAll(Class<T> clazz) {
        Model m = Model.of(clazz);
        List<T> list = new ArrayList<T>();
        try(PreparedStatement stmnt = conn.prepareStatement(getSelectAllStatement(m));) {
            Constructor<?> ctor = clazz.getConstructor(Void.class);
            m.getFields().stream().forEach(f -> f.getValue().setAccessible(true));
            ResultSet rs = stmnt.executeQuery();
            while(rs.next()) {
                T t = (T)ctor.newInstance(null);
                m.getFields().stream().forEach(e -> {
                    try {
                        e.getValue().set(t, rs.getObject(e.getKey()));
                    } catch (IllegalArgumentException | IllegalAccessException | SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
                list.add(t);
            }
        } catch(SQLException ex) {

        } catch(InstantiationException | InvocationTargetException | IllegalAccessException | IllegalArgumentException ex) {

        } catch (NoSuchMethodException | SecurityException ex) {

        }

        m.getFields().stream().forEach(f -> f.getValue().setAccessible(false));
        return list;
    }

    
    @Override
    public <T> void remove(T obj) {
        Model m = Model.of(obj);
        try(PreparedStatement stmnt = conn.prepareStatement(getDeleteStatement(m));){
            java.lang.reflect.Field fld = m.getKeyField().getValue();
            fld.setAccessible(true);
            stmnt.setObject(1, fld.get(obj));
            stmnt.executeUpdate();
        } catch(SQLException ex) {
            
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            
        }
        
    }
    
    private static String getInsertStatement(Model m) {
        
        String fieldString = String.join(", ",
        m.getFields().stream()
        .map(fld -> fld.getKey())
        .toList());
        
        String[] array = new String[m.getFields().size()];
        Arrays.setAll(array, i -> "?");
        String valuesString = String.join(", ", array);
        return """
                INSERT INTO %s (%s)
                VALUES (%s)
                """.formatted(m.getResourceName(), fieldString, valuesString);
    
    }
    
    private static String getUpdateStatement(Model m) {
        String fldString = String.join(", ",
                m.getNonKeyFields().stream()
                        .map(f -> "%s = ?".formatted(f.getKey()))
                        .toList());
    
        return """
                UPDATE %s SET
                   %s
                WHERE %s = ?
                """.formatted(m.getResourceName(), fldString, m.getKeyField().getKey());
    }
    
    private static String getDeleteStatement(Model m) {
        return """
                DELETE FROM %s
                WHERE %s = ?
                """
                .formatted(m.getResourceName(), m.getKeyField().getKey());
    }
    
    private static String getSelectStatement(Model m) {
        return """
                SELECT *
                FROM %s
                WHERE %s = ?
                """.formatted(m.getResourceName(), m.getKeyField().getKey());
    }
    
    private static String getSelectAllStatement(Model m) {
        return """
                SELECT *
                FROM %s
                """.formatted(m.getResourceName());
    }
}
