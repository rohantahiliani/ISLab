package edu.gatech.islab.chat.user;

import edu.gatech.islab.chat.enums.AccountType;
import edu.gatech.islab.chat.session.Session;

public class User {
    
    private final String login;
    private final String username;
    private final AccountType accountType;
    private Session session;

    protected User() {
        login = null;
        username = null;
        accountType = null;
    }
    
    public User(String username, String login, 
                AccountType accountType) {
        assert login != null;
        assert username != null;
        assert accountType != null && accountType != AccountType.NULL;
        this.login = login;
        this.username = username;
        this.accountType = accountType;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public String getLogin() {
        return this.login;
    }
    
    public String getUserName() {
        return this.username;
    }

    public void setSession(Session session) {
        assert session != null;
        this.session = session;
    }

    public Session getSession() {
        return this.session;
    }

    @Override
    public boolean equals(Object user) {
        if(user instanceof User) {
	        User that = (User) user;
	        
	        if((this.session == null && that.session != null) ||
	           (this.session != null && that.session == null) || 
	           (this.session != null && that.session != null &&
	             !this.session.equals(that.session))) {
	            return false;
	        } 
	
	        return this.accountType == that.accountType && 
	            this.login.equals(that.login) &&
	            this.username.equals(that.username);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Login: " + this.login + ", " +
            "Username: " + this.username + ", " +
            "AccountType: " + this.accountType + ", " +
            "Session: " + ((this.session == null) ? 
                           "NULL" : this.session.toString());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += 13 + this.login.hashCode();
        hashCode += 23 + this.username.hashCode();
        hashCode += 5 + this.accountType.toString().hashCode();
        if(this.session == null) {
            hashCode += 3;
        } else {
            hashCode += 3 * session.hashCode();
        }
        return hashCode;
    }

}