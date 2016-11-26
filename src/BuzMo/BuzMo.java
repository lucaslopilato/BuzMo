package BuzMo;

import BuzMo.Database.Database;
import BuzMo.GUI.GUI;
import BuzMo.Logger.Logger;

/**
 * Created by Lucas Lopilato on 11/25/2016.
 * Main class to initialize all component parts for BuzMo
 */
public class BuzMo {
    private Logger log = new Logger();
    private Database db = new Database(log);
    private GUI gui = new GUI(log);


    public static void main(String args[]){
        //Create new Instance of BuzMo
        BuzMo app = new BuzMo();
    }

    //Loads Logger
    private BuzMo()
    {
        //Load Logger

        //Load JDBC

        //Load GUI
    }
}
