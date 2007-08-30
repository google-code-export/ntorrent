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
package ntorrent.settings;


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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Settings implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Description {
		String value();
	}
	
	protected void serialize(File settingsFile, Object obj) throws IOException, IllegalArgumentException, IllegalAccessException{
		settingsFile.createNewFile();
		FileOutputStream stream = new FileOutputStream(settingsFile);
		ObjectOutputStream objectstream = new ObjectOutputStream(stream);
		for(Field f : this.getClass().getDeclaredFields()){
			if(!Modifier.isFinal(f.getModifiers()) && Modifier.isPublic(f.getModifiers())){
				String type = f.getType().getSimpleName();

				if(type.equals("String"))
					objectstream.writeUTF((String)f.get(obj));
				else if(type.equals("boolean"))
					objectstream.writeBoolean(f.getBoolean(obj));
				else if(type.equals("File"))
					objectstream.writeObject(f.get(obj));

			}
		}
		objectstream.close();  
	}

	protected boolean deserialize(File settingsFile, Object obj) throws IOException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		if(settingsFile.exists()){
			FileInputStream stream = new FileInputStream(settingsFile);
			ObjectInputStream objectstream = new ObjectInputStream(stream);
			for(Field f : this.getClass().getDeclaredFields())
				if(!Modifier.isFinal(f.getModifiers()) && Modifier.isPublic(f.getModifiers())){
					String type = f.getType().getSimpleName();

					if(type.equals("String"))
						f.set(obj, objectstream.readUTF());
					else if(type.equals("boolean"))
						f.setBoolean(obj, objectstream.readBoolean());
					else if(type.equals("File"))
						f.set(obj,(File)objectstream.readObject());

				}
			objectstream.close();
		}
		return settingsFile.exists();
	}
}

