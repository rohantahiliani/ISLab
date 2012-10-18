package edu.gatech.islab.chat.main;

import edu.gatech.islab.chat.utilities.*;
import edu.gatech.islab.chat.utilities.xmpp.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Main {
    
    private HashMap<String, User> userMap;
    private HashMap<User, Session> userSession;

    public Main() {
        userMap = new HashMap<String, User>();
        userSession = new HashMap<User, Session>();
    }

    public Object[] doOperation(ArrayList<Object> args) {
        
        Operation operation = (Operation)args.get(0);
        AccountType type = (AccountType)args.get(1);
        String username = (String)args.get(2);
        User user = userMap.get(username);

        Object[] retArray = null;
        
        if(user == null) {
            switch(type) {
            case GOOGLE:
                user = new GoogleUser(username, username);
                break;
            default:
                break;
            }
            userMap.put(username, user);
        }

        Session session = userSession.get(user);

        if(session == null) {
            switch(type) {
            case GOOGLE:
                session = new GoogleSession();
                break;
            default:
                break;
            }
            userSession.put(user, session);
        }

        XMPPUtility xmpp = session.getNewSession();
        
        switch(operation) {
        case LOGIN:
            String pass = (String)args.get(3);
            xmpp.login(username, pass);
            break;

        case SENDMESSAGE:
            String message = (String)args.get(3);
            String recipient = (String)args.get(4);
            xmpp.sendMessage(message, new GoogleUser(recipient, recipient));
            break;

        case GETFRIENDS:
            List<User> friends = xmpp.getFriendList();
            retArray = new Object[]{friends};
            break;

        case DISCONNECT:
            xmpp.disconnect();
            userMap.remove(user);
            userSession.remove(user);
            break;
        }
        return retArray;
    }

}