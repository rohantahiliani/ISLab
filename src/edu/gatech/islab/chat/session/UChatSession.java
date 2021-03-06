package edu.gatech.islab.chat.session;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;

import edu.gatech.islab.chat.db.DB;
import edu.gatech.islab.chat.db.DBUtilities;

public class UChatSession extends Session {
    
    private DB db;
    private final String insertStmt;
    private final String userExistsQuery;
    private final String validLoginQuery;

    public UChatSession() {
        this.sessionType = "UChatSession";

        db = DBUtilities.getInstance();
        insertStmt = "INSERT INTO Users(Username, Password, Salt) " +
            "VALUES(?, ?, ?);";
        userExistsQuery = "SELECT Username FROM Users " +
            "WHERE Username = ?";
        validLoginQuery = "SELECT Username, Password, Salt FROM Users " +
            "WHERE Username = ?";
    }

    public String createUser(String username, String password) {
        if(username != null &&  password != null) {

            Object[] args = new Object[]{username};

            if(db.selectNonEmpty(userExistsQuery, args)) {
                return "Username exists";
            }

            boolean retVal = false;
            String salt = System.currentTimeMillis() + "";
            String hashedPass = hashPassword(password, salt);
        
            args = new Object[]{username, hashedPass, salt};

            retVal = db.updateCommand(insertStmt, args);

            if(retVal) {
                this.username = username;
                this.logUser("REGISTER");
                return "Success";
            } else {
                return "Account Creation Failed";
            }
        } else {
            return "Invalid Username or Password";
        }
    }

    @Override
    public boolean login(String username, String password) {
        boolean retVal = false;

        if(username != null &&  password != null) {
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
                    ex.printStackTrace();
                }
            }
            if(retVal) {
                this.username = username;
                this.logUser("LOGIN");
            }
        } 
        return retVal;
    }

    @Override
    public boolean disconnect() {
        this.logUser("LOGOUT");
        return true;
    }

    private String hashPassword(String password, String salt) {
        String retVal = null;
        retVal = DigestUtils.sha512Hex(password + salt);
        return retVal;
    }

}