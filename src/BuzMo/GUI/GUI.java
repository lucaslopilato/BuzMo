package BuzMo.GUI;

import BuzMo.Logger.Logger;

/**
 * Created by Lucas Lopilato on 11/25/2016.
 * Initializes the GUI for BuzMo Handles all graphical requests for both Manager and User
 */
public class GUI {
    private Logger log;

    public GUI(Logger log){
        //Hook up Logger to GUI
        this.log = log;


        log.Log("GUI Properly Loaded");
    }
}
