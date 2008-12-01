/*****************************************************************************
 * Java Plug-in Framework (JPF)
 * Copyright (C) 2006-2007 Dmitry Olshansky
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
package org.java.plugin.tools.mocks;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.java.plugin.registry.Library;
import org.java.plugin.registry.Version;

/**
 * @version $Id$
 */
public class MockLibrary extends MockPluginElement<Library> implements Library {
    private boolean isCodeLibrary;
    private String path;
    private Version version;
    private LinkedList<String> exports = new LinkedList<String>();

    /**
     * @see org.java.plugin.registry.Library#getExports()
     */
    public Collection<String> getExports() {
        return Collections.unmodifiableCollection(exports);
    }
    
    /**
     * @param exportPrefix export prefix to add
     * @return this instance
     */
    public MockLibrary addExport(final String exportPrefix) {
        exports.add(exportPrefix);
        return this;
    }

    /**
     * @see org.java.plugin.registry.Library#getPath()
     */
    public String getPath() {
        return path;
    }
    
    /**
     * @param value the path to set
     * @return this instance
     */
    public MockLibrary setPath(final String value) {
        path = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Library#getVersion()
     */
    public Version getVersion() {
        return version;
    }
    
    /**
     * @param value the version to set
     * @return this instance
     */
    public MockLibrary setVersion(final Version value) {
        version = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Library#isCodeLibrary()
     */
    public boolean isCodeLibrary() {
        return isCodeLibrary;
    }
    
    /**
     * @param value the code library flag to set
     * @return this instance
     */
    public MockLibrary setCodeLibrary(final boolean value) {
        isCodeLibrary = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.UniqueIdentity#getUniqueId()
     */
    public String getUniqueId() {
        return getDeclaringPluginDescriptor().getId() + '@' + getId();
    }
}
