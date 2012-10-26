package edu.gatech.islab.chat.session;

public abstract class Session {

    public String sessionId;
    public String sessionType;
    
    public void setSessionId(String id) {
        this.sessionId = id;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public abstract boolean login(String username, String password);   
 
    public abstract boolean disconnect();

    @Override
    public boolean equals(Object session) {
        if(session == null) {
            return false;
        }

        Session that = (Session) session;
        if((this.sessionId == null && that.sessionId != null) ||
           (this.sessionId != null && that.sessionId == null) ||
           (this.sessionType == null && that.sessionType != null) ||
           (this.sessionType != null && that.sessionType == null)) {
            return false;
        }
        
        return this.sessionId.equals(that.sessionId) &&
            this.sessionType.equals(that.sessionType);
    }

    @Override
    public String toString() {
        if(this.sessionId == null || this.sessionType == null) {
            return "";
        }
        
        return "Session ID: " + this.sessionId + ", " +
            "Session Type: " + this.sessionType;
    }

    @Override
    public int hashCode() {
        if(this.sessionId == null || this.sessionType == null) {
            return 3;
        }

        int hashCode = 3;
        hashCode += 13 * this.sessionId.hashCode();
        hashCode += 17 * this.sessionType.hashCode();

        return hashCode;
    }

}