package fr.victor.scanLineMonitor;

public class Monitor {

	static final float version = 0.3F;
	
	public static WindowSL window;
	public static DataFetcher dataFetch;
	
	
	public static final String fileName = "debug.txt";
	
	public static void main(String[] args){
		
		System.out.println("Init ScanLineMonitor");
		
		window = new WindowSL();
	}

}
