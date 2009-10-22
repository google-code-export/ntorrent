/*****************************************************************************
 * Java Plug-in Framework (JPF)
 * Copyright (C) 2004 Dmitry Olshansky
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.tools.ant.BuildException;
import org.java.plugin.tools.docgen.DocGenerator;


/**
 * The Ant task to generate documentation from plug-in manifest.
 * @version $Id$
 */
public final class DocTask extends BaseJpfTask {
    private File destDir;
    private File overviewFile;
    private String encoding;
    private String docEncoding;
    private String templatesPath;
    private File stylesheetFile;

    /**
     * @param aDestDir base directory for generated documentation files
     */
    public void setDestDir(final File aDestDir) {
        this.destDir = aDestDir;
    }

    /**
     * @param anOverviewFile documentation overview HTML file
     */
    public void setOverview(final File anOverviewFile) {
        this.overviewFile = anOverviewFile;
    }
    
    /**
     * @param anEncoding source files encoding name (templates, overview etc.)
     */
    public void setEncoding(final String anEncoding) {
        this.encoding = anEncoding;
    }

    /**
     * @param anEncoding output files encoding name
     */
    public void setDocEncoding(final String anEncoding) {
        this.docEncoding = anEncoding;
    }

    /**
     * @param aStylesheetFile CSS style sheet to use
     */
    public void setStylesheetFile(final File aStylesheetFile) {
        this.stylesheetFile = aStylesheetFile;
    }

    /**
     * @param aTemplatesPath path to template files
     *                      (should be available in classpath)
     */
    public void setTemplates(final String aTemplatesPath) {
        this.templatesPath = aTemplatesPath;
    }
    
    /**
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() {
        if (destDir == null) {
            throw new BuildException("destdir attribute must be set!", //$NON-NLS-1$
                    getLocation());
        }
        if (!destDir.exists() && !destDir.mkdirs()) {
            throw new BuildException("can't make " + destDir //$NON-NLS-1$
                    + " folder", getLocation()); //$NON-NLS-1$
        }
        if (destDir.list().length != 0) {
            throw new BuildException("directory " + destDir //$NON-NLS-1$
                    + " is not empty", getLocation()); //$NON-NLS-1$
        }
        initRegistry(true);
        try {
            DocGenerator docGen;
            if (templatesPath != null) {
                docGen = new DocGenerator(getRegistry(), getPathResolver(),
                        templatesPath, encoding);
            } else {
                docGen = new DocGenerator(getRegistry(), getPathResolver());
            }
            if (overviewFile != null) {
                docGen.setDocumentationOverview(getFileContent(overviewFile));
            }
            if (stylesheetFile != null) {
                docGen.setStylesheet(getFileContent(stylesheetFile));
            }
            if (docEncoding != null) {
                docGen.setOutputEncoding(docEncoding);
            }
            docGen.generate(destDir);
            log("Documentation generated to folder " + destDir); //$NON-NLS-1$
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
    
    private String getFileContent(final File file) throws IOException {
        Reader reader;
        if (encoding != null) {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), encoding));
        } else {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
        }
        try {
            StringBuilder result = new StringBuilder();
            char[] buf = new char[256];
            int len;
            while ((len = reader.read(buf)) != -1) {
                result.append(buf, 0, len);
            }
            return result.toString();
        } finally {
            reader.close();
        }
    }
}
