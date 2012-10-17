package edu.gatech.islab.chat.utilities.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;

public class GoogleXMPPUtility extends XMPPUtility {

    public GoogleXMPPUtility() {
        super(new ConnectionConfiguration
              ("talk.google.com", 5222, "gmail.com"));
    }
}
