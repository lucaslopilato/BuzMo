package BuzMo.Database;

import BuzMo.Logger.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public Vector<String> getWords(int messageID) throws DatabaseException {
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
    public Insert insert(int messageID, Vector<String> words) throws DatabaseException {
        if(!Message.exists(st, messageID)){
            return Insert.NOEXIST_MSG;
        }

        Vector<String> associated = getWords(messageID);

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
