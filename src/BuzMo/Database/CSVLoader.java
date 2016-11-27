package BuzMo.Database;

import BuzMo.Logger.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Lucas Lopilato on 11/26/2016.
 * Reads CSV File and inserts all data from CSV into the SQL connection
 */
class CSVLoader {
    private Logger log;
    private Connection connection;
    private char separator = ',';

    CSVLoader(Logger log, Connection connection){
        this.connection = connection;
        this.log = log;
    }

    void loadCSV(String csvFile, String tableName) throws DatabaseException
    {
        BufferedReader br = null;
        InputStream in = null;
        InputStreamReader inr = null;
        String line;
        String delimiter = ",";

        try {
            in = this.getClass().getClassLoader().getResourceAsStream(csvFile);
            inr = new InputStreamReader(in);
            br = new BufferedReader(inr);

            line = br.readLine();
            String[] fields = line.split(delimiter);
            int numLines =  fields.length;

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(delimiter);

                //Format Base SQL for table insert
                String sql = "INSERT INTO "+tableName+" (";
                for(int i=0; i<numLines; i++){
                    sql += fields[i];
                    if(i != numLines -1)
                        sql += ',';
                }

                sql += " ) VALUES ( ";

                for(int i=0; i<numLines; i++){
                    sql+="'"+data[i]+"'";
                    if(i != numLines -1 )
                        sql += ',';
                }

                sql += ")";

                log.Log("attempting to write "+sql);

                Statement st =connection.createStatement();
                st.execute(sql);

                log.Log("line successfully inserted into "+ tableName);
            }

        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        finally
        {
            if (br != null) {
                try {
                    br.close();
                    inr.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
