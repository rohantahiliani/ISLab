package edu.gatech.islab.chat.db;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtilities implements DB {

    private static DBUtilities instance;
    private Connection connection;
    private PreparedStatement batchStmt;

    private final int BATCH_SIZE = 50;
    private int updateCounter = 0;

    protected DBUtilities() {

    }

    public static synchronized DBUtilities getInstance() {
        if(instance == null) {
            instance = new DBUtilities();
        }
        return instance;
    }

    public boolean updateCommand(String query, Object[] args, boolean forceUpdate) {
        connect();
        try {
            if(this.connection != null) {
                if(batchStmt == null || batchStmt.isClosed()) {
                    batchStmt = this.connection.prepareStatement(query);
                }
                int i = 1;
                for(Object arg: args) {
                    batchStmt.setObject(i++, arg);
                }
                batchStmt.addBatch();
                updateCounter++;

                if(forceUpdate ||
                   updateCounter >= BATCH_SIZE) {
                    executeBatch();
                }
                return true;
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        } 
        return false;
    }

    public ResultSet selectCommand(String query, Object[] args) {
        connect();
        ResultSet retVal = null;
        PreparedStatement selectStmt = null;
        try {
            if(this.connection != null) {
                selectStmt = this.connection.prepareStatement(query);
                int i = 1;
                for(Object arg: args) {
                    selectStmt.setObject(i++, arg);
                }
                retVal = selectStmt.executeQuery();
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        } 
        return retVal;
    }

    public boolean selectNonEmpty(String query, Object[] args) {
        ResultSet rs = selectCommand(query, args);
        boolean retVal = false;
        try {
            if(rs != null) {
                retVal = rs.next();
                rs.close();
            }
        } catch(SQLException ex) {
        }
        return retVal;
    }

    private void connect() {
        try {
            if(this.connection == null || this.connection.isClosed()) {
                URI dbUri = new URI(System.getenv("CLEARDB_DATABASE_URL"));

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:mysql://us-cdbr-east-02.cleardb.com/heroku_1b1fd0a7eae634e?autoReconnectForPools=true";
                this.connection = DriverManager.getConnection
                    (dbUrl, username, password);
                this.connection.setAutoCommit(false);
            }
        } catch(SQLException sex) {
            sex.printStackTrace();
        } catch(URISyntaxException uex) {
            uex.printStackTrace();
        }
    }

    private void commit() throws SQLException{
        if(this.connection != null) {
            this.connection.commit();
        }
    }

    private void executeBatch() throws SQLException{
        if(batchStmt != null && !batchStmt.isClosed()) {
            batchStmt.executeBatch();
            commit();
            updateCounter = 0;
        }
    }

    public static synchronized void close() {
        try {
            DBUtilities db = getInstance();
            db.executeBatch();
            if(db.connection != null) {
                db.connection.close();
            }
            db = null;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

}