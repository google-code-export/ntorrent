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
package ntorrent.locale;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class gives access to locale sensitive data.
 * @author Kim Eik
 *
 */
public class ResourcePool {
	/** Locale specification **/
	private static Locale locale;
	
	/** Translations **/
	private final static HashMap<Integer,ResourceBundle> messages = new HashMap<Integer, ResourceBundle>();	
	public static String getString(String key, String bundle,Object obj) {
		ClassLoader loader = obj.getClass().getClassLoader();
		int id = loader.hashCode();
		try{
			if(!messages.containsKey(id)){
				messages.put(id, ResourceBundle.getBundle(bundle,locale,loader));
				Logger.global.info("Added new resource bundle to resource pool. ("+id+")");
			}
			return messages.get(id).getString(key);	
		}catch(MissingResourceException x){
			Logger.global.info("Unknown key ["+key+"] requested from class "+obj.getClass().getName()+" in resource pool with bundle="+bundle);
		}catch(Exception x){
			x.printStackTrace();
			//Logger.global.warning("Resource bundle "+bundle+" is missing from "+obj.getClass().getName()+"!");
		}
		return key;
	}
	
	public static Locale getLocale() {
		return locale;
	}
	
	public static void setLocale(String language,String country) {
		ResourcePool.locale = new Locale(language,country);
	}
	
}
