package fr.victor.scanLineMonitor;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindowSL extends JFrame{
	
	private JPanel mainPanel;
	
	protected SerialPanel serialPan;
	protected PixelPanel pixPan;
	protected ConsolePanel consolePan;
	
	public WindowSL(){
			
			this.setTitle("Scan Line Monitor (v"+Monitor.version+")");
			this.setBackground(Color.white);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(1000,750);
			
			this.serialPan = new SerialPanel();
			this.pixPan = new PixelPanel(Monitor.dataFetch);
			this.consolePan = new ConsolePanel();
			
			Monitor.dataFetch.addDataObserver((DataObserver)this.pixPan);
			
			this.mainPanel = new JPanel();
			this.mainPanel.setBackground(Color.white);
			this.mainPanel.setLayout(new BorderLayout());
			
			this.mainPanel.add(this.serialPan, BorderLayout.NORTH);
			this.mainPanel.add(this.pixPan, BorderLayout.CENTER);
			this.mainPanel.add(this.consolePan, BorderLayout.SOUTH);
			
			this.setContentPane(this.mainPanel);
			this.setVisible(true);
	}
	
	public void print(String str){
		this.consolePan.addToDisplay(str);
	}
	
}
