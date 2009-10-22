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

import org.apache.tools.ant.BuildException;
import org.java.plugin.registry.IntegrityCheckReport;

/**
 * The Ant task to perform integrity check of plug-in set.
 * @version $Id$
 */
public final class CheckTask extends BaseJpfTask {
    private boolean usePathResolver;
    
    /**
     * @param aUsePathResolver <code>true</code> if PathResolver should be used
     */
    public void setUsePathResolver(final boolean aUsePathResolver) {
        this.usePathResolver = aUsePathResolver;
    }
    
    /**
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() {
        initRegistry(usePathResolver);
        IntegrityCheckReport report =
            getRegistry().checkIntegrity(getPathResolver());
        if (getVerbose()) {
            log(integrityCheckReport2str(report));
        }
        log("Integrity check done. Errors: " + report.countErrors() //$NON-NLS-1$
                + ". Warnings: " + report.countWarnings() + "."); //$NON-NLS-1$ //$NON-NLS-2$
        if (report.countErrors() > 0) {
            throw new BuildException("plug-ins set integrity check failed," //$NON-NLS-1$
                    + " errors count - " + report.countErrors()); //$NON-NLS-1$
        }
    }

    private static String integrityCheckReport2str(
            final IntegrityCheckReport report) {
        StringBuilder buf = new StringBuilder();
        buf.append("Integrity check report:\r\n"); //$NON-NLS-1$
        buf.append("-------------- REPORT BEGIN -----------------\r\n"); //$NON-NLS-1$
        for (IntegrityCheckReport.ReportItem item : report.getItems()) {
            buf.append("severity=").append(item.getSeverity()) //$NON-NLS-1$
                .append("; code=").append(item.getCode()) //$NON-NLS-1$
                .append("; message=").append(item.getMessage()) //$NON-NLS-1$
                .append("; source=").append(item.getSource()).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        buf.append("-------------- REPORT END -----------------"); //$NON-NLS-1$
        return buf.toString();
    }
}
