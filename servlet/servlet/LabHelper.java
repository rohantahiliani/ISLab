package servlet;

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

    private List<String> paramList;
    private Map<String, String[]> paramValues;
    private Set<String> acceptParams;
    private List<String> requiredParams;
    private PrintWriter writer;

    @SuppressWarnings("unchecked") 
    public LabHelper(HttpServletRequest request, PrintWriter writer) {
        acceptParams = new HashSet<String>();
        paramList = Collections.list((Enumeration<String>)request.getParameterNames());
        paramValues = request.getParameterMap();
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
        acceptParams.add("warn");
    }

    public void initRequiredParams() {
        requiredParams = new LinkedList<String>();
        requiredParams.add("Operation");
        requiredParams.add("AccountType");
        String opString = getParam("Operation");
        if(opString == null) {
            return;
        } else {
            Operation operation = Operation.getType(opString);

            switch(operation) {
            case LOGIN:
                requiredParams.add("Username");
                requiredParams.add("Password");
                break;
            case SENDMESSAGE:
                requiredParams.add("Message");
                requiredParams.add("Recipient");
                break;
            case GETFRIENDS:
            case DISCONNECT:
            case NULL:
            default:
                break;
            }
        }
    }

    public boolean validateParams() {
        initRequiredParams();

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

    public boolean containsParam(String param) {
        return paramList.contains(param);
    }

    public String getParam(String param) {
        if(!containsParam(param)) {
            writeError("Invalid parameter");
            return null;
        }
        return paramValues.get(param)[0];
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

}