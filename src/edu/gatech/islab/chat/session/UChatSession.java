package edu.gatech.islab.chat.session;

import edu.gatech.islab.chat.db.*;
import edu.gatech.islab.chat.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

public class UChatSession extends Session {
    
    private DB db;
    private final String insertStmt;
    private final String validLoginQuery;
    

    public UChatSession() {
        this.sessionType = "UChatSession";

        db = DBUtilities.getInstance();
        insertStmt = "INSERT INTO USERS(Username, Password, Salt) " +
            "VALUES(?, ?, ?);";
        validLoginQuery = "SELECT Username, Password, Salt FROM Users " +
            "WHERE Username = ?";
    }

    public boolean createUser(String username, String password) {
        assert username != null;
        assert password != null;

        boolean retVal = false;
        String salt = System.currentTimeMillis() + "";
        String hashedPass = hashPassword(password, salt);
        
        Object[] args = new Object[]{username, hashedPass, salt};

        retVal = db.updateCommand(insertStmt, args);

        return retVal;
    }

    @Override
    public boolean login(String username, String password) {
        assert username != null;
        assert password != null;

        boolean retVal = false;
        Object[] args = new Object[]{username};
        ResultSet result = db.selectCommand(validLoginQuery, args);
        if(result!=null) {
            try {
                if(result.next()) {
                    String resultPass = result.getString("Password");
                    String resultSalt = result.getString("Salt");
                    retVal = resultPass.equals(hashPassword(password, resultSalt));
                }
                result.close();
            } catch(SQLException ex) {
            }
        }
        return retVal;
    }

    @Override
    public boolean disconnect() {
        return true;
    }

    private String hashPassword(String password, String salt) {
        String retVal = null;
        retVal = DigestUtils.sha512Hex(password + salt);
        return retVal;
    }

}