package fr.victor.scanLineMonitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ComPanel extends JPanel{
	
	protected DataFetcher dataFetch;

	private JButton refresh = new JButton("Refresh");
	private JComboBox<String> ports = new JComboBox<String>();
	private JButton open = new JButton("Open");
	private JButton close = new JButton("Close");
	private JButton save = new JButton("Save");
	private JButton acquire1 = new JButton("Acquire 1 frame");
	private JLabel feedback = new JLabel("");
	
	public ComPanel(DataFetcher df){
		
		this.dataFetch = df;
		this.loadPortName();
		
		this.refresh.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				ComPanel.this.loadPortName();
				ComPanel.this.repaint();
			}
		});
		
		this.open.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ComPanel.this.dataFetch.loadData(ComPanel.this.ports.getSelectedIndex()-1))
					ComPanel.this.feedback.setText("Port opened successfully");
				else
					ComPanel.this.feedback.setText("Unable to open the port");
			}
		});
		
		this.close.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ComPanel.this.dataFetch.closePort();
				ComPanel.this.feedback.setText("Port closed");
			}
		});
		
		this.acquire1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				ComPanel.this.feedback.setText("Scanning...");
				ComPanel.this.repaint();
				ComPanel.this.dataFetch.acquireOneFrame();
				ComPanel.this.feedback.setText("Scan complete");
				ComPanel.this.repaint();
			}
		});
		
		this.save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				ComPanel.this.dataFetch.saveToTxt();
				ComPanel.this.feedback.setText("Saved");
			}
		});
		
		this.add(this.refresh);
		this.add(this.ports);
		this.add(this.open);
		this.add(this.close);
		this.add(this.feedback);
		this.add(this.acquire1);
		this.add(this.save);
	}
	
	public void loadPortName(){
		ArrayList<String> names = this.dataFetch.getPortNames();
		this.ports.removeAllItems();
		for(String str : names)
			this.ports.addItem(str);
	}
	
}
