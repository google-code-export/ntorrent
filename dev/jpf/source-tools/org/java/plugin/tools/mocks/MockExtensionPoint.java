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
import java.util.HashSet;
import java.util.LinkedList;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionMultiplicity;
import org.java.plugin.registry.ExtensionPoint;

/**
 * @version $Id$
 */
public class MockExtensionPoint extends MockPluginElement<ExtensionPoint>
        implements ExtensionPoint {
    private ExtensionMultiplicity multiplicity;
    private String parentExtensionPointId;
    private String parentPluginId;
    private boolean isValid = true;
    private LinkedList<Extension> availableExtensions = new LinkedList<Extension>();
    private LinkedList<Extension> connectedExtensions = new LinkedList<Extension>();
    private LinkedList<ExtensionPoint> descendants = new LinkedList<ExtensionPoint>();
    private LinkedList<ParameterDefinition> parameterDefinitions = new LinkedList<ParameterDefinition>();
    private HashSet<String> predecessors = new HashSet<String>();

    /**
     * @see org.java.plugin.registry.ExtensionPoint#getAvailableExtension(
     *      java.lang.String)
     */
    public Extension getAvailableExtension(final String uniqueId) {
        for (Extension ext : availableExtensions) {
            if (ext.getUniqueId().equals(uniqueId)) {
                return ext;
            }
        }
        throw new IllegalArgumentException("extension UID " //$NON-NLS-1$
                + uniqueId + " not available"); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#getAvailableExtensions()
     */
    public Collection<Extension> getAvailableExtensions() {
        return Collections.unmodifiableCollection(availableExtensions);
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#getConnectedExtension(
     *      java.lang.String)
     */
    public Extension getConnectedExtension(final String uniqueId) {
        for (Extension ext : connectedExtensions) {
            if (ext.getUniqueId().equals(uniqueId)) {
                return ext;
            }
        }
        throw new IllegalArgumentException("extension UID " //$NON-NLS-1$
                + uniqueId + " not connected"); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#getConnectedExtensions()
     */
    public Collection<Extension> getConnectedExtensions() {
        return Collections.unmodifiableCollection(connectedExtensions);
    }
    
    /**
     * @param extension extension to add
     * @param isConnected if <code>true</code> extension will be marked as
     *                    "connected" also
     * @return this instance
     */
    public MockExtensionPoint addExtension(final Extension extension,
            final boolean isConnected) {
        availableExtensions.add(extension);
        if (isConnected) {
            connectedExtensions.add(extension);
        }
        return this;
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#getDescendants()
     */
    public Collection<ExtensionPoint> getDescendants() {
        return Collections.unmodifiableCollection(descendants);
    }
    
    /**
     * @param extensionPoint descendant extension to add
     * @return this instance
     */
    public MockExtensionPoint addParameter(final ExtensionPoint extensionPoint) {
        descendants.add(extensionPoint);
        return this;
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#getMultiplicity()
     */
    public ExtensionMultiplicity getMultiplicity() {
        return multiplicity;
    }
    
    /**
     * @param value the multiplicity to set
     * @return this instance
     */
    public MockExtensionPoint setMultiplicity(
            final ExtensionMultiplicity value) {
        multiplicity = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#getParameterDefinition(
     *      java.lang.String)
     */
    public ParameterDefinition getParameterDefinition(String id) {
        for (ParameterDefinition paramDef : parameterDefinitions) {
            if (paramDef.getId().equals(id)) {
                return paramDef;
            }
        }
        throw new IllegalArgumentException(
                "unknown parameter definition ID " + id); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#getParameterDefinitions()
     */
    public Collection<ParameterDefinition> getParameterDefinitions() {
        return Collections.unmodifiableCollection(parameterDefinitions);
    }
    
    /**
     * @param parameterDefinition parameter definition to add
     * @return this instance
     */
    public MockExtensionPoint addParameterDefinition(
            final ParameterDefinition parameterDefinition) {
        parameterDefinitions.add(parameterDefinition);
        return this;
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#getParentExtensionPointId()
     */
    public String getParentExtensionPointId() {
        return parentExtensionPointId;
    }
    
    /**
     * @param pluginId the parent plug-in id to set
     * @param extensionPointId the parent extension point id to set
     * @return this instance
     */
    public MockExtensionPoint setParentExtensionPoint(final String pluginId,
            final String extensionPointId) {
        parentPluginId = pluginId;
        parentExtensionPointId = extensionPointId;
        predecessors.add(pluginId + '@' + extensionPointId);
        return this;
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#getParentPluginId()
     */
    public String getParentPluginId() {
        return parentPluginId;
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#isExtensionAvailable(
     *      java.lang.String)
     */
    public boolean isExtensionAvailable(final String uniqueId) {
        for (Extension ext : availableExtensions) {
            if (ext.getUniqueId().equals(uniqueId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#isExtensionConnected(
     *      java.lang.String)
     */
    public boolean isExtensionConnected(final String uniqueId) {
        for (Extension ext : connectedExtensions) {
            if (ext.getUniqueId().equals(uniqueId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#isSuccessorOf(
     *      org.java.plugin.registry.ExtensionPoint)
     */
    public boolean isSuccessorOf(final ExtensionPoint extensionPoint) {
        return predecessors.contains(extensionPoint.getUniqueId());
    }
    
    /**
     * @param pluginId predecessor plug-in ID to add
     * @param extensionPointId predecessor extension point ID to add
     * @return this instance
     */
    public MockExtensionPoint addPredecessors(final String pluginId,
            final String extensionPointId) {
        predecessors.add(pluginId + '@' + extensionPointId);
        return this;
    }

    /**
     * @see org.java.plugin.registry.ExtensionPoint#isValid()
     */
    public boolean isValid() {
        return isValid;
    }
    
    /**
     * @param value the valid flag to set
     * @return this instance
     */
    public MockExtensionPoint setValid(final boolean value) {
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
