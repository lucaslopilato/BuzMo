package BuzMo.GUI;

import BuzMo.Logger.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Ben on 11/26/2016.
 * This is what the GUI calls - the initial login window that leads to all other windows
 *
 * Might be useful when implementing actual SQL:
 * http://www.thaicreate.com/java/java-gui-example-login-username-password.html
 * http://www.thepcwizard.in/2011/10/create-login-form-using-netbeans-ide.html
 */
public class LoginWindow extends JFrame {
    private Logger log;
    private JFrame loginWindow = new JFrame("Enter 'test' for both fields to login");
    private JButton loginButton =  new JButton("Login");
    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(10);

    public LoginWindow(Logger log) {
        // Hook up logger to GUI
        this.log = log;

        // Set the login panel parameters
        loginWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginWindow.setSize(400, 200);
        loginWindow.setLocation(500, 280);
        loginWindow.setResizable(false);

        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout(5,5));
        mainPanel.setLayout(null);

        // Create labels
        JLabel usernameText = new JLabel("Username:");
        JLabel passwordText = new JLabel("Password:");

        // Set the field + button parameters
        usernameField.setBounds(175, 30, 150, 20);
        passwordField.setBounds(175, 75, 150, 20);
        usernameText.setBounds(75, 30, 150, 20);
        passwordText.setBounds(75,75,150,20);
        loginButton.setBounds(160, 120, 80, 20);

        // Add the fields + buttons to the login panel
        mainPanel.add(usernameField);
        mainPanel.add(passwordField);
        mainPanel.add(usernameText);
        mainPanel.add(passwordText);
        mainPanel.add(loginButton);

        // Add the login panel + ActionListeners to the Content Pane
        loginWindow.setContentPane(mainPanel);
        loginWindow.setVisible(true);
        handleLoginAction();

        log.Log("GUI -- LoginWindow properly loaded");
    }

    private void handleLoginAction() {
        loginButton.addActionListener(
                (ActionEvent e) -> {
                    String usernameInput = usernameField.getText();
                    String passwordInput = new String(passwordField.getPassword());

                    // HANDLE LOGIN STUFF BELOW! Do a SQL query for email and password in Users table.
                    // If login successful, bring up a new Main Menu and dispose of the current window
                    if (usernameInput.equals("test") && passwordInput.equals("test")) {
                        System.out.println("Successful login attempt [username: "+usernameInput+", password: "+passwordInput+"]");
                        new MainMenu(log, usernameInput);
                        loginWindow.dispose();
                        dispose();
                    }
                    // If username or password are incorrect, ask for it again
                    else {
                        System.out.println("Failed login attempt [username: "+usernameInput+", password: "+passwordInput+"]");
                        JOptionPane.showMessageDialog(null, "Invalid Password / Username");
                        usernameField.setText("");
                        passwordField.setText("");
                        usernameField.requestFocus();
                    }
                }
        );
    }
}
