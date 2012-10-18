package servlet;

import edu.gatech.islab.chat.main.Main;

import edu.gatech.islab.chat.utilities.AccountType;
import edu.gatech.islab.chat.utilities.Operation;
import edu.gatech.islab.chat.utilities.User;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class InfoSecLab extends HttpServlet {

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

        writer = response.getWriter();
        helper = new LabHelper(request, writer);

        if(!helper.validateParams()) {
            helper.writeError("Invalid parameter list");
            return;
        }

        String user = helper.getParam("User");
        Operation operation = Operation.getType(helper.getParam("Operation"));
        AccountType acctType = AccountType.getType(helper.getParam("AcctType"));

        if(operation == Operation.NULL || acctType == AccountType.NULL) {
            helper.writeError("Invalid parameter values");
            return;
        }

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(operation);
        params.add(acctType);
        params.add(user);

        switch(operation) {
        case LOGIN:
            String password = helper.getParam("Password");
            if(password == null) {
                return;
            }
            params.add(password);
            break;

        case SENDMESSAGE:
            String message = helper.getParam("Message");
            String recipient = helper.getParam("Recipient");

            if(message == null || recipient == null) {
                return;
            }
            params.add(message);
            params.add(recipient);

            break;

        case GETFRIENDS:
        case DISCONNECT:
            break;
        }

        Object[] ret = obj.doOperation(params);
        writer.println(ret[0]);
    }
}