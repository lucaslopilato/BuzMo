package BuzMo.Database;

import BuzMo.Logger.Logger;

/**
 * Created by Lucas Lopilato on 11/25/2016.
 * Class to create a connection to desired database {SQL PLUS}
 * May also need a function to initialize the database and import the base information provided
 */
public class Database {
    private Logger log;

    public Database(Logger log){
        //Hook up the log to the JDBC Class
        this.log = log;
    }
}
