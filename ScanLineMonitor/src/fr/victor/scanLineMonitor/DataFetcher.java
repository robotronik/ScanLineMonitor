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
	
	protected ComPanel comPan;
	protected PixelPanel pixPan;
	
	private int[] data = new int[128];
	//public static ArrayList<Integer> dataReg = new ArrayList<Integer>();
	//coef pour reg
	private double a=1;
	private double x0 = 64;
	private double b;
	private double c;
	
	Thread thread;
	Thread mainThread;
	SerialPort[] ports;
	SerialPort serialPort;
	//private int dataSelector = 0;//-1: fichier, sinon index
	
	public DataFetcher(){
		
		//init debug
		for(int i=0; i<128; i++)
			data[i] = 100*i/128;
		
		this.mainThread = Thread.currentThread();
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
			this.pixPan.repaint();
			return true;
		} catch (IOException e) {
			System.out.println("DataFetcher] Erreur lecture debug.txt");
	    	e.printStackTrace();
	    	return false;
	    }
		
	}
	
	public boolean loadData(int id){
		if(id <0){
			this.pixPan.repaint();
			return this.loadFromDebugFile();
		}else{
			this.pixPan.repaint();
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
				this.pixPan.repaint();
			}
		}catch(Exception e) {}
		
		/*/ancienne version text
		Scanner scanner = new Scanner(DataFetcher.this.serialPort.getInputStream());
		//int[] values = new int[128];
		int[] bufferScan = new int[256];
		
		System.out.println("acquire");
		
		try {
			//scanning:
			for(int s=0; s<bufferScan.length; s++) {
				while(!scanner.hasNext()){};//attente
				bufferScan[s] = scanner.nextInt();//enregistre
			}
			System.out.println("Buffer: ");
			for(int i=0; i<bufferScan.length; i++)
				System.out.print(bufferScan[i]+" ");
			//detection debut trame:
			int debut=0;
			for(int j=0; j<bufferScan.length; j++){
				if(bufferScan[j] == -1) {
					debut=j+1;
					break;
				}
			}
			//transfert
			for(int i=0; i<data.length; i++) {
				data[i] = bufferScan[debut+i];
			}
			//Affichage
			System.out.println("\nValues: ");
			for(int i=0; i<data.length; i++)
				System.out.print(data[i]+" ");
			this.notifyObserver();
		}catch(Exception e) {};
		//*/
	}
	
	public void computeReg(){
		
	}
	
	public void closePort(){
		if(this.serialPort != null) {
			this.serialPort.closePort();
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
	
	public void setPixelPanel(PixelPanel p) {
		this.pixPan = p;
	}

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
						DataFetcher.this.pixPan.repaint();
					}
				}catch(Exception e) {
				};
			}
			
		}//fin run
		
	}
	
}
