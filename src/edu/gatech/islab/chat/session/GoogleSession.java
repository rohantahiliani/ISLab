package edu.gatech.islab.chat.session;

import edu.gatech.islab.chat.xmpp.*;

public class GoogleSession extends Session {
    
    private XMPPUtility session;

    @Override
    public XMPPUtility getNewSession() {
        if(this.session == null) {
            this.session = new GoogleXMPPUtility();
        }
        return this.session;
    }

}