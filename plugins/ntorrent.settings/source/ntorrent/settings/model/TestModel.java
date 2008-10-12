package ntorrent.settings.model;

import ntorrent.settings.model.SettingsExtension.UserSetting;

public class TestModel {
	@UserSetting(label="Boolean")
	private boolean bool = true;
	
	@UserSetting(label="Character")
	public char character = 'A';
	
	@UserSetting(label="Byte")
	public byte byt3 = 0;
	
	@UserSetting(label="Short")
	public short sh0rt = 0;
	
	@UserSetting(label="Integer")
	public int iint = 10;
	
	@UserSetting(label="Long")
	public long l0ng = 123;
	
	@UserSetting(label="Float")
	public float fl0at = 123123;
	
	@UserSetting(label="Double")
	public double duble = 1.22;
	
	@UserSetting(label="Enum")
	public EnumTest e = EnumTest.YES;
	
	@UserSetting(label="String")
	public String string = "hallo";
	
	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public double getDuble() {
		return duble;
	}

	public void setDuble(double duble) {
		this.duble = duble;
	}

	public EnumTest getE() {
		return e;
	}

	public void setE(EnumTest e) {
		this.e = e;
	}

	public float getFl0at() {
		return fl0at;
	}

	public void setFl0at(float fl0at) {
		this.fl0at = fl0at;
	}

	public int getIint() {
		return iint;
	}

	public void setIint(int iint) {
		this.iint = iint;
	}

	public long getL0ng() {
		return l0ng;
	}

	public void setL0ng(long l0ng) {
		this.l0ng = l0ng;
	}

	public short getSh0rt() {
		return sh0rt;
	}

	public void setSh0rt(short sh0rt) {
		this.sh0rt = sh0rt;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public byte getByt3() {
		return byt3;
	}
	
	public void setByt3(byte byt3) {
		this.byt3 = byt3;
	}
	
	public enum EnumTest {
		YES,
		NO;
		
		public String toString(){
			switch(this){
				case NO:
					return "nei";
				case YES:
					return "ja";
			}
			return null;
		}
	}
}
