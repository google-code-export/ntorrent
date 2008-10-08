package ntorrent.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
/**
 * This class takes care of serializing and deserializing class states, and is ment for a tool for
 * plugins that needs saving and restorations of class states.
 * @author Kim Eik
 *
 */
@SuppressWarnings("unchecked")
public abstract class Serializer {
	private static final long serialVersionUID = 1L;
	private static final String prefix = "data/";
	private static final String postfix = ".dat";
	
	/**
	 * Saves a serializable object to the parent directory/data/$[getClassName(Class obj)]
	 * @param obj
	 * @param parent
	 * @throws IOException
	 */
	public static void serialize(Serializable obj, File parent) throws IOException{
		File file = new File(parent,prefix+(getClassName(obj.getClass())));
		if(!file.exists()){
			if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);
		ObjectOutputStream objectout = new ObjectOutputStream(out);
		objectout.writeObject(obj);
		objectout.close();
	}
	
	/**
	 * Restores a serialized object.
	 * see also serialize()
	 * @param obj
	 * @param parent
	 * @return Object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserialize(Class obj, File parent) throws IOException, ClassNotFoundException{
		File file = new File(parent,prefix+(getClassName(obj)));
		if(file.exists()){
			FileInputStream stream = new FileInputStream(file);
			ObjectInputStream objectstream = new ObjectInputStream(stream);			
			return objectstream.readObject();
		}
		throw new FileNotFoundException("Can't deserialize a nonexistant file!");
		
	}
	
	/**
	 * Generates a lowercase classname with the postfix .dat
	 * @param obj
	 * @return
	 */
	protected static String getClassName(Class obj){
		return obj.getName().toLowerCase()+postfix;
	}

	
}

