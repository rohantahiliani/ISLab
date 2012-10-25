package edu.gatech.islab.chat.user;

public class UChatUser extends User {

    private String username;
    private String login;

    public UChatUser(String login, String username) {
        this.username = username;
        this.login = login;
    }

    public String getUserName() {
        return this.username;
    }

    public String getUserNick() {
        return "";
    }

    public String getLogin() {
        return this.login;
    }
    
    @Override
    public boolean hasUChatLogin() {
        return true;
    }

    @Override
    public String toString() {
        return this.username + " " + this.login;
    }

    @Override
    public boolean equals(Object user) {
        UChatUser that = (UChatUser) user;

        return this.username.equals(that.username) && 
            this.login.equals(that.login);
    }
}
