package servlet;

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
        requiredParams = new LinkedList<String>();
        paramList = Collections.list((Enumeration<String>)request.getParameterNames());
        paramValues = request.getParameterMap();
        this.writer = writer;
        initAcceptParams();
        initRequiredParams();
    }

    public void initAcceptParams() {
        acceptParams.add("Operation");
        acceptParams.add("User");
        acceptParams.add("Password");
        acceptParams.add("Message");
        acceptParams.add("Recipient");
        acceptParams.add("JID");
        acceptParams.add("AcctType");
    }

    public void initRequiredParams() {
        requiredParams.add("Operation");
        requiredParams.add("User");
        requiredParams.add("AcctType");
    }

    public boolean validateParams() {
        for(String parameter: paramList) {
            if(!acceptParams.contains(parameter)) {
                return false;
            }
        }
        
        if(!paramList.containsAll(requiredParams)) {
            return false;
        }

        return true;
    }

    public boolean containsParam(String param) {
        return paramList.contains(param);
    }

    public String getParam(String param) {
        if(!containsParam(param)) {
            writeError("Invalid parameter list");
            return null;
        }
        return paramValues.get(param)[0];
    }

    public void writeError(String error) {
        writer.println(error);
    }

}