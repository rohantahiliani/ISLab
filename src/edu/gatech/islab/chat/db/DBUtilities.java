package edu.gatech.islab.chat.db;

import java.sql.ResultSet;

public class DBUtilities implements DB {

    private static DBUtilities instance;

    protected DBUtilities() {

    }

    public static DBUtilities getInstance() {
        if(instance == null) {
            instance = new DBUtilities();
        }
        return instance;
    }

    public boolean insertCommand(String query) {
        return false;
    }

    public boolean updateCommand(String query) {
        return false;
    }

    public ResultSet selectCommand(String query) {
        return null;
    }

}