package com.github.mukhlisov.repositories;

import com.github.mukhlisov.exceptions.CouldNotConnectDataBaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SQLiteRepository<K, E> implements AutoCloseable {

    private static final String URL = "jdbc:sqlite:currency_exchange.db";
    protected Connection connection;
    protected String table;

    public SQLiteRepository(String table) throws CouldNotConnectDataBaseException{
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            this.table = table;
        }catch (ClassNotFoundException | SQLException e){
            throw new CouldNotConnectDataBaseException(e.getMessage(), e);
        }
    }

    public List<E> getAll() throws SQLException{
        List<E> list = new ArrayList<E>();
        try (Statement statement = connection.createStatement()){
            String sql = String.format("SELECT * FROM %s", table);
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                list.add(castToEntity(result));
            }
        }
        return list;
    }

    public Optional<E> getById(K id) throws SQLException{
        try (Statement statement = connection.createStatement()){
            String sql = String.format("SELECT * FROM %s WHERE id = %d", table, id);

            ResultSet result = statement.executeQuery(sql);
            return Optional.of(castToEntity(result));
        }
    }

    public void insert(E entity) throws SQLException{
        String sql = String.format("INSERT INTO %s (%s) VALUES %s", table, getColumns(), getPlaceHolders());
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        setInsertValues(preparedStatement, entity);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void update(E entity) throws SQLException{
        String sql = String.format("UPDATE %s SET %s WHERE id = ?", table, getUpdateColumns());
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        setUpdateValues(preparedStatement, entity);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void delete(E entity) throws SQLException{
        String sql = "DELETE FROM " + table + " WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, getEntityId(entity));
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    @Override
    public void close() throws Exception {
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }

    /**
     * Override cast method as in this example
     * {@code return new E(result.getString(stringName), result.getString(anotherStringName), etc...);}
     */
    protected abstract E castToEntity(ResultSet result) throws SQLException;

    /**
     * Override method as in this example {@code return "(?, ?, ...)";}
     * Place ? in brackets as many times as the number of fields in ur Entity
     * except the id.
     */
    protected abstract String getPlaceHolders();

    /**
     * Override it in this way {@code return "(field_name1, field_name2, ...)";}
     * Put all fields except the id field.
     * Field names must be same as field in database table
    */
    protected abstract String getColumns();

    /**
     * Override this method so that it returns a string of the form
     * {@code return "field_name1 = ?, field_name2 = ?, ...";}
    * */
    protected abstract String getUpdateColumns();

    /**
     * Override get method to get Entity id {@code return entity.getId();}
     */
    protected abstract String getEntityId(E entity);

    /**
     * This method should fill prepared statement with entity fields values except entity id.
    * */
    protected abstract void setInsertValues(PreparedStatement preparedStatement, E entity) throws SQLException;
    /**
     * This method should fill prepared statement with entity fields values.
     * */
    protected abstract void setUpdateValues(PreparedStatement preparedStatement, E entity) throws SQLException;
}
