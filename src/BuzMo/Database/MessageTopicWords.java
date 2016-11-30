package BuzMo.Database;

import BuzMo.Logger.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * Created by lucas on 11/28/2016.
 * CRUD Class for the MessageTopicWords table
 */
public class MessageTopicWords extends DatabaseObject{

    public MessageTopicWords(Logger log, Connection connection) throws DatabaseException {
        super(log, connection);
    }

    //Get All Topic Words for User
    public static Vector<String> getWords(Logger log, Statement st, int messageID) throws DatabaseException {
        Vector<String> response = new Vector<>();
        String sql = "SELECT word FROM messagetopicwords WHERE message_id="+messageID;
        try {
            st.execute(sql);

            //Get results
            ResultSet rs = st.getResultSet();
            while(rs.next()){
                response.add(rs.getString(1));
            }
        } catch (SQLException e) {
            log.Log("Invalid SQL: "+sql);
            throw new DatabaseException(e);
        }

        return response;
    }

    //Insert New Topic Words
    public static Insert insert(Logger log, Statement st, int messageID, Vector<String> words) throws DatabaseException {
        if(words == null){
            return Insert.SUCCESS;
        }

        if(!Message.exists(st, messageID)){
            return Insert.NOEXIST_MSG;
        }

        Vector<String> associated = getWords(log, st, messageID);

        String sql;
        for(String word : words){
            if(associated.contains(word)){
                log.Log("error: inserting "+word+" for "+messageID+" is redundant");
                return Insert.DUPLICATE;
            }

            sql = "INSERT INTO usertopicwords(user,word) VALUES (" +
                    messageID+","+addTicks(word)+")";

            try {
                st.execute(sql);
            } catch (SQLException e) {
                log.Log("Invalid SQL: "+sql);
                throw new DatabaseException(e);
            }
        }

        return Insert.SUCCESS;
    }
}
