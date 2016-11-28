package BuzMo.Database;

import BuzMo.Logger.Logger;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by lucas on 11/28/2016.
 * Base Class for a database object
 */
public class DatabaseObject {
    //Connection to the database via the driver
    protected Logger log;
    protected Statement st;

    //Initializes a new DatabaseObject
    public DatabaseObject(Logger log, Connection connection) throws DatabaseException{
        this.log = log;

        //Check if connection is valid
        if(connection == null){
            throw new DatabaseException("Cannot initialize a new DatabaseObject with a null connection");
        }

        //Initialize sql statment
        try{
            this.st = connection.createStatement();
        }catch(Exception e){
            throw new DatabaseException("Error initializing SQL Statement for new DatabaseObject");
        }
    }

}
