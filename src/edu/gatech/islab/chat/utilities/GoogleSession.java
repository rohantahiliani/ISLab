package edu.gatech.islab.chat.utilities;

import edu.gatech.islab.chat.utilities.xmpp.*;

public class GoogleSession extends Session {
    
    private XMPPUtility session;

    public XMPPUtility getNewSession() {
        if(this.session == null) {
            this.session = new GoogleXMPPUtility();
        }
        return this.session;
    }

}