package edu.gatech.islab.chat.session;

import edu.gatech.islab.chat.xmpp.GoogleXMPPUtility;
import edu.gatech.islab.chat.xmpp.XMPPUtility;

public class GoogleSession extends XMPPSession {
    
    private XMPPUtility session;

    public GoogleSession() {
        this.sessionType = "GoogleSession";
    }

    @Override
    public XMPPUtility getSession() {
        if(this.session == null) {
            this.session = new GoogleXMPPUtility();
        }
        return this.session;
    }
}