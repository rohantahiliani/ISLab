package edu.gatech.islab.chat.session;

import edu.gatech.islab.chat.user.User;
import edu.gatech.islab.chat.xmpp.XMPPUtility;

import java.util.List;
import java.util.Map;

public abstract class XMPPSession extends Session {

    public abstract XMPPUtility getSession();

    @Override
    public boolean login(String username, String password) {
        return getSession().login(username, password);
    }

    public void sendMessage(String message, User recipient) {
        getSession().sendMessage(message, recipient);
    }

    public Map<User, List<String>> getMessages() {
        return getSession().getMessages();
    }

    public List<User> getFriendList() {
        return getSession().getFriendList();
    }

    @Override
    public boolean disconnect() {
        return getSession().disconnect();
    }

}