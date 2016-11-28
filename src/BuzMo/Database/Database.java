package BuzMo.Database;

import BuzMo.Logger.Logger;
import BuzMo.Properties.AppProperties;
import BuzMo.Properties.PropertiesException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.io.Console;
import java.sql.*;
import java.util.Vector;


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
    private int newMsg = 0;

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

        CreateDatabase init = new CreateDatabase(log,connection);

    }

    //Closes all connections
    public void dispose() throws DatabaseException{
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException("Error attempting to close SQL connection");
        }
    }



    //Adds new private message
    public Insert addMessage(int id, String sender, String message, String timestamp, Vector<String> recipients, boolean isPublic, Vector<String> topicWords){
        String sql = "INSERT INTO Messages(message_id, sender, message, timestamp, is_public) VALUES (";
        try{
            if(!userExists(sender)){
                return Insert.USR_NOEXIST;
            }

            if(msgExists(id)){
                return Insert.OBJ_EXISTS;
            }

            sql += id + "," + addTicks(sender) + "," + addTicks(message) + "," + addTicks(timestamp) + "," + isPublic+")";

            Statement st = connection.createStatement();
            st.execute(sql);
            log.Log("wrote: "+ sql);

            //Add all recipients
            for(String s: recipients){
                if(!userExists(s)){
                    return Insert.USR_NOEXIST;
                }

                sql = "INSERT INTO messageReceivers (message_id, recipient) VALUES (" + id +","+addTicks(s) + ")";
                st.execute(sql);
                log.Log("wrote: "+sql);
            }

            //Add all
            if(isPublic && topicWords.isEmpty()) {
                return Insert.EMPTY_TOPIC_WORDS;
            }

            for(String s: topicWords){
                sql = "INSERT INTO MessageTopicWords (message_id, word) VALUES (" + id +","+addTicks(s)+")";
                st.execute(sql);
                log.Log("wrote: "+sql);
            }


        }catch (Exception e){
            log.Log("Invalid sql: "+ sql);
        }

        return Insert.SUCCESS;
    }


    //Adds new Chat Group to the Database
    public Insert addChatGroups(String owner, String name, int duration, Vector<String> members){
        String sql = "";
        try {
            //Create new statement
            Statement st = connection.createStatement();
            ResultSet res;

            if(chatGroupExists(name)){
                return Insert.OBJ_EXISTS;
            }

            if(!userExists(owner)){
                return Insert.USR_NOEXIST;
            }


            //Insert ChatGroup into the database
            sql = "INSERT INTO ChatGroups (owner, name, duration) VALUES " +
                    "(" + addTicks(owner) +","+ addTicks(name) +","+ duration +")";
            st.execute(sql);
            log.Log("Chatgroup "+name+" inserted ");

            //Associate new users with chatgroup
            for(String s: members){
                if(!userExists(s)){
                    log.Log("user "+s+" could not be added to the chatgroup "+name+" because they do not exist");
                    return Insert.USR_NOEXIST;
                }

                sql = "INSERT INTO ChatGroupMembers(group_name, member) VALUES (" + addTicks(name) +"," + addTicks(s) +")";
                st.execute(sql);
            }

        }catch(Exception e){
            log.Log("Invalid SQL: "+sql);
            return Insert.INVALID;
        }

        return Insert.SUCCESS;
    }

    //Surrounds a string with ticks
    private String addTicks(String original){
        if(original.charAt(0) == '\'')
            return original;
        return "'"+original+"'";
    }

    //Checks if a user exists by email
    private boolean userExists(String user) throws DatabaseException{
        String sql;
        try {
            Statement st = connection.createStatement();
            sql = "SELECT COUNT(1) FROM Users WHERE email_address = " + addTicks(user);
            st.execute(sql);

            ResultSet res = st.getResultSet();
            res.first();
            if (res.getInt(1)!= 0) {
                return true;
            }
            else{
                return false;
            }


        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

    //Returns true if a chat group exists
    public boolean chatGroupExists(String group) throws DatabaseException {
        String sql;
        try {
            Statement st = connection.createStatement();
            sql = "SELECT COUNT(1) FROM ChatGroups C WHERE name = " + addTicks(group);
            st.execute(sql);

            ResultSet res = st.getResultSet();
            res.first();
            if (res.getInt(1) != 0) {
                return true;
            }
            else{
                return false;
            }


        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

    //Returns true if a chat group exists
    public boolean msgExists(int id) throws DatabaseException {
        String sql;
        try {
            Statement st = connection.createStatement();
            sql = "SELECT COUNT(1) FROM Messages M WHERE message_id = " + id;
            st.execute(sql);

            ResultSet res = st.getResultSet();
            res.first();
            if (res.getInt(1) != 0) {
                return true;
            }
            else{
                return false;
            }


        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }
}
