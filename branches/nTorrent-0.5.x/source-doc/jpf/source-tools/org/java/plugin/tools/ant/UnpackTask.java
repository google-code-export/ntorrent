/*****************************************************************************
 * Java Plug-in Framework (JPF)
 * Copyright (C) 2004-2007 Dmitry Olshansky
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
package org.java.plugin.tools.ant;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.java.plugin.ObjectFactory;
import org.java.plugin.registry.ManifestProcessingException;
import org.java.plugin.tools.PluginArchiver;
import org.java.plugin.util.IoUtil;

/**
 * The ant task for extracting plug-ins from archive.
 * @version $Id$
 */
public final class UnpackTask extends Task {
    private File destDir;
    private File srcFile;

    /**
     * @param aSrcFile archive file to be unpacked
     */
    public void setSrcFile(final File aSrcFile) {
        this.srcFile = aSrcFile;
    }

    /**
     * @param aDestFolder folder where to extract archived plug-ins
     */
    public void setDestDir(final File aDestFolder) {
        this.destDir = aDestFolder;
    }
    
    /**
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() {
        if (srcFile == null) {
            throw new BuildException("srcfile attribute must be set!", //$NON-NLS-1$
                    getLocation());
        }
        if (destDir == null) {
            throw new BuildException("destdir attribute must be set!", //$NON-NLS-1$
                    getLocation());
        }
        try {
            PluginArchiver.unpack(IoUtil.file2url(srcFile),
                    ObjectFactory.newInstance().createRegistry(), destDir);
            log("Plug-ins archive unpacked to folder " + destDir); //$NON-NLS-1$
        } catch (IOException ioe) {
            throw new BuildException(ioe);
        } catch (ManifestProcessingException mpe) {
            throw new BuildException(mpe);
        } catch (ClassNotFoundException cnfe) {
            throw new BuildException(cnfe);
        }
    }
}
