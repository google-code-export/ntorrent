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
package ntorrent.io.logging;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ntorrent.data.Environment;

public class SystemLog {
    
	// preserve old stdout/stderr streams in case they might be useful      
    PrintStream stdout = System.out;                                       
    PrintStream stderr = System.err; 
    
	public SystemLog() throws IOException {
		File parent = new File(Environment.getNtorrentDir(),"log");
		File log = new File(parent,"ntorrent");
		if(!parent.isDirectory())
			parent.mkdir();
		if(!log.isFile())
			log.createNewFile();
		
        // initialize logging to go to rolling log file
        LogManager logManager = LogManager.getLogManager();
        logManager.reset();

        // log file max size 100K, 3 rolling files, append-on-open
        FileHandler fileHandler = new FileHandler(log.toString(), 100000, 3, true);
        fileHandler.setFormatter(new SimpleFormatter());
        Logger.getLogger("").addHandler(fileHandler);
                                                                                                           
        // now rebind stdout/stderr to logger                                  
        Logger logger;                                                         
        LoggingOutputStream los;                                               
                                                                               
        logger = Logger.getLogger("stdout");                                   
        los = new LoggingOutputStream(logger, StdOutErrLevel.STDOUT);          
        System.setOut(new PrintStream(los, true));                             
                                                                               
        logger = Logger.getLogger("stderr");                                   
        los= new LoggingOutputStream(logger, StdOutErrLevel.STDERR);           
        System.setErr(new PrintStream(los, true));       
	}

}
