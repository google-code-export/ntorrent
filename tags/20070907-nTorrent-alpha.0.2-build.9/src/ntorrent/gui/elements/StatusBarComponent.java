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

package ntorrent.gui.elements;

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

import ntorrent.model.units.Byte;


public class StatusBarComponent {
	//Statusbar component
	JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEADING));
	JPanel container = new JPanel();
	JSpinner maxDownloadRate = new JSpinner(new SpinnerNumberModel(0,0,null,5));
	JSpinner maxUploadRate = new JSpinner(new SpinnerNumberModel(0,0,null,2));
	Byte downloadRate = new Byte(0);
	Byte uploadRate = new Byte(0);
	String port = new String();
	
	JLabel rateGroup = new JLabel();
	JLabel portGroup = new JLabel();
	
	private void update(){
		rateGroup.setText("Up: "+uploadRate.toString()+" / Down: "+downloadRate.toString());
		portGroup.setText("Port: "+port);
	}
	
	public StatusBarComponent(){
		statusBar.add(container);
		container.setLayout(new BoxLayout(container,BoxLayout.LINE_AXIS));
		//statusBar.setLayout(new FlowLayout(FlowLayout.LEADING));
		Dimension tsize = new Dimension(60,20);
		update();
		maxDownloadRate.setPreferredSize(tsize);
		maxUploadRate.setPreferredSize(tsize);
		maxDownloadRate.setEnabled(false);
		maxUploadRate.setEnabled(false);
		container.add(new JLabel("Throttle:"));
		container.add(maxUploadRate);
		container.add(maxDownloadRate);
		addSeperator();
		container.add(rateGroup);
		addSeperator();
		container.add(portGroup);
	}
	
	private void addSeperator(){
		container.add(Box.createHorizontalStrut(5));
		container.add(new JSeparator(SwingConstants.VERTICAL));
		container.add(Box.createHorizontalStrut(5));
	}
	
	public void setDownloadRate(long downloadRate) {
		this.downloadRate = new Byte(downloadRate);
	}
	
	public void setUploadRate(long uploadRate) {
		this.uploadRate = new Byte(uploadRate);
	}
	
	public void setMaxDownloadRate(int maxDownloadRate) {
		this.maxDownloadRate.setValue(maxDownloadRate);
	}
	
	public void setMaxUploadRate(int maxUploadRate) {
		this.maxUploadRate.setValue(maxUploadRate);
	}
	
	public JLabel addStatusItem(String title, String value){
		JLabel label = new JLabel(title+": "+value);
		label.setVisible(true);
		container.add(label);
		return label;
	}
	
	public Component getStatusBar() {
		return statusBar;
	}

	public void setPort(String portRange) {
		port = portRange;
	}
	
	public void repaint(){
		update();
		statusBar.repaint();
		statusBar.revalidate();
	}
}
