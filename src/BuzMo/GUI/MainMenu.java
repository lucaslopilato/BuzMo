package BuzMo.GUI;

import BuzMo.Logger.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Created by Ben on 11/26/2016.
 * This is created after a successful login. Presents user with many options
 */
public class MainMenu extends JFrame {
    private Logger log;
    private JFrame mainMenu = new JFrame("Main Menu");
    private JButton existingConvosButton =  new JButton("Existing MyCircle Conversations");
    private JButton createNewConvoButton =  new JButton("Create new MyCircle Convo");
    private JButton myCircleButton =  new JButton("Go to MyCircle");
    private JButton chatGroupsButton =  new JButton("Go to ChatGroups");
    private JButton createChatGroupsButton =  new JButton("Create a ChatGroup");
    private JButton browseMessagesButton =  new JButton("Browse messages");
    private JButton managerModeButton =  new JButton("Open manager mode");
    private JButton debugModeButton =  new JButton("Debug mode");

    MainMenu(Logger log) {
        // Set the main menu panel parameters
        mainMenu.setResizable(false);
        mainMenu.setSize(700,500);
        mainMenu.setLocation(350,280);
        mainMenu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Hook up logger to GUI
        this.log = log;

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

        mainMenu.add(gridComponents);

        //Display the window.
        mainMenu.pack();
        mainMenu.setVisible(true);
        handleButtonAction();

        log.Log("GUI -- MainMenu properly loaded");
    }

    private void handleButtonAction() {
        existingConvosButton.addActionListener(
                (ActionEvent e) -> {
                    mainMenu.dispose();
                    dispose();
                }
        );
        createNewConvoButton.addActionListener(
                (ActionEvent e) -> {
                    mainMenu.dispose();
                    dispose();
                }
        );
        myCircleButton.addActionListener(
                (ActionEvent e) -> {
                    mainMenu.dispose();
                    dispose();
                }
        );
        chatGroupsButton.addActionListener(
                (ActionEvent e) -> {
                    mainMenu.dispose();
                    dispose();
                }
        );
        createChatGroupsButton.addActionListener(
                (ActionEvent e) -> {
                    mainMenu.dispose();
                    dispose();
                }
        );
        browseMessagesButton.addActionListener(
                (ActionEvent e) -> {
                    mainMenu.dispose();
                    dispose();
                }
        );
        managerModeButton.addActionListener(
                (ActionEvent e) -> {
                    mainMenu.dispose();
                    dispose();
                }
        );
        debugModeButton.addActionListener(
                (ActionEvent e) -> {
                    mainMenu.dispose();
                    dispose();
                }
        );
    }
}
