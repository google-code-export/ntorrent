package ntorrent.skins.model;

import javax.swing.ComboBoxModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListDataListener;

public class ComboBoxSkinModel implements ComboBoxModel {
	private PrettyLookAndFeelInfo laf;
	private PrettyLookAndFeelInfo[] lafs = new PrettyLookAndFeelInfo[UIManager.getInstalledLookAndFeels().length];

	public ComboBoxSkinModel() {
		for(int x = 0; x < lafs.length; x++){
			LookAndFeelInfo lafi = UIManager.getInstalledLookAndFeels()[x];
			lafs[x] = new PrettyLookAndFeelInfo(lafi.getName(),lafi.getClassName());
		}
	}
	
	@Override
	public Object getSelectedItem() {
		return laf;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		laf = (PrettyLookAndFeelInfo) anItem;
	}

	@Override
	public void addListDataListener(ListDataListener l) {}

	@Override
	public Object getElementAt(int index) {
		return lafs[index];
	}

	@Override
	public int getSize() {
		return lafs.length;
	}

	@Override
	public void removeListDataListener(ListDataListener l) { }


}
