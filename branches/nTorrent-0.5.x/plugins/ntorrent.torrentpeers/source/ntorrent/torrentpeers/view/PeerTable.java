package ntorrent.torrentpeers.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ntorrent.torrentpeers.model.PeerTableColumnModel;
import ntorrent.torrentpeers.model.PeerTableModel;
import ntorrent.torrenttable.model.DataUnit;
import ntorrent.torrenttable.view.renderers.DataUnitRenderer;

public class PeerTable extends JTable {
	private static final long serialVersionUID = 19971434789844502L;
	private final static PeerTableColumnModel columnModel = new PeerTableColumnModel();
	
	JPanel panel = new JPanel(new BorderLayout());
	
	public JPanel getDisplay(){
		return panel;
	}
	
	public PeerTable(PeerTableModel model) {
		super(model,columnModel.getModel());
		setDefaultRenderer(DataUnit.class, new DataUnitRenderer());
		panel.add(new JScrollPane(this));
	}
	
	public PeerTableModel getModel() {
		return (PeerTableModel) super.getModel();
	}

}
