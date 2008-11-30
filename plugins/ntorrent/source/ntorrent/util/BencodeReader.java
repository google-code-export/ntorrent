package ntorrent.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BencodeReader {
	private final InputStream stream;
	
	/**
	 * Reads a bencoded stream with mark support.
	 * @throws IllegalArgumentException if mark is not supported.
	 * @param stream
	 */
	public BencodeReader(InputStream stream) {
		if(!stream.markSupported()){
			throw new IllegalArgumentException("inputstream must support mark.");
		}else{
			stream.mark(0);
		}
		this.stream = stream;
		
		//DEBUG
		/*int read;
		try {
			while((read = stream.read()) != -1){
				System.out.print((char)read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.out.println();
			try {
				this.stream.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}
	
	/**
	 * Returns the bencoded data as either a List of objects of type {String,int,List,Map} 
	 * or as a single object. It will first populate the result into a list, and return the list
	 * only if the list contains more than one element. else it will return the single object.
	 * @return Object
	 */
	public Object read(){
		final ArrayList<Object> result = new ArrayList<Object>();
		try {
			while(true){
				Object item = decode();
				if(item == null){
					/**
					 * if it cant find any of these bencoding identifiers, 
					 * then stop. metadata probably ended.
					 */
					break;
				}
				stream.mark(1);
				result.add(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if(result.size() == 1){
			return result.get(0);
		}
		return result;
		
	}
	
	/**
	 * decodes the current marked position, the character marked in the stream can either be
	 * a digit, i, l or d, if neither of these it will return null.
	 * @return Object
	 * @throws IOException
	 */
	public Object decode() throws IOException {
		stream.reset();
		char charBuf = (char)stream.read();
		stream.reset();
		if(Character.isDigit(charBuf)){
			//STRING
			return readString();
		}else if(charBuf == 'i'){
			//INTEGER
			return readInteger();
		}else if(charBuf == 'l'){
			//LIST
			return readList();
		}else if(charBuf == 'd'){
			//DICTIONARY
			return readDictionary();
		}else{
			return null;
		}
	}

	/**
	 * Reads a dictionary in the format d<key><value>...e
	 * @return
	 * @throws IOException
	 */
	public Map<String,Object> readDictionary() throws IOException {
		stream.skip(1); // skips d
		/** dictionary value **/
		HashMap<String,Object> dictionary = new HashMap<String, Object>();
		//read stream until e encountered.
		stream.mark(1);
		while((stream.read()) != 'e'){
			Object key = decode();
			stream.mark(1);
			Object value = decode();
			stream.mark(1);
			if(key == null)
				throw new IOException("key in dictionary was decoded to null!");
			dictionary.put(key.toString(),value);
		}
		return dictionary;
	}

	/**
	 * Reads a list in the format l<list-item>...e
	 * @return
	 * @throws IOException
	 */
	public List<Object> readList() throws IOException {
		stream.skip(1); // skips l
		/** list value **/
		ArrayList<Object> list = new ArrayList<Object>();
		//read stream until e encountered.
		stream.mark(1);
		while(stream.read() != 'e'){
			list.add(decode());
			stream.mark(1);
		}
		return list;
	}

	/**
	 * Reads an integer in the format i<integer>e
	 * @return long
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public long readInteger() throws NumberFormatException, IOException {
		stream.skip(1); // skips i
		/** integer value **/
		long value = 0;
		char charBuf;
		//read stream until e encountered.
		while((charBuf = (char)stream.read()) != 'e') {
			value *= 10;
			value += Long.parseLong(""+charBuf);
		};
		//add integer to list
		return value;
	}


	/**
	 * Reads a string of the format <length>:<string>
	 * @return String
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public String readString() throws NumberFormatException, IOException{
		/** String value **/
		char charBuf;
		//Get the string length
		long length = 0;
		//read stream until : encountered.
		while((charBuf = (char)stream.read()) != ':'){
			length *= 10;
			length += Long.parseLong(""+charBuf);
		}
		//read up to <length> chars from stream
		String value = new String();
		while(length > 0){
			value += (char)stream.read();
			length--;
		}
		//return
		return value;
	}
}
