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

import org.java.plugin.registry.Documentation;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginElement;
import org.java.plugin.registry.PluginFragment;

/**
 * @version $Id$
 * @param <T> plug-in element owner type
 */
public abstract class MockPluginElement<T extends PluginElement<T>>
        extends MockIdentity implements PluginElement<T> {
    private PluginDescriptor declaringPluginDescriptor;
    private PluginFragment declaringPluginFragment;
    private String docsPath;
    private Documentation<T> documentation;

    /**
     * @see org.java.plugin.registry.PluginElement#getDeclaringPluginDescriptor()
     */
    public PluginDescriptor getDeclaringPluginDescriptor() {
        return declaringPluginDescriptor;
    }
    
    /**
     * @param value the declaring plug-in descriptor to set
     * @return this instance
     */
    public MockPluginElement<T> setDeclaringPluginDescriptor(
            final PluginDescriptor value) {
        declaringPluginDescriptor = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginElement#getDeclaringPluginFragment()
     */
    public PluginFragment getDeclaringPluginFragment() {
        return declaringPluginFragment;
    }
    
    /**
     * @param value the declaring plug-in fragment to set
     * @return this instance
     */
    public MockPluginElement<T> setDeclaringPluginFragment(
            final PluginFragment value) {
        declaringPluginFragment = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Documentable#getDocsPath()
     */
    public String getDocsPath() {
        return docsPath;
    }
    
    /**
     * @param value the docs path to set
     * @return this instance
     */
    public MockPluginElement<T> setDocsPath(final String value) {
        docsPath = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Documentable#getDocumentation()
     */
    public Documentation<T> getDocumentation() {
        return documentation;
    }

    /**
     * @param value the documentation to set
     * @return this instance
     */
    public MockPluginElement<T> setDocumentation(final Documentation<T> value) {
        documentation = value;
        return this;
    }
}
