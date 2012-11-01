package edu.gatech.islab.chat.session;

import edu.gatech.islab.chat.user.User;
import edu.gatech.islab.chat.xmpp.JabberXMPPUtility;
import edu.gatech.islab.chat.xmpp.XMPPUtility;

import java.util.List;

public class JabberSession extends XMPPSession {
    
    private XMPPUtility session;

    public JabberSession() {
        this.sessionType = "JabberSession";
    }

    @Override
    public XMPPUtility getSession() {
        if(this.session == null) {
            this.session = new JabberXMPPUtility();
        }
        return this.session;
    }
}