package edu.gatech.islab.chat.session;

import edu.gatech.islab.chat.user.User;
import edu.gatech.islab.chat.xmpp.XMPPUtility;

import java.util.List;

public abstract class XMPPSession extends Session {

    public abstract XMPPUtility getSession();

    public abstract void sendMessage(String message, User recipient);
    
    public abstract List<User> getFriendList();

}