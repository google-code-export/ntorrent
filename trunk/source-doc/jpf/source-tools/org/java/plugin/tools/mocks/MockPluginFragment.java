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

import org.java.plugin.registry.Documentation;
import org.java.plugin.registry.MatchingRule;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginFragment;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.registry.Version;

/**
 * @version $Id$
 */
public class MockPluginFragment extends MockIdentity implements PluginFragment {
    private URL location;
    private String pluginId;
    private Version pluginVersion;
    private PluginRegistry registry;
    private String vendor;
    private Version version;
    private String docsPath;
    private Documentation<PluginFragment> documentation;
    private MatchingRule matchingRule;

    /**
     * @see org.java.plugin.registry.PluginFragment#getLocation()
     */
    public URL getLocation() {
        return location;
    }
    
    /**
     * @param value the location to set
     * @return this instance
     */
    public MockPluginFragment setLocation(final URL value) {
        location = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginFragment#getPluginId()
     */
    public String getPluginId() {
        return pluginId;
    }
    
    /**
     * @param value the plug-in ID to set
     * @return this instance
     */
    public MockPluginFragment setPluginId(final String value) {
        pluginId = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginFragment#getPluginVersion()
     */
    public Version getPluginVersion() {
        return pluginVersion;
    }
    
    /**
     * @param value the plug-in version to set
     * @return this instance
     */
    public MockPluginFragment setPluginVersion(final Version value) {
        pluginVersion = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginFragment#getRegistry()
     */
    public PluginRegistry getRegistry() {
        return registry;
    }
    
    /**
     * @param value the registry to set
     * @return this instance
     */
    public MockPluginFragment setRegistry(final PluginRegistry value) {
        registry = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginFragment#getVendor()
     */
    public String getVendor() {
        return vendor;
    }
    
    /**
     * @param value the vendor to set
     * @return this instance
     */
    public MockPluginFragment setVendor(final String value) {
        vendor = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginFragment#getVersion()
     */
    public Version getVersion() {
        return version;
    }
    
    /**
     * @param value the version to set
     * @return this instance
     */
    public MockPluginFragment setVersion(final Version value) {
        version = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginFragment#matches(
     *      org.java.plugin.registry.PluginDescriptor)
     */
    public boolean matches(final PluginDescriptor descr) {
        return getVersion().isCompatibleWith(descr.getVersion());
    }
    
    /**
     * @see org.java.plugin.registry.PluginFragment#getMatchingRule()
     */
    public MatchingRule getMatchingRule() {
        return matchingRule;
    }
    
    /**
     * @param value the matching rule to set
     * @return this instance
     */
    public MockPluginFragment setMatchingRule(final MatchingRule value) {
        matchingRule = value;
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
    public MockPluginFragment setDocsPath(final String value) {
        docsPath = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.Documentable#getDocumentation()
     */
    public Documentation<PluginFragment> getDocumentation() {
        return documentation;
    }

    /**
     * @param value the documentation to set
     * @return this instance
     */
    public MockPluginFragment setDocumentation(
            final Documentation<PluginFragment> value) {
        documentation = value;
        return this;
    }
}
