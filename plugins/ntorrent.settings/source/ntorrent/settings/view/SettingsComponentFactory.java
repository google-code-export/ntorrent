package ntorrent.settings.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.CharConversionException;
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
import javax.swing.SwingConstants;

import ntorrent.settings.model.SettingsExtension.UserSetting;

public class SettingsComponentFactory {
	
	private final Object model;
	private final Class clazz;
	private final SettingsComponentContainer container = new SettingsComponentContainer();

	public SettingsComponentFactory(final Object model) throws IllegalArgumentException, IllegalAccessException {
		this.model = model;
		this.clazz = model.getClass();
		generateDisplayFromReflection();
	}
	
	public JPanel getDisplay() {
		return container.getContainer();
	}
	
	/**
	 * Creates a simple display based on an object through reflection, the fields in this object
	 * having the notation @UserSetting assosciated will be populated in this gui.
	 * @param object
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private final void generateDisplayFromReflection() throws IllegalArgumentException, IllegalAccessException{

		for(Field f : clazz.getDeclaredFields()){
			try {
				if(methodExist(f)){
					Method getMethod = getMethods(f.getName())[0];
					UserSetting userSetting = f.getAnnotation(UserSetting.class);
					String label = userSetting.label();
					if (label.equals("")){
						label = null;
					}
					container.addSettingsComponent(f.getName(),label, getJComponentFromClass(f,getMethod));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public final void restoreToModel(){
		for(Field f : clazz.getDeclaredFields()){
			try {
				if(methodExist(f)){
					Method[] methods = getMethods(f.getName());
					Method getMethod = methods[0];
					Method setMethod = methods[1];
					setMethod.invoke(this.model,getValueFromComponent(f.getName(),getMethod.getReturnType()));
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public final <T> T getValueFromComponent(String name, Class<T> type){
		Object value = null;
		Component c = container.getComponent(name);
		if(c != null){
			if(c instanceof JCheckBox){
				boolean b = ((JCheckBox)c).isSelected();
				if(type.equals(Byte.class) || type.equals(byte.class))
					value = b ? (byte)1 : (byte)0;
				else
					value = b;
			}else if(c instanceof JSpinner){
				value = ((JSpinner)c).getValue();
			}else if(c instanceof JComboBox){
				value = ((JComboBox)c).getSelectedItem();
			}else if(c instanceof JTextField) {
				String text = ((JTextField)c).getText();
				if(type.equals(char.class) || type.equals(Character.class))
					value = text.length() == 1 ? new Character(text.charAt(0)) : null;
				else
					value = ((JTextField)c).getText();
			}
		}		
		return (T)value;
	}
	
	private final boolean methodExist(Field f) throws IllegalArgumentException, SecurityException, IllegalAccessException{
		try {
			Method[] methods = getMethods(f.getName());
			return methods != null && f.isAnnotationPresent(UserSetting.class);
		} catch (NoSuchFieldException e) {
			return false;
		}
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
	private final Method[] getMethods(final String property) throws IllegalArgumentException, NoSuchFieldException, SecurityException, IllegalAccessException{;
		try {
			final Class propertyType = clazz.getDeclaredField(property).getType();
			Method getMethod;
			if(propertyType.equals(boolean.class)){
				 getMethod = clazz.getDeclaredMethod("is"+firstCharUpper(property), new Class[]{});
			}else{
				 getMethod = clazz.getDeclaredMethod("get"+firstCharUpper(property), new Class[]{});
			}
			final Method setMethod = clazz.getDeclaredMethod("set"+firstCharUpper(property),new Class[]{propertyType});
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
	private final JComponent getJComponentFromClass(Field f,Method getMethod) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class fieldClass = f.getType();
		Object rawField = getMethod.invoke(this.model, new Object[]{});
		JComponent component = null;
		
		if(fieldClass.equals(Byte.TYPE)){
			JCheckBox c = new JCheckBox();
			c.setHorizontalTextPosition(SwingConstants.LEFT);
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
			c.setHorizontalTextPosition(SwingConstants.LEFT);
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
			return component;
		}
	}
}
