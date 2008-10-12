package ntorrent.settings.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import ntorrent.settings.model.SettingsExtension.UserSetting;

public class SettingsComponentFactory {
	/**
	 * Creates a simple display based on an object through reflection, the fields in this object
	 * having the notation @UserSetting assosciated will be populated in this gui.
	 * @param object
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public final static Component generateDisplayFromReflection(final Object object) throws IllegalArgumentException, IllegalAccessException{
		Class c = object.getClass();
		GridLayout layout = new GridLayout(0,2);
		layout.setHgap(30);
		JPanel panel = new JPanel(layout);
		JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
		wrapper.add(panel);
		for(Field f : c.getDeclaredFields()){
			try {
				Method[] methods = getMethods(f.getName(), object);
				if(methods != null){
					if(f.isAnnotationPresent(UserSetting.class)){
						UserSetting userSetting = f.getAnnotation(UserSetting.class);
						String label = userSetting.label();
						if (label == null || label.equals("")){
							label = f.getName();
						}
						panel.add(new JLabel(label+":"));
						panel.add(getJComponentFromClass(f,methods[0],object));
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return wrapper;
		
	}
	
	/**
	 * Converts a string to have the first char uppercase.
	 * @param s
	 * @return
	 */
	private final static String firstCharUpper(final String s){
		final String end = s.toLowerCase().substring(1);
		final String result = s.substring(0, 1).toUpperCase().concat(end);
		return result;
	}
		
	/**
	 * @param property
	 * @param reference
	 * @return
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private final static Method[] getMethods(final String property, final Object reference) throws IllegalArgumentException, NoSuchFieldException, SecurityException, IllegalAccessException{
		final Class c = reference.getClass();
		try {
			final Class propertyType = c.getDeclaredField(property).getType();
			Method getMethod;
			if(propertyType.equals(boolean.class)){
				 getMethod = c.getDeclaredMethod("is"+firstCharUpper(property), new Class[]{});
			}else{
				 getMethod = c.getDeclaredMethod("get"+firstCharUpper(property), new Class[]{});
			}
			final Method setMethod = c.getDeclaredMethod("set"+firstCharUpper(property),new Class[]{propertyType});
			return new Method[]{getMethod,setMethod};
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	/**
	 * Gets a JComponent that coincides with the field type in a class.
	 * @param f
	 * @param getMethod
	 * @param reference
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private final static JComponent getJComponentFromClass(Field f,Method getMethod,Object reference) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class fieldClass = f.getType();
		Object rawField = getMethod.invoke(reference, new Object[]{});
		JComponent component = null;
		
		if(fieldClass.equals(Byte.TYPE)){
			JCheckBox c = new JCheckBox();
			c.setName(f.getName());
			c.setSelected((Byte)rawField == 1);
			component = c;
		}else if(fieldClass.equals(Short.TYPE) || 
				fieldClass.equals(Integer.TYPE) ||
				fieldClass.equals(Long.TYPE) ||
				fieldClass.equals(Float.TYPE)|| 
				fieldClass.equals(Double.TYPE)){
			JSpinner c = new JSpinner();
			c.setName(f.getName());
			c.setValue(rawField);
			component = c;
		}else if(fieldClass.equals(Character.TYPE)){
			JTextField c = new JTextField(1);
			c.setName(f.getName());
			c.setText(((Character)rawField).toString());
			component = c;
		}else if(fieldClass.equals(String.class)){
			JTextField c = new JTextField((String) rawField);
			c.setName(f.getName());
			component = c;
		}else if(fieldClass.equals(Boolean.TYPE)){
			JCheckBox c = new JCheckBox();
			c.setName(f.getName());
			c.setSelected((Boolean) rawField);
			component = c;
		}else if(rawField instanceof Enum){
			JComboBox c = new JComboBox(fieldClass.getEnumConstants());
			c.setName(f.getName());
			c.setSelectedItem(rawField);
			component = c;
		}
		if(component == null){
			return new JLabel("UNSUPPORTED FIELDTYPE:"+fieldClass);
		}else{
			LayoutManager layout = new FlowLayout(FlowLayout.LEFT);
			JPanel wrapper = new JPanel(layout);
			wrapper.add(component);
			return wrapper;
		}
	}
}
