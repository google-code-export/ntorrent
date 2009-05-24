package ntorrent.plugins.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import ntorrent.NtorrentApplication;

import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.EventListener;
import org.java.plugin.registry.PluginDescriptor;

public class PluginListView extends JPanel implements EventListener,ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PluginManager manager;
	private final DefaultListModel disabledPluginsModel = new DefaultListModel();
	private final DefaultListModel enabledPluginsModel = new DefaultListModel();
	private final JList disabledList = new JList(disabledPluginsModel);
	private final JList enabledList = new JList(enabledPluginsModel);
	private final JPanel buttonPanel = new JPanel(new GridLayout(0,1));
	private final JButton left = new JButton("<");
	private final JButton right = new JButton(">");

	public PluginListView() {
		this.manager = NtorrentApplication.MANAGER;
		for(PluginDescriptor pd : manager.getRegistry().getPluginDescriptors()){
			if(manager.isPluginActivated(pd)){
				enabledPluginsModel.add(enabledPluginsModel.size(),pd);
			}else{
				disabledPluginsModel.add(disabledPluginsModel.size(),pd);
			}
		}
		
		this.setLayout(new BorderLayout());
		this.buttonPanel.add(right);
		this.buttonPanel.add(left);
		
		this.left.addActionListener(this);
		this.right.addActionListener(this);
		
		JScrollPane disabledListScroller = new JScrollPane(disabledList);
		JScrollPane enabledListScroller = new JScrollPane(enabledList);
		
		ListCellRenderer cellRenderer = new DefaultListCellRenderer(){

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if(!(value instanceof PluginDescriptor))
					throw new IllegalArgumentException("expected PluginDescriptor got "+value.getClass());
				return super.getListCellRendererComponent(list, ((PluginDescriptor)value).getUniqueId(), index, isSelected, cellHasFocus);
			}
		};
		
		this.disabledList.setLayoutOrientation(JList.VERTICAL);
		this.disabledList.setCellRenderer(cellRenderer);
		this.enabledList.setLayoutOrientation(JList.VERTICAL);
		this.enabledList.setCellRenderer(cellRenderer);

		this.add(disabledListScroller,BorderLayout.WEST);
		this.add(enabledListScroller,BorderLayout.EAST);
		this.add(buttonPanel,BorderLayout.CENTER);
		
		manager.registerListener(this);
	}
	

	@Override
	public void pluginActivated(Plugin plugin) {
		this.enabledPluginsModel.add(this.enabledPluginsModel.size(),plugin.getDescriptor());
		this.disabledPluginsModel.removeElement(plugin.getDescriptor());
	}

	@Override
	public void pluginDeactivated(Plugin plugin) {
		this.disabledPluginsModel.add(this.disabledPluginsModel.size(),plugin.getDescriptor());
		this.enabledPluginsModel.removeElement(plugin.getDescriptor());
	}

	@Override
	public void pluginDisabled(PluginDescriptor plugin) {

		
	}

	@Override
	public void pluginEnabled(PluginDescriptor plugin) {
	
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == this.left){
			disablePlugin();
		}else if(event.getSource() == this.right){
			enablePlugin();
		}
	}
	
	private void disablePlugin(){
		PluginDescriptor pd  = (PluginDescriptor) this.enabledList.getSelectedValue();
		manager.deactivatePlugin(pd.getId());
		manager.disablePlugin(pd);
	}
	
	private void enablePlugin(){
		PluginDescriptor pd  = (PluginDescriptor) this.disabledList.getSelectedValue();
		manager.enablePlugin(pd,true);
		try {
			manager.activatePlugin(pd.getId());
		} catch (PluginLifecycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
