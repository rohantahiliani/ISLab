package edu.gatech.islab.chat.xmpp;

import edu.gatech.islab.chat.enums.AccountType;
import edu.gatech.islab.chat.user.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

public class XMPPMessageListener implements MessageListener {

    private Map<User, List<String>> messageMap = null;

    public XMPPMessageListener() {
        messageMap = new HashMap<User, List<String>>();
    }

    public void processMessage(Chat chat, Message message) {
        if(message.getType() == Type.chat && message.getBody() != null) {
            User user = new User
                (chat.getParticipant(), chat.getParticipant(), AccountType.UCHAT);
            addMessage(user, message.getBody());
        }
    }

    public Map<User, List<String>> getMessages() {
        Map<User, List<String>> tempMap = 
            new HashMap<User, List<String>>(messageMap);
        messageMap.clear();
        return tempMap;
    }

    private synchronized void addMessage(User user, String message) {
        List<String> messages = messageMap.get(user);
        if(messages == null) {
            messages = new LinkedList<String>();
            messageMap.put(user, messages);
        }
        messages.add(message);
    }
}