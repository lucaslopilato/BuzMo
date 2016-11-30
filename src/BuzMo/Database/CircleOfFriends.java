package BuzMo.Database;

import BuzMo.Logger.Logger;
import sun.rmi.runtime.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * Created by lucas on 11/28/2016.
 * CRUD Class for CircleOfFriends
 */
public class CircleOfFriends extends DatabaseObject {
    public CircleOfFriends(Logger log, Connection connection) throws DatabaseException {
        super(log, connection);
    }

    //Get the circle of Friends for a user
    //Note: Does not contain the user whose circle it is
    public static Vector<String> getCircleOfFriends(Logger log, Statement st, String userID)throws DatabaseException{
        Vector<String> response = new Vector<>();

        //Check if the user exists
        if(!User.exists(st,userID)){
            throw new DatabaseException("Cannot find circle of Friends for non existent user: "+userID);
        }
        String sql = "SELECT * FROM circleOfFriends C WHERE C.user="+addTicks(userID)+" OR C.friend="+addTicks(userID);
        try {
            st.execute(sql);
            log.gSQL(sql);
            ResultSet rs = st.getResultSet();

            while(rs.next()){
                String first = rs.getString("user");
                if(first.compareTo(userID) != 0){
                    response.add(first);
                }
                else{
                    response.add(rs.getString("friend"));
                }
            }

            log.Log("Circle of Friends Retrieved for user "+userID);
            return response;
        } catch (SQLException e) {
            log.bSQL(sql);
            throw new DatabaseException(e);
        }
    }


    //Add new Friendship
    public Insert addFriends(String user1, String user2) throws DatabaseException {
        //Check if both users are the same
        if(user1.compareTo(user2) == 0){
            return Insert.DUPLICATE;
            //throw new DatabaseException("Users are implicitly friends with each other");
        }

        if(!User.exists(st, user1)){
            return Insert.NOEXIST_USR;
            //throw new DatabaseException("Cannot add friendship, "+user1+" doesn't exist");
        }

        if(!User.exists(st, user2)){
            return Insert.NOEXIST_USR;
            //throw new DatabaseException("Cannot add friendship, "+user2+" doesn't exist");
        }

        String sql = "INSERT INTO circleoffriends (user,friend) VALUES (" +addTicks(user1)+","+addTicks(user2)+")";
        try {
            st.execute(sql);
            log.gSQL(sql);
        } catch (SQLException e) {
            log.bSQL(sql);
            throw new DatabaseException(e);
        }
        return Insert.SUCCESS;
    }
}
