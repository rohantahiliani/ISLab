package edu.gatech.islab.chat.main;

import java.util.HashMap;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

import edu.gatech.islab.chat.enums.AccountType;
import edu.gatech.islab.chat.enums.Operation;
import edu.gatech.islab.chat.session.GoogleSession;
import edu.gatech.islab.chat.session.JabberSession;
import edu.gatech.islab.chat.session.Session;
import edu.gatech.islab.chat.session.UChatSession;
import edu.gatech.islab.chat.user.User;

public class MainHelper {


    private Random random;
    private HashMap<String, User> userMap;

    public MainHelper(HashMap<String, User> userMap) {
        random = new Random();
        this.userMap = userMap;
    }

    public synchronized String getNewSessionId() {
        String randTime = (random.nextLong() + System.currentTimeMillis()) + "";
        return DigestUtils.sha256Hex(randTime);
    }

    public User getUser
        (String username, String uchatUsername, AccountType accountType, Operation operation) {

        assert username !=  null && uchatUsername != null;

        User user = null;

        user = getFromUserMap(uchatUsername, accountType);
        
        if(user == null && 
           (operation == Operation.LOGIN || operation == Operation.REGISTER)) {

            user = new User(username, username, accountType);
        }

        return user;
    }

    public Session getSession(User user, AccountType accountType) {
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

    public void setSessionId(Session session) {
        if(session != null) {
            String sessionId = session.getSessionId();
            if(sessionId == null) {
                sessionId = getNewSessionId();
                session.setSessionId(sessionId);
            }
        }
    }

    public boolean disconnectUserSession(User user) {
        assert user != null;

        boolean retVal = false;
        Session session = user.getSession();

        if(session != null) {
            retVal = session.disconnect();
        } 

        return retVal;
    }

    public boolean removeUserSessions(String uchatUsername, AccountType accountType) {
        boolean retVal = false;

        if(uchatUsername != null &&
           accountType != null) {

            if(accountType == AccountType.UCHAT) {
                for(AccountType type: AccountType.values()) {
                    retVal = disconnectUserSession(getFromUserMap(uchatUsername, type)) && retVal;
                    removeFromUserMap(uchatUsername, type);
                }
            } else {
                retVal = disconnectUserSession(getFromUserMap(uchatUsername, accountType));
                removeFromUserMap(uchatUsername, accountType);
            }
        }

        return retVal;
    }

    public void addToUserMap(String username, AccountType accountType, User value) {
        userMap.put(username + accountType, value);
    }

    public User getFromUserMap(String username, AccountType accountType) {
        return userMap.get(username + accountType);
    }

    public void removeFromUserMap(String username, AccountType accountType) {
        userMap.remove(username + accountType);
    }
}
