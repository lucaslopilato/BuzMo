package BuzMo.Database;

/**
 * Created by lucas on 11/28/2016.
 * Enum describing each possible error code for trying to add a new item into the database
 */
public enum Insert {
    OBJ_EXISTS ("Object Already Exists"),
    USR_NOEXIST ("User Doesn't exist"),
    SUCCESS ("Successfully Entered"),
    EMPTY_TOPIC_WORDS("Empty Topic Words when Message Public"),
    INVALID ("Invalid Arguments");

    private final String message;

    Insert(String s){
        this.message = s;
    }

    public String toString(){
        return message;
    }
}
