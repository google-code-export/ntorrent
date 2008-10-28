package ntorrent.settings.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SettingsComponentContainer{
	private static final long serialVersionUID = 1L;
	
	private final JPanel container = new JPanel(new GridLayout(0,1));
	private final JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private final HashMap<String,Component> map = new HashMap<String, Component>();
	
	public SettingsComponentContainer() {
		this.wrapper.add(container);
	}
	
	public JPanel getContainer() {
		return this.wrapper;
	}
	
	public void addSettingsComponent(String name, Component c){
		addSettingsComponent(name, null, c);
	}
	
	public void addSettingsComponent(String name, String label, Component c){
		this.map.put(name, c);
		if(label == null)
			label = name;
		this.container.add(new JLabel(label+":"));
		this.container.add(c);
	}
	
	public Component getComponent(String name){
		return map.get(name);
	}

}
