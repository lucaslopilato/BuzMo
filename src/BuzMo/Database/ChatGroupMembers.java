package BuzMo.Database;

import BuzMo.Logger.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * Created by lucas on 11/29/2016.
 * CRUD Class for ChatGroupMembers Table
 */
public class ChatGroupMembers extends DatabaseObject {

    public ChatGroupMembers(Logger log, Connection connection) throws DatabaseException {
        super(log, connection);
    }

    //Gets a list of all recipients for a specific message
    public static Vector<String> members(Logger log, Statement st, String groupName) throws DatabaseException{
        Vector<String> response = new Vector<>();

        //Check if the user exists
        if(!ChatGroups.exists(st,groupName)){
            throw new DatabaseException("Cannot find recipients for non existent group: "+groupName);
        }
        String sql = "SELECT member FROM chatgroupmembers C WHERE C.group_name="+addTicks(groupName);
        try {
            st.execute(sql);
            log.gSQL(sql);
            ResultSet rs = st.getResultSet();

            while(rs.next()){
                response.add(rs.getString("member"));
            }

            log.Log("Members retrieved for "+groupName);
            return response;
        } catch (SQLException e) {
            log.bSQL(sql);
            throw new DatabaseException(e);
        }
    }


    //Attempt to insert members for a chat group
    public Insert insertMembers(String groupName, Vector<String> members) throws DatabaseException{

        if(!ChatGroups.exists(st, groupName)){

            return Insert.NOEXIST_GROUP;
        }

        //Check if the message exists
        //Associate each user with a groupName
        for(String s: members){
            String sql = "INSERT INTO chatgroupmembers (group_name, member) VALUES (";

            if(!User.exists(st, s)){
                log.Log("cannot add group member "+s+" to group "+groupName+": user doesn't exist");
                return Insert.NOEXIST_USR;
            }

            sql += addTicks(groupName) + "," + addTicks(s) + ')';

            try {
                st.execute(sql);
                log.gSQL(sql);
            } catch (SQLException e) {
                log.bSQL(sql);
                throw new DatabaseException(e);
            }
        }

        return Insert.SUCCESS;
    }
}
