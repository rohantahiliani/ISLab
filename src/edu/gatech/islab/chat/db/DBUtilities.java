package edu.gatech.islab.chat.db;

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

    public static DBUtilities getInstance() {
        if(instance == null) {
            instance = new DBUtilities();
        }
        return instance;
    }

    public boolean updateCommand(String query, Object[] args) {
        connect();
        try {
            PreparedStatement insertStmt = this.connection.prepareStatement(query);
            int i = 0;
            for(Object arg: args) {
                insertStmt.setObject(i++, arg);
            }
            insertStmt.executeUpdate();
            return true;
        } catch(SQLException ex) {
        }
        return false;
    }

    public ResultSet selectCommand(String query, Object[] args) {
        connect();
        ResultSet retVal = null;
        try {
            PreparedStatement selectStmt = this.connection.prepareStatement(query);
            int i = 0;
            for(Object arg: args) {
                selectStmt.setObject(i++, arg);
            }
            retVal = selectStmt.executeQuery();
        } catch(SQLException ex) {
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
                Class.forName("com.mysql.jdbc.Driver");
                this.connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/islab", "tomcat6", "");
            }
        } catch(ClassNotFoundException cex) {
            cex.printStackTrace();
        } catch(SQLException sex) {
            sex.printStackTrace();
        }
    }

}