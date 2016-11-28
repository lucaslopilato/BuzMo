package BuzMo.Database;

import BuzMo.Logger.Logger;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by lucas on 11/28/2016.
 * CRUD Class for User table
 */
public class User extends DatabaseObject{
    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private String screenname = null;

    //Base Information for a user
    public User(Logger log, Connection connection, String name ,String phoneNumber, String email, String password) throws DatabaseException {
        super(log, connection);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;

        //Check all constraints before trying to enter
        if (name.length() > 20) {
            throw new DatabaseException("Cannot have userID > 20 chars");
        }

        if (phoneNumber.length() > 10) {
            throw new DatabaseException("Cannot have phone number > 10 chars");
        }

        if (email.length() > 20) {
            throw new DatabaseException("Cannot have email > 20 chars");
        }

        if (password.length() < 2 || password.length() > 10) {
            throw new DatabaseException("password " + password + " is not valid");
        }
    }

    //Initialize User with optional screenname
    public User(Logger log, Connection connection, String name, String phoneNumber, String email, String password, String screenname) throws DatabaseException {
        super(log, connection);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.screenname = screenname;

            //Check all constraints before trying to enter
            if(name.length() > 20){
                throw new DatabaseException("Cannot have userID > 20 chars");
            }

            if(phoneNumber.length() > 10){
                throw new DatabaseException("Cannot have phone number > 10 chars");
            }

            if(email.length() > 20){
                throw new DatabaseException("Cannot have email > 20 chars");
            }

            if(password.length() < 2 || password.length() > 10){
                throw new DatabaseException("password "+password+" is not valid");
            }

            if(screenname.length() > 20){
                throw new DatabaseException("Cannot have screenname > 20 chars");
            }
    }

    //CRUD Functions

    public boolean exists() throws DatabaseException{
        return exists(this.st, this.email);
    }

    public static boolean exists(User user) throws DatabaseException{
        return exists(user.st, user.email);
    }

    //Check if user already exists
    public static boolean exists(Statement st, String email) throws DatabaseException{
        String sql;
        try {
            sql = "SELECT COUNT(1) FROM Users WHERE email_address = " + addTicks(email);
            st.execute(sql);

            ResultSet res = st.getResultSet();
            res.first();
            if (res.getInt(1) == 0) {
                return false;
            }
            else{
                return true;
            }

        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    //Insert
    public static void insert(Statement st, User user, Boolean isManager) throws DatabaseException {
        if(user.exists()){
            throw new DatabaseException("User already exists");
        }
        String sql = "INSERT INTO users (name,phone_number,email_address,password,screenname,isManager) " +
                "VALUES ("+addTicks(user.name)+","+addTicks(user.phoneNumber)+","+addTicks(user.email)+","+
                addTicks(user.password)+",";

                //Insert screenname on selector
                sql += (user.screenname == null) ? "NULL" : addTicks(user.screenname);
                sql += ",";
                sql += (isManager) ? "TRUE" : "FALSE";
                sql += ")";

        try {
            st.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        user.log.Log("successfully executed "+sql);
    }

}
