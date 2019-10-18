/*Barre de bouton pour la gestion des ports
 * 
 */

package fr.victor.scanLineMonitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SerialPanel extends JPanel{
	
	protected DataFetcher dataFetch;

	private JButton refresh = new JButton("Refresh");
	private JComboBox<String> ports = new JComboBox<String>();
	private JButton open = new JButton("Open");
	private JButton close = new JButton("Close");
	//private JButton acquire1 = new JButton("Acquire 1 frame");
	private JLabel feedback = new JLabel("");
	
	public SerialPanel(DataFetcher df){
		
		this.dataFetch = df;
		this.loadPortName();
		
		this.refresh.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				SerialPanel.this.loadPortName();
				SerialPanel.this.repaint();
			}
		});
		
		this.open.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(SerialPanel.this.dataFetch.loadData(SerialPanel.this.ports.getSelectedIndex()-1))
					SerialPanel.this.feedback.setText("Port opened successfully");
				else
					SerialPanel.this.feedback.setText("Unable to open the port");
			}
		});
		
		this.close.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SerialPanel.this.dataFetch.closePort();
				SerialPanel.this.feedback.setText("Port closed");
			}
		});
		
		/*/
		this.acquire1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				SerialPanel.this.feedback.setText("Scanning...");
				SerialPanel.this.repaint();
				SerialPanel.this.dataFetch.acquireOneFrame();
				SerialPanel.this.feedback.setText("Scan complete");
				SerialPanel.this.repaint();
			}
		});
		//*/
		
		
		this.add(this.refresh);
		this.add(this.ports);
		this.add(this.open);
		this.add(this.close);
		this.add(this.feedback);
		//this.add(this.acquire1);
		//this.add(this.save);
	}
	
	public void loadPortName(){
		ArrayList<String> names = this.dataFetch.getPortNames();
		this.ports.removeAllItems();
		for(String str : names)
			this.ports.addItem(str);
	}
	
}
