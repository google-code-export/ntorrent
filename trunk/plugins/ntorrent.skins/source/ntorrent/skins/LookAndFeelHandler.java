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
package ntorrent.skins;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import ntorrent.NtorrentApplication;
import ntorrent.data.Environment;
import ntorrent.gui.MainWindow;
import ntorrent.session.ConnectionSession;
import ntorrent.settings.DefaultSettingsImpl;
import ntorrent.skins.model.PrettyLookAndFeelInfo;
import ntorrent.skins.model.SkinModel;
import ntorrent.tools.Serializer;

public class LookAndFeelHandler extends DefaultSettingsImpl<SkinModel> {
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(LookAndFeelHandler.class);
	
	public LookAndFeelHandler() {
		super(Serializer.deserialize(SkinModel.class, Environment.getNtorrentDir()));
	}
	
	@Override
	protected void doStart() throws Exception {
		setWindowSkin();
	}

	@Override
	protected void doStop() throws Exception {
		setDefaultWindowSkin();
	}
	
	private void setDefaultWindowSkin(){
		getModel().setLafClass(new PrettyLookAndFeelInfo(null,UIManager.getSystemLookAndFeelClassName()));
		setWindowSkin();
	}

	
	private void setWindowSkin(){
		MainWindow w = NtorrentApplication.MAIN_WINDOW;
		try {
			UIManager.setLookAndFeel(getModel().getLafClass().getClassName());
			SwingUtilities.updateComponentTreeUI(w);
			w.pack();
		} catch (Exception x){
			log.warn(x.getMessage(),x);
		}
	}


	public void saveActionPerformed() throws Exception {
		super.saveActionPerformed();
		setWindowSkin();
		Serializer.serialize(getModel(), Environment.getNtorrentDir());
	}

}
