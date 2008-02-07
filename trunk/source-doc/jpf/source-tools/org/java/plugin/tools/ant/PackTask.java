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

import org.apache.tools.ant.BuildException;
import org.java.plugin.tools.PluginArchiver;

/**
 * The ant task for creating plug-ins archive file.
 * @version $Id$
 */
public final class PackTask extends BaseJpfTask {
    private File destFile;

    /**
     * @param aDestFile target archive file
     */
    public void setDestFile(final File aDestFile) {
        this.destFile = aDestFile;
    }
    
    /**
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() {
        if (destFile == null) {
            throw new BuildException("destfile attribute must be set!", //$NON-NLS-1$
                    getLocation());
        }
        initRegistry(true);
        try {
            PluginArchiver.pack(getRegistry(), getPathResolver(), destFile);
            log("Plug-ins archive created in file " + destFile); //$NON-NLS-1$
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
}
