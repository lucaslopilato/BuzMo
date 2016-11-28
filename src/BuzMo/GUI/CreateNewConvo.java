package BuzMo.GUI;

import BuzMo.Logger.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Created by Ben on 11/27/2016.
 * This displays a list of ALL friends, period.
 * It allows you to click on one of them and send them a message.
 */
public class CreateNewConvo extends FriendList {
    private Logger log;

    CreateNewConvo(Logger log, String username) {
        super(log, username);
    }
    // REPLACE WITH SQL QUERIES
    public String[] getFriends() {
        String result[] = {"THIS", "IS", "ALL", "YOUR", "FRIENDS"};
        return result;
    }
    public void handleFriendClick(JButton newButton, String username) {
        newButton.addActionListener(
                (ActionEvent e) -> {
                    new NewMessage(log, username);
                }
        );
    }
}
