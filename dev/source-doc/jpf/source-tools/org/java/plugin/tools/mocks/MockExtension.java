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
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.PluginDescriptor;

/**
 * @version $Id$
 */
public class MockExtension extends MockPluginElement<Extension>
        implements Extension {
    private String extendedPluginId;
    private String extendedPointId;
    private boolean isValid = true;
    private LinkedList<Parameter> parameters = new LinkedList<Parameter>();
    
    /**
     * No-arguments constructor.
     */
    public MockExtension() {
        // no-op
    }

    /**
     * @param id extension ID
     */
    public MockExtension(final String id) {
        setId(id);
    }

    /**
     * @param id extension ID
     * @param declaringPluginDescriptor declaring plug-in descriptor
     */
    public MockExtension(final String id,
            final PluginDescriptor declaringPluginDescriptor) {
        setDeclaringPluginDescriptor(declaringPluginDescriptor);
        setId(id);
    }

    /**
     * @see org.java.plugin.registry.Extension#getExtendedPluginId()
     */
    public String getExtendedPluginId() {
        return extendedPluginId;
    }
    
    /**
     * @param value the extended plug-in id to set
     * @return this instance
     */
    public MockExtension setExtendedPluginId(final String value) {
        extendedPluginId = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Extension#getExtendedPointId()
     */
    public String getExtendedPointId() {
        return extendedPointId;
    }
    
    /**
     * @param value the extended point id to set
     * @return this instance
     */
    public MockExtension setExtendedPointId(final String value) {
        extendedPointId = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Extension#getParameter(java.lang.String)
     */
    public Parameter getParameter(final String id) {
        for (Parameter param : parameters) {
            if (param.getId().equals(id)) {
                return param;
            }
        }
        throw new IllegalArgumentException("unknown parameter ID " + id); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.Extension#getParameters()
     */
    public Collection<Parameter> getParameters() {
        return Collections.unmodifiableCollection(parameters);
    }

    /**
     * @see org.java.plugin.registry.Extension#getParameters(java.lang.String)
     */
    public Collection<Parameter> getParameters(final String id) {
        LinkedList<Parameter> result = new LinkedList<Parameter>();
        for (Parameter param : parameters) {
            if (param.getId().equals(id)) {
                result.add(param);
            }
        }
        return result;
    }
    
    /**
     * @param parameter parameter to add
     * @return this instance
     */
    public MockExtension addParameter(final Parameter parameter) {
        parameters.add(parameter);
        return this;
    }

    /**
     * @see org.java.plugin.registry.Extension#isValid()
     */
    public boolean isValid() {
        return isValid;
    }
    
    /**
     * @param value the valid flag to set
     * @return this instance
     */
    public MockExtension setValid(final boolean value) {
        isValid = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.UniqueIdentity#getUniqueId()
     */
    public String getUniqueId() {
        return getDeclaringPluginDescriptor().getId() + '@' + getId();
    }
}
