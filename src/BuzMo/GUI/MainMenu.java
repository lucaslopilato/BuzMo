package BuzMo.GUI;

import BuzMo.Logger.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Ben on 11/26/2016.
 * This is created after a successful login. Presents user with many options
 */
public class MainMenu extends JFrame {
    private Logger log;
    private JLabel sampleText = new JLabel("Welcome to the Main Menu!");
    private JPanel mainPanel = new JPanel();

    MainMenu(Logger log) {
        // Set the main menu panel parameters
        super("Main Menu");
        setSize(300,200);
        setLocation(500,280);
        mainPanel.setLayout (null);

        // Hook up logger to GUI
        this.log = log;

        // Set the field + button parameters
        sampleText.setBounds(70,50,180,60);

        // Add the fields + buttons to the main menu panel
        mainPanel.add(sampleText);

        // Add the main menu panel + ActionListeners to the Content Pane
        getContentPane().add(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        log.Log("GUI -- MainMenu properly loaded");
    }
}
