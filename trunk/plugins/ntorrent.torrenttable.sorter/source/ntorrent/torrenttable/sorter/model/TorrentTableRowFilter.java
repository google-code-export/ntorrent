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
public class TorrentTableRowFilter extends RowFilter<TorrentTableModel,Torrent> implements KeyListener {
	private final TorrentTableRowSorter sorter;
	
	Vector<RowFilter> extensions = new Vector<RowFilter>();
	String nameFilter = "";
	
	
	public TorrentTableRowFilter(TorrentTableRowSorter r) {
		sorter = r;
		sorter.setRowFilter(this);	
	}
	

	@Override
	public boolean include(RowFilter.Entry<? extends TorrentTableModel, ? extends Torrent> entry) {
		for(RowFilter f : extensions)
			if(!f.include(entry))
				return false;
		
		if(nameFilter == "")
			return true;
		
		String name = entry.getIdentifier().getName().toLowerCase();
		return name.contains(nameFilter.toLowerCase());
	}

	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		nameFilter = ((JTextField)e.getSource()).getText();
		sorter.sort();
	}


	public void addFilter(RowFilter<TorrentTableModel, Torrent> filter) {
		if(!extensions.contains(filter)){
			extensions.add(filter);
			sorter.sort();
		}
	}


	public void removeFilter(RowFilter<TorrentTableModel, Torrent> filter) {
		if(extensions.contains(filter)){
			extensions.remove(filter);
			sorter.sort();
		}
	}
	
	


}
