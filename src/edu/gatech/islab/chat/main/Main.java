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

        String username = (String) args.get("Username");
        Operation operation = (Operation) args.get("Operation");
        AccountType accountType = (AccountType) args.get("AccountType");
        User user = null;
        Session session = null;
        Object[] retArray = null;

        if(operation == null || 
           operation == Operation.NULL) {

            return new Object[]{"Invalid account type or operation"};
        }

        user = getUser(username, accountType, operation);

        if(user != null) {
            session = user.getSession();
            accountType = user.getAccountType();
        } 

        if(user == null || session == null || 
           accountType == null || accountType == AccountType.NULL) {
            return new Object[]{"Invalid user, session or account type"};
        } 

        if(accountType != AccountType.UCHAT) {
            if(!validateUChatSession(args)) {
                return new Object[]{"Invalid UChat session"};
            }
        } 

        if((operation != Operation.LOGIN  && operation != Operation.REGISTER) &&
           !session.getSessionId().equals(args.get("SessionId"))) {
            
            return new Object[]{"Invalid session"};
        }

        switch(operation) {
        case REGISTER:
        case LOGIN:
            String password = (String)args.get("Password");
            String retMessage = "";
            boolean opSuccess = false;

            if(operation == Operation.REGISTER) {
                if(user.getAccountType() != AccountType.UCHAT) {
                    retArray = new Object[]{"Invalid command. Trying to hack?"};
                } else {
                   retMessage = ((UChatSession)session).
                        createUser(user.getLogin(), password);
                   if(retMessage.equals("Success")) {
                       opSuccess = true;
                   } else {
                       opSuccess = false;
                   }
                }
            } else {
                opSuccess = session.login(user.getLogin(), password);
            }

            if(opSuccess) {
                retArray = new Object[]{"Success", session.getSessionId()};
                userMap.put(user.getLogin() + accountType, user);
            } else {
                if(retMessage.equals("")) {
                    retMessage = "Login Failed";
                }
                retArray = new Object[]{retMessage};
            }
            break;

        case VALIDATE:
            retArray = new Object[]{"Success"};
            break;

        case SENDMESSAGE:
            if(session instanceof XMPPSession) {
                String message = (String)args.get("Message");
                String recipient = (String)args.get("Recipient");
                if(message != null && recipient != null) {
                    ((XMPPSession)session).sendMessage
                        (message, new User(recipient, recipient, accountType));
                    retArray = new Object[]{"Success"};
                }
            } else {
                retArray = new Object[]{"Invalid command. Trying to hack?"};
            }
            break;

        case GETMESSAGES:
            if(session instanceof XMPPSession) {
                retArray = new Object[]{((XMPPSession)session).getMessages()};
            } else {
                retArray = new Object[]{"Invalid command. Trying to hack?"};
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
        (String username, AccountType accountType, Operation operation) {

        User user = null;

        user = userMap.get(username + accountType);
        
        if(user!=null) {
            if(accountType == null || user.getAccountType() != accountType) {
                user = null;
            } 
        }

        if(user == null && 
           (operation == Operation.LOGIN || operation == Operation.REGISTER)) {

            user = new User(username, username, accountType);

            Session session = null;
            String sessionId = helper.getNewSessionId();
            
            switch(accountType) {
            case GOOGLE:
                session = new GoogleSession();
                break;
            case UCHAT:
                session = new UChatSession();
                break;
            case JABBER:
                session = new JabberSession();
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
        for(AccountType accountType: AccountType.values()) {
            userMap.remove(user.getLogin() + accountType);
        }
    }

    private boolean validateUChatSession(HashMap<String, Object> args) {
        String ucUsername = AccountType.UCHAT + "Username";
        String ucSessionId = AccountType.UCHAT + "SessionId";
        HashMap<String, Object> validateArgs = new HashMap<String, Object>();
        validateArgs.put("Username", args.get(ucUsername));
        validateArgs.put("SessionId", args.get(ucSessionId));
        validateArgs.put("Operation", Operation.VALIDATE);
        validateArgs.put("AccountType", AccountType.UCHAT);
        Object[] validate = this.doOperation(validateArgs);
        if(validate != null && validate.length > 0) {
            if(validate[0].toString().equals("Success")) {
                return true;
            }
        }
        return false;

    }

}