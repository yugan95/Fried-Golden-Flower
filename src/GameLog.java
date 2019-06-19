import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class GameLog {
	private static GameLog log = null;
	private String logFileName = "";
	private PrintWriter writer;
	   
    private GameLog() {
    	createFile();
    }

    public synchronized static GameLog getLogWriter() { 
        if (log == null){ 
        	log = new GameLog(); 
        } 
        return log; 
      } 
    
    private void createFile() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");// Set Date format;
	    this.logFileName = df.format(new Date()) + ".log";

	    //Create File
	    File logFile = new File(this.logFileName); 
            
	    try { 
	    	this.writer = new PrintWriter(new FileWriter(logFile, true), true); 
	    	System.out.println("logging start......" + this.logFileName); 
	    } catch (IOException ex) {
	    	ex.printStackTrace();
        } 
    }
    
    public synchronized void log(String logMsg) { 
    	this.writer.println(logMsg); 
	} 
    
    public void close() { 
    	log = null; 
    	if (writer != null){ 
    		writer.close(); 
    	}
    }
}
