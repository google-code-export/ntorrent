/**
 * 
 */
package org.heldig.ntorrent.language;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Action;

import org.heldig.ntorrent.NTorrent;




/**
 * @author Kim Eik
 *
 */

public enum Language implements Action {
		Profile_Window_profile,
		Profile_protocol,
		Profile_host,
		Profile_connection_port,
		Profile_socket_port,
		Profile_mountpoint,
		Profile_username,
		Profile_password,
		Profile_remember_password,
		Profile_connect,
		Profile_save_profile,
		Profile_delete_profile,
		Menu_file,
		Menu_File_add_torrent,
		Menu_File_add_url,
		Menu_File_start_all,
		Menu_File_stop_all,
		Menu_File_quit,
		Menu_help,
		Menu_Help_settings,
		Menu_Help_about,
		Menu_Help_About_content,
		Viewlist_main,
		Viewlist_started,
		Viewlist_stopped,
		Viewlist_complete,
		Viewlist_incomplete,
		Viewlist_hashing,
		Viewlist_seeding,
		Torrent_Table_Col_name,
		Torrent_Table_Col_size,
		Torrent_Table_Col_downloaded,
		Torrent_Table_Col_uploaded,
		Torrent_Table_Col_seeders,
		Torrent_Table_Col_leechers,
		Torrent_Table_Col_download_rate,
		Torrent_Table_Col_upload_rate,
		Torrent_Table_Col_percent,
		Torrent_Table_Col_ratio,
		Torrent_Table_Col_priority,
		Torrent_Menu_start,
		Torrent_Menu_stop,
		Torrent_Menu_remove_torrent,
		Torrent_Menu_check_hash,
		Torrent_Menu_Priority_set_priority,
		Priority_Menu_high,
		Priority_Menu_low,
		Priority_Menu_normal,
		Priority_Menu_off,
		Torrent_Menu_Local_local,
		Local_Menu_open_file,
		Local_Menu_remove_data,
		Ssh_Menu_copy,
		Ssh_Menu_remove_data,
		Torrent_Menu_Ssh_ssh,
		Filetab_info,
		Filetab_file_list,
		Filetab_tracker_list,
		Filetab_log,
		Statusbar_throttle,
		Statusbar_up,
		Statusbar_down,
		Statusbar_port,
		Statusbar_ping,
		Label_none, 
		Local_Menu_local, 
		Ssh_Menu_ssh,
		Tracker_List_Menu_enable,
		Tracker_List_Menu_disable,
		File_List_Menu_set_priority,
		File_List_Menu_high,
		File_List_Menu_low,
		File_List_Menu_off,
		Torrent_Menu_Priority_set_label, 
		Label_new_label, 
		Label_custom
		;
	
	final static LanguageReader language = new LanguageReader();
	HashMap<String, Object> actionMap = new HashMap<String, Object>();
	Vector<PropertyChangeListener> propListeners = new Vector<PropertyChangeListener>();
	
	Language() {
		putValue("ActionCommandKey", this.name());
	}
	
	public void storeWord(String word){
		language.setProperty(this.name(), word);
		language.store();
	}
	
	private final static String firstCharToUpperCase(String s){
		String result = s.substring(0,1).toUpperCase();
		result += s.substring(1);
		return result;
	}
	
	@Override
	public String toString() {
		String key = this.name();
		String word = language.getProperty(key);
		
		if(word == null){
			word = toRawString(key);
			language.put(key,word);
		}
		return word;
	}
	
	public final static String toRawString(String s){
		String raw = "";
		for(String substring : s.split("_"))
			if(substring.charAt(0) >= 'a' && substring.charAt(0) <= 'z')
				raw += (raw == "" ? firstCharToUpperCase(substring)  : " "+substring);
		return raw;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propListeners.add(listener);
		
	}

	@Override
	public Object getValue(String key) {
		if(key.equals("Name"))
			return this.toString();
		return actionMap.get(key);
	}

	@Override
	public boolean isEnabled() {
		Object b = actionMap.get("enabled");
		return (b == null ? true : (Boolean)b);
	}

	@Override
	public void putValue(String key, Object value) {
		actionMap.put(key, value);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propListeners.remove(listener);
	}

	@Override
	public void setEnabled(boolean b) {
		actionMap.put("enabled", b);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(NTorrent.settings.debug)
			System.out.println(e.paramString());
	}
	
	public static Language getFromString(String s){
		for(Language e : values())
			if(e.name().equals(s))
				return e;
		return null;
	}
	
	public static void main(String[] args){
		System.out.println(org.heldig.ntorrent.settings.Constants.getLicense());
		for(Language l : values())
			l.storeWord(l.toString());
	}

}

