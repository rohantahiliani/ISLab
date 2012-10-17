package edu.gatech.islab.chat.utilities;

public abstract class User {
    
    public abstract String getUserName();

    public abstract String getUserNick();

    public abstract String getLogin();
    
    public boolean hasGoogleLogin() {
        return false;
    }

    public boolean hasFacebookLogin() {
        return false;
    }

    public boolean hasYahooLogin() {
        return false;
    }

    public abstract boolean equals(Object user);
    
    public abstract String toString();
}