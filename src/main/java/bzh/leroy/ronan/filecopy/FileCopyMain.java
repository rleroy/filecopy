package bzh.leroy.ronan.filecopy;

import java.io.IOException;

public class FileCopyMain {
	
    public static void main(String[] args) throws IOException  {
    	String from = "/Volumes/DATA/Music/Music/";
    	String to = "/Volumes/Lima-rleroy/Music/";
    	FileCopier fc = new FileCopierV2(from, to);
    	fc.run();
    }
    
}
