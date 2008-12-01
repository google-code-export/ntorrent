package ntorrent.skins.model;

import java.io.Serializable;

public class PrettyLookAndFeelInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private final String name;
	private final String className;
	
	public PrettyLookAndFeelInfo(String name, String className) {
		this.name = name;
		this.className = className;
	}
	
	public String getName() {
		return name;
	}
	
	public String getClassName() {
		return className;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}