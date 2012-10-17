package edu.gatech.islab.chat.utilities.xmpp;

import edu.gatech.islab.chat.utilities.User;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public abstract class XMPPUtility {

    Connection connection;
    ChatManager chatManager;
    private Map<User, Chat> chatMap;

    protected XMPPUtility() {
        this.connection = new XMPPConnection("jabber.org");
        connect();
    }

    protected XMPPUtility(String server) {
        this.connection = new XMPPConnection(server);
        connect();
    }

    protected XMPPUtility(ConnectionConfiguration config) {
        this.connection = new XMPPConnection(config);
        connect();
    }

    private void connect() {
        try {
            this.connection.connect();
            chatMap = new HashMap<User, Chat>();
        } catch (XMPPException ex) {
        }
    }

    private Chat getChat(User recipient) {
        if(this.chatManager == null) {
            this.chatManager = this.connection.getChatManager();
        }
        
        if(this.chatMap.containsKey(recipient)) {
            return this.chatMap.get(recipient);
        }
        
        Chat chat = this.chatManager.createChat(recipient.getUserName(), null);
        this.chatMap.put(recipient, chat);
        
        return chat;
    }

    public boolean isSane(String text) {
        return isSane(new String[]{text});
    }

    public boolean isSane(String[] allText) {
        for(String text: allText) {
            
        }
        return true;
    }

    public void login(String user, String password) {
        if(isSane(new String[]{user,password})) {
            try {
                this.connection.login(user, password);
            } catch(XMPPException ex) {
            }
        }
        
    }

    public void sendMessage(String message, User recipient) {
        Chat chat = getChat(recipient);
        try {
            chat.sendMessage(message);
        } catch(XMPPException ex) {
        }
    }

    public void disconnect() {
        this.connection.disconnect();
    }
}