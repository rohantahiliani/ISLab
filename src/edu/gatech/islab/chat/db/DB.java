package edu.gatech.islab.chat.db;

import java.sql.ResultSet;

public interface DB {

    public boolean updateCommand(String query, Object[] args);

    public ResultSet selectCommand(String query, Object[] args);
}