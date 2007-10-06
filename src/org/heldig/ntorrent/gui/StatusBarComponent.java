/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Kim Eik
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.heldig.ntorrent.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import org.apache.xmlrpc.XmlRpcRequest;
import org.heldig.ntorrent.Controller;
import org.heldig.ntorrent.model.units.Bit;
import org.heldig.ntorrent.threads.ThrottleThread;
import org.heldig.ntorrent.xmlrpc.XmlRpcCallback;
import org.heldig.ntorrent.xmlrpc.XmlRpcQueue;


/**
 * @author   netbrain
 */
public class StatusBarComponent extends XmlRpcCallback implements ChangeListener {
	//Statusbar component
	JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEADING));
	JPanel container = new JPanel();
	JSpinner maxDownloadRate = new JSpinner(new SpinnerNumberModel(0,0,null,10));
	JSpinner maxUploadRate = new JSpinner(new SpinnerNumberModel(0,0,null,10));
	Bit downloadRate = new Bit(0);
	Bit uploadRate = new Bit(0);
	String port = new String();
	Controller C;
	
	JLabel rateGroup = new JLabel();
	JLabel portGroup = new JLabel();
	JLabel commandStatus = new JLabel();
	
	private void update(){
		rateGroup.setText("Up: "+uploadRate.toString()+" / Down: "+downloadRate.toString());
		portGroup.setText("Port: "+port);
	}
	
	public StatusBarComponent(Controller c){
		C = c;
		statusBar.add(container);
		container.setLayout(new BoxLayout(container,BoxLayout.LINE_AXIS));
		Dimension tsize = new Dimension(80,20);
		update();
		maxDownloadRate.setPreferredSize(tsize);
		maxUploadRate.setPreferredSize(tsize);
		container.add(new JLabel("Throttle:"));
		container.add(maxUploadRate);
		container.add(maxDownloadRate);
		addSeperator();
		container.add(rateGroup);
		addSeperator();
		container.add(portGroup);
		addSeperator();
		container.add(commandStatus);
		maxDownloadRate.addChangeListener(this);
		maxUploadRate.addChangeListener(this);
	}
	
	private void addSeperator(){
		container.add(Box.createHorizontalStrut(5));
		container.add(new JSeparator(SwingConstants.VERTICAL));
		container.add(Box.createHorizontalStrut(5));
	}
	
	/**
	 * @param  downloadRate
	 */
	public void setDownloadRate(Bit downloadRate) {
		this.downloadRate = downloadRate;
	}
	
	/**
	 * @param  uploadRate
	 */
	public void setUploadRate(Bit uploadRate) {
		this.uploadRate = uploadRate;
	}
	
	public void setMaxDownloadRate(long maxDownloadRate) {
		this.maxDownloadRate.setValue((int)maxDownloadRate/1024);
	}
	
	public void setMaxUploadRate(long maxUploadRate) {
		this.maxUploadRate.setValue((int)maxUploadRate/1024);
		
	}
	
	public JLabel addStatusItem(String title, String value){
		JLabel label = new JLabel(title+": "+value);
		label.setVisible(true);
		container.add(label);
		return label;
	}
	
	/**
	 * @return
	 */
	public Component getStatusBar() {
		return statusBar;
	}

	/**
	 * @param  portRange
	 */
	public void setPort(String portRange) {
		port = portRange;
	}
	
	public void repaint(){
		commandStatus.setText("Ping: "+XmlRpcQueue.lag()+" sec");
		update();
		statusBar.repaint();
		statusBar.revalidate();
	}

	@Override
	public void handleResult(XmlRpcRequest pRequest, Object pResult) {
		String methodname = pRequest.getMethodName();
		if(methodname == "get_port_range")
			setPort((String)pResult);
		else if(methodname == "get_download_rate")
			setMaxDownloadRate((Long)pResult);
		else if(methodname == "get_upload_rate")
			setMaxUploadRate((Long)pResult);
	}

	public void stateChanged(ChangeEvent e) {
		ThrottleThread thread = C.getTC().getThrottleThread();
		JSpinner src = (JSpinner)e.getSource();
		if(src.equals(maxDownloadRate)){
			thread.setDownloadRate((Integer)src.getValue()*1024);
		}else if(src.equals(maxUploadRate)){
			thread.setUploadRate((Integer)src.getValue()*1024);
		}
	}
}
