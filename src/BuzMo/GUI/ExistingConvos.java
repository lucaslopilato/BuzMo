package BuzMo.GUI;

import BuzMo.Logger.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Created by Ben on 11/27/2016.
 * This displays a list of all friends who have at least one message to you.
 * You can click each friend element to send a message to them.
 */
public class ExistingConvos extends FriendList {
    ExistingConvos(Logger log, String yourUsername) {
        super(log, yourUsername);
    }
    // REPLACE WITH SQL QUERIES
    public String[] getFriends() {
        String result[] = {"Alice", "Bob", "Calvin"};
        return result;
    }
}
