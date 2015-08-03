package bzh.leroy.ronan.filecopy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileCopierV2 extends FileCopier{

	public FileCopierV2(String from, String to) {
		super(from, to);
	}

	public void run() {
		Map<Path, Path> selectedFiles = new LinkedHashMap<>();
		walkFiles(this.from, p -> add(p, selectedFiles));
		starttime = System.currentTimeMillis();
		selectedFiles.entrySet().stream()
				.forEach(e -> copy(e.getKey(), e.getValue()));
	}
	
	private void add(Path p, Map<Path, Path> selectedFiles){
		Path dest = Paths.get(p.toAbsolutePath().toString().replace(this.from.toAbsolutePath().toString(), this.to.toAbsolutePath().toString()));
		if (!dest.toFile().exists() || dest.toFile().length() != p.toFile().length()){
			selectedFiles.put(p, dest);
			countTotal++;
			sizeTotal += p.toFile().length();
		}
	}
	
	private void copy(Path from, Path to){
		try{
			Files.createDirectories(to.getParent());
			System.out.println("Writing : "+to.toAbsolutePath().toString());
			Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
			countDone++;
			sizeDone += to.toFile().length();
			
			log("count", countDone, countTotal, this.starttime, l -> l.toString());
			log("size.", sizeDone, sizeTotal, this.starttime, l -> getSizeStr(l));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
