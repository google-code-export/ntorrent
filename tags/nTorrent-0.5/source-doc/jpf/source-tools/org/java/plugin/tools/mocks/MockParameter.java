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

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import org.java.plugin.PathResolver;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.Extension.Parameter;
import org.java.plugin.registry.ExtensionPoint.ParameterDefinition;

/**
 * @version $Id$
 */
public class MockParameter extends MockPluginElement<Parameter>
        implements Parameter {
    private Extension declaringExtension;
    private ParameterDefinition definition;
    private Parameter superParameter;
    private String rawValue;
    private Object typedValue;
    private LinkedList<Parameter> subParameters = new LinkedList<Parameter>();
    
    /**
     * No-arguments constructor.
     */
    public MockParameter() {
        // no-op
    }

    /**
     * @param id parameter ID
     * @param aRawValue raw parameter value
     * @param aTypedValue typed parameter value
     */
    public MockParameter(final String id, final String aRawValue,
            final Object aTypedValue) {
        setId(id);
        rawValue = aRawValue;
        typedValue = aTypedValue;
    }

    /**
     * @param id parameter ID
     * @param aRawValue raw parameter value
     * @param aTypedValue typed parameter value
     * @param aDeclaringExtension declaring extension
     */
    public MockParameter(final String id, final String aRawValue,
            final Object aTypedValue, final Extension aDeclaringExtension) {
        setId(id);
        rawValue = aRawValue;
        typedValue = aTypedValue;
        declaringExtension = aDeclaringExtension;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#getDeclaringExtension()
     */
    public Extension getDeclaringExtension() {
        return declaringExtension;
    }
    
    /**
     * @param value the declaring extension to set
     * @return this instance
     */
    public MockParameter setDeclaringExtension(final Extension value) {
        declaringExtension = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#getDefinition()
     */
    public ParameterDefinition getDefinition() {
        return definition;
    }
    
    /**
     * @param value the parameter definition to set
     * @return this instance
     */
    public MockParameter setDefinition(final ParameterDefinition value) {
        definition = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#getSubParameter(
     *      java.lang.String)
     */
    public Parameter getSubParameter(final String id) {
        for (Parameter param : subParameters) {
            if (param.getId().equals(id)) {
                return param;
            }
        }
        throw new IllegalArgumentException("unknown parameter ID " + id); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#getSubParameters()
     */
    public Collection<Parameter> getSubParameters() {
        return Collections.unmodifiableCollection(subParameters);
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#getSubParameters(
     *      java.lang.String)
     */
    public Collection<Parameter> getSubParameters(final String id) {
        LinkedList<Parameter> result = new LinkedList<Parameter>();
        for (Parameter param : subParameters) {
            if (param.getId().equals(id)) {
                result.add(param);
            }
        }
        return result;
    }
    
    /**
     * @param parameter sub-parameter to add
     * @return this instance
     */
    public MockParameter addParameter(final Parameter parameter) {
        subParameters.add(parameter);
        return this;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#getSuperParameter()
     */
    public Parameter getSuperParameter() {
        return superParameter;
    }
    
    /**
     * @param value the super parameter to set
     * @return this instance
     */
    public MockParameter setSuperParameter(final Parameter value) {
        superParameter = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#rawValue()
     */
    public String rawValue() {
        return rawValue;
    }
    
    /**
     * @param raw raw parameter value
     * @param typed typed parameter value
     * @return this instance
     */
    public MockParameter setValue(final String raw, final Object typed) {
        rawValue = raw;
        typedValue = typed;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#valueAsBoolean()
     */
    public Boolean valueAsBoolean() {
        return (Boolean) typedValue;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#valueAsDate()
     */
    public Date valueAsDate() {
        return (Date) typedValue;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#valueAsExtension()
     */
    public Extension valueAsExtension() {
        return (Extension) typedValue;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#valueAsExtensionPoint()
     */
    public ExtensionPoint valueAsExtensionPoint() {
        return (ExtensionPoint) typedValue;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#valueAsNumber()
     */
    public Number valueAsNumber() {
        return (Number) typedValue;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#valueAsPluginDescriptor()
     */
    public PluginDescriptor valueAsPluginDescriptor() {
        return (PluginDescriptor) typedValue;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#valueAsString()
     */
    public String valueAsString() {
        return (String) typedValue;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#valueAsUrl()
     */
    public URL valueAsUrl() {
        return (URL) typedValue;
    }

    /**
     * @see org.java.plugin.registry.Extension.Parameter#valueAsUrl(
     *      org.java.plugin.PathResolver)
     */
    public URL valueAsUrl(final PathResolver pathResolver) {
        return (URL) typedValue;
    }
}
