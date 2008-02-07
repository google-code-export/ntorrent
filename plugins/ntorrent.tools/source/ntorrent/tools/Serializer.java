package ntorrent.tools;


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
	
	public static void serialize(Serializable obj, File parent) throws IOException{
		File file = new File(parent,"data/"+(getClassName(obj.getClass())));
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		ObjectOutputStream objectout = new ObjectOutputStream(out);
		objectout.writeObject(obj);
		objectout.close();
	}
	
	@SuppressWarnings("unchecked")
	public static Object deserialize(Class obj, File parent) throws IOException, ClassNotFoundException{
		File file = new File(parent,"data/"+(getClassName(obj)));
		file.getParentFile().mkdirs();
		FileInputStream stream = new FileInputStream(file);
		ObjectInputStream objectstream = new ObjectInputStream(stream);			
		return objectstream.readObject();
		
	}
	
	protected static String getClassName(Class obj){
		return obj.getName().toLowerCase()+".dat";
	}

	
}

