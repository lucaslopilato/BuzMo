package BuzMo.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;

/**
 * Created by Lucas Lopilato on 11/25/2016.
 * Logs all operations that occur and all errors that occur
 */
public class Logger{
    private FileHandler log;
    private FileHandler debug;
    //Keeping currentDay Static since this application will only run for 30 mins
    private String currentDay  = new SimpleDateFormat("yyyy.MM.dd").format(new Date());

    //Creates a new logger for BuzMo
    public Logger() throws Exception {
        //Try to create FileHandlers for both debug and general logging
        try{
            log = new FileHandler(currentDay+".log", true);
            debug = new FileHandler(currentDay+".debug", true);
        }
        catch(IOException io){
            throw new LoggerException("IOError Initializing Logger", io);
        }
        catch(Exception e){
            throw new LoggerException("Error Initializing Logger", e);
        }
    }

    //dispose must be called when BuzMo is closing to prevent lock files
    public void dispose(){
        log.close();
        debug.close();
    }



}
