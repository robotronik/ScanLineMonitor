package fr.victor.scanLineMonitor;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindowSL extends JFrame{
	
	private JPanel mainPanel;
	protected DataFetcher dataFetch;
	protected ComPanel comPan;
	protected PixelPanel pixPan;
	
	public WindowSL(){
			
			this.setTitle("Scan Line Monitor (v"+Monitor.version+")");
			this.setBackground(Color.white);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(1000,600);
			
			dataFetch = new DataFetcher();
			this.comPan = new ComPanel(this.dataFetch);
			this.pixPan = new PixelPanel(this.dataFetch);
			this.dataFetch.setPixelPanel(this.pixPan);;
			
			this.mainPanel = new JPanel();
			this.mainPanel.setBackground(Color.white);
			this.mainPanel.setLayout(new BorderLayout());
			
			this.mainPanel.add(this.comPan, BorderLayout.NORTH);
			this.mainPanel.add(this.pixPan, BorderLayout.CENTER);
			
			this.setContentPane(this.mainPanel);
			this.setVisible(true);
	}
	
}
