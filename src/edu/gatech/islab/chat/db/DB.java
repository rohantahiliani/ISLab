package edu.gatech.islab.chat.db;

import java.sql.ResultSet;

public interface DB {

    public boolean insertCommand(String query);

    public boolean updateCommand(String query);

    public ResultSet selectCommand(String query);
}