package edu.gatech.islab.chat.utilities;

public enum AccountType {
    
    GOOGLE, FACEBOOK, YAHOO, NULL;

    public static AccountType getType(int code) {
        switch(code) {
        case 0:
            return AccountType.GOOGLE;
        case 1:
            return AccountType.FACEBOOK;
        case 2:
            return AccountType.YAHOO;
        default:
            return AccountType.NULL;
        }
    }

    public static AccountType getType(String value) {
        if(value.equals("GOOGLE")) {
            return AccountType.GOOGLE;
        } else if(value.equals("FACEBOOK")) {
            return AccountType.FACEBOOK;
        } else if(value.equals("YAHOO")) {
            return AccountType.YAHOO;
        } else {
            return AccountType.NULL;
        }
    }
}