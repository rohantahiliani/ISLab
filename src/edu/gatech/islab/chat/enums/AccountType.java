package edu.gatech.islab.chat.enums;

public enum AccountType {
    
    UCHAT, GOOGLE, FACEBOOK, YAHOO, NULL;

    public static AccountType getType(int code) {
        switch(code) {
        case 0:
            return AccountType.UCHAT;
        case 1:
            return AccountType.GOOGLE;
        case 2:
            return AccountType.FACEBOOK;
        case 3:
            return AccountType.YAHOO;
        default:
            return AccountType.NULL;
        }
    }

    public static AccountType getType(String value) {
        if(value.equals("UCHAT")) {
            return AccountType.UCHAT;
        }else if(value.equals("GOOGLE")) {
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