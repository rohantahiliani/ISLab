package edu.gatech.islab.chat.client;

import edu.gatech.islab.chat.session.Session;
import edu.gatech.islab.chat.user.User;

import java.util.ArrayList;

public interface Client {
    
    public void register(User user);

    public boolean validateSession(User user, Session session);

    public ArrayList<String> getFriends(User user);

    public boolean sendMessage(User user, String message);
}