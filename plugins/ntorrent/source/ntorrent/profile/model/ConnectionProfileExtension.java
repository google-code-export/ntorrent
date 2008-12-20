package ntorrent.profile.model;

import java.awt.Component;

public interface ConnectionProfileExtension<T extends ConnectionProfile> {	
	/**
	 * @return
	 */
	public T getConnectionProfile();
	
	/**
	 * @return
	 */
	public Component getDisplay();
}
