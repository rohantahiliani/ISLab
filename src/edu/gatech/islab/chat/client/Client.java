package edu.gatech.islab.chat.client;

import edu.gatech.islab.chat.utilities.Session;
import edu.gatech.islab.chat.utilities.User;

import java.util.ArrayList;

public interface Client {
    
    public void register(User user);

    public boolean validateSession(User user, Session session);

    public ArrayList<String> getFriends(User user);

    public boolean sendMessage(User user, String message);
}