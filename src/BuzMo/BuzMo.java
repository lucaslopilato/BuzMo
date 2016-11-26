package BuzMo;

import BuzMo.Database.Database;
import BuzMo.GUI.GUI;
import BuzMo.Logger.Logger;

import static java.lang.System.exit;

/**
 * Created by Lucas Lopilato on 11/25/2016.
 * Main class to initialize all component parts for BuzMo
 */
public class BuzMo {
    private Logger log;
    private Database db;
    private GUI gui;


    public static void main(String args[]){
        //Create new Instance of BuzMo
        BuzMo app = new BuzMo();
    }

    //Loads Logger
    private BuzMo()
    {
        try {
            //Load Logger
            log = new Logger();

            //Load Database Connector
            db = new Database(log);

            //Load GUI
            gui = new GUI(log);

        }
        catch(Exception e){
            System.out.println("Error "+e.getMessage()+" occurred when loading BuzMo");
            e.printStackTrace();
            exit(2);
        }
    }
}
