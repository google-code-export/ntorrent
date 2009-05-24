package ntorrent.tools;

import java.io.File;

import ntorrent.tools.Serializer;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SerializerTest {
	private final static SimpleObject simpleObject = new SimpleObject();
	private final static File path = new File("/tmp");
	
	@BeforeClass
	public static void init(){
		simpleObject.setData("test");
	}
	
	@Test
	public void SerializeSimpleClassTest(){
		try {
			Serializer.serialize(simpleObject, path);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void DeserializeSimpleClassTest(){
		try {
			SimpleObject object = Serializer.deserialize(SimpleObject.class, path);
			Assert.assertEquals("test", object.getData());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
}
