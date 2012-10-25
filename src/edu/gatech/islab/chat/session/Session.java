package edu.gatech.islab.chat.session;

import edu.gatech.islab.chat.xmpp.XMPPUtility;

public abstract class Session {

    public String sessionId;
    
    public abstract XMPPUtility getNewSession();

    public void setSessionId(String id) {
        this.sessionId = id;
    }

    public String getSessionId() {
        return this.sessionId;
    }
}