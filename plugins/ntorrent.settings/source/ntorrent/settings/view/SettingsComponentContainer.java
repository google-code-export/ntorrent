package ntorrent.settings.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SettingsComponentContainer{
	private static final long serialVersionUID = 1L;
	
	private final JPanel container = new JPanel(new GridBagLayout());
	private final JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private final HashMap<String,Component> map = new HashMap<String, Component>();
	private final GridBagConstraints constraint = new GridBagConstraints();
	private int x = 0;
	private int y = 0;
	
	public SettingsComponentContainer() {
		this.wrapper.add(container);
		this.constraint.fill = GridBagConstraints.HORIZONTAL;
		this.constraint.weightx = 1;
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
		this.constraint.gridy = this.y++;
		this.constraint.gridx = this.x++;
		this.constraint.gridwidth=1;
		this.container.add(new JLabel(label+":"),this.constraint);
		this.constraint.gridx = this.x++;
		this.container.add(c,this.constraint);
		this.x=0;
	}
	
	public Component getComponent(String name){
		return map.get(name);
	}

}
