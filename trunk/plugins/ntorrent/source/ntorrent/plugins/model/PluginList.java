package ntorrent.plugins.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

import ntorrent.tools.Serializer;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.PluginDescriptor;

public class PluginList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final HashSet<String> enabledPlugins = new HashSet<String>();
	private final HashSet<String> disabledPlugins = new HashSet<String>();
	
	public void saveState(PluginManager manager) throws IOException {
		for(PluginDescriptor pd : manager.getRegistry().getPluginDescriptors()){
			if(manager.isPluginActivated(pd)){
				enabledPlugins.add(pd.getId());
			}else{
				disabledPlugins.add(pd.getId());
			}
		}
		Serializer.serialize(this);
	}
	
	public HashSet<String> getDisabledPlugins() {
		return disabledPlugins;
	}
	
	public HashSet<String> getEnabledPlugins() {
		return enabledPlugins;
	}

}
