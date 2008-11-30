package ntorrent.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
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
	private Object decode() throws IOException {
		stream.reset();
		char charBuf = (char)stream.read();
		if(Character.isDigit(charBuf)){
			//STRING
			stream.reset();
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
	 * @return
	 * @throws IOException
	 */
	private Map<Object,Object> readDictionary() throws IOException {
		/** dictionary value **/
		HashMap<Object,Object> dictionary = new HashMap<Object, Object>();
		//read stream until e encountered.
		stream.mark(1);
		while((stream.read()) != 'e'){
			Object key = decode();
			stream.mark(1);
			Object value = decode();
			stream.mark(1);
			dictionary.put(key,value);
		}
		return dictionary;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private List<Object> readList() throws IOException {
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
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private int readInteger() throws NumberFormatException, IOException {
		/** integer value **/
		int value = 0;
		char charBuf;
		//read stream until e encountered.
		while((charBuf = (char)stream.read()) != 'e') {
			value *= 10;
			value += Integer.parseInt(""+charBuf);
		};
		//add integer to list
		return value;
	}


	/**
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private String readString() throws NumberFormatException, IOException{
		/** String value **/
		char charBuf;
		//Get the string length
		int length = 0;
		//read stream until : encountered.
		while((charBuf = (char)stream.read()) != ':'){
			length *= 10;
			length += Integer.parseInt(""+charBuf);
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
	
	public static void main(String[] args) throws FileNotFoundException {
		StringBufferInputStream s1 = new StringBufferInputStream("10:hallohallo");
		StringBufferInputStream s2 = new StringBufferInputStream("i536e");
		StringBufferInputStream s3 = new StringBufferInputStream("5:halloi5e");
		StringBufferInputStream s4 = new StringBufferInputStream("li66e4:spam4:eggse");
		StringBufferInputStream s5 = new StringBufferInputStream("d9:publisher3:bob17:publisher-webpage15:www.example.com18:publisher.location4:homee");
		StringBufferInputStream s6 = new StringBufferInputStream("d4:spaml1:a1:bee");
		StringBufferInputStream s7 = new StringBufferInputStream("d3:cow3:moo4:spam4:eggse");
		StringBufferInputStream s8 = new StringBufferInputStream("l4:spam4:eggse");
		
		
		System.out.println(new BencodeReader(new BufferedInputStream(s1)).read());
		System.out.println(new BencodeReader(new BufferedInputStream(s2)).read());
		System.out.println(new BencodeReader(new BufferedInputStream(s3)).read());
		System.out.println(new BencodeReader(new BufferedInputStream(s4)).read());
		System.out.println(new BencodeReader(new BufferedInputStream(s5)).read());
		System.out.println(new BencodeReader(new BufferedInputStream(s6)).read());
		System.out.println(new BencodeReader(new BufferedInputStream(s7)).read());
		System.out.println(new BencodeReader(new BufferedInputStream(s8)).read());
		
	}
}
