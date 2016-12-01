package BuzMo.GUI;

import BuzMo.Logger.Logger;
import javax.swing.*;
import java.awt.event.ActionEvent;
/**
 * Created by Ben on 11/30/2016.
 * Takes an input of topic words and returns messages containing them.
 * If empty, then return messages containing the user's topic words.
 * Also allows you to select a message and send its author a friend request.
 */
public class TopicSearchResults {
    TopicSearchResults(Logger log, String yourUsername, String input) {
        JOptionPane.showMessageDialog(null, "Created TopicSearchResults");
    }
}
