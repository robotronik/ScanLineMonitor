package fr.victor.scanLineMonitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class FilePanel extends JPanel implements DataObserver{

	private JButton open = new JButton("Open");
	private JButton save = new JButton("Save");
	
	public FilePanel(){
		
	}
	
	private void initMenu(){
		//this.add(this.save);
		//this.add(this.open);
	}
	
	private void initActionButtons(){
		this.save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//SerialPanel.this.dataFetch.saveToTxt();
				//SerialPanel.this.feedback.setText("Saved");
			}
		});
	}

	@Override
	public void notifyPortClosed(DataFetcher data, String name) {}

	@Override
	public void notifyPortOpenned(DataFetcher data, String name) {}

	@Override
	public void notifyDataUpdated(DataFetcher data) {
		this.repaint();
	}
	
	
}
