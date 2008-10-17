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

public class LookAndFeelHandler extends Plugin implements SettingsExtension,ItemListener {

	private JMenuBar bar = Main.getMainWindow().getJMenuBar();
	private JMenu skins = new JMenu(ResourcePool.getString("skins","locale",this));
	private ButtonGroup bgroup = new ButtonGroup();
	private SkinModel model = new SkinModel();
	private SettingsComponentFactory settingsComponent;
	
	public LookAndFeelHandler() {
		try {
			settingsComponent = new SettingsComponentFactory(model);
			if(model.getLafClass() != null)
				setWindowSkin();
		} catch (Exception x){
			x.printStackTrace();
		}
	}
	
	@Override
	protected void doStart() throws Exception {
		for(LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()){
			JRadioButtonMenuItem radio = new JRadioButtonMenuItem(lafi.getName());
			String currentLF = UIManager.getLookAndFeel().getClass().getName();
			radio.setName(lafi.getClassName());
			if(currentLF.equals(lafi.getClassName()))
				radio.setSelected(true);
			radio.addItemListener(this);
			bgroup.add(radio);
			skins.add(radio);
		}
		bar.add(skins);
		bar.revalidate();
		bar.repaint();
	}

	@Override
	protected void doStop() throws Exception {
		bar.remove(skins);
		bar.revalidate();
		bar.repaint();
	}

	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED){
			JRadioButtonMenuItem radio = ((JRadioButtonMenuItem)e.getItem());
			model.setLafClass(radio.getName());
			setWindowSkin();
		}
	}
	
	private void setWindowSkin(){
		MainWindow w = Main.getMainWindow();
		try {
			UIManager.setLookAndFeel(model.getLafClass());
			SwingUtilities.updateComponentTreeUI(w);
			w.pack();
		} catch (Exception x){
			Logger.global.warning(x.getMessage());
		}
	}

	public Component getDisplay() {
		return settingsComponent.getDisplay();
	}

	public void saveActionPerformed() throws Exception {
		settingsComponent.restoreToModel();
		Serializer.serialize(model, Environment.getNtorrentDir());
	}

	public String toString() {
		return ResourcePool.getString("skins", "locale", this);
	}

}
