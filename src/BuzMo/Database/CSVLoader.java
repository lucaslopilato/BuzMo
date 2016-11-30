package BuzMo.Database;

import BuzMo.Logger.Logger;

import java.io.*;

/**
 * Created by Lucas Lopilato on 11/26/2016.
 * Reads CSV File and inserts all data from CSV into the SQL connection
 */
class CSVLoader {
    private Logger log;
    private char separator = ',';
    private BufferedReader br = null;
    private InputStream in = null;
    private InputStreamReader inr = null;
    private String delimiter = ",";
    private String fileName;

    CSVLoader(Logger log){
        this.log = log;
    }

    //Loads a new csvFile csvFile and saves a reference to it
    void loadCSV(String csvFile){
        fileName = csvFile;

        //Initialize all classes needed to read csv
        try{
            this.br.close();
            this.in.close();
            this.inr.close();
        }
        catch(Exception e){
            log.Log("br, in, or inr tried to be closed when already closed");
        }

        this.br = null;
        this.in = null;
        this.inr = null;
        String line;

        this.in = this.getClass().getClassLoader().getResourceAsStream(csvFile);
        this.inr = new InputStreamReader(in);
        this.br = new BufferedReader(inr);

        log.Log("file "+csvFile+"read in");
    }


    String[] getNextLine() throws DatabaseException{
        if(br == null){
            throw new DatabaseException("No CSV File to read");
        }

        //Try to read a line and return it
        try{
            String line = br.readLine();

            //If the line is empty close up all resources for the file.
            if(line == null) {
                log.Log("line empty, closing out readers");
                this.in.close();
                this.inr.close();
                this.br.close();
                this.in = null;
                this.inr = null;
                this.br = null;

                return null;
            }


            log.Log("successfully read a line of "+fileName);
            String[] response = line.split(delimiter);

            //Change special characters and replace 's for SQL
            for(int i=0; i< response.length; i++){
                response[i] = response[i].replace('|',',');
                response[i] = response[i].replaceAll("'", "\'\'").trim();
            }

            return response;
        }
        catch(Exception e){
            throw new DatabaseException(e);
        }
    }

    /*void loadCSV(String csvFile, String tableName) throws DatabaseException
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
    }*/
}
