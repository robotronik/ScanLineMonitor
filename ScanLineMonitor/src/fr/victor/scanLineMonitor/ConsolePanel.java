package fr.victor.scanLineMonitor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ConsolePanel extends JPanel{

	JTextArea txt = new JTextArea();
	JTextField cmdField = new JTextField();
	JButton send = new JButton("Send");
	JButton clear = new JButton("Clear");
	
	JCheckBox box = new JCheckBox("This is a box");
	
	public ConsolePanel(){
		
		this.initPanel();
		this.initActionButtons();
	}
	
	private void initPanel(){

		this.setPreferredSize(new Dimension(400, 180));
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		JPanel rightPane = new JPanel();
		JPanel bottomPane = new JPanel();
		
		//centerPane
		this.txt.setEditable(false);
		this.txt.setMargin(new Insets(1,3,1,3));
		
		JScrollPane scrollPan = new JScrollPane();
		scrollPan.getVerticalScrollBar().setUnitIncrement(10);
		scrollPan.getHorizontalScrollBar().setUnitIncrement(10);
		scrollPan.setViewportView(this.txt);
		
		//rightPane
		rightPane.add(this.box);
		
		//bottomPane
		bottomPane.setLayout(new BorderLayout());
		bottomPane.add(this.cmdField, BorderLayout.CENTER);
		bottomPane.add(this.send, BorderLayout.EAST);
		
		this.setLayout(new BorderLayout());
		this.add(scrollPan, BorderLayout.CENTER);
		this.add(rightPane, BorderLayout.EAST);
		this.add(bottomPane, BorderLayout.SOUTH);
		
	}
	
	private void initActionButtons(){
		this.send.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ConsolePanel.this.addToDisplay(ConsolePanel.this.cmdField.getText());
			}
		});
	}
	
	public void addToDisplay(String str){
		this.txt.setText(this.txt.getText().concat("\n"+str));
	}
}
