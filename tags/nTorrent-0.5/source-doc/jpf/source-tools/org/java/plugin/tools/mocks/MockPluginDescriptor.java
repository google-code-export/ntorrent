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
import java.util.LinkedList;
import org.java.plugin.registry.Documentation;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.Library;
import org.java.plugin.registry.PluginAttribute;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginFragment;
import org.java.plugin.registry.PluginPrerequisite;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.registry.Version;

/**
 * @version $Id$
 */
public class MockPluginDescriptor extends MockIdentity
        implements PluginDescriptor {
    private URL location;
    private String pluginClassName;
    private PluginRegistry registry;
    private String vendor;
    private Version version;
    private String docsPath;
    private Documentation<PluginDescriptor> documentation;
    private LinkedList<PluginAttribute> attributes = new LinkedList<PluginAttribute>();
    private LinkedList<Extension> extensions = new LinkedList<Extension>();
    private LinkedList<ExtensionPoint> extPoints = new LinkedList<ExtensionPoint>();
    private LinkedList<PluginFragment> fragments = new LinkedList<PluginFragment>();
    private LinkedList<Library> libraries = new LinkedList<Library>();
    private LinkedList<PluginPrerequisite> prerequisites = new LinkedList<PluginPrerequisite>();
    
    /**
     * No-arguments constructor.
     */
    public MockPluginDescriptor() {
        // no-op
    }

    /**
     * @param id plug-in ID
     */
    public MockPluginDescriptor(final String id) {
        setId(id);
    }

    /**
     * @param id plug-in ID
     * @param aVersion plug-in version
     */
    public MockPluginDescriptor(final String id, final Version aVersion) {
        setId(id);
        setVersion(aVersion);
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getAttribute(
     *      java.lang.String)
     */
    public PluginAttribute getAttribute(final String id) {
        for (PluginAttribute attr : attributes) {
            if (attr.getId().equals(id))
                return attr;
        }
        throw new IllegalArgumentException("unknown attribute ID " + id); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getAttributes()
     */
    public Collection<PluginAttribute> getAttributes() {
        return Collections.unmodifiableCollection(attributes);
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getAttributes(
     *      java.lang.String)
     */
    public Collection<PluginAttribute> getAttributes(final String id) {
        LinkedList<PluginAttribute> result = new LinkedList<PluginAttribute>();
        for (PluginAttribute attr : attributes) {
            if (attr.getId().equals(id)) {
                result.add(attr);
            }
        }
        return result;
    }
    
    /**
     * @param attribute attribute to add
     * @return this instance
     */
    public MockPluginDescriptor addAttribute(final PluginAttribute attribute) {
        attributes.add(attribute);
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getExtension(
     *      java.lang.String)
     */
    public Extension getExtension(final String id) {
        for (Extension ext : extensions) {
            if (ext.getId().equals(id)) {
                return ext;
            }
        }
        throw new IllegalArgumentException("unknown extension ID " + id); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getExtensionPoint(
     *      java.lang.String)
     */
    public ExtensionPoint getExtensionPoint(final String id) {
        for (ExtensionPoint extPoint : extPoints) {
            if (extPoint.getId().equals(id)) {
                return extPoint;
            }
        }
        throw new IllegalArgumentException("unknown extension point ID " + id); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getExtensionPoints()
     */
    public Collection<ExtensionPoint> getExtensionPoints() {
        return Collections.unmodifiableCollection(extPoints);
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getExtensions()
     */
    public Collection<Extension> getExtensions() {
        return Collections.unmodifiableCollection(extensions);
    }
    
    /**
     * @param extPoint extension point to add
     * @return this instance
     */
    public MockPluginDescriptor addExtensionPoint(
            final ExtensionPoint extPoint) {
        extPoints.add(extPoint);
        return this;
    }
    
    /**
     * @param extension extension to add
     * @return this instance
     */
    public MockPluginDescriptor addExtension(final Extension extension) {
        extensions.add(extension);
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getFragments()
     */
    public Collection<PluginFragment> getFragments() {
        return Collections.unmodifiableCollection(fragments);
    }
    
    /**
     * @param fragment plug-in fragment to add
     * @return this instance
     */
    public MockPluginDescriptor addFragment(final PluginFragment fragment) {
        fragments.add(fragment);
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getLibraries()
     */
    public Collection<Library> getLibraries() {
        return Collections.unmodifiableCollection(libraries);
    }
    
    /**
     * @param library library to add
     * @return this instance
     */
    public MockPluginDescriptor addLibrary(final Library library) {
        libraries.add(library);
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getLibrary(
     *      java.lang.String)
     */
    public Library getLibrary(final String id) {
        for (Library lib : libraries) {
            if (lib.getId().equals(id)) {
                return lib;
            }
        }
        throw new IllegalArgumentException("unknown library ID " + id); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getLocation()
     */
    public URL getLocation() {
        return location;
    }
    
    /**
     * @param value the location to set
     * @return this instance
     */
    public MockPluginDescriptor setLocation(final URL value) {
        location = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getPluginClassName()
     */
    public String getPluginClassName() {
        return pluginClassName;
    }
    
    /**
     * @param value the plug-in class name to set
     * @return this instance
     */
    public MockPluginDescriptor setPluginClassName(final String value) {
        pluginClassName = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getPrerequisite(
     *      java.lang.String)
     */
    public PluginPrerequisite getPrerequisite(final String id) {
        for (PluginPrerequisite pre : prerequisites) {
            if (pre.getId().equals(id)) {
                return pre;
            }
        }
        throw new IllegalArgumentException(
                "unknown plug-in prerequisite ID " + id); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getPrerequisites()
     */
    public Collection<PluginPrerequisite> getPrerequisites() {
        return Collections.unmodifiableCollection(prerequisites);
    }
    
    /**
     * @param pre plug-in prerequisite to add
     * @return this instance
     */
    public MockPluginDescriptor addPrerequisite(final PluginPrerequisite pre) {
        prerequisites.add(pre);
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getRegistry()
     */
    public PluginRegistry getRegistry() {
        return registry;
    }
    
    /**
     * @param value the registry to set
     * @return this instance
     */
    public MockPluginDescriptor setRegistry(final PluginRegistry value) {
        registry = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getVendor()
     */
    public String getVendor() {
        return vendor;
    }
    
    /**
     * @param value the vendor to set
     * @return this instance
     */
    public MockPluginDescriptor setVendor(final String value) {
        vendor = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginDescriptor#getVersion()
     */
    public Version getVersion() {
        return version;
    }
    
    /**
     * @param value the version to set
     * @return this instance
     */
    public MockPluginDescriptor setVersion(final Version value) {
        version = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.UniqueIdentity#getUniqueId()
     */
    public String getUniqueId() {
        return getId() + '@' + getVersion();
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
    public MockPluginDescriptor setDocsPath(final String value) {
        docsPath = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Documentable#getDocumentation()
     */
    public Documentation<PluginDescriptor> getDocumentation() {
        return documentation;
    }
    
    /**
     * @param value the documentation to set
     * @return this instance
     */
    public MockPluginDescriptor setDocumentation(
            final Documentation<PluginDescriptor> value) {
        documentation = value;
        return this;
    }
}
