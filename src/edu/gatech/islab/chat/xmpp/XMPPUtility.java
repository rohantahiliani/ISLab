package edu.gatech.islab.chat.xmpp;

import edu.gatech.islab.chat.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public abstract class XMPPUtility {

    private Connection connection;
    private ChatManager chatManager;
    private Map<User, Chat> chatMap;

    protected XMPPUtility() {
        throw new NullPointerException("Illegal Connection Attempt");
    }

    protected XMPPUtility(String server) {
        this.connection = new XMPPConnection(server);
        connect();
    }

    protected XMPPUtility(ConnectionConfiguration config) {
        this.connection = new XMPPConnection(config);
        connect();
    }

    public abstract List<User> getFriendList();

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
        
        Chat chat = this.chatManager.createChat(recipient.getLogin(), null);
        this.chatMap.put(recipient, chat);
        
        return chat;
    }

    public boolean login(String user, String password) {
        if(!this.connection.isConnected()) {
            connect();
        } else if(this.connection.isAuthenticated()) {
            return true;
        }

        try {
            this.connection.login(user, password);
            return this.connection.isAuthenticated();
        } catch(XMPPException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void sendMessage(String message, User recipient) {
        Chat chat = getChat(recipient);
        try {
            chat.sendMessage(message);
        } catch(XMPPException ex) {
            ex.printStackTrace();
        }
    }

    public Collection<RosterEntry> getRosterEntries() {
        Roster roster = this.connection.getRoster();
        Collection<RosterEntry> rosterEntries = roster.getEntries();

        return rosterEntries;
    }

    public boolean disconnect() {
        this.connection.disconnect();
        return this.connection.isAuthenticated() &&
            this.connection.isConnected();
    }

}