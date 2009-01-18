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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.java.plugin.PathResolver;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.Identity;
import org.java.plugin.registry.IntegrityCheckReport;
import org.java.plugin.registry.ManifestInfo;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginFragment;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.registry.Version;
import org.java.plugin.registry.xml.PluginRegistryImpl;
import org.java.plugin.util.ExtendedProperties;

/**
 * @version $Id: MockPluginRegistry.java,v 1.4 2007/03/03 17:16:22 ddimon Exp $
 */
public class MockPluginRegistry implements PluginRegistry {
    private IntegrityCheckReport integrityCheckReport;
    private PluginRegistryImpl xmlRegistryImpl;
    private LinkedList<RegistryChangeListener> listeners =
        new LinkedList<RegistryChangeListener>();
    private IntegrityCheckReport registrationReport;
    private HashMap<String, ExtensionPoint> extensionPoints =
        new HashMap<String, ExtensionPoint>();
    private HashMap<String, PluginDescriptor> pluginDescriptors =
        new HashMap<String, PluginDescriptor>();
    private LinkedList<PluginFragment> pluginFragments =
        new LinkedList<PluginFragment>();

    /**
     * @see org.java.plugin.registry.PluginRegistry#checkIntegrity(
     *      org.java.plugin.PathResolver)
     */
    public IntegrityCheckReport checkIntegrity(
            final PathResolver pathResolver) {
        return integrityCheckReport;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#checkIntegrity(
     *      org.java.plugin.PathResolver, boolean)
     */
    public IntegrityCheckReport checkIntegrity(final PathResolver pathResolver,
            final boolean includeRegistrationReport) {
        return integrityCheckReport;
    }
    
    /**
     * @param value the integrity check report to set
     * @return this instance
     */
    public MockPluginRegistry setIntegrityCheckReport(
            final IntegrityCheckReport value) {
        integrityCheckReport = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#configure(
     *      org.java.plugin.util.ExtendedProperties)
     */
    public void configure(final ExtendedProperties config) {
        // no-op
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#extractId(
     *      java.lang.String)
     */
    public String extractId(final String uniqueId) {
        if (xmlRegistryImpl == null) {
            xmlRegistryImpl = new PluginRegistryImpl();
        }
        return xmlRegistryImpl.extractId(uniqueId);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#extractPluginId(
     *      java.lang.String)
     */
    public String extractPluginId(final String uniqueId) {
        if (xmlRegistryImpl == null) {
            xmlRegistryImpl = new PluginRegistryImpl();
        }
        return xmlRegistryImpl.extractPluginId(uniqueId);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#extractVersion(
     *      java.lang.String)
     */
    public Version extractVersion(final String uniqueId) {
        if (xmlRegistryImpl == null) {
            xmlRegistryImpl = new PluginRegistryImpl();
        }
        return xmlRegistryImpl.extractVersion(uniqueId);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getDependingPlugins(
     *      org.java.plugin.registry.PluginDescriptor)
     */
    public Collection<PluginDescriptor> getDependingPlugins(
            final PluginDescriptor descr) {
        // no-op
        return Collections.emptyList();
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getExtensionPoint(
     *      java.lang.String, java.lang.String)
     */
    public ExtensionPoint getExtensionPoint(String pluginId, String pointId) {
        String uid = makeUniqueId(pluginId, pointId);
        ExtensionPoint result = extensionPoints.get(uid);
        if (result == null) {
            throw new IllegalArgumentException(
                    "unknown extenssion point UID " + uid); //$NON-NLS-1$
        }
        return result;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getExtensionPoint(
     *      java.lang.String)
     */
    public ExtensionPoint getExtensionPoint(String uniqueId) {
        ExtensionPoint result = extensionPoints.get(uniqueId);
        if (result == null) {
            throw new IllegalArgumentException(
                    "unknown extenssion point UID " + uniqueId); //$NON-NLS-1$
        }
        return result;
    }
    
    /**
     * @param extPoint extension point to add
     * @return this instance
     */
    public MockPluginRegistry addExtensionPoint(final ExtensionPoint extPoint) {
        extensionPoints.put(extPoint.getUniqueId(), extPoint);
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getPluginDescriptor(
     *      java.lang.String)
     */
    public PluginDescriptor getPluginDescriptor(final String pluginId) {
        PluginDescriptor result =
            pluginDescriptors.get(pluginId);
        if (result == null) {
            throw new IllegalArgumentException(
                    "unknown plug-in ID " + pluginId); //$NON-NLS-1$
        }
        return result;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getPluginDescriptors()
     */
    public Collection<PluginDescriptor> getPluginDescriptors() {
        return Collections.unmodifiableCollection(pluginDescriptors.values());
    }
    
    /**
     * @param descr plug-in descriptor to add
     * @return this instance
     */
    public MockPluginRegistry addPluginDescriptor(
            final PluginDescriptor descr) {
        pluginDescriptors.put(descr.getId(), descr);
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getPluginFragments()
     */
    public Collection<PluginFragment> getPluginFragments() {
        return Collections.unmodifiableCollection(pluginFragments);
    }
    
    /**
     * @param fragment plug-in fragment to add
     * @return this instance
     */
    public MockPluginRegistry addPluginFragment(final PluginFragment fragment) {
        pluginFragments.add(fragment);
        return this;
    }
    
    /**
     * @see org.java.plugin.registry.PluginRegistry#getRegistrationReport()
     */
    public IntegrityCheckReport getRegistrationReport() {
        return registrationReport;
    }
    
    /**
     * @param value the registration report to set
     * @return this instance
     */
    public MockPluginRegistry setRegistrationReport(
            final IntegrityCheckReport value) {
        registrationReport = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#isExtensionPointAvailable(
     *      java.lang.String, java.lang.String)
     */
    public boolean isExtensionPointAvailable(final String pluginId,
            final String pointId) {
        return extensionPoints.containsKey(makeUniqueId(pluginId, pointId));
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#isExtensionPointAvailable(
     *      java.lang.String)
     */
    public boolean isExtensionPointAvailable(final String uniqueId) {
        return extensionPoints.containsKey(uniqueId);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#isPluginDescriptorAvailable(
     *      java.lang.String)
     */
    public boolean isPluginDescriptorAvailable(final String pluginId) {
        return pluginDescriptors.containsKey(pluginId);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#makeUniqueId(
     *      java.lang.String, java.lang.String)
     */
    public String makeUniqueId(final String pluginId, final String elementId) {
        if (xmlRegistryImpl == null) {
            xmlRegistryImpl = new PluginRegistryImpl();
        }
        return xmlRegistryImpl.makeUniqueId(pluginId, elementId);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#makeUniqueId(
     *      java.lang.String, org.java.plugin.registry.Version)
     */
    public String makeUniqueId(final String pluginId, final Version version) {
        if (xmlRegistryImpl == null) {
            xmlRegistryImpl = new PluginRegistryImpl();
        }
        return xmlRegistryImpl.makeUniqueId(pluginId, version);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#readManifestInfo(
     *      java.net.URL)
     */
    public ManifestInfo readManifestInfo(final URL manifest) {
        // no-op
        return null;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#register(java.net.URL[])
     */
    public Map<String, Identity> register(final URL[] manifests) {
        // no-op
        return Collections.emptyMap();
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#registerListener(
     *      org.java.plugin.registry.PluginRegistry.RegistryChangeListener)
     */
    public void registerListener(final RegistryChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#unregister(
     *      java.lang.String[])
     */
    public Collection<String> unregister(final String[] ids) {
        // no-op
        return Collections.emptyList();
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#unregisterListener(
     *      org.java.plugin.registry.PluginRegistry.RegistryChangeListener)
     */
    public void unregisterListener(final RegistryChangeListener listener) {
        listeners.remove(listener);
    }
}
