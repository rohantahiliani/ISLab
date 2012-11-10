package edu.gatech.islab.chat.db;

import java.sql.ResultSet;

public interface DB {

    public boolean batchCommand(String query, Object[] args);

    public boolean updateCommand(String query, Object[] args);

    public ResultSet selectCommand(String query, Object[] args);

    public boolean selectNonEmpty(String query, Object[] args);
}