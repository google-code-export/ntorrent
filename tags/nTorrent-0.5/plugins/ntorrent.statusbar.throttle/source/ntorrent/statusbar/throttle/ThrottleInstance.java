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
package ntorrent.statusbar.throttle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ntorrent.io.rtorrent.Global;
import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionInstance;
import ntorrent.session.SessionStateListener;
import ntorrent.statusbar.throttle.view.ThrottlePane;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Bit;
import ntorrent.torrenttable.model.Torrent;

/**
 * @author Kim Eik
 *
 */
public class ThrottleInstance implements SessionInstance, SessionStateListener, ChangeListener {
	private final ConnectionSession session;
	private final Thread updater,throttle;
	private final TorrentTableInterface tc;

	private final Global global;
	
	private final Bit speedUp,speedDown;
	private final SpinnerNumberModel upModel = new SpinnerNumberModel();
	private final SpinnerNumberModel downModel = new SpinnerNumberModel();

	private final JPanel panel = new JPanel();
	private final JPanel statusBar;
	private final ThrottlePane throttleView = new ThrottlePane(upModel,downModel);
	private final JLabel speedLabel = new JLabel();
	
	
	private boolean started = false;

	public ThrottleInstance(final ConnectionSession session) {
		this.session = session;
		tc = session.getTorrentTableController();
		statusBar = session.getDisplay().getStatusBar();
		global = session.getConnection().getGlobalClient();
		
		speedUp = new Bit(0);
		speedDown = new Bit(0);
		
		int down = global.get_download_rate().intValue();
		int up = global.get_upload_rate().intValue();
		downModel.setValue(down > 0 ? down/1024 : 0);
		upModel.setValue(up > 0 ? up/1024 : 0);
		
		//add gui
		panel.add(throttleView);
		panel.add(speedLabel);
		
		//add listeners
		session.addSessionStateListener(this);
		upModel.addChangeListener(this);
		downModel.addChangeListener(this);
		
		/**
		 * Maybe add a torrenttable listener that notifies everytime the table updates?
		 */
		updater = new Thread(){
			@Override
			public void run() {
				while(!session.isShutdown() && started){
					long sDown = 0;
					long sUp = 0;
					for(Torrent tor : tc.getTorrents().values()){
						sDown += tor.getDownRate().getValue();
						sUp += tor.getUpRate().getValue();
					}
					speedUp.setValue(sUp);
					speedDown.setValue(sDown);
					speedLabel.setText(
							"U: "+speedUp+"  D: "+speedDown
					);
					try{
						if(session.isStarted())
							sleep(2000);
						else
							this.join();
					}catch(InterruptedException x){
						//interrupted
					}
				}
			}
		};
		
		throttle = new Thread(){
			private SpinnerNumberModel down = (SpinnerNumberModel) throttleView.getDown().getModel();
			private SpinnerNumberModel up = (SpinnerNumberModel) throttleView.getUp().getModel();
			private boolean valueChanged = false;
			@Override
			public void run() {
				while(!session.isShutdown() && started){
					try{
						if(valueChanged){
							sleep(3000);
							setValues();
							valueChanged = false;
						}else
							join();
					}catch(InterruptedException x){
						valueChanged = true;
					}
				}
			}
			
			private void setValues(){
				global.set_upload_rate(up.getNumber().intValue()*1024);
				global.set_download_rate(down.getNumber().intValue()*1024);
			}
		};
	}

	public boolean isStarted() {
		return started;
	}


	public void start() {
		started = true;
		updater.start();
		throttle.start();
		statusBar.add(panel);
		statusBar.repaint();
		statusBar.revalidate();

	}

	public void stop() {
		started = false;
		statusBar.remove(panel);
		statusBar.repaint();
		statusBar.revalidate();

	}

	public void sessionStateChanged() {
		if(session.isStarted())
			updater.interrupt();
	}

	public void stateChanged(ChangeEvent e) {
		throttle.interrupt();
	}

}
