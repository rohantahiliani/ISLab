package edu.gatech.islab.chat.main;

import edu.gatech.islab.chat.enums.*;
import edu.gatech.islab.chat.session.*;
import edu.gatech.islab.chat.user.*;
import edu.gatech.islab.chat.xmpp.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Main {
    
    private HashMap<String, User> userMap;
    private HashMap<User, Session> userSession;

    private MainHelper helper;

    public Main() {
        userMap = new HashMap<String, User>();
        userSession = new HashMap<User, Session>();
        helper = new MainHelper();
    }

    public Object[] doOperation(HashMap<String, Object> args) {
        
        Operation operation = (Operation)args.get("Operation");
        AccountType type = (AccountType)args.get("AccountType");
        String username = (String)args.get("Username");
        User user = null;
        Session session = null;
        Object[] retArray = null;

        if(type == null ||
           type == AccountType.NULL ||
           operation == null || 
           operation == Operation.NULL) {

            return new Object[]{"Failure"};
        }

        getUserAndSession(username, user, session, type, operation);

        if(user == null || session == null || 
        (operation != Operation.LOGIN &&
         !session.getSessionId().equals(args.get("SessionId")))) {
            
            return new Object[]{"Failure"};
        } 

        XMPPUtility xmpp = session.getNewSession();

        switch(operation) {
        case LOGIN:
            String pass = (String)args.get("Password");
            if(xmpp.login(username, pass)) {
                retArray = new Object[]{"Success", session.getSessionId()};
            } else {
                retArray = new Object[]{"Failure"};
            }
            break;

        case SENDMESSAGE:
            String message = (String)args.get("Message");
            String recipient = (String)args.get("Recipient");
            xmpp.sendMessage(message, new GoogleUser(recipient, recipient));
            break;

        case GETFRIENDS:
            List<User> friends = xmpp.getFriendList();
            retArray = new Object[]{friends};
            break;

        case DISCONNECT:
            if(!xmpp.disconnect()) {
                retArray = new Object[]{"Success"};
            } else {
                retArray = new Object[]{"Failure"};
            }
            userMap.remove(user);
            userSession.remove(user);
            break;
        }
        return retArray;
    }

    private void getUserAndSession
        (String username, User user, Session session, 
         AccountType type, Operation operation) {

        user = userMap.get(username + type);
        
        if(user!=null) {
            switch(type) {
            case GOOGLE:
                if(!(user instanceof GoogleUser)) {
                    user = null;
                }
                break;
            default:
                break;
            }
        }

        if(user == null && operation == Operation.LOGIN) {
            switch(type) {
            case GOOGLE:
                user = new GoogleUser(username, username);
                break;
            default:
                break;
            }
            userMap.put(username + type, user);
        }

        if(user == null) {
            return;
        }

        session = userSession.get(user);

        if(session == null) {
            String sessionId = helper.getNewSessionId();
            
            switch(type) {
            case GOOGLE:
                session = new GoogleSession();
                break;
            default:
                break;
            }

            session.setSessionId(sessionId);
            userSession.put(user, session);
        } 
    }

}