package edu.gatech.islab.chat.session;

public abstract class Session {

    public String sessionId;
    
    public void setSessionId(String id) {
        this.sessionId = id;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public abstract boolean login(String username, String password);   
 
    public abstract boolean disconnect();
}