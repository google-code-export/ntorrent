package ntorrent.torrentlabels;

import java.util.HashSet;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;

import ntorrent.io.rtorrent.Download;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.locale.ResourcePool;
import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionInstance;
import ntorrent.torrentlabels.model.LabelListModel;
import ntorrent.torrentlabels.model.TorrentTableFilter;
import ntorrent.torrentlabels.view.LabelList;
import ntorrent.torrentlabels.view.LabelPopupMenu;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableActionListener;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.view.TorrentTable;
import ntorrent.torrenttable.view.TorrentTableJPopupMenu;

/**
 * @author Kim Eik
 *
 */
@SuppressWarnings("unchecked")
public class LabelInstance implements  SessionInstance, TorrentTableActionListener, TableModelListener, ListSelectionListener {
	
	public final static String PROPERTY = "d.get_custom1=";
	
	private final TorrentTableFilter labelFilter = new TorrentTableFilter();
	
	private final LabelListModel listModel;
	private final LabelList labelList;
	private final JScrollPane container;
	
	private final XmlRpcConnection connection;
	
	private final TorrentTableInterface tc;
	private final TorrentTable table;
	private final TorrentTableJPopupMenu tablePopup;
	private final TableRowSorter<TorrentTableModel> sorter;
	private final RowFilter<? super TorrentTableModel, ? super Integer> filter;
	
	private final JSplitPane menuPane;
	//jpane with gridlayout
	private final JPanel gridPane;
	private boolean started;
	
	//jmenu adding label functionality to tablepopup
	private final JMenu labelMenu;
	
	public LabelInstance(ConnectionSession session) {
		//initiate needed variables.
		connection = session.getConnection();
		tc = session.getTorrentTableController();
		table = tc.getTable();
		sorter = (TableRowSorter<TorrentTableModel>) table.getRowSorter();
		filter = sorter.getRowFilter();
		tablePopup = table.getTablePopup();
		menuPane = session.getDisplay().getMenu();
		gridPane = (JPanel) menuPane.getBottomComponent();
		labelMenu = new LabelPopupMenu(tablePopup);
		
		//Create the list
		listModel = new LabelListModel();
		labelList = new LabelList(listModel);
		container = new JScrollPane(labelList);
		
		//add this as a list selection listener
		labelList.getSelectionModel().addListSelectionListener(this);
		
		//add this as a tablelistener
		table.getModel().addTableModelListener(this);
		
		//add this as a torrent action listener
		tablePopup.addTorrentTableActionListener(this);
	}

	public void torrentActionPerformed(Torrent[] tor, String command) {
		String[] mitems = LabelPopupMenu.MENU_ITEMS;
		if(command.equals(mitems[0])){
			setLabel("", tor, connection);
		}else if(command.equals(mitems[1])){
			String label = JOptionPane.showInputDialog(ResourcePool.getString("setalabel", "locale", this));
			setLabel(label, tor, connection);
		}else{
            if(command.startsWith("label:")){
                setLabel(command.split(":")[1], tor,connection);
            }
		}
			
	}

	public void tableChanged(TableModelEvent e) {
		int type = e.getType();
		boolean updateOnce = true;
		
		if(type == TableModelEvent.INSERT || (type == TableModelEvent.UPDATE && updateOnce)){
			if(updateOnce)
				updateOnce = false;
			updateLabels();
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()){
			table.clearSelection(); //keep this.
			if(labelList.getSelectedIndex() > 1){
					String label = (String)labelList.getSelectedValue();
					labelFilter.setLabel(label);
				}else{
					if(labelList.getSelectedIndex() == 0){
						labelFilter.allLabel();
					}else if(labelList.getSelectedIndex() == 1){
						labelFilter.noneLabel();
					}
			}
		}
		sorter.sort();
	}

	public void start() {
		started = true;
		//add download variable.
		tc.getDownloadVariable().add(PROPERTY);
		
		//add gui element and refresh
		gridPane.add(container);
		gridPane.revalidate();
		gridPane.repaint();
		
		//add jmenu to torrent table popup
		tablePopup.add(labelMenu);
		
		//set new filter
		HashSet<RowFilter<? super TorrentTableModel, ? super Integer>> 
			filters = new HashSet<RowFilter<? super TorrentTableModel, ? super Integer>>();
		
		filters.add(labelFilter);
		filters.add(filter);
		sorter.setRowFilter(RowFilter.andFilter(filters));
		
	}

	public void stop() {
		started = false;
		//remove download variable
		tc.getDownloadVariable().remove(PROPERTY);
		
		//remove gui element and refresh
		gridPane.remove(container);
		gridPane.revalidate();
		gridPane.repaint();
		
		//remove jmenu to torrent table popup
		tablePopup.remove(labelMenu);
		
		//restore old filter
		sorter.setRowFilter(filter);
	}

	public boolean isStarted() {
		return started;
	}

	private void updateLabels(){
		for(Torrent t : tc.getTorrents().values()){
			String label = (String)t.getProperty(PROPERTY);
			if(label != null && label.length() > 0 && !listModel.containsKey(label)){
				JMenuItem item = new JMenuItem(label);
				item.addActionListener(tablePopup);
				item.setActionCommand("label:"+label);
				
				int row = listModel.getSize()-1;
				listModel.put(label, item);
				this.labelMenu.add(item);
				
				// yes yes yes, im cheating i know..
				listModel.fireIntervallAdded(this, row, row+1);
			}
		}
	}
	
    private void setLabel(final String label, final Torrent[] tor, final XmlRpcConnection connection) {
        //set labels async
        new Thread(){
                public void run(){
                        Download d = connection.getDownloadClient();
                        for(Torrent t : tor){
                                System.out.println("setting label to "+label+" on "+t.getHash()+" ("+t.getName()+")");
                                d.set_custom1(t.getHash(),label);
                        }
                 sorter.sort();
                }
        }.start();
    }
}
