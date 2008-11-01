package ntorrent.settings;

import java.awt.Component;

import ntorrent.locale.ResourcePool;
import ntorrent.settings.model.SettingsExtension;
import ntorrent.settings.view.SettingsComponentFactory;

import org.java.plugin.Plugin;

public abstract class DefaultSettingsImpl<T> extends Plugin implements SettingsExtension {
	private final SettingsComponentFactory scf;
	private final T model; 
	
	@SuppressWarnings("unchecked")
	public DefaultSettingsImpl(T model){
		try {
			if(model == null)
				this.model = (T) model.getClass().newInstance();
			else
				this.model = model;
			this.scf = new SettingsComponentFactory(model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected T getModel(){
		return this.model;
	}
	
	@Override
	public Component getDisplay() {
		return scf.getDisplay();
	}

	@Override
	public void saveActionPerformed() throws Exception {
		scf.restoreToModel();
	}
	
	public String toString() {
		return ResourcePool.getString("settings.name", "locale", this);
	}

}
