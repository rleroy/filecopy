package bzh.leroy.ronan.filecopy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileCopierV1 extends FileCopier{

	public FileCopierV1(String from, String to) {
		super(from, to);
	}

	public void run() {
		walkFiles(this.from, p -> add(p));
		starttime = System.currentTimeMillis();
		walkFiles(this.from, p -> copy(p));
	}
	
	private void add(Path p){
		countTotal++;
		sizeTotal += p.toFile().length();
	}
	
	private void copy(Path p) {
		try{
			Path dest = Paths.get(p.toAbsolutePath().toString().replace(this.from.toAbsolutePath().toString(), this.to.toAbsolutePath().toString()));
			if (!dest.toFile().exists()){
				System.out.println("Writing : "+dest.toAbsolutePath().toString());
				Files.createDirectories(dest.getParent());
				Files.copy(p, dest);
			}else if (dest.toFile().length() != p.toFile().length()){
				System.out.println("Rewriting : "+dest.toAbsolutePath().toString());
				Files.createDirectories(dest.getParent());
				Files.copy(p, dest, StandardCopyOption.REPLACE_EXISTING);
			}
			countDone++;
			sizeDone += p.toFile().length();
			
			log("count", countDone, countTotal, this.starttime, l -> l.toString());
			log("size.", sizeDone, sizeTotal, this.starttime, l -> getSizeStr(l));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	

}
