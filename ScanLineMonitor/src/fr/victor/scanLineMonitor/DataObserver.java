package fr.victor.scanLineMonitor;

public interface DataObserver {

	public void notifyPortClosed(DataFetcher data, String name);
	
	public void notifyPortOpenned(DataFetcher data, String name);
	
	public void notifyDataUpdated(DataFetcher data);
}
