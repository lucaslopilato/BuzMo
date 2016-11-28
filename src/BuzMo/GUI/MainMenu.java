package BuzMo.GUI;

import BuzMo.Logger.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Ben on 11/26/2016.
 * This is created after a successful login. The portal for all other options.
 */
public class MainMenu extends JFrame {
    private Logger log;

    // The main frame + buttons
    private JFrame mainMenu = new JFrame("Main Menu");
    private JButton existingConvosButton =  new JButton("Existing MyCircle Conversations");
    private JButton createNewConvoButton =  new JButton("Create new MyCircle Convo");
    private JButton myCircleButton =  new JButton("Go to MyCircle");
    private JButton chatGroupsButton =  new JButton("Go to ChatGroups");
    private JButton createChatGroupsButton =  new JButton("Create a ChatGroup");
    private JButton browseMessagesButton =  new JButton("Browse messages");
    private JButton managerModeButton =  new JButton("Open manager mode");
    private JButton debugModeButton =  new JButton("Debug mode");

    MainMenu(Logger log, String yourUsername) {
        // Set the main menu panel parameters
        mainMenu.setResizable(false);
        mainMenu.setSize(700,500); // Is ignored because of button preferences
        mainMenu.setLocation(350,280);
        mainMenu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Hook up logger to GUI
        this.log = log;

        // The GridLayout panel to be inserted into the main frame
        final JPanel gridComponents = new JPanel();
        gridComponents.setLayout(new GridLayout(0,3));

        //Set up components preferred size
        JButton b = new JButton("Just fake button");
        Dimension buttonSize = b.getPreferredSize();
        gridComponents.setPreferredSize(new Dimension((int)(buttonSize.getWidth() * 5)+20,
                (int)(buttonSize.getHeight() * 7)+20 * 2));

        //Add buttons to Grid Layout
        gridComponents.add(existingConvosButton);
        gridComponents.add(createNewConvoButton);
        gridComponents.add(myCircleButton);
        gridComponents.add(chatGroupsButton);
        gridComponents.add(createChatGroupsButton);
        gridComponents.add(browseMessagesButton);
        gridComponents.add(managerModeButton);
        gridComponents.add(debugModeButton);

        // Add the GridLayout panel to the main frame
        mainMenu.add(gridComponents);

        // Display the window
        mainMenu.pack(); // Packs the gridlayout compactly
        mainMenu.setVisible(true);
        handleButtonAction(log, yourUsername);

        log.Log("GUI -- MainMenu properly loaded");
    }

    private void handleButtonAction(Logger log, String yourUsername) {
        // Look at existing conversations -- NEED TO IMPLEMENT FRIENDCONVO
        existingConvosButton.addActionListener(
                (ActionEvent e) -> {
                    new ExistingConvos(log, yourUsername);
                }
        );
        // Send a new message -- COMPLETE
        createNewConvoButton.addActionListener(
                (ActionEvent e) -> {
                    new CreateNewConvo(log, yourUsername);
                }
        );
        // Check out MyCircle -- NEED TO IMPLEMENT MYCIRCLE
        myCircleButton.addActionListener(
                (ActionEvent e) -> {

                }
        );
        // Check out your ChatGroups -- NEED TO IMPLEMENT CHATGROUPS
        chatGroupsButton.addActionListener(
                (ActionEvent e) -> {

                }
        );
        // Make a new ChatGroup -- COMPLETE
        createChatGroupsButton.addActionListener(
                (ActionEvent e) -> {
                    new NewChatGroup(log, yourUsername);
                }
        );
        // Browse messages
        browseMessagesButton.addActionListener(
                (ActionEvent e) -> {

                }
        );
        // Enter Manager menu
        managerModeButton.addActionListener(
                (ActionEvent e) -> {

                }
        );
        // Enter Debog menu
        debugModeButton.addActionListener(
                (ActionEvent e) -> {

                }
        );
    }
}
