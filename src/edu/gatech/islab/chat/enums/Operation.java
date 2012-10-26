package edu.gatech.islab.chat.enums;

public enum Operation {
    
    LOGIN, DISCONNECT, SENDMESSAGE, GETFRIENDS, REGISTER, NULL;

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
        case 4:
            return Operation.REGISTER;
        default:
            return Operation.NULL;
        }
    }

    public static Operation getType(String value) {
        if(value.equals("LOGIN")) {
            return Operation.LOGIN;
        } else if(value.equals("DISCONNECT")) {
            return Operation.DISCONNECT;
        } else if(value.equals("SENDMESSAGE")) {
            return Operation.SENDMESSAGE;
        } else if(value.equals("GETFRIENDS")) {
            return Operation.GETFRIENDS;
        } else if(value.equals("REGISTER")) {
            return Operation.REGISTER;
        } else {
            return Operation.NULL;
        }
    }
}