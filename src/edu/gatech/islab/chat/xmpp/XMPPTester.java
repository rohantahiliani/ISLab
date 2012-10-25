package edu.gatech.islab.chat.xmpp;

import edu.gatech.islab.chat.user.GoogleUser;
import edu.gatech.islab.chat.user.User;

public class XMPPTester {

    public static void main(String args[]) {
        GoogleXMPPUtility gtalk = new GoogleXMPPUtility();

        User user = new GoogleUser("rohan.tahil@gmail.com", "Rohan Tahiliani");

        gtalk.login("tinkertahiliani@gmail.com", "tester.124");
        System.out.println(gtalk.getFriendList());
        gtalk.sendMessage("Hello", user);
        gtalk.sendMessage("Good Bye", user);
        gtalk.disconnect();
    }

}