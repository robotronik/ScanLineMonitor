package fr.victor.scanLineMonitor;

public class Monitor {

	static final float version = 0.4F;
	
	public static WindowSL window;
	public static DataFetcher dataFetch;
	
	
	public static final String fileName = "debug.txt";
	
	public static void main(String[] args){
		
		System.out.println("Init ScanLineMonitor");
		
		dataFetch = new DataFetcher();
		window = new WindowSL();
	}
	
	public static void printGlobal(String str){
		window.print(str);
	}

}
