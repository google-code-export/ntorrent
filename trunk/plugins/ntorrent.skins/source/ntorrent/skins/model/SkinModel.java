package ntorrent.skins.model;

import java.io.Serializable;

import ntorrent.settings.model.SettingsExtension.UserSetting;

public class SkinModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@UserSetting
	private String lafClass = null;
	
	public void setLafClass(String lafClass) {
		this.lafClass = lafClass;
	}
	
	public String getLafClass() {
		return lafClass;
	}
}
