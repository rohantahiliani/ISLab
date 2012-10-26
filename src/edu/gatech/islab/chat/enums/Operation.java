package edu.gatech.islab.chat.enums;

public enum Operation {
    LOGIN, 
    DISCONNECT, 
    SENDMESSAGE, 
    GETFRIENDS, 
    REGISTER, 
    NULL;

    public static Operation value(String type) {
        Operation retVal = null;
        try {
            retVal = valueOf(type);
        } catch (IllegalArgumentException ex) {
            retVal = Operation.NULL;
        }
        return retVal;
    }

}