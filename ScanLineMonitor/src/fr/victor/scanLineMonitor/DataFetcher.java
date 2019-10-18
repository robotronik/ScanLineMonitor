package fr.victor.scanLineMonitor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import com.fazecast.jSerialComm.*;

public class DataFetcher{

	public static final String fileName = "debug.txt";
	
	protected int[] data = new int[128];
	protected ArrayList<DataObserver> observers = new ArrayList<DataObserver>();
	Thread thread;
	
	SerialPort[] ports;
	SerialPort serialPort;
	
	public DataFetcher(){
		
		//init debug
		for(int i=0; i<128; i++)
			data[i] = 100*i/128;
		
		this.ports = SerialPort.getCommPorts();
	    
	}//fin constructeur
	
	public boolean loadFromDebugFile(){
		try{
			File ff=new File("debug.txt");
			BufferedReader b = new BufferedReader(new FileReader(ff));
			String[] lignes = b.readLine().split(" ");
			for(int i=0; (i<128 && i<lignes.length); i++) {
				this.data[i] = Integer.valueOf(lignes[i]);
			}
			
			/*/
			String[] tab;
			for(String ligne : Files.readAllLines(Paths.get(fileName))) {
				System.out.println("size: "+ligne.length());
				tab = ligne.split(" ");
				 for(int i =0; (i<128 && i<tab.length); i++){
					this.data[i] = Integer.valueOf(tab[i]);
					//System.out.println(this.data[i]);
				 }
			}
			//*/
			this.notifyObserversDataUpdated(this.serialPort.getSystemPortName());
			
			
			return true;
		} catch (IOException e) {
			System.out.println("DataFetcher] Erreur lecture debug.txt");
	    	e.printStackTrace();
	    	return false;
	    }
		
	}
	
	public boolean loadData(int id){
		if(id <0){
			this.notifyObserversDataUpdated(this.serialPort.getSystemPortName());
			return this.loadFromDebugFile();
		}else{
			this.notifyObserversDataUpdated(this.serialPort.getSystemPortName());
			return this.openPortAndLoad(id);
		}	
	}
	
	public boolean openPortAndLoad(int portIndex){
		if(portIndex>=this.ports.length)
			return false;
		
		this.serialPort = this.ports[portIndex];
		if(this.serialPort.openPort()){
			
			//init
			this.serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
			this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
			
			//start
			this.thread = new ScanThread();
			this.thread.start();
			this.notifyObserversPortOpenned(this.serialPort.getSystemPortName());
			return true;
		}
		else{
			this.serialPort = null;
			return false;
		}
	}
	
	public void saveToTxt(){
		try{
			File ff=new File("debug.txt");
			ff.createNewFile();
			BufferedWriter b = new BufferedWriter(new FileWriter(ff));
			for(int i =0; i<128; i++){
				b.write(this.data[i]+" ");
			}
			b.close(); // fermer le fichier a la fin des traitements
		} catch (Exception e) {}
	}
	
	public void acquireOneFrame(){
		if(this.serialPort == null)
			return;
		
		DataInputStream dataStream = new DataInputStream(DataFetcher.this.serialPort.getInputStream());
		int[] values = new int[128];
		
		try {
			do{}while(dataStream.available() == 0);
			if(dataStream.readByte() == -1) {//debut scan
				for(int i=0; i<values.length; i++) {
					values[i]=dataStream.readByte();
				}
				System.out.println("Values: ");
				for(int i=0; i<values.length; i++) {
					System.out.print(values[i]+" ");
				}
				System.out.print("\n");
				for(int i=0; i<this.data.length; i++) {
					this.data[i] = values[i];
				}
				this.notifyObserversDataUpdated(this.serialPort.getSystemPortName());
			}
		}catch(Exception e) {}
		
	}
	
	public void closePort(){
		if(this.serialPort != null) {
			this.serialPort.closePort();
			this.notifyObserversPortClosed(this.serialPort.getSystemPortName());
			this.serialPort = null;
		}
	}
	
	public ArrayList<String> getPortNames(){
		
		ArrayList<String> names = new ArrayList<String>();
		names.add("debug.txt");
		
		this.ports = SerialPort.getCommPorts();
		for(SerialPort p : this.ports)
			names.add(p.getSystemPortName());
		//load
		return names;
	}
	
	public int[] getData(){
		return this.data;
	}
	
	//---------------------------------- Observers ----------------------------------
	protected void addDataObserver(DataObserver obs){
		this.observers.add(obs);
	}
	
	protected void removeDataObserver(DataObserver obs){
		this.observers.remove(obs);
	}
	
	protected void notifyObserversPortOpenned(String name){
		for(DataObserver obs : this.observers)
			obs.notifyPortOpenned(this, name);
	}
	
	protected void notifyObserversPortClosed(String name){
		for(DataObserver obs : this.observers)
			obs.notifyPortClosed(this, name);
	}
	
	protected void notifyObserversDataUpdated(String portName){
		for(DataObserver obs : this.observers)
			obs.notifyDataUpdated(this);
	}
	
	//---------------------------------- Thread ----------------------------------

	private class ScanThread extends Thread{
		
		public void run(){
			
			DataInputStream dataStream = new DataInputStream(DataFetcher.this.serialPort.getInputStream());
			int[] values = new int[128];
			
			boolean stop = false;
			while(!stop) {
				if(DataFetcher.this.serialPort == null) {
					this.interrupt();
					System.out.println("thread stopped");
					return;
				}
					
				//System.out.println("acquire");
				
				try {
					
					if(dataStream.readByte() == -1) {//debut scan
						for(int i=0; i<values.length; i++) {
							values[i]=dataStream.readByte();
						}
						for(int i=0; i<DataFetcher.this.data.length; i++) {
							DataFetcher.this.data[i] = values[i];
							//System.out.print(values[i]+" ");
						}
						//System.out.print("\n");
						DataFetcher.this.notifyObserversDataUpdated(DataFetcher.this.serialPort.getSystemPortName());
					}
				}catch(Exception e) {
				};
			}
			
		}//fin run
		
	}
	
}
