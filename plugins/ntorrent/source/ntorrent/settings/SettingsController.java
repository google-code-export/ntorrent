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
package ntorrent.settings;

import java.util.HashSet;

import ntorrent.NtorrentApplication;
import ntorrent.plugins.NTorrentPlugins;
import ntorrent.settings.model.SettingsExtension;
import ntorrent.settings.view.SettingsWindow;

import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;

/**
 * @author Kim Eik
 *
 */
public class SettingsController {
	private final PluginManager manager;
	private final ExtensionPoint ext;
	
	public SettingsController() {
		this.manager = NtorrentApplication.MANAGER;
		this.ext = manager.getRegistry().getExtensionPoint("ntorrent@SettingsExtension");
	}
	
	public void drawWindow(){
		HashSet<SettingsExtension> set = new HashSet<SettingsExtension>();
		try {
			for(Extension e : ext.getAvailableExtensions()){
				PluginDescriptor pd = e.getDeclaringPluginDescriptor();
				//if(manager.isPluginActivated(pd)){
					Plugin plugin = manager.getPlugin(pd.getId());
					if(plugin instanceof SettingsExtension){
						set.add((SettingsExtension) plugin);
					}
				//}
			}
		} catch (PluginLifecycleException x) {
			x.printStackTrace();
		}
		set.add(new NTorrentPlugins());
		SettingsExtension[] array = new SettingsExtension[set.size()];
		set.toArray(array);
		SettingsWindow window = new SettingsWindow(array);
		window.validate();
		window.pack();
		window.setSize(640, 480);
		window.setVisible(true);
	}
}
