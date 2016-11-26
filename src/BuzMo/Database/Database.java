package BuzMo.Database;

import BuzMo.Logger.Logger;
import oracle.jdbc.driver.OracleDriver;

import java.io.Console;
import java.util.prefs.Preferences;


/**
 * Created by Lucas Lopilato on 11/25/2016.
 * Class to create a connection to desired database {SQL PLUS}
 * This class has a dependency on
 * May also need a function to initialize the database and import the base information provided
 *
 */
public class Database {
    private Logger log;
    private Preferences preferences;

    public Database(Logger log){
        log.Log("Attempting to Load the Database");

        //Hook up the log to the JDBC Class
        this.log = log;






        log.Log("Database properly loaded");
    }
}
