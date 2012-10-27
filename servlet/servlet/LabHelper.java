package servlet;

import edu.gatech.islab.chat.enums.AccountType;
import edu.gatech.islab.chat.enums.Operation;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;

public class LabHelper {

    private final AccountType uChat = AccountType.UCHAT;

    private Map<String, String[]> paramValues;
    private Set<String> acceptParams;
    private List<String> requiredParams;
    private Cookie[] cookies;
    private PrintWriter writer;

    @SuppressWarnings("unchecked") 
    public LabHelper(HttpServletRequest request, PrintWriter writer) {
        acceptParams = new HashSet<String>();
        paramValues = new HashMap<String, String[]>(request.getParameterMap());
        cookies = request.getCookies();
        this.writer = writer;
        initAcceptParams();
    }

    public void initAcceptParams() {
        acceptParams.add("Operation");
        acceptParams.add("Username");
        acceptParams.add("Password");
        acceptParams.add("Message");
        acceptParams.add("Recipient");
        acceptParams.add("JID");
        acceptParams.add("AccountType");
        acceptParams.add("SessionId");
        acceptParams.add("CaptchaId");
        for(AccountType type: AccountType.values()) {
            acceptParams.add(type+"SessionId");
            acceptParams.add(type+"Username");
        }
        acceptParams.add("warn");
    }

    public void initRequiredParams() {
        requiredParams = new LinkedList<String>();
        requiredParams.add("AccountType");
        requiredParams.add("Operation");

        String opString = getParam("Operation");
        if(opString == null) {
            return;
        } else {
            Operation operation = Operation.value(opString);

            switch(operation) {
            case LOGIN:
            case REGISTER:
                requiredParams.add("Username");
                requiredParams.add("Password");
                break;
            case SENDMESSAGE:
                requiredParams.add("Message");
                requiredParams.add("Recipient");
                break;
            default:
                break;
            }
        }
    }

    public boolean validateParams() {
        initRequiredParams();
        addCookieValues(AccountType.value(getParam("AccountType")));

        Set<String> paramList = paramValues.keySet();

        for(String param: paramList) {
            if(!isSane(param)) {
                writer.println("Invalid parameter value: " + param);
                return false;
            }
        }

        for(String parameter: paramList) {
            if(!acceptParams.contains(parameter)) {
                writer.println("Invalid parameter: " + parameter);
                return false;
            }
        }

        if(!paramList.containsAll(requiredParams)) {
            writer.println("Required parameter missing:" + paramList);
            return false;
        }

        return true;
    }

    public String getParam(String param) {
        if(paramValues.containsKey(param)) {
            return paramValues.get(param)[0];
        } else {
            return null;
        }
    }

    public void writeError(String error) {
        writer.println(error);
    }

    public boolean isSane(String text) {
        return isSane(new String[]{text});
    }

    public boolean isSane(String[] allText) {
        for(String text: allText) {
            
        }
        return true;
    }

    private void addCookieValues(AccountType accountType) {
        if(cookies != null && accountType != null) {
            if(accountType != uChat) {
                for(Cookie cookie: cookies) {
                    if(cookie.getName().equals(uChat + "SessionId")) {
                        paramValues.put
                            (uChat+"SessionId", new String[]{cookie.getValue()});
                    } else if(cookie.getName().equals(uChat + "Username")) {
                        paramValues.put
                            (uChat + "Username", new String[]{cookie.getValue()});
                    }
                }
            } 
            for(Cookie cookie: cookies) {
                if(cookie.getName().equals(accountType + "SessionId")) {
                    paramValues.put
                        ("SessionId", new String[]{cookie.getValue()});
                } else if(cookie.getName().equals(accountType + "Username")) {
                    paramValues.
                        put("Username", new String[]{cookie.getValue()});
                }
            }
        }
    }

}