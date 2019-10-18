package fr.victor.scanLineMonitor;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PixelPanel extends JPanel implements DataObserver{
	
	//px
	public static final int width = 1000;
	public static final int height = 500;
	protected static final int pxNb = 128;
	protected static final int pxMax = 100;
	protected static final int nbHzDiv = 5;
	protected static final int nbVertDiv = 8;
	
	protected DataFetcher dataFetch;
	
	public PixelPanel(DataFetcher df){
		
		this.dataFetch = df;
	}
	
	public void paintComponent(Graphics g){
		//System.out.println("repaint");
		//clear
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//affiche data
		float colWidth = (float)(this.getWidth()*1.0/pxNb);
		float colHeight = 0;
		g.setColor(Color.blue);
		
		int[] data = this.dataFetch.getData();
		
		for(int i=0; i<data.length; i++){
			colHeight = this.getHeight()*(1.0f*data[i]/pxMax);
			g.fillRect( (int)(colWidth*i), this.getHeight()-(int)(colHeight), (int)(colWidth)+1, (int)(colHeight));
		}
		
		//affiche regression
		
		//ligne de repere
		g.setColor(Color.black);
		float step = this.getHeight()/nbHzDiv;
		for(int i=0; i<nbHzDiv;i++){//horizontal
			g.drawLine(0, (int)(i*step), this.getWidth(), (int)(i*step));
		}
		step = this.getWidth()/nbVertDiv;
		for(int i=0; i<nbVertDiv;i++){//vertical
			g.drawLine((int)(i*step), 0, (int)(i*step), this.getHeight());
		}
		
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
