/**
 * 
 */
package org.heldig.ntorrent.gui.label;

import java.util.Vector;

import javax.swing.AbstractListModel;

import org.apache.xmlrpc.XmlRpcRequest;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;
import org.heldig.ntorrent.language.Language;

/**
 * @author Kim Eik
 *
 */
public class LabelListModel extends AbstractListModel implements XmlRpcCallback {
	private static final long serialVersionUID = 1L;
	Vector<Object> data = new Vector<Object>();

	public LabelListModel() {
		init();
	}
	
	private void init(){
		data.add(Language.Label_none);

	}
	
	@Override
	public Object getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public int getSize() {
		return data.size();
	}
	
	public void addLabel(String label){
		if(!data.contains(label))
			data.add(label);
		fireIntervalAdded(this, data.size()-1, data.size()-1);
	}

	@Override
	public void handleResult(XmlRpcRequest request, Object result) {
		data.clear();
		init();
		for(int x = 0; x < request.getParameterCount(); x++){
			if(((String)request.getParameter(x)).equals("d.get_custom1="))
				for(Object obj : (Object[])result){
					Object[] raw = (Object[]) obj;
					String label = (String)raw[x-1];
					//if(label != "")
						//addLabel(label);
				}
		}
	}
}
