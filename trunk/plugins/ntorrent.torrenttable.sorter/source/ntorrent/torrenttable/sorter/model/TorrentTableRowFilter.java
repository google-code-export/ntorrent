package ntorrent.torrenttable.sorter.model;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.DefaultRowSorter;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;

import ntorrent.env.Environment;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableModel;

@SuppressWarnings("unchecked")
public class TorrentTableRowFilter extends RowFilter<TorrentTableModel,Torrent> implements KeyListener, TorrentTableFilterExtensionPoint {
	private final TorrentTableRowSorter sorter;
	
	Vector<RowFilter> extensions = new Vector<RowFilter>();
	String nameFilter = "";
	
	
	public TorrentTableRowFilter(TorrentTableRowSorter r) {
		sorter = r;
		sorter.setRowFilter(this);
		
		PluginManager manager = Environment.getPluginManager();
		ExtensionPoint ext = manager.getRegistry().getExtensionPoint("ntorrent.torrenttable.sorter", "FilterExtension");
		for(Extension e : ext.getAvailableExtensions()){
			
		}
		
	}
	

	@Override
	public boolean include(RowFilter.Entry<? extends TorrentTableModel, ? extends Torrent> entry) {
		if(nameFilter == "")
			return true;
		
		for(RowFilter f : extensions)
			if(!f.include(entry))
				return false;
		
		String name = entry.getIdentifier().getName().toLowerCase();
		return name.contains(nameFilter.toLowerCase());
	}

	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		nameFilter = ((JTextField)e.getSource()).getText();
		updateFilter();
	}
	
	public void updateFilter(){
		sorter.sort();
	}


	public void addFilter(RowFilter<TorrentTableModel, Torrent> filter) {
		extensions.add(filter);
	}


	public TorrentTableModel getModel() {
		return sorter.getModel();
	}


}
