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
package org.heldig.ntorrent.settings;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class Settings implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Description {
		String value();
	}
	
	public static boolean serialize(File settingsFile, Object obj) throws IOException, IllegalArgumentException, IllegalAccessException{
		settingsFile.createNewFile();
		FileOutputStream stream = new FileOutputStream(settingsFile);
		ObjectOutputStream objectstream = new ObjectOutputStream(stream);
		
		objectstream.writeObject(obj);
		
		objectstream.close();
		return true;
	}

	@SuppressWarnings("unchecked")
	public static boolean deserialize(File settingsFile, Object obj) throws IOException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		if(settingsFile.exists()){
			FileInputStream stream = new FileInputStream(settingsFile);
			ObjectInputStream objectstream = new ObjectInputStream(stream);			
			Object data = objectstream.readObject();
			
			
			
			if(data instanceof Settings)
				((Settings)obj).restoreData(data);
			else
				return false;
			
			objectstream.close();
		}
		return settingsFile.exists();
	}
	
	protected abstract void restoreData(Object obj);
	
}

