package ntorrent;

import java.io.File;
import java.io.Serializable;
import java.util.Locale;

import ntorrent.settings.model.SettingsExtension.UserSetting;

public class NtorrentSettingsModel implements Serializable {
	/** the users ntorrent dir **/
	//TODO Make home ntorrent folder user selectable
	private File ntorrent = new File(System.getProperty("user.home")+"/.ntorrent/");
	
	/** Internal communication port **/
	@UserSetting(label="settings.internal-socket-port")
	private int intSocketPort = 4050;

	@UserSetting(label="settings.user-language")
	private String userLanguage = Locale.getDefault().getLanguage();
	
	@UserSetting(label="settings.user-country")
	private String userCountry = Locale.getDefault().getCountry();

	public int getIntSocketPort() {
		return intSocketPort;
	}
	
	/**
	 * Returns the ntorrent user dir
	 * @return
	 */
	public File getNtorrent() {
		return ntorrent;
	}
	
	public String getUserCountry() {
		return userCountry;
	}
	
	public String getUserLanguage() {
		return userLanguage;
	}
	
	public void setIntSocketPort(int intSocketPort) {
		this.intSocketPort = intSocketPort;
	}
	
	public void setUserCountry(String userCountry) {
		this.userCountry = userCountry;
	}
	
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}
	
}
