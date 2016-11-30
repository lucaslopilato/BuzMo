package BuzMo.Database;

import BuzMo.Logger.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
    private CSVLoader csv;
    private Message message;
    private MessageReceivers messageReceivers;
    private MessageTopicWords messageTopicWords;
    private UserTopicWords userTopicWords;
    private CircleOfFriends circleOfFriends;
    private ChatGroups chatGroups;
    private ChatGroupMembers chatGroupMembers;

     CreateDatabase(Logger log, Connection connection) throws DatabaseException{
        this.log = log;
        this.connection = connection;
        this.csv = new CSVLoader(log);

        //Initialize CRUD Classes
         this.message = new Message(log, connection);
         this.messageReceivers = new MessageReceivers(log,connection);
         this.messageTopicWords = new MessageTopicWords(log, connection);
         this.userTopicWords = new UserTopicWords(log, connection);
         this.circleOfFriends = new CircleOfFriends(log, connection);
         this.chatGroups = new ChatGroups(log, connection);
         this.chatGroupMembers = new ChatGroupMembers(log, connection);



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
        //May need read receipt here

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

        //InsertMessages();
        InsertDirectMessages();

        //Insert Groups
        InsertGroups();
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
            Statement st = connection.createStatement();

            while ((data = csv.getNextLine()) != null) {
                //Format Base SQL for table insert
                User u = new User(log,connection,data[0], data[1], data[2], data[3], data[4]);
                User.insert(st, u, false);
            }

            st.close();
        }
        catch(Exception e){
            throw new DatabaseException(e);
        }
    }

    private void InsertFriends() throws DatabaseException{
        csv.loadCSV("circleOfFriends.csv");

        String[] data;
        try {
            CircleOfFriends circle = new CircleOfFriends(log, connection);
            //Operate on each line individually
            while ((data = csv.getNextLine()) != null) {

                //For each name, add the whole circle of friends excluding the same user
                for (int i = 0; i < data.length; i++) {
                    //Insert the User
                    for (int j = i+1; j < data.length; j++) {
                        circle.addFriends(data[i], data[j]);
                    }
                }
            }
        } catch(Exception e){
            throw new DatabaseException(e);
        }

    }

    private void InsertIndividualFriends() throws DatabaseException{
        csv.loadCSV("ifriends.csv");
        String[] line;

        while ((line = csv.getNextLine()) != null) {
            circleOfFriends.addFriends(line[0], line[1]);
        }
    }

    private void InsertDirectMessages() throws DatabaseException{
        csv.loadCSV("privateMessages.csv");
        String[] line;

        while ((line = csv.getNextLine()) != null) {
            line[4] = line[4].replace('|',',');
            Vector<String> recipients = new Vector<>();
            recipients.add(line[2]);
            message.insertPrivateMsg(new Integer(line[0]),line[1],line[3],line[4], recipients).toString();
        }
    }

    private void InsertGroups() throws DatabaseException{
        csv.loadCSV("groups.csv");
        String[] line;

        while ((line = csv.getNextLine()) != null) {
            Vector<String> members = new Vector<>();
            for(int i=2; i<line.length; i++){
                members.add(line[i]);
            }
            chatGroups.insertGroupAndUsers(line[1],line[0],2,members);
        }
    }






}
