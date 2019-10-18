package fr.victor.scanLineMonitor;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindowSL extends JFrame{
	
	private JPanel mainPanel;
	
	protected SerialPanel serialPan;
	protected PixelPanel pixPan;
	
	public WindowSL(){
			
			this.setTitle("Scan Line Monitor (v"+Monitor.version+")");
			this.setBackground(Color.white);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(1000,600);
			
			Monitor.dataFetch = new DataFetcher();
			this.serialPan = new SerialPanel(Monitor.dataFetch);
			this.pixPan = new PixelPanel(Monitor.dataFetch);
			Monitor.dataFetch.addDataObserver((DataObserver)this.pixPan);
			
			this.mainPanel = new JPanel();
			this.mainPanel.setBackground(Color.white);
			this.mainPanel.setLayout(new BorderLayout());
			
			this.mainPanel.add(this.serialPan, BorderLayout.NORTH);
			this.mainPanel.add(this.pixPan, BorderLayout.CENTER);
			
			this.setContentPane(this.mainPanel);
			this.setVisible(true);
	}
	
}
