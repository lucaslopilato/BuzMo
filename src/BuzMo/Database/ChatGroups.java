package BuzMo.Database;

import BuzMo.Logger.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by lucas on 11/29/2016.
 * CRUD Class for ChatGroups table
 */
public class ChatGroups extends DatabaseObject{
    public ChatGroups(Logger log, Connection connection) throws DatabaseException {
        super(log, connection);
    }

    public static boolean exists(Statement st, String name) throws DatabaseException{
        String sql;

        try {
            sql = "SELECT COUNT(1) FROM chatgroups WHERE name = " + addTicks(name);
            st.execute(sql);

            ResultSet res = st.getResultSet();
            res.first();
            return res.getInt(1) != 0;

        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }


    //Insert Group into the table
    public Insert insertGroup(String owner, String name, int duration) throws DatabaseException{
        //Check if the owner is a valid name
        if(!User.exists(st, owner)){
            return Insert.NOEXIST_USR;
        }

        if(!ChatGroups.exists(st, name)){
            return Insert.DUPLICATE;
        }

        String sql = "INSERT INTO chatgroups (owner, name, duration) VALUES (" +
                addTicks(owner) + "," + addTicks(name) + "," + duration + ")";

        try {
            st.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }


        return Insert.SUCCESS;
    }


}
