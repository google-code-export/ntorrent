package ntorrent.plugins;

import java.awt.Component;

import ntorrent.NtorrentApplication;
import ntorrent.plugins.model.PluginList;
import ntorrent.plugins.view.PluginListView;
import ntorrent.settings.model.SettingsExtension;
import ntorrent.tools.Serializer;

import org.apache.log4j.Logger;
import org.java.plugin.PluginLifecycleException;


public class NTorrentPlugins implements SettingsExtension {
	
	private static PluginList model;
	
	private final PluginListView view = new PluginListView();
	
	private final static Logger log = Logger.getLogger(NTorrentPlugins.class);

	static {
		try{
			model = Serializer.deserialize(PluginList.class);
		}catch (RuntimeException e) {
			log.warn(e.getMessage(), e);
			model = new PluginList();
		}
		
		if(model == null)
			model = new PluginList();
	}
	
	public static void restoreSettings() throws PluginLifecycleException{
		for(String id : model.getDisabledPlugins()){
			NtorrentApplication.MANAGER.deactivatePlugin(id);
		}
		
		for(String id : model.getEnabledPlugins()){
			NtorrentApplication.MANAGER.activatePlugin(id);
		}
	}

	@Override
	public Component getSettingsDisplay() {
		return view;
	}

	@Override
	public String getSettingsDisplayName() {
		return "plugins";
	}

	@Override
	public void saveActionPerformedOnSettings() throws Exception {
		log.info("saving plugin state");
		model.saveState(NtorrentApplication.MANAGER);
	}

}
