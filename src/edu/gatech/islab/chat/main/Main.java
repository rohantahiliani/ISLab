package edu.gatech.islab.chat.main;

import edu.gatech.islab.chat.utilities.*;
import edu.gatech.islab.chat.utilities.xmpp.*;

import java.util.List;
import java.util.HashMap;

public class Main {
    
    private HashMap<String, User> userMap;
    private HashMap<User, Session> userSession;

    public Main() {
        userMap = new HashMap<String, User>();
        userSession = new HashMap<User, Session>();
    }

    public void doOperation(Object args[]) {
        
        Operation operation = (Operation)args[0];
        AccountType type = (AccountType)args[1];
        String username = (String)args[2];
        User user = userMap.get(username);
        
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
            String pass = (String)args[3];
            xmpp.login(username, pass);
            break;

        case SENDMESSAGE:
            String message = (String)args[3];
            String recipient = (String)args[4];
            xmpp.sendMessage(message, new GoogleUser(recipient, recipient));
            break;

        case GETFRIENDS:
            List<String> friends = xmpp.getFriendList();
            System.out.println(friends);
            break;

        case DISCONNECT:
            xmpp.disconnect();
            break;
        }
        
    }

}