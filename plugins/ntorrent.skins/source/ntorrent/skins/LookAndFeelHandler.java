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

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import ntorrent.Main;
import ntorrent.data.Environment;
import ntorrent.gui.MainWindow;
import ntorrent.locale.ResourcePool;
import ntorrent.settings.model.SettingsExtension;
import ntorrent.settings.view.SettingsComponentFactory;
import ntorrent.skins.model.SkinModel;
import ntorrent.tools.Serializer;

import org.java.plugin.Plugin;

public class LookAndFeelHandler extends Plugin implements SettingsExtension {
	private SkinModel model = new SkinModel();
	private SettingsComponentFactory settingsComponent;
	
	public LookAndFeelHandler() {
		try {
			model = Serializer.deserialize(SkinModel.class, Environment.getNtorrentDir());
			if(model.getLafClass() != null)
				setWindowSkin();
				
		} catch (Exception x){
			x.printStackTrace();
		}
		settingsComponent = new SettingsComponentFactory(model);
	}
	
	@Override
	protected void doStart() throws Exception {
		
	}

	@Override
	protected void doStop() throws Exception {
		
	}

	
	private void setWindowSkin(){
		MainWindow w = Main.getMainWindow();
		try {
			UIManager.setLookAndFeel(model.getLafClass().getClassName());
			SwingUtilities.updateComponentTreeUI(w);
			w.pack();
		} catch (Exception x){
			Logger.global.warning(x.getMessage());
		}
	}

	public Component getDisplay() {
		return settingsComponent == null ? null : settingsComponent.getDisplay();
	}

	public void saveActionPerformed() throws Exception {
		settingsComponent.restoreToModel();
		setWindowSkin();
		Serializer.serialize(model, Environment.getNtorrentDir());
	}

	public String toString() {
		return ResourcePool.getString("skins", "locale", this);
	}

}
