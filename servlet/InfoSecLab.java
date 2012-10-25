package servlet;

import edu.gatech.islab.chat.main.Main;

import edu.gatech.islab.chat.enums.*;
import edu.gatech.islab.chat.user.User;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class InfoSecLab extends HttpServlet {

    private String DOMAIN = "";

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

        DOMAIN = request.getHeader("referer");

        response.addHeader("Access-Control-Allow-Origin", "*");
	response.setContentType("text/html");

        writer = response.getWriter();
        helper = new LabHelper(request, writer);
        
        if(!helper.validateParams()) {
            return;
        }

        String username = null;
        String sessionId = null;
        AccountType accountType = AccountType.NULL;
        Operation operation = Operation.getType(helper.getParam("Operation"));

        if(operation == Operation.LOGIN) {
            username = helper.getParam("Username");
            accountType = AccountType.getType(helper.getParam("AccountType"));
            sessionId = "";
        } else {
            for(Cookie cookie: request.getCookies()) {
                if(cookie.getName().equals("SessionId")) {
                    sessionId = cookie.getValue();
                } else if(cookie.getName().equals("Username")) {
                    username = cookie.getValue();
                }
            }
        } 
        
        if(username == null || 
           sessionId == null ||
           operation == Operation.NULL) {

            helper.writeError("Invalid parameter values");
            return;
        } 

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Operation", operation);
        params.put("AccountType", accountType);
        params.put("Username", username);
        params.put("SessionId", sessionId);

        switch(operation) {
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
        case DISCONNECT:
            break;
        }

        Object[] ret = obj.doOperation(params);

        if(ret!=null && ret.length > 0) {
            switch(operation) {
            case LOGIN:
                if(ret.length == 2 && 
                   ret[0].toString().equals("Success")) {

                    response.addCookie(new Cookie("SessionId", ret[1].toString()));
                    response.addCookie(new Cookie("Username", username));
                    response.sendRedirect(DOMAIN + "send.html");
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