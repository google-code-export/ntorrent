package ntorrent.skins.model;

import java.io.Serializable;

import javax.swing.LookAndFeel;
import javax.swing.UIManager.LookAndFeelInfo;

import ntorrent.settings.model.SettingsExtension.UserSetting;

public class SkinModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@UserSetting(oneOf="ntorrent.skins.model.ComboBoxSkinModel")
	private PrettyLookAndFeelInfo lafClass = null;
	
	public PrettyLookAndFeelInfo getLafClass() {
		return lafClass;
	}
	
	public void setLafClass(PrettyLookAndFeelInfo lafClass) {
		this.lafClass = lafClass;
	}
}
