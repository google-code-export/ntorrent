/**
 * 
 */
package org.heldig.ntorrent.language;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.heldig.ntorrent.NTorrent;

/**
 * @author Kim Eik
 *
 */
public final class LanguageReader extends Properties {
	private static final long serialVersionUID = 1L;
	public LanguageReader() {
		try {
			NTorrent.settings.language.createNewFile();
			FileInputStream in = new FileInputStream(NTorrent.settings.language);
			this.load(in);
			in.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public final void store() {
		FileOutputStream out;
		try {
			out = new FileOutputStream(NTorrent.settings.language);
			this.store(out, "Language");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
