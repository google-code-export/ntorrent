package ntorrent.io;

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

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
    
    public class ErrorLog extends JFrame {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		static PipedInputStream piOut;
		static PipedInputStream piErr;
		static PipedOutputStream poOut;
		static PipedOutputStream poErr;
        static JTextArea textArea = new JTextArea();
    
        public ErrorLog() throws IOException {
            // Set up System.out
            piOut = new PipedInputStream();
            poOut = new PipedOutputStream(piOut);
            System.setOut(new PrintStream(poOut));
    
            // Set up System.err
            piErr = new PipedInputStream();
            poErr = new PipedOutputStream(piErr);
            System.setErr(new PrintStream(poErr));

            // Add a scrolling text area
            textArea.setEditable(false);
            textArea.setRows(20);
            textArea.setColumns(50);
            getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
            pack();
            setVisible(true);
            // Create reader threads
            new ReaderThread(piOut).start();
            new ReaderThread(piErr).start();
        }
    
        static class ReaderThread extends Thread {
            PipedInputStream pi;
    
            ReaderThread(PipedInputStream pi) {
                this.pi = pi;
            }
    
            public void run() {
                try {
                    while (true) {
                        final int b = pi.read();
                        if (b == -1) {
                            break;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                textArea.append(""+(char)b);
    
                                // Make sure the last line is always visible
                                textArea.setCaretPosition(textArea.getDocument().getLength());
    
                                //
                                if(textArea.getLineCount() > 300){
                                	try {
										textArea.replaceRange("", textArea.getLineStartOffset(0), textArea.getLineEndOffset(0));
									} catch (BadLocationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                }
            }
        }
    }
