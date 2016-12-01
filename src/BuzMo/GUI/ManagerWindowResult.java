package BuzMo.GUI;

import BuzMo.Logger.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by admin on 11/30/16.
 */
public class ManagerWindowResult extends JFrame {
    private Logger log;
    private JFrame managerResultWindow;
    private JButton loginButton =  new JButton("Login");
    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(10);

    public ManagerWindowResult(Logger log, String yourUsername, String optionName, int optionIndex) {
        // Hook up logger to GUI
        this.log = log;

        managerResultWindow = new JFrame(optionName);
        managerResultWindow.setSize(350, 400);
        managerResultWindow.setLocation(450, 200);

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        // Option text
        JLabel optionText = new JLabel(optionName);
        optionText.setBounds(75,15,400,200);

        // THE RESULT
        JLabel resultText;
        if (optionIndex < 3) {
            resultText = new JLabel(getResult(optionIndex));
        }
        else {
            resultText = new JLabel("Placeholder for total report");
        }
        resultText.setBounds(75,75,400,200);


        mainPanel.add(optionText);
        mainPanel.add(resultText);

        managerResultWindow.setContentPane(mainPanel);
        managerResultWindow.setVisible(true);

        log.Log("GUI -- LoginWindow properly loaded");
    }

    // ADD SQL QUERIES HERE!
    public String getResult(int optionIndex) {
        String result = "";
        // Find top 3 active users last 7 days (highest message count)
        if (optionIndex == 0) {
            String[] activeUsers = {"Alice", "Bob", "Calvin"};
            for (int i=0; i<activeUsers.length; i++)
                result = result + (i+1) + ".) " + activeUsers[i] + "\n\n";
        }
        // Find top 3 messages in last 7 days (highest read count)
        else if (optionIndex == 1) {
            String[] topMessages = {"Hello Eric, how are you?", "LMAO", "Hi there"};
            for (int i=0; i<topMessages.length; i++)
                result = result + (i+1) + ".) " + topMessages[i] + "\n\n";
        }
        // Find number of inactive users (3 or less messages sent)
        else if (optionIndex == 2) {
            int numInactive = 4;
            result = Integer.toString(numInactive);
        }
        return result;
    }
}