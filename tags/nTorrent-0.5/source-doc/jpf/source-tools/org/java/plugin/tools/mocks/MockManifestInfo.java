/*****************************************************************************
 * Java Plug-in Framework (JPF)
 * Copyright (C) 2007 Dmitry Olshansky
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

import org.java.plugin.registry.ManifestInfo;
import org.java.plugin.registry.MatchingRule;
import org.java.plugin.registry.Version;

/**
 * @version $Id: MockManifestInfo.java,v 1.1 2007/02/06 16:25:16 ddimon Exp $
 */
public class MockManifestInfo implements ManifestInfo {
    private String id;
    private MatchingRule matchingRule;
    private String pluginId;
    private Version pluginVersion;
    private String vendor;
    private Version version;

    /**
     * @see org.java.plugin.registry.ManifestInfo#getId()
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param value the id to set
     * @return this instance
     */
    public MockManifestInfo setId(final String value) {
        id = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.ManifestInfo#getMatchingRule()
     */
    public MatchingRule getMatchingRule() {
        return matchingRule;
    }
    
    /**
     * @param value the matching rule to set
     * @return this instance
     */
    public MockManifestInfo setMatchingRule(final MatchingRule value) {
        matchingRule = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.ManifestInfo#getPluginId()
     */
    public String getPluginId() {
        return pluginId;
    }
    
    /**
     * @param value the plug-in id to set
     * @return this instance
     */
    public MockManifestInfo setPluginId(final String value) {
        pluginId = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.ManifestInfo#getPluginVersion()
     */
    public Version getPluginVersion() {
        return pluginVersion;
    }
    
    /**
     * @param value the plug-in version to set
     * @return this instance
     */
    public MockManifestInfo setPluginVersion(final Version value) {
        pluginVersion = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.ManifestInfo#getVendor()
     */
    public String getVendor() {
        return vendor;
    }
    
    /**
     * @param value the vendor to set
     * @return this instance
     */
    public MockManifestInfo setVendor(final String value) {
        vendor = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.ManifestInfo#getVersion()
     */
    public Version getVersion() {
        return version;
    }
    
    /**
     * @param value the version to set
     * @return this instance
     */
    public MockManifestInfo setVersion(final Version value) {
        version = value;
        return this;
    }
}
