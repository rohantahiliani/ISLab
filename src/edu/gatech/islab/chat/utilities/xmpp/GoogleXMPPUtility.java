package edu.gatech.islab.chat.utilities.xmpp;

import edu.gatech.islab.chat.utilities.GoogleUser;
import edu.gatech.islab.chat.utilities.User;

import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

public class GoogleXMPPUtility extends XMPPUtility {

    public GoogleXMPPUtility() {
        super(new ConnectionConfiguration
              ("talk.google.com", 5222, "gmail.com"));
    }

    public List<User> getFriendList() {
        List<User> friends = new LinkedList<User>();

        for(RosterEntry entry: getRosterEntries()) {
            if(entry.getType().equals(ItemType.both)) {
                User user = new GoogleUser(entry.getUser(), entry.getName());
                friends.add(user);
            }
        }
        
        return friends;
    }
}