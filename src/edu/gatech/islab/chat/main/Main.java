package edu.gatech.islab.chat.main;

import edu.gatech.islab.chat.enums.*;
import edu.gatech.islab.chat.session.*;
import edu.gatech.islab.chat.user.User;
import edu.gatech.islab.chat.xmpp.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Main {
    
    private final String SUCCESS = "Success";

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
           operation == Operation.NULL ||
           accountType == null || 
           accountType == AccountType.NULL ||
           username == null) {

            return new Object[]{"Invalid operation, account type or username"};
        }

        user = getUser(username, accountType, operation);

        if(user != null) {
            session = getSession(user, accountType);
        } 

        if(user == null || session == null) {
            return new Object[]{"Invalid user or session"};
        } 

        if(accountType != AccountType.UCHAT) {
            if(!validateUChatSession(args)) {
                return new Object[]{"Invalid UChat session"};
            }
        } 

        if((operation != Operation.LOGIN  && operation != Operation.REGISTER) &&
           (!session.getSessionId().equals(args.get("SessionId")))) {
            
            return new Object[]{"Invalid session"};
        }

        switch(operation) {
        case REGISTER:
        case LOGIN:
            String password = (String)args.get("Password");
            String retMessage = "Login Failed";
            boolean opSuccess = false;

            if(operation == Operation.REGISTER) {
                if(user.getAccountType() != AccountType.UCHAT) {
                    retArray = new Object[]{"Invalid command. Trying to hack?"};
                } else {
                   retMessage = ((UChatSession)session).
                        createUser(user.getLogin(), password);
                   if(retMessage.equals(SUCCESS)) {
                       opSuccess = true;
                   } else {
                       opSuccess = false;
                   }
                }
            } else {
                opSuccess = session.login(user.getLogin(), password);
            }

            if(opSuccess) {
                setSessionId(session);
                retArray = new Object[]{SUCCESS, session.getSessionId()};
                userMap.put(user.getLogin() + accountType, user);
            } else {
                retArray = new Object[]{retMessage};
            }
            break;

        case VALIDATE:
            retArray = new Object[]{SUCCESS};
            break;

        case SENDMESSAGE:
            if(session instanceof XMPPSession) {
                String message = (String)args.get("Message");
                String recipient = (String)args.get("Recipient");
                if(message != null && recipient != null) {
                    ((XMPPSession)session).sendMessage
                        (message, new User(recipient, recipient, accountType));
                    retArray = new Object[]{SUCCESS};
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
                retArray = new Object[]{SUCCESS};
            } else {
                retArray = new Object[]{"Failed to Disconnect"};
            }
            removeUserSessions(user, accountType);
            break;
        default:
            break;
        }
        return retArray;
    }

    private User getUser
        (String username, AccountType accountType, Operation operation) {

        assert username ==  null;
        User user = null;

        user = userMap.get(username + accountType);
        
        if(user == null && 
           (operation == Operation.LOGIN || operation == Operation.REGISTER)) {

            user = new User(username, username, accountType);
        }

        return user;
    }

    private Session getSession(User user, AccountType accountType) {
        if(user == null) {
            return null;
        }
        Session session = null;

        session = user.getSession();

        if(session == null) {
            
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
        }
        
        return session;
    }

    private void setSessionId(Session session) {
        if(session != null) {
            String sessionId = helper.getNewSessionId();
            session.setSessionId(sessionId);
        }
    }

    private void removeUserSessions(User user, AccountType accountType) {
        if(user != null &&
           accountType != null) {

            if(accountType == AccountType.UCHAT) {
                for(AccountType type: AccountType.values()) {
                    userMap.remove(user.getLogin() + accountType);
                }
            } else {
                userMap.remove(user.getLogin() + accountType);
            }
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
            if(validate[0].toString().equals(SUCCESS)) {
                return true;
            }
        }
        return false;

    }

}