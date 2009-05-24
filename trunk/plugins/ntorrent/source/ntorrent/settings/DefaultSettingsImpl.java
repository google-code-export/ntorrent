package ntorrent.settings;

import java.awt.Component;

import ntorrent.locale.ResourcePool;
import ntorrent.settings.model.SettingsExtension;
import ntorrent.settings.view.SettingsComponentFactory;

import org.java.plugin.Plugin;

public abstract class DefaultSettingsImpl<T> extends Plugin implements SettingsExtension {
	private final SettingsComponentFactory scf;
	protected final T model; 
	
	@SuppressWarnings("unchecked")
	public DefaultSettingsImpl(T model, Class<T> c){
		try {
			if(model == null){
				this.model = c.newInstance();
			}else{
				this.model = model;
			}
			this.scf = new SettingsComponentFactory(this.model);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}
	
	protected T getModel(){
		return this.model;
	}
	
	@Override
	public Component getSettingsDisplay() {
		return scf.getDisplay();
	}

	@Override
	public void saveActionPerformedOnSettings() throws Exception {
		scf.restoreToModel();
	}
	
	/**
	 * This method should be overidden.
	 */
	@Override
	public String getSettingsDisplayName() {
		return this.getDescriptor().getId();
	}

}
