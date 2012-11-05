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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.http.*;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

public class LabHelper {

    private final AccountType uChat = AccountType.UCHAT;
    private final String captchaKey;
    private static final String HACKFAIL = "Trying to hack? You have failed.";

    private Map<String, String[]> paramValues;
    private Set<String> acceptParams;
    private List<String> requiredParams;
    private Pattern[] patterns;

    private PrintWriter writer;
    private Cookie[] cookies;

    @SuppressWarnings("unchecked") 
    public LabHelper(HttpServletRequest request, PrintWriter writer) {
        acceptParams = new HashSet<String>();
        paramValues = new HashMap<String, String[]>(request.getParameterMap());
        cookies = request.getCookies();
        this.writer = writer;
        initAcceptParams();
        initPatterns();
        captchaKey = System.getenv("CAPTCHA_PRIVATE_KEY");
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
        acceptParams.add("recaptcha_challenge_field");
        acceptParams.add("recaptcha_response_field");
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
                if(operation == Operation.REGISTER) {
                    requiredParams.add("CaptchaId");
                }
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

    public void initPatterns() {
        String[] regexes = new String[] {
            ".*?([0-9]).*",
            ".*?([a-z]).*",
            ".*?([A-Z]).*",
            ".*?([\\._\\-\\$#@%]).*",
            ".*?[^0-9a-zA-Z\\._\\-\\$#@%].*"
        };
        patterns = new Pattern[regexes.length];

        int i = 0;
        for(String regex: regexes) {
            patterns[i++] = Pattern.compile(regex);
        }
    }

    public boolean validateParams() {
        initRequiredParams();
        addCookieValues(AccountType.value(getParam("AccountType")));

        Set<String> paramList = paramValues.keySet();

        for(String param: paramList) {
            if(!isSane(param)) {
                writeError();
                return false;
            }
        }

        for(String parameter: paramList) {
            if(!acceptParams.contains(parameter)) {
                writeError();
                return false;
            }
        }

        if(!paramList.containsAll(requiredParams)) {
            writeError();
            return false;
        }

        return true;
    }

    public boolean invalidPassword(String password) {
        boolean retVal = false;
        Matcher matcher = null;

        for(int i = 0; i < patterns.length - 1; i++) {
            matcher = patterns[i].matcher(password);
            if(!matcher.matches()) {
                retVal = true;
                break;
            }
        }
        if(!retVal) {
            matcher = patterns[patterns.length - 1].matcher(password);
            if(matcher.matches()) {
                retVal = true;
            }
        }
        return retVal;
    }

    public String getParam(String param) {
        if(paramValues.containsKey(param)) {
            return paramValues.get(param)[0];
        } else {
            return null;
        }
    }

    public void writeError() {
        writer.println(HACKFAIL);
    }

    public boolean isSane(String text) {
        return isSane(new String[]{text});
    }

    public boolean isSane(String[] allText) {
        for(String text: allText) {
            
        }
        return true;
    }

    public void addCookieValues(AccountType accountType) {
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

    public void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, "");
        cookie.setDomain(".herokuapp.com");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    
    public boolean validCaptcha() {
        boolean retVal = false;

        String captchaId = getParam("CaptchaId");
        if(captchaId != null &&
           captchaId.equals(captchaKey)) {
            retVal = true;
        }

        if(!retVal) {
            writeError();
        }
        return retVal;
    }

    public boolean validateCaptcha(String ipaddr) {
        String remoteAddr = ipaddr;
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(captchaKey);

        String challenge = getParam("recaptcha_challenge_field");
        String uresponse = getParam("recaptcha_response_field");
        if(ipaddr == null || challenge == null || uresponse == null) {
            writeError();
            return false;
        }
        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
        writer.println(reCaptchaResponse.toString() + " " + reCaptchaResponse.isValid());
        return reCaptchaResponse.isValid();
    }

}