package BuzMo.GUI;

import BuzMo.Logger.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Created by Ben on 11/27/2016.
 * This serves as a base class for ExistingConvos and CreateNewConvo.
 * It displays a list of friends, each of which can be selected to send a message to.
 * IMPLEMENT getFriends() UNIQUELY WITH DIFFERENT SQL!
 */
public abstract class FriendList {
    private Logger log;
    private JFrame FriendWindow = new JFrame("Default abstract class text");

    FriendList(Logger log, String userName) {
        // Get a string array of friends based on SQL query
        String testData[] = getFriends();
        int numFriends = testData.length;

        // Hook up logger to GUI
        this.log = log;

        // Set the login window parameters
        FriendWindow.setSize(300,600);
        FriendWindow.setLocation(500,100);
        FriendWindow.setResizable(false);

        // Create and populate the panel
        JPanel p = new JPanel(new GridLayout(0,1));
        for (int i = 0; i < numFriends; i++) {
            JButton temp = new JButton(testData[i]);
            p.add(temp);
            handleFriendClick(temp, userName);
        }

        p.setOpaque(true);
        FriendWindow.setContentPane(p);
        FriendWindow.setVisible(true);

        log.Log("FriendList was loaded successfully");
    }
    // THIS SHOULD BE OVERRIDDEN IN CHILD CLASSES WITH SQL QUERIES!
    public String[] getFriends() {
        String result[] = {"Jerry", "Jason", "Jack"};
        return result;
    }
    // THIS CAN BE CUSTOMIZED!
    public void handleFriendClick(JButton newButton, String username) {
        newButton.addActionListener(
                (ActionEvent e) -> {
                    new FriendConvo(log, username);
                    FriendWindow.dispose();
                }
        );
    }
}