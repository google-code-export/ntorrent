/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Kim Eik
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.heldig.ntorrent.io;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

/**
 * @author Kim Eik
 *
 */
public class ErrorStream extends OutputStream {
    
	private final JTextArea textArea = new JTextArea();
	
	public ErrorStream() {
        textArea.setEditable(false);
	}
    
	@Override
	public void write(final int b) throws IOException {
        SwingUtilities.invokeLater(new Runnable(){
        	public void run() {
                textArea.append(""+(char)b);
        	}
        });
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);
		setFocus();
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
		setFocus();
	}
	
	private void setFocus(){
        // Make sure the last line is always visible
        textArea.setCaretPosition(textArea.getDocument().getLength());

        //
        while(textArea.getLineCount() > 300){
        	try {
				textArea.replaceRange("", textArea.getLineStartOffset(0), textArea.getLineEndOffset(0));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
        }
	}
	
	
	public JTextArea getTextArea() {
		return textArea;
	}
}
