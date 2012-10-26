package edu.gatech.islab.chat.xmpp;

import edu.gatech.islab.chat.enums.AccountType;
import edu.gatech.islab.chat.user.User;

public class XMPPTester {

    public static void main(String args[]) {
        GoogleXMPPUtility gtalk = new GoogleXMPPUtility();

        User user = new User
            ("rohan.tahil@gmail.com", "Rohan Tahiliani", AccountType.GOOGLE);

        gtalk.login("tinkertahiliani@gmail.com", "tester.124");
        System.out.println(gtalk.getFriendList());
        gtalk.sendMessage("Hello", user);
        gtalk.sendMessage("Good Bye", user);
        gtalk.disconnect();
    }

}