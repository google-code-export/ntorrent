package ntorrent.settings;

import ntorrent.settings.view.SettingsComponentFactory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SettingsComponentFactoryTest {
	private final static TestModel model = new TestModel();
	private final static TestModel controlModel = new TestModel();
	private static SettingsComponentFactory factory;
	
	@BeforeClass
	public final static void init() throws IllegalArgumentException, IllegalAccessException{
		factory = new SettingsComponentFactory(model);
		model.changeValues();
	}
	
	@Test
	public void testRestoreToModel() throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException{
		factory.restoreToModel();
		Object v = null;
		String[] sarray = {"character","byt3","sh0rt","iint","l0ng","fl0at","duble","e","string"};
		for(String s : sarray){
			Object value = controlModel.getValueFromName(s);
			v = factory.getValueFromComponent(s,value.getClass());
			Assert.assertEquals(value,v);
		}
	}
	
	
}
