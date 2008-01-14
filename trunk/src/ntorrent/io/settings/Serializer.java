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
package ntorrent.io.settings;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import ntorrent.Main;

public abstract class Serializer {
	private static final long serialVersionUID = 1L;
	
	public static void serialize(Serializable obj) throws IOException{
		File file = new File(Main.ntorrent,(obj.getClass().getSimpleName()+".dat").toLowerCase());
		FileOutputStream out = new FileOutputStream(file);
		ObjectOutputStream objectout = new ObjectOutputStream(out);
		objectout.writeObject(obj);
		objectout.close();
	}
	
	@SuppressWarnings("unchecked")
	public static Object deserialize(Class obj) throws IOException, ClassNotFoundException{
		File file = new File(Main.ntorrent,(obj.getSimpleName()+".dat").toLowerCase());
		FileInputStream stream = new FileInputStream(file);
		ObjectInputStream objectstream = new ObjectInputStream(stream);			
		return objectstream.readObject();
		
	}

	
}

