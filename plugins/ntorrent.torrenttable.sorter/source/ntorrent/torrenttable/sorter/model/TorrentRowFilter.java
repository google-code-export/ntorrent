package ntorrent.torrenttable.sorter.model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.table.TableRowSorter;

import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableModel;


@SuppressWarnings("unchecked")
public class TorrentRowFilter extends javax.swing.RowFilter<TorrentTableModel,Integer> implements KeyListener {

	String nameFilter = "";
	final TableRowSorter<TorrentTableModel> sorter;

	public TorrentRowFilter(TableRowSorter sorter) {
		this.sorter = sorter;
		sorter.setRowFilter(this);
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		nameFilter = ((JTextField)e.getSource()).getText();
		System.out.println(nameFilter);
		sorter.sort();
	}
	@Override
	public boolean include(javax.swing.RowFilter.Entry<? extends TorrentTableModel, ? extends Integer> entry) {
		
		if(nameFilter == "")
			return true;
		
		Torrent t = entry.getModel().getRow(entry.getIdentifier());
		String name = t.getName().toLowerCase();
		System.out.println(name);
		return name.contains(nameFilter.toLowerCase());
	}
	




}
