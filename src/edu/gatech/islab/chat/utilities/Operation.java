package edu.gatech.islab.chat.utilities;

public enum Operation {
    
    LOGIN, DISCONNECT, SENDMESSAGE, GETFRIENDS, NULL;

    public static Operation getType(int code) {
        switch(code) {
        case 0:
            return Operation.LOGIN;
        case 1:
            return Operation.DISCONNECT;
        case 2:
            return Operation.SENDMESSAGE;
        case 3:
            return Operation.GETFRIENDS;
        default:
            return Operation.NULL;
        }
    }

}