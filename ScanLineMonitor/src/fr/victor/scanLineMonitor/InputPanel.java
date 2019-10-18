/*Barre de bouton pour la gestion des ports
 * 
 */

package fr.victor.scanLineMonitor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputPanel extends JPanel{

	
	//Serial part
	private JButton refresh = new JButton("Refresh");
	private JComboBox<String> ports = new JComboBox<String>();
	private JButton open = new JButton("Open");
	private JButton close = new JButton("Close");
	private JButton settings = new JButton("Settings");
	
	//File part
	private JTextField fileName = new JTextField();
	private JButton openFile = new JButton("Open file");
	private JButton saveFile = new JButton("Save file");
	
	public InputPanel(){
		
		this.loadPortName();
		this.initPanel();
		this.initActionButtons();
	}
	
	private void initPanel(){
		
		this.ports.setMaximumSize(new Dimension(this.getWidth()/2, this.ports.getMaximumSize().height));
		this.fileName.setMaximumSize(new Dimension(this.getWidth()/2, this.fileName.getMaximumSize().height));
		this.fileName.setMinimumSize(new Dimension(this.getWidth()/4, this.fileName.getMinimumSize().height));
		//this.fileName.setPreferredSize(new Dimension(this.getWidth()/4, this.fileName.getPreferredSize().height));
		this.fileName.setText("debug.txt");
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(this.refresh);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		this.add(this.ports);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		this.add(this.open);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		this.add(this.close);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		this.add(this.settings);
		this.add(Box.createGlue());
		this.add(this.fileName);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		this.add(this.openFile);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		this.add(this.saveFile);
		
	}
	
	private void initActionButtons(){
		
		this.refresh.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				InputPanel.this.loadPortName();
				InputPanel.this.repaint();
			}
		});
		
		this.open.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Monitor.dataFetch.loadData(InputPanel.this.ports.getSelectedIndex()-1))
					Monitor.printGlobal("Port opened successfully");
				else
					Monitor.printGlobal("Unable to open the port");
			}
		});
		
		this.close.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Monitor.dataFetch.closePort();
				Monitor.printGlobal("Port closed");
			}
		});
		
	}
	
	public void loadPortName(){
		ArrayList<String> names = Monitor.dataFetch.getPortNames();
		this.ports.removeAllItems();
		for(String str : names)
			this.ports.addItem(str);
	}
	
}
