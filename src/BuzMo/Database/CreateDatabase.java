package BuzMo.Database;

import BuzMo.Logger.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.Vector;

/**
 * Created by Lucas Lopilato on 11/26/2016.
 * This class will attempt to write all tables to the database
 * based on the BuzMo Schema
 *
 * This is a helper class that is only used to initialize the database
 * Will not be used in later operation
 */
class CreateDatabase {
    private Logger log;
    private Connection connection;

     CreateDatabase(Logger log, Connection connection) throws DatabaseException{
        this.log = log;
        this.connection = connection;

         //Drop all tables before adding
         dropAllTables();

         //Import schema based on BuzMo Schematic
         writeAllTables();

         inputInfo();

        log.Log("Wrote BuzMo Schema to Connected Database");
    }

    //Attempt to write all tables in the BuzMo Schema to the database
    private void writeAllTables() throws DatabaseException{
        String users = "CREATE TABLE Users( " +
                "name VARCHAR(20)," +
                "phone_number CHAR(10)," +
                "email_address VARCHAR(20)," +
                "password VARCHAR(10)," +
                "screenname VARCHAR(20)," +
                "isManager BOOLEAN NOT NULL DEFAULT 0," +
                "PRIMARY KEY(email_address))";

        writeTable("Users", users);

        String CircleOfFriends = "CREATE TABLE CircleOfFriends(" +
                "user VARCHAR(20)," +
                "friend VARCHAR(20)," +
                "FOREIGN KEY(user) REFERENCES Users(email_address)," +
                "FOREIGN KEY(friend) REFERENCES users(email_address)," +
                "PRIMARY KEY (user,friend))";
        //Check user != friend
        //Check user, friend != friend user

        writeTable("CircleOfFriends", CircleOfFriends);


        String UserTopicWords = "CREATE TABLE UserTopicWords(" +
                "user VARCHAR(20)," +
                "word VARCHAR(20)," +
                "FOREIGN KEY(user) REFERENCES Users(email_address)," +
                "PRIMARY KEY (user,word))";

        writeTable("UserTopicWords", UserTopicWords);


        String Messages = "CREATE TABLE Messages(" +
                "message_id INTEGER," +
                "sender VARCHAR(20)," +
                "message VARCHAR(1400)," +
                "timestamp VARCHAR(20)," +
                "is_public BOOLEAN," +
                "PRIMARY KEY(message_id))";
                //Check if message is public, topic words cannot be null

        writeTable("Messages", Messages);

        String MessageTopicWords = "CREATE TABLE MessageTopicWords(" +
                "message_id INTEGER," +
                "word VARCHAR(20)," +
                "FOREIGN KEY(message_id) REFERENCES Messages(message_id)," +
                "PRIMARY KEY(message_id, word))";
        //Check if message is public, topic words cannot be null


        writeTable("MessageTopicWords", MessageTopicWords);

        String MessageReceivers = "CREATE TABLE MessageReceivers(" +
                "message_id INTEGER," +
                "recipient VARCHAR(20)," +
                "FOREIGN KEY(message_id) REFERENCES Messages(message_id)," +
                "FOREIGN KEY(recipient) REFERENCES Users(email_address)," +
                "PRIMARY KEY(message_id, recipient))";

        writeTable("MessageReceivers", MessageReceivers);

        String ChatGroups = "CREATE TABLE ChatGroups(" +
                "owner VARCHAR(20)," +
                "group_name VARCHAR(20)," +
                "duration INTEGER," +
                "PRIMARY KEY (group_name)," +
                "FOREIGN KEY(owner) REFERENCES Users(email_address))";

        writeTable("ChatGroups", ChatGroups);

        String ChatGroupMembers = "CREATE TABLE ChatGroupMembers(" +
                "group_name VARCHAR(20)," +
                "member VARCHAR(20)," +
                "FOREIGN KEY(group_name) REFERENCES ChatGroups(group_name)," +
                "FOREIGN KEY(member) REFERENCES Users(email_address)," +
                "PRIMARY KEY(group_name, member))";

        writeTable("ChatGroupMembers", ChatGroupMembers);

    }


    //Attempt to write the Table tableName with SQL statement
    //If the table already exists, will log the occurrence and fail silently
    //Otherwise throws database exception
    private void writeTable(String tableName, String sql) throws DatabaseException{
        try{
            Statement st = connection.createStatement();
            st.execute(sql);
            log.Log("wrote "+tableName+" to the database");
        }
        catch(SQLException e){
            if(e.getMessage().compareTo("Table '" + tableName.toLowerCase()+ "' already exists") == 0){
                log.Log(e.getMessage());
            }
            else{
                throw new DatabaseException("Error writing tables to the database",e);
            }
        }
    }

    //Make an attempt to drop all tables. If tables
    //Are not present, fail silently
    private void dropAllTables(){
        Vector<String> tables = new Vector<>();
        tables.add("circleoffriends");
        tables.add("usertopicwords");
        tables.add("messagetopicwords");
        tables.add("messagereceivers");
        tables.add("chatgroupmembers");
        tables.add("chatgroups");
        tables.add("messages");
        tables.add("users");


        //Try to drop each table and fail silently if not found
        for(String s : tables) {
            dropTable(s);
        }

        log.Log("All BuzMo Tables dropped");
    }

    //Attempt to drop table, fail silently if error occurs
    private void dropTable(String name){
        try{
            Statement st = connection.createStatement();
            st.execute("DROP TABLE "+name);
            log.Log("Successfully dropped table "+name);
        }
        catch(SQLException sql){
            log.Log("Error dropping table: "+ sql.getMessage());
        }
    }

    //Input Given info to all tables
    private void inputInfo() throws DatabaseException
    {
        InsertUsers();
        InsertFriends();
        InsertIndividualFriends();
        InsertMessages();
    }

    //Input Users into Users table
    private void InsertUsers() throws DatabaseException{
        CSVLoader csv = new CSVLoader(log);

        csv.loadCSV("users.csv");

        //Get Headers
        String[] fields = csv.getNextLine();
        int numFields = fields.length;

        //Read each line
        String[] data;

        try {
            while ((data = csv.getNextLine()) != null) {
                //Format Base SQL for table insert
                String sql = "INSERT INTO " + "users" + " (";
                for (int i = 0; i < numFields; i++) {
                    sql += fields[i];
                    if (i != numFields - 1)
                        sql += ',';
                }

                sql += " ) VALUES ( ";

                for (int i = 0; i < numFields; i++) {
                    sql += "'" + data[i] + "'";
                    if (i != numFields - 1)
                        sql += ',';
                }

                sql += ")";

                log.Log("attempting to write " + sql);

                Statement st = connection.createStatement();
                st.execute(sql);

                log.Log("line successfully inserted into users");
            }
        }
        catch(Exception e){
            throw new DatabaseException(e);
        }
    }

    private void InsertFriends() throws DatabaseException{
        CSVLoader csv = new CSVLoader(log);
        csv.loadCSV("circleOfFriends.csv");

        String[] data;
        try {
            //Operate on each line individually
            while ((data = csv.getNextLine()) != null) {

                //For each name, add the whole circle of friends excluding the same user
                for (int i = 0; i < data.length; i++) {
                    //Insert the User
                    for (int j = i+1; j < data.length; j++) {
                        if (j != i) {
                            String sql = "INSERT INTO circleoffriends ( user, friend ) VALUES (";
                            sql += ("'" + data[i] + "'");
                            sql += ",";
                            sql += ("'" + data[j] + "')");

                            Statement st = connection.createStatement();
                            st.execute(sql);
                        }
                    }
                }

            }
        } catch(Exception e){
            throw new DatabaseException(e);
        }

    }

    private void InsertIndividualFriends() throws DatabaseException{
        CSVLoader csv = new CSVLoader(log);
        csv.loadCSV("ifriends.csv");
        String[] line;

        try {
            while ((line = csv.getNextLine()) != null) {
                String sql = "INSERT INTO circleoffriends ( user, friend ) VALUES (";
                sql += ("'" + line[0] + "','" + line[1] + "')");

                Statement st = connection.createStatement();
                st.execute(sql);
                log.Log("successfully executed individual friend q: "+sql);
            }
        } catch(SQLException s){
            throw new DatabaseException(s);
        }
    }

    private void InsertMessages() throws DatabaseException{
        CSVLoader csv = new CSVLoader(log);
        csv.loadCSV("messages.csv");
        String[] line;

        String sql = "";
        try {
            while ((line = csv.getNextLine()) != null) {
                sql = "INSERT INTO messages ( message_id, sender, message, timestamp, is_public ) VALUES (";
                sql += line[0] + ",";
                for(int i=1; i<line.length; i++){
                    sql += ("'" + line[i].replace('|',',') + "',");
                }
                sql += "0)";

                Statement st = connection.createStatement();
                st.execute(sql);
                log.Log("successfully executed individual friend q: "+sql);
            }
        } catch(SQLException s){
            log.Log("sql error trying to write message "+sql);
            throw new DatabaseException(s);
        }
    }




}
