package tests.ntorrent.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import ntorrent.util.BencodeReader;

import static org.junit.Assert.*;
import org.junit.Test;

public class BencodeReaderTest {
	
	private BencodeReader initReader(Object s){
		return new BencodeReader(
				new BufferedInputStream(
						new StringBufferInputStream(s.toString())
						)
				);
	}
	
	private Object[] getString(String s){
		String encoded = s.length()+":"+s;
		return new Object[]{s,encoded};
	}
	
	private Object[] getInteger(long i){
		String encoded = "i"+i+"e";
		return new Object[]{i,encoded};
	}
	
	private Object[] getList(List<Object> list){
		String encoded = "";
		for(int x = 0; x < list.size(); x++){
			Object item = list.get(x);
			list.remove(x);
			list.add(x, getUnknown(item)[0]);
			encoded += getUnknown(item)[1];
		}
		return new Object[]{list,"l"+encoded+"e"};
	}
	
	private Object[] getDictionary(Map<String,Object> map) {
		String encoded = "";
		for(String key : map.keySet()){
			encoded += getString(key)[1];
			encoded += getUnknown(map.get(key))[1];
		}
		return new Object[]{map,"d"+encoded+"e"};
	}
	
	private Object[] getUnknown(Object o){
		if(o instanceof String){
			return getString((String) o);
		}else if(o instanceof Integer){
			return getInteger(((Integer)o).longValue());
		}else if (o instanceof Long){
			return getInteger((Long)o);
		}else if (o instanceof List){
			return getList((List<Object>)o);
		} else if(o.getClass().isArray()){
			return getList(createList((Object[]) o));
		}else if(o instanceof Map){
			return getDictionary((Map<String,Object>) o);
		}else{
			throw new IllegalArgumentException(o.getClass()+": was not a class of String, Integer, List or Map");
		}
	}
	
	@Test
	public void readStringTest(){
		Object[] o = getString("spam");
		BencodeReader reader = initReader(o[1]);
		try {
			assertEquals(o[0], reader.readString());
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void readIntegerTest(){
		Object[] o = getInteger(3);
		BencodeReader reader = initReader(o[1]);
		try {
			assertEquals(o[0], reader.readInteger());
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	private List<Object> createList(Object[] list){
		ArrayList<Object> result = new ArrayList<Object>();
		for(Object o : list){
			result.add(o);
		}
		return result;
	}
	
	private Map<String,Object> createMap(String[] keys, Object[] vals){
		HashMap<String, Object> result = new HashMap<String, Object>();
		for(int x = 0; x < keys.length; x++){
			result.put((String) getUnknown(keys[x])[0], getUnknown(vals[x])[0]);
		}
		return result;
	}
	
	@Test
	public void readListTest(){
		String[] slist = {"spam","eggs"};
		Object[] o = getList(createList(slist));
		BencodeReader reader = initReader(o[1]);
		try {
			List<Object> list = reader.readList();
			for(Object item : (Iterable<Object>)o[0]){
				assertTrue(list.contains(item));
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void readDictionaryTest(){
		String[] keys = {"cow","spam"};
		String[] vals = {"moo","eggs"};
		
		Object[] o = getDictionary(createMap(keys, vals));
		BencodeReader reader = initReader(o[1]);
		try {
			Map<String,Object> readMap = reader.readDictionary();
			Map<String,Object> argMap = (Map<String, Object>) o[0];
			assertEquals(argMap, readMap);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void readAllInOneTest(){
		String[] keys = {"id","publisher","publisher-webpage","publisher.location","info-list"};
		Object[] vals = {92382,"bob","www.example.com","home",new Object[]{"lis1","list2",3,createMap(new String[]{"key1","key2"}, new Object[]{"val1",534})}};
		
		Object[] o = getDictionary(createMap(keys, vals));
		BencodeReader reader = initReader(o[1]);
		
		try {
			Map<String,Object> readMap = reader.readDictionary();
			Map<String,Object> argMap = (Map<String, Object>) o[0];
			assertEquals(argMap, readMap);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void read64IntegerTest(){
		long i = Integer.MAX_VALUE;
		i++;
		BencodeReader reader = initReader("i"+i+"e");
		try {
			assertEquals(i, reader.readInteger());
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
}
