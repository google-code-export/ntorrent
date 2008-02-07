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

import org.java.plugin.registry.MatchingRule;
import org.java.plugin.registry.PluginPrerequisite;
import org.java.plugin.registry.Version;

/**
 * @version $Id$
 */
public class MockPluginPrerequisite
        extends MockPluginElement<PluginPrerequisite>
        implements PluginPrerequisite {
    private String pluginId;
    private Version pluginVersion;
    private boolean isExported;
    private boolean isOptional;
    private boolean isReverseLookup;
    private boolean matches = true;
    private MatchingRule matchingRule;
    
    /**
     * @see org.java.plugin.registry.PluginPrerequisite#getPluginId()
     */
    public String getPluginId() {
        return pluginId;
    }
    
    /**
     * @param value the plug-in id to set
     * @return this instance
     */
    public MockPluginPrerequisite setPluginId(final String value) {
        pluginId = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginPrerequisite#getPluginVersion()
     */
    public Version getPluginVersion() {
        return pluginVersion;
    }
    
    /**
     * @param value the plug-in version to set
     * @return this instance
     */
    public MockPluginPrerequisite setPluginVersion(final Version value) {
        pluginVersion = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginPrerequisite#isExported()
     */
    public boolean isExported() {
        return isExported;
    }
    
    /**
     * @param value the exported flag to set
     * @return this instance
     */
    public MockPluginPrerequisite setExported(final boolean value) {
        isExported = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginPrerequisite#isOptional()
     */
    public boolean isOptional() {
        return isOptional;
    }
    
    /**
     * @param value the optional flag to set
     * @return this instance
     */
    public MockPluginPrerequisite setOptional(final boolean value) {
        isOptional = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginPrerequisite#isReverseLookup()
     */
    public boolean isReverseLookup() {
        return isReverseLookup;
    }
    
    /**
     * @param value the reverse look-up flag to set
     * @return this instance
     */
    public MockPluginPrerequisite setReverseLookup(final boolean value) {
        isReverseLookup = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.PluginPrerequisite#matches()
     */
    public boolean matches() {
        return matches;
    }
    
    /**
     * @param value the matches flag to set
     * @return this instance
     */
    public MockPluginPrerequisite setMatches(final boolean value) {
        matches = value;
        return this;
    }
    
    /**
     * @see org.java.plugin.registry.PluginPrerequisite#getMatchingRule()
     */
    public MatchingRule getMatchingRule() {
        return matchingRule;
    }
    
    /**
     * @param value the matchingRule to set
     * @return this instance
     */
    public MockPluginPrerequisite setMatchingRule(final MatchingRule value) {
        matchingRule = value;
        return this;
    }

    /**
     * @see org.java.plugin.registry.UniqueIdentity#getUniqueId()
     */
    public String getUniqueId() {
        return getDeclaringPluginDescriptor().getId() + '@' + getId();
    }
}
