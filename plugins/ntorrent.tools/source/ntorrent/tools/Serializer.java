package ntorrent.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import ntorrent.NtorrentApplication;
import ntorrent.NtorrentSettingsModel;
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
	 * Serializes an object and gives and stores the output file in the default
	 * ntorrent dir.
	 * @param obj
	 * @throws IOException
	 */
	public static void serialize(Serializable obj) throws IOException{
		serialize(obj, NtorrentApplication.SETTINGS.getNtorrent(),getClassName(obj.getClass()));
	}
	
	/**
	 * Serializes an object and gives the output file a name. and stores the file in 
	 * the default ntorrent dir.
	 * @param obj
	 * @param name
	 * @throws IOException
	 */
	public static void serialize(Serializable obj, String name) throws IOException{
		serialize(obj, NtorrentApplication.SETTINGS.getNtorrent(), name);
	}
	
	/**
	 * Serializes a object and stores it as the objects classname in the given parent folder.
	 * @param obj
	 * @param parent
	 * @throws IOException
	 */
	public static void serialize(Serializable obj, File parent) throws IOException{
		serialize(obj, parent,getClassName(obj.getClass()));
	}
	
	/**
	 * Saves a serializable object to the parent directory/data/$[getClassName(Class obj)]
	 * @param obj
	 * @param parent
	 * @throws IOException
	 */
	public static void serialize(Serializable obj, File parent, String name) throws IOException{
		File file = new File(parent,prefix+name+postfix);
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
	 * @return 
	 * @return Object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> T deserialize(Class<T> obj, File parent, String name){
		try {
			File file = new File(parent,prefix+name+postfix);
			if(file.exists()){
				FileInputStream stream = new FileInputStream(file);
				ObjectInputStream objectstream = new ObjectInputStream(stream);			
				return (T) objectstream.readObject();
			}
			return null;
		} catch (IOException e) {
			throw new RuntimeException("Error on reading serialized class",e);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static <T> T deserialize(Class<T> obj) {
		return deserialize(obj, NtorrentApplication.SETTINGS.getNtorrent(),getClassName(obj));
	}
	
	public static <T> T deserialize(Class<T> obj, String name) {
		return deserialize(obj, NtorrentApplication.SETTINGS.getNtorrent(),name);
	}
	
	public static <T> T deserialize(Class<T> obj, File parent){
		return deserialize(obj, parent,getClassName(obj));
	}
	
	/**
	 * Generates a lowercase classname with the postfix .dat
	 * @param obj
	 * @return
	 */
	protected static String getClassName(Class obj){
		return obj.getName().toLowerCase();
	}
	
}

