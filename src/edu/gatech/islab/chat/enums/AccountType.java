package edu.gatech.islab.chat.enums;

public enum AccountType {    
    UCHAT, 
    GOOGLE, 
    JABBER,
    NULL;

    public static AccountType value(String type) {
        AccountType retVal = null;
        try {
            if(type != null) {
                retVal = valueOf(type);
            } else {
                retVal = AccountType.NULL;
            }
        } catch (IllegalArgumentException ex) {
            retVal = AccountType.NULL;
        }
        return retVal;
    }
}