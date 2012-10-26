package edu.gatech.islab.chat.main;

import edu.gatech.islab.chat.enums.*;
import edu.gatech.islab.chat.session.*;
import edu.gatech.islab.chat.user.User;
import edu.gatech.islab.chat.xmpp.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Main {
    
    private HashMap<String, User> userMap;

    private MainHelper helper;

    public Main() {
        userMap = new HashMap<String, User>();
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

            return new Object[]{"Invalid account type or operation"};
        }

        user = getUser(username, type, operation);

        if(user != null) {
            session = user.getSession();
        }

        if(user == null || session == null) {            
            return new Object[]{userMap, "Invalid user or session"};
        } 

        if((operation != Operation.LOGIN  && operation != Operation.REGISTER) &&
           !session.getSessionId().equals(args.get("SessionId"))) {
            
            return new Object[]{"Invalid session"};
        }

        switch(operation) {
        case LOGIN:
        case REGISTER:
            String pass = (String)args.get("Password");
            if(session.login(user.getLogin(), pass)) {
                retArray = new Object[]{"Success", session.getSessionId()};
                userMap.put(user.getLogin() + type, user);
            } else {
                retArray = new Object[]{"Login Failed"};
            }
            break;

        case SENDMESSAGE:
            String message = (String)args.get("Message");
            String recipient = (String)args.get("Recipient");

            if(session instanceof XMPPSession) {
                ((XMPPSession)session).sendMessage
                    (message, new User(recipient, recipient, type));
            }
            break;

        case GETFRIENDS:
            if(session instanceof XMPPSession) {
                List<User> friends = ((XMPPSession)session).getFriendList();
                retArray = new Object[]{friends};
            }
            break;

        case DISCONNECT:
            if(!session.disconnect()) {
                retArray = new Object[]{"Success"};
            } else {
                retArray = new Object[]{"Failed to Disconnect"};
            }
            removeUserSessions(user);
            break;
        default:
            break;
        }
        return retArray;
    }

    private User getUser
        (String username, AccountType type, Operation operation) {

        User user = null;

        user = userMap.get(username + type);
        
        if(user!=null) {
            if(user.getAccountType() != type) {
                user = null;
            } 
        }

        if(user == null && 
           (operation == Operation.LOGIN || operation == Operation.REGISTER)) {

            user = new User(username, username, type);

            Session session = null;
            String sessionId = helper.getNewSessionId();
            
            switch(type) {
            case GOOGLE:
                session = new GoogleSession();
                break;
            case UCHAT:
                session = new UChatSession();
                break;
            default:
                break;
            }
            if(session != null) {
                session.setSessionId(sessionId);
                user.setSession(session);
            }
        }

        return user;
    }

    private void removeUserSessions(User user) {
        for(AccountType type: AccountType.values()) {
            userMap.remove(user.getLogin() + type);
        }
    }

}