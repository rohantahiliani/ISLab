package servlet;

import edu.gatech.islab.chat.main.Main;

import edu.gatech.islab.chat.enums.*;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class InfoSecLab extends HttpServlet {

    private String DOMAIN = "http://localhost/islab/";

    private Main obj;
    private LabHelper helper;
    private PrintWriter writer;

    public void init(ServletConfig config) throws ServletException {
        obj = new Main();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, java.io.IOException {
        response.getWriter().println("Not Supported");
    }

    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, java.io.IOException {

        HashMap<String, Object> params = new HashMap<String, Object>();

        response.addHeader("Access-Control-Allow-Origin", "*");
	response.setContentType("text/html");

        writer = response.getWriter();
        helper = new LabHelper(request, writer);
        
        if(!helper.validateParams()) {
            return;
        }

        String username = null;
        String sessionId = null;
        AccountType accountType = AccountType.value
            (helper.getParam("AccountType"));
        Operation operation = Operation.value(helper.getParam("Operation"));

        username = helper.getParam("Username");
        sessionId = helper.getParam("SessionId");
        if(operation == Operation.LOGIN || operation == Operation.REGISTER) {
            sessionId = "";
        } 
        if(accountType != AccountType.UCHAT) {
            String ucUsername = AccountType.UCHAT + "Username";
            String ucSessionId = AccountType.UCHAT + "SessionId";
            if(helper.getParam(ucUsername) != null && 
               helper.getParam(ucSessionId) != null) {
                params.put(ucUsername, helper.getParam(ucUsername));
                params.put(ucSessionId, helper.getParam(ucSessionId));
            } else {
                helper.writeError("UCHAT Username/SessionId missing");
                return;
            }
        } 

        if(username == null || 
           sessionId == null ||
           operation == Operation.NULL) {

            helper.writeError("Invalid parameter values");
            return;
        } 

        params.put("Operation", operation);
        params.put("AccountType", accountType);
        params.put("Username", username);
        params.put("SessionId", sessionId);

        switch(operation) {
        case REGISTER:
        case LOGIN:
            String password = helper.getParam("Password");
            if(password == null) {
                return;
            }
            params.put("Password", password);
            break;

        case SENDMESSAGE:
            String message = helper.getParam("Message");
            String recipient = helper.getParam("Recipient");

            if(message == null || recipient == null) {
                return;
            }
            params.put("Message", message);
            params.put("Recipient", recipient);

            break;

        case GETFRIENDS:
        case GETMESSAGES:
        case DISCONNECT:
        default:
            break;
        }

        Object[] ret = obj.doOperation(params);

        if(ret!=null && ret.length > 0) {
            switch(operation) {
            case LOGIN:
                if(ret.length == 2 && 
                   ret[0].toString().equals("Success")) {

                    response.addCookie
                        (new Cookie
                         (accountType + "SessionId", ret[1].toString()));
                    response.addCookie
                        (new Cookie
                         (accountType + "Username", username));
                    response.sendRedirect(DOMAIN + "chat.php");
                }
                break;
            default:
                for(Object retObject: ret) {
                    writer.println(retObject);
                }
                break;
            }
        }
    }
}