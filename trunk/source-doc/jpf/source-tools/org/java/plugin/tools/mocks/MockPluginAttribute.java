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
import org.java.plugin.registry.PluginAttribute;

/**
 * @version $Id$
 */
public class MockPluginAttribute extends MockPluginElement<PluginAttribute>
        implements PluginAttribute {
    private LinkedList<PluginAttribute> subAttributes = new LinkedList<PluginAttribute>();
    private PluginAttribute superAttribute;
    private String value;
    
    /**
     * No-arguments constructor.
     */
    public MockPluginAttribute() {
        // no-op
    }

    /**
     * @param id attribute ID
     * @param aValue attribute value
     */
    public MockPluginAttribute(final String id, final String aValue) {
        setId(id);
        value = aValue;
    }

    /**
     * @see org.java.plugin.registry.PluginAttribute#getSubAttribute(
     *      java.lang.String)
     */
    public PluginAttribute getSubAttribute(final String id) {
        for (PluginAttribute attr : subAttributes) {
            if (attr.getId().equals(id)) {
                return attr;
            }
        }
        throw new IllegalArgumentException("unknown attribute ID " + id); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.PluginAttribute#getSubAttributes()
     */
    public Collection<PluginAttribute> getSubAttributes() {
        return Collections.unmodifiableCollection(subAttributes);
    }

    /**
     * @see org.java.plugin.registry.PluginAttribute#getSubAttributes(
     *      java.lang.String)
     */
    public Collection<PluginAttribute> getSubAttributes(final String id) {
        LinkedList<PluginAttribute> result = new LinkedList<PluginAttribute>();
        for (PluginAttribute attr : subAttributes) {
            if (attr.getId().equals(id)) {
                result.add(attr);
            }
        }
        return result;
    }
    
    /**
     * @param attribute sub-attribute to add
     * @return this instance
     */
    public MockPluginAttribute addSubAttribute(
            final PluginAttribute attribute) {
        subAttributes.add(attribute);
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginAttribute#getSuperAttribute()
     */
    public PluginAttribute getSuperAttribute() {
        return superAttribute;
    }
    
    /**
     * @param attribute the super attribute to set
     * @return this instance
     */
    public MockPluginAttribute setSuperAttribute(
            final PluginAttribute attribute) {
        superAttribute = attribute;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginAttribute#getValue()
     */
    public String getValue() {
        return value;
    }
    
    /**
     * @param attributeValue the attribute value to set
     * @return this instance
     */
    public MockPluginAttribute setValue(final String attributeValue) {
        value = attributeValue;
        return this;
    }
}
