package edu.gatech.islab.chat.xmpp;

import edu.gatech.islab.chat.enums.AccountType;
import edu.gatech.islab.chat.user.User;

import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

public class JabberXMPPUtility extends XMPPUtility {

    public JabberXMPPUtility() {
        super("jabber.org");
    }

    public List<User> getFriendList() {
        List<User> friends = new LinkedList<User>();

        for(RosterEntry entry: getRosterEntries()) {
            if(entry.getType().equals(ItemType.both)) {
                User user = new User
                    (entry.getUser(), entry.getName(), AccountType.JABBER);
                friends.add(user);
            }
        }
        return friends;
    }
}
