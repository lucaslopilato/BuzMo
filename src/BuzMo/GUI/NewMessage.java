package BuzMo.GUI;

import BuzMo.Logger.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Ben on 11/27/2016.
 * This is a simple form allowing you to send a message to a friend.
 */
public class NewMessage {
    private Logger log;
    private JFrame messageWindow = new JFrame("Send a Message");
    private JButton sendButton =  new JButton("Send");
    private JTextField messageField = new JTextField();

    public NewMessage(Logger log, String username) {
        // Hook up logger to GUI
        this.log = log;

        // Set the login panel parameters
        messageWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        messageWindow.setSize(400, 200);
        messageWindow.setLocation(500, 280);
        messageWindow.setResizable(false);

        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout(5,5));
        mainPanel.setLayout(null);

        // Create labels
        JLabel messageFieldText = new JLabel("Message:");

        // Set the field + button parameters
        messageField.setBounds(175, 30, 150, 20);
        messageFieldText.setBounds(75, 30, 150, 20);
        sendButton.setBounds(160, 120, 80, 20);

        // Add the fields + buttons to the login panel
        mainPanel.add(messageField);
        mainPanel.add(messageFieldText);
        mainPanel.add(sendButton);

        // Add the login panel + ActionListeners to the Content Pane
        messageWindow.setContentPane(mainPanel);
        messageWindow.setVisible(true);
        handleMessageSubmit();

        log.Log("GUI -- LoginWindow properly loaded");
    }

    private void handleMessageSubmit() {
        sendButton.addActionListener(
                (ActionEvent e) -> {
                    // ADD A SQL QUERY UPDATING THE TABLE
                    messageWindow.dispose();
                }
        );
    }
}
