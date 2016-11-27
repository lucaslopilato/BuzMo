package BuzMo.Database;

import BuzMo.Logger.Logger;
import BuzMo.Properties.AppProperties;
import BuzMo.Properties.PropertiesException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.io.Console;
import java.sql.*;


/**
 * Created by Lucas Lopilato on 11/25/2016.
 * Class to create a connection to desired database {SQL PLUS}
 * This class has a dependency on
 * May also need a function to initialize the database and import the base information provided
 *
 */
public class Database {
    private Logger log;
    private Connection connection;
    private AppProperties properties;

    //private OracleDataSource source;
    private MysqlDataSource source;

    //Choose which URL to use depending on where you are running the program
    //private String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
    //private String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";

    //Initialize Database and Establish a connection
    public Database(Logger log) throws DatabaseException {

        //Hook up the log to the JDBC Class
        this.log = log;

        //MySQL driver usage since SQLPlus sucks
        this.source = new MysqlDataSource();


        //Oracle OJDBC
        /*try {
            //Initialize DataSource class
            this.source = new OracleDataSource();

            //Set Parameters
            //source.setDatabaseName("XE");
            //source.setPortNumber(1521);
            //source.getDriverType("thin")
            source.setURL(url);

        }
        catch(SQLException sql){
            throw new DatabaseException("Error trying to Initialize Oracle Data Source", sql);
        }*/

        //Establish the connection to the database
        try {
            //Read in Properties
            properties = new AppProperties(log);
            String username = properties.getUsername();
            String password = properties.getPassword();

            source.setUser(username);
            source.setPassword(password);
        } catch (PropertiesException pe) {
            throw new DatabaseException(pe);
        }


        //Establish MySQL connection
        try {
            source.setURL("jdbc:mysql://localhost/buzmo");
            this.connection = source.getConnection();
        } catch (SQLException sql) {
            throw new DatabaseException("Error establishing connection", sql);
        }
        log.Log("Database properly loaded");


        CreateAllTables();
    }

    //Deletes all tables, recreates the whole schema, and imports all info
    private void ResetDatabase() {

    }

    private void DeleteAllTables() {

    }

    //Inserts all tables based on BuzMo Database Design
    private void CreateAllTables() throws DatabaseException {
        String users = "CREATE TABLE Users( " +
                "name VARCHAR(20)," +
                "phone_number CHAR(10)," +
                "email_address VARCHAR(20)," +
                "password VARCHAR(10)," +
                "screenname VARCHAR(20)," +
                "isManager BOOLEAN," +
                "PRIMARY KEY(email_address))";

    try
    {
         Statement st = connection.createStatement();
        st.execute(users);

    }
    catch(SQLException sql)
    {
        throw new DatabaseException("Error Creating All New Tables", sql);
    }
}
}
