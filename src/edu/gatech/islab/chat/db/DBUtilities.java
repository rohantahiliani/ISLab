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

    protected DBUtilities() {

    }

    public static synchronized DBUtilities getInstance() {
        if(instance == null) {
            instance = new DBUtilities();
        }
        return instance;
    }

    public boolean updateCommand(String query, Object[] args) {
        connect();
        PreparedStatement insertStmt = null;
        try {
            if(this.connection != null) {
                insertStmt = this.connection.prepareStatement(query);
                int i = 1;
                for(Object arg: args) {
                    insertStmt.setObject(i++, arg);
                }
                insertStmt.executeUpdate();
                return true;
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
            	if(insertStmt != null) {
                    insertStmt.close();
            	}
            } catch(SQLException ex) {
            }
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
            }
        } catch(SQLException sex) {
            sex.printStackTrace();
        } catch(URISyntaxException uex) {
            uex.printStackTrace();
        }
    }

}