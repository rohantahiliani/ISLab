package edu.gatech.islab.chat.enums;

public enum AccountType {    
    UCHAT, 
    GOOGLE, 
    FACEBOOK, 
    YAHOO, 
    NULL;

    public static AccountType value(String type) {
        AccountType retVal = null;
        try {
            retVal = valueOf(type);
        } catch (IllegalArgumentException ex) {
            retVal = AccountType.NULL;
        }
        return retVal;
    }
}