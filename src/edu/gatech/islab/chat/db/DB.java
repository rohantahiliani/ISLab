package edu.gatech.islab.chat.db;

import java.sql.ResultSet;

public interface DB {

    public boolean updateCommand(String query, Object[] args, boolean forceUpdate);

    public ResultSet selectCommand(String query, Object[] args);

    public boolean selectNonEmpty(String query, Object[] args);
}