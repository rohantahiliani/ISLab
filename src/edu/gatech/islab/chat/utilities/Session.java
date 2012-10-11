package edu.gatech.islab.chat.utilities;

public interface Session {

    public void registerSession();

    public boolean validateSession();

    public void unregisterSession();

}