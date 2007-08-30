package ntorrent.gui.elements;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FileList {
	//Simple filelist.
	JPanel container = new JPanel(new  GridLayout(0,3,10,5));
	JPanel stretchPrevent = new JPanel(new FlowLayout(FlowLayout.LEADING));
	JScrollPane fileList = new JScrollPane(stretchPrevent);
	
	FileList(){
		stretchPrevent.add(container);
		container.setOpaque(false);
		stretchPrevent.setOpaque(false);
		container.setVisible(false);
		headers();
	}
	
	private void headers(){
		container.add(new JLabel("Priority"));
		container.add(new JLabel("Filename"));
		container.add(new JLabel("Size"));
	}
	
	public JScrollPane getFileList() {
		return fileList;
	}

	public void hideInfo() {
		container.setVisible(false);
	}

	public void setInfo(Vector<Object>[] list) {
		container.removeAll();
		headers();
		for(Vector obj : list)
			for(int x = 0; x < 3; x++)
				container.add(new JLabel(""+obj.get(x).getClass().cast(obj.get(x))));
		container.setVisible(true);
		
		
	}
}
