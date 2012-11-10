package edu.gatech.islab.chat.session;

import edu.gatech.islab.chat.user.User;
import edu.gatech.islab.chat.xmpp.XMPPUtility;

import java.util.List;
import java.util.Map;

public abstract class XMPPSession extends Session {

    public abstract XMPPUtility getSession();

    @Override
    public boolean login(String username, String password) {
        boolean retVal = getSession().login(username, password);
        if(retVal) {
            this.username = username;
            this.logUser("LOGIN");
        }
        return retVal;
    }

    public void sendMessage(String message, User recipient) {
        getSession().sendMessage(message, recipient);
        this.logUser("SENDMESSAGE");
    }

    public Map<User, List<String>> getMessages() {
        return getSession().getMessages();
    }

    public List<User> getFriendList() {
        return getSession().getFriendList();
    }

    @Override
    public boolean disconnect() {
        boolean retVal = getSession().disconnect();
        if(retVal) {
            this.logUser("LOGOUT");
        }
        return retVal;
    }

}