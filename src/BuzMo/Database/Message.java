package BuzMo.Database;

import BuzMo.Logger.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.Vector;

/**
 * Created by lucas on 11/28/2016.
 *CRUD Class to handle all CRUD operations on Messages
 */
public class Message extends DatabaseObject{

    public Message(Logger log, Connection connection) throws DatabaseException {
        super(log, connection);
    }

    public static boolean exists(Statement st, Integer messageID) throws DatabaseException {
        String sql;
        try {
            sql = "SELECT COUNT(1) FROM messages WHERE message_id= " + messageID;
            st.execute(sql);

            ResultSet res = st.getResultSet();
            res.first();
            return res.getInt(1) != 0;

        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    //Inserts Public Message with Timestamp
    Insert insertPublicMessage(Integer messageID, String sender, String message, String timestamp, Vector<String> topicWords) throws DatabaseException{
        Vector<String> recipients = CircleOfFriends.getCircleOfFriends(log, st, sender);
        return insert(messageID, sender, message, timestamp, true, topicWords, recipients);
    }

    //Inserts Public Message without Timestamp
    public Insert insertPublicMessage(Integer messageID, String sender, String message, Vector<String> topicWords) throws DatabaseException{
        Vector<String> recipients = CircleOfFriends.getCircleOfFriends(log, st, sender);
        return insert(messageID, sender, message, Timestamp.getTimestamp(), true, topicWords, recipients);
    }

    //Insert Private Group Message with timestamp and topicWords
    Insert insertPrivateGroupMessage(Integer messageID, String sender, String message, String timestamp, Vector<String> topicWords, String group_name) throws DatabaseException{
        Vector<String> recipients = ChatGroupMembers.members(log, st, group_name);
        return insert(messageID, sender, message, timestamp, false, topicWords, recipients);
    }

    //Insert Private Group Message with timestamp and no topic words
    Insert insertPrivateGroupMessage(Integer messageID, String sender, String message, String timestamp, String group_name) throws DatabaseException{
        Vector<String> recipients = ChatGroupMembers.members(log, st, group_name);
        return insert(messageID, sender, message, timestamp, false,null, recipients);
    }

    //Insert Private Group Message with no timestamp and no topic words
    public Insert insertPrivateGroupMessage(Integer messageID, String sender, String message, String group_name) throws DatabaseException{
        Vector<String> recipients = ChatGroupMembers.members(log, st, group_name);
        return insert(messageID, sender, message, Timestamp.getTimestamp(), false,null, recipients);
    }

    //Insert Private Group Message with no timestamp and topic words
    public Insert insertPrivateGroupMessage(Integer messageID, String sender, String message, Vector<String> topicWords, String group_name) throws DatabaseException{
        Vector<String> recipients = ChatGroupMembers.members(log, st, group_name);
        return insert(messageID, sender, message, Timestamp.getTimestamp(), false,topicWords, recipients);
    }


    //Insert Private Message Functions
    Insert insertPrivateMsg(Integer messageID, String sender, String message, String timestamp, Vector<String> topicWords, Vector<String> recipients){
        return insert(messageID, sender, message, timestamp, false, topicWords, recipients);
    }

    //Insert Private message with no topic words
    Insert insertPrivateMsg(Integer messageID, String sender, String message, String timestamp, Vector<String> recipients){
        return insert(messageID,sender,message,timestamp, false, null, recipients);
    }

    //Insert Private message with no topic words and no timestamp
    public Insert insertPrivateMsg(Integer messageID, String sender, String message, Vector<String> recipients){
        return insert(messageID, sender, message, Timestamp.getTimestamp(), false, null, recipients);
    }

    //Insert Private Message With no timestamp
    public Insert insertPrivateMsg(Integer messageID, String sender, String message, Vector<String> topicWords, Vector<String> recipients){
        return insert(messageID, sender, message, Timestamp.getTimestamp(), false, topicWords, recipients);
    }

    public Insert insert(Integer messageID, String sender, String message, String timestamp, boolean isPublic, Vector<String> topicWords, Vector<String> recipients){

        String sql = "INSERT INTO Messages(message_id, sender, message, timestamp, is_public) VALUES (";
        try{
            if(!User.exists(st, sender)){
                return Insert.NOEXIST_USR;
            }

            if(Message.exists(st, messageID)){
                return Insert.DUPLICATE;
            }

            String pub = isPublic ? "TRUE" : "FALSE";

            //Screen for any 's to be inserted
            message = message.replaceAll("'", "\'\'");

            sql += messageID + "," + addTicks(sender) + "," + addTicks(message) + "," + addTicks(timestamp) + "," + pub+")";

            st.execute(sql);
            log.Log("wrote: "+ sql);

            //Add all recipients
            Insert addRecipients = MessageReceivers.insertRecipients(st, messageID, recipients);
            if(addRecipients != Insert.SUCCESS){
                return addRecipients;
            }


            //Make sure public messages have at least one topic words
            if(isPublic && topicWords.isEmpty()) {
                return Insert.EMPTY_TOPIC_WORDS;
            }

            //Insert topic words
            MessageTopicWords.insert(log, st, messageID, topicWords);


        }catch (Exception e){
            log.Log("Invalid sql: "+ sql);
            log.Log(e.getMessage());
            return Insert.INVALID;
        }

        return Insert.SUCCESS;
    }

}