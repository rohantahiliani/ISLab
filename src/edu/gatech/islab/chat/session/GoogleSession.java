package edu.gatech.islab.chat.session;

import edu.gatech.islab.chat.user.User;
import edu.gatech.islab.chat.xmpp.GoogleXMPPUtility;
import edu.gatech.islab.chat.xmpp.XMPPUtility;

import java.util.List;

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

    @Override
    public boolean login(String username, String password) {
        return getSession().login(username, password);
    }

    @Override
    public void sendMessage(String message, User recipient) {
        getSession().sendMessage(message, recipient);
    }

    @Override
    public List<User> getFriendList() {
        return getSession().getFriendList();
    }

    @Override
    public boolean disconnect() {
        return getSession().disconnect();
    }

}