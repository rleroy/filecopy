package bzh.leroy.ronan.filecopy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class FileCopier {

	protected Path from;
	protected Path to;
	
	int countTotal;
	long sizeTotal;
	
	int countDone;
	long sizeDone;
	
	long starttime;
	
	public FileCopier(String from, String to) {
		this.from = Paths.get(from);
		this.to = Paths.get(to);
		countTotal = 0;
		sizeTotal = 0;
		countDone = 0;
		sizeDone = 0;
		starttime = 0;
	}
	
	public abstract void run();
	
	protected void walkFiles(Path p, Consumer<Path> action) {
		try{
			if (!p.getFileName().toString().startsWith(".")){
				if (p.toFile().isDirectory()){
					Files.list(p)
						.forEach(sub -> walkFiles(sub, action));
				}else{
					action.accept(p);
				}
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected void log(String label, long current, long total, long timeStart, Function<Long, String> formater){
		System.out.println(label+":"+formater.apply(current)+"/"+formater.apply(total)+" ("+100*current/total+"%)");
		long elapsed = System.currentTimeMillis()-timeStart;
		long estimated = elapsed * total / current;
		long remaining = estimated - elapsed;
		System.out.println("Elapsed:"+getDuractionStr(elapsed)
		               +" / Estimated:"+getDuractionStr(estimated)
		               +" / Remaining:"+getDuractionStr(remaining));
		
	}
	
	protected String getSizeStr(long bytes){
		long kilobytes = (bytes / 1024);
		long megabytes = (kilobytes / 1024);
		long gigabytes = (megabytes / 1024);
		kilobytes -= megabytes * 1024;
		megabytes -= gigabytes * 1024;

		String res = gigabytes+"Go "+megabytes+"Mo "+kilobytes+"Ko" ;
		return res;
	}

	protected String getDuractionStr(long ms){
		long seconds = ms / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		seconds -= minutes * 60;
		minutes -= hours * 60;
		
		String res = hours+"h"+minutes+"'"+seconds+"\"";
		return res;
	}

}
