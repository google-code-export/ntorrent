package ntorrent.tools;

import java.io.Serializable;

class SimpleObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private String data;
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
}
