package edu.gatech.islab.chat.enums;

public enum Operation {
    LOGIN, 
    DISCONNECT, 
    SENDMESSAGE,
    GETMESSAGES,
    GETFRIENDS, 
    REGISTER, 
    VALIDATE,
    NULL;

    public static Operation value(String type) {
        Operation retVal = null;
        try {
            if(type != null) {
                retVal = valueOf(type);
            } else {
                retVal = Operation.NULL;
            }
        } catch (IllegalArgumentException ex) {
            retVal = Operation.NULL;
        }
        return retVal;
    }

}