package servlet;

import edu.gatech.islab.chat.main.Main;

import edu.gatech.islab.chat.enums.*;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet(
    name = "InfoSecLab", 
    urlPatterns = {"/islab/InfoSecLab"}
)
public class InfoSecLab extends HttpServlet {

    private String DOMAIN = "https://salty-cove-9056.herokuapp.com/";

    private Main obj;
    private LabHelper helper;
    private PrintWriter writer;

    HashMap<String, Integer> failedRequests;

    public void init(ServletConfig config) throws ServletException {
        obj = new Main();
        failedRequests = new HashMap<String, Integer>();
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
        
        String remoteIp = request.getHeader("X-FORWARDED-FOR");
        int fails = 0;
        if(failedRequests.containsKey(remoteIp)) {
            fails = failedRequests.get(remoteIp);
        }

        if(fails > 2) {
            String capDomain = DOMAIN;
            if(helper.validateCaptcha(remoteIp)) {
                fails = 2;
                failedRequests.put(remoteIp, fails);
            } else {
                capDomain += "solvecaptcha.php";
            }
            Cookie failCookie = new Cookie
                ("FailedRequests", fails + "");
            failCookie.setDomain(".herokuapp.com");
            failCookie.setPath("/");
            response.addCookie(failCookie);
            response.sendRedirect(capDomain);
            return;
        }
        
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
                helper.writeError();
                return;
            }
        } 

        if(username == null || 
           sessionId == null ||
           operation == Operation.NULL) {

            helper.writeError();
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
                helper.writeError();
                return;
            }
            if(operation == Operation.REGISTER &&
               !helper.validCaptcha()) {
                return;
            }
            if(accountType == AccountType.UCHAT) {
                if(username.length() > 16 || 
                   username.length() < 6 ||
                   password.length() > 20 ||
                   password.length() < 6 ||
                   helper.invalidPassword(password)) {
                    helper.writeError();
                    return;
                }
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

                    Cookie sessionCookie = new Cookie
                        (accountType + "SessionId", ret[1].toString());
                    sessionCookie.setDomain(".herokuapp.com");
                    sessionCookie.setPath("/");
                    response.addCookie(sessionCookie);

                    Cookie userCookie = new Cookie
                        (accountType + "Username", username);
                    userCookie.setDomain(".herokuapp.com");
                    userCookie.setPath("/");
                    response.addCookie(userCookie);

                    Cookie failCookie = new Cookie
                        ("FailedRequests", fails+"");
                    failCookie.setDomain(".herokuapp.com");
                    failCookie.setPath("/");
                    failCookie.setMaxAge(0);
                    response.addCookie(failCookie);

                    response.sendRedirect(DOMAIN + "chat.php");
                    failedRequests.remove(remoteIp);
                } else {
                    failedRequests.put(remoteIp, fails + 1);
                    Cookie failCookie = new Cookie
                        ("FailedRequests", (fails + 1) +"");
                    failCookie.setDomain(".herokuapp.com");
                    failCookie.setPath("/");
                    response.addCookie(failCookie);
                    if(fails + 1 > 2) {
                        response.sendRedirect(DOMAIN + "solvecaptcha.php");
                    } else {
                        response.sendRedirect(DOMAIN);
                    }
                }
                break;
            case DISCONNECT:
                if(accountType == AccountType.UCHAT) {
                    for(AccountType type: AccountType.values()) {
                        helper.deleteCookie(type + "SessionId", response);
                        helper.deleteCookie(type + "Username", response);
                    }
                    response.sendRedirect(DOMAIN);
                } else {
                    helper.deleteCookie(accountType + "SessionId", response);
                    helper.deleteCookie(accountType + "Username", response);
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