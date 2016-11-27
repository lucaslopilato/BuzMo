package BuzMo.GUI;

import BuzMo.Logger.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Ben on 11/26/2016.
 * This is what the GUI calls - the initial login window that leads to all other windows
 */
public class LoginWindow extends JFrame {
    private Logger log;
    private JButton loginButton =  new JButton("Login");
    private JPanel mainPanel = new JPanel(new BorderLayout(5,5));
    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(10);
    private final JLabel usernameText = new JLabel("Username");
    private final JLabel passwordText = new JLabel("Password");

    public LoginWindow(Logger log) {
        // Set the login panel parameters
        super("Enter 'test' for both fields to login");
        setSize(400, 200);
        setLocation(500, 280);
        mainPanel.setLayout(null);

        // Hook up logger to GUI
        this.log = log;

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
        getContentPane().add(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        handleLoginAction();

        log.Log("GUI -- LoginWindow properly loaded");
    }

    public void handleLoginAction() {
        // Lambda function
        loginButton.addActionListener(
                (ActionEvent e) -> {
                    String usernameInput = usernameField.getText();
                    // getPassword returns a char array - save as string
                    String passwordInput = new String(passwordField.getPassword());

                    // HANDLE LOGIN STUFF BELOW! Do a SQL query for email and password in Users table.
                    // If login successful, bring up a new Main Menu and dispose of the current window
                    if (usernameInput.equals("test") && passwordInput.equals("test")) {
                        // The logger below doesn't work?
                        log.Log("Successful login attempt [username: "+usernameInput+", password: "+passwordInput+"]");
                        System.out.println("Successful login attempt [username: "+usernameInput+", password: "+passwordInput+"]");
                        MainMenu newMainMenu = new MainMenu(log);
                        newMainMenu.setVisible(true);
                        dispose();
                    }
                    // If username or password are incorrect, ask for it again
                    else {
                        // The logger below doesn't work?
                        log.Log("Failed login attempt [username: "+usernameInput+", password: "+passwordInput+"]");
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
