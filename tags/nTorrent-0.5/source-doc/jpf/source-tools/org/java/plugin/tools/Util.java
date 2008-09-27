/*****************************************************************************
 * Java Plug-in Framework (JPF)
 * Copyright (C) 2004-2006 Dmitry Olshansky
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *****************************************************************************/
package org.java.plugin.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.util.IoUtil;

/**
 * @version $Id$
 */
final class Util {
    private static Log log = LogFactory.getLog(Util.class);

    private static File tempFolder;
    private static boolean tempFolderInitialized = false;
    
    static File getTempFolder() throws IOException {
        if (tempFolder != null) {
            return tempFolderInitialized ? tempFolder : null;
        }
        synchronized (Util.class) {
            tempFolder = new File(System.getProperty("java.io.tmpdir"), //$NON-NLS-1$
                    System.currentTimeMillis() + ".jpf-tool-cache"); //$NON-NLS-1$
            log.debug("libraries cache folder is " + tempFolder); //$NON-NLS-1$
            File lockFile = new File(tempFolder, "lock"); //$NON-NLS-1$
            if (lockFile.exists()) {
                throw new IOException("can't initialize temporary folder " //$NON-NLS-1$
                        + tempFolder + " as lock file indicates that it is " //$NON-NLS-1$
                        + "owned by another JPF instance"); //$NON-NLS-1$
            }
            if (tempFolder.exists()) {
                // clean up folder
                IoUtil.emptyFolder(tempFolder);
            } else {
                tempFolder.mkdirs();
            }
            if (!lockFile.createNewFile()) {
                throw new IOException("can\'t create lock file in JPF " //$NON-NLS-1$
                        + "tool temporary folder " + tempFolder); //$NON-NLS-1$
            }
            lockFile.deleteOnExit();
            tempFolder.deleteOnExit();
            tempFolderInitialized = true;
        }
        return tempFolder;
    }

    static byte[] readUrlContent(final URL url) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        InputStream urlStrm = url.openStream();
        try {
            IoUtil.copyStream(urlStrm, result, 256);
        } finally {
            urlStrm.close();
        }
        return result.toByteArray();
    }
    
    private Util() {
        // no-op
    }
}
