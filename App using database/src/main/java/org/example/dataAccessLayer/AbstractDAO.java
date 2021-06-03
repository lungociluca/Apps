package org.example.dataAccessLayer;

import org.example.model.DbObject;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.beans.PropertyDescriptor;

public class AbstractDAO<T extends DbObject>{
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO(Class<T> type) {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private static void closeConnection(ResultSet rs, PreparedStatement statement, Connection connection) {
        ConnectionFactory.close(connection);
        ConnectionFactory.close(statement);
        ConnectionFactory.close(rs);
    }

    /**
     *
     * @param field parameter for the select query
     * @return returns a string representing a sql statement
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM  schooldb.");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    private String createSelectByNameQuery(String name) {
        return "SELECT * FROM schooldb." + type.getSimpleName() + " WHERE name = '" + name + "'";
    }

    private String createViewTableQuery() {
        return "SELECT * FROM  schooldb." + type.getSimpleName();
    }

    private String createInsertQuery(String values) {
        return "INSERT INTO  schooldb." + type.getSimpleName() + " VALUES (" + values + ");";
    }

    private String deleteQuery(String name) {
        return "DELETE FROM " + type.getSimpleName() + " WHERE name = '" + name + "'";
    }

    private String updateQuery(String name, String field, String newValue) {
        return "UPDATE " + type.getSimpleName() + " SET " + field +" = " + newValue + " WHERE name = '" + name + "'";
    }

    /**
     *
     * @param cls (the class for which you want to get the fields)
     * @return a list of Strings with the names of the fields
     */
    public static List<String> getTableHeader(Class cls) {
        List<String> list = new ArrayList<>();
        for(Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                list.add(field.getName());
            } catch (Exception e) {
                System.out.println("Error in AbstractDAO, getTableHeader()");
            }
        }
        return list;
    }

    /**
     *
     * @param resultSet (the result of a select query)
     * @return a list with the equivalent classes of the result set
     */
    public List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();

                int id = 0;
                for(Field field : type.getSuperclass().getDeclaredFields()) {
                    field.setAccessible(true);
                    id = resultSet.getInt(field.getName());
                }

                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();

                    try {
                        method.invoke(instance, value);
                    }
                    catch (Exception e) {

                    }
                }
                instance.setID(id);
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     *
     * @param object  an instance of an object
     * @return a string with the values of the fields and superclass fields.
     */
    private String getObjectFields(Object object) {
        String values = "";

        for(Field field : object.getClass().getSuperclass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(object);
                values += value + ", ";
            }catch (Exception e) {
                System.out.println("ceva eroare");
            }
        }

        for(Field field : object.getClass().getDeclaredFields()){
            field.setAccessible(true);
            Object value;
            try{
                value = field.get(object);
                values += "'" + value + "', ";
            }
            catch (Exception e) {
                System.out.println("an error");
            }
        }
        values += "-";
        values = values.replaceAll(", -", "");
        return values;
    }

    /**
     *
     * @return list with the equivalent objects of the data base result set
     */
    public List<T> viewTable() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createViewTableQuery();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        }
        finally {
            closeConnection(resultSet, statement, connection);
        }

        return null;
    }

    /**
     *
     * @param id the id of the searched record
     * @return list with the equivalent objects of the data base result set
     */
    public List<T> findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public List<T> findByName(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectByNameQuery(name);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByName " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /*
    public String find(int id) {
        String toReturn = "";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery(id);

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            System.out.println(query);
            resultSet.next();

            while(!resultSet.isAfterLast()) {
                for(Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    toReturn += resultSet.getString(field.getName()) + " & ";
                }
                toReturn += "\n";
                resultSet.next();
            }
        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        }
        finally {
            closeConnection(resultSet, statement, connection);
        }

        return toReturn;
    }*/

    public String insert(Object object) {
        String values = getObjectFields(object);
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createInsertQuery(values);

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            System.out.println(query);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:insert " + e.getMessage());
        }
        finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return values;
    }

    public void delete(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = deleteQuery(name);
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        }
        finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void update(String name, String field, String newValue) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = updateQuery(name, field, newValue);

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            System.out.println(query);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:update " + e.getMessage());
        }
        finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}
