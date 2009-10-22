/*****************************************************************************
 * Java Plug-in Framework (JPF)
 * Copyright (C) 2004-2007 Dmitry Olshansky
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
package org.java.plugin.tools.ant;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.java.plugin.ObjectFactory;
import org.java.plugin.registry.ManifestInfo;
import org.java.plugin.registry.ManifestProcessingException;
import org.java.plugin.registry.MatchingRule;
import org.java.plugin.registry.Version;
import org.java.plugin.util.IoUtil;

/**
 * Simple task to read some data from plug-in manifest into project properties.
 * <p>
 * Inspired by Sebastian Kopsan.
 * 
 * @version $Id$
 */
public class PluginInfoTask extends Task {
    private File manifest;
    private String propertyId;
    private String propertyVersion;
    private String propertyVendor;
    private String propertyPluginId;
    private String propertyPluginVersion;
    private String propertyMatchingRule;

    /**
     * @param aManifest plug-in manifest to read data from
     */
    public void setManifest(final File aManifest) {
        manifest = aManifest;
    }
    
    /**
     * @param propertyName name of the property to read plug-in or plug-in
     *                     fragment ID in
     * 
     * @see org.java.plugin.registry.ManifestInfo#getId()
     */
    public void setPropertyId(String propertyName) {
        propertyId = propertyName;
    }
    
    /**
     * @param propertyName name of the property to read plug-in or plug-in
     *                     fragment version in
     * 
     * @see org.java.plugin.registry.ManifestInfo#getVersion()
     */
    public void setPropertyVersion(String propertyName) {
        propertyVersion = propertyName;
    }
    
    /**
     * @param propertyName name of the property to read plug-in or plug-in
     *                     fragment vendor in
     * 
     * @see org.java.plugin.registry.ManifestInfo#getVendor()
     */
    public void setPropertyVendor(String propertyName) {
        propertyVendor = propertyName;
    }
    
    /**
     * @param propertyName name of the property to read plug-in ID in
     * 
     * @see org.java.plugin.registry.ManifestInfo#getPluginId()
     */
    public void setPropertyPluginId(String propertyName) {
        propertyPluginId = propertyName;
    }
    
    /**
     * @param propertyName name of the property to read plug-in version in
     * 
     * @see org.java.plugin.registry.ManifestInfo#getPluginVersion()
     */
    public void setPropertyPluginVersion(String propertyName) {
        propertyPluginVersion = propertyName;
    }
    
    /**
     * @param propertyName name of the property to read plug-in fragment
     *                     matching rule in
     * 
     * @see org.java.plugin.registry.ManifestInfo#getMatchingRule()
     */
    public void setPropertyMatchingRule(String propertyName) {
        this.propertyMatchingRule = propertyName;
    }
    
    /**
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {
        if (manifest == null) {
            throw new BuildException("manifest attribute must be set!", //$NON-NLS-1$
                    getLocation());
        }
        URL url;
        try {
            url = IoUtil.file2url(manifest);
        } catch (MalformedURLException mue) {
            throw new BuildException("failed converting file " + manifest //$NON-NLS-1$
                    + " to URL", mue, getLocation()); //$NON-NLS-1$
        }
        ManifestInfo manifestInfo;
        try {
            manifestInfo = ObjectFactory.newInstance().createRegistry()
                .readManifestInfo(url);
            //log("Data read from manifest " + manifest); //$NON-NLS-1$
        } catch (ManifestProcessingException mpe) {
            throw new BuildException("failed reading data from manifest " //$NON-NLS-1$
                    + url, mpe, getLocation());
        }
        if (propertyId != null) {
            getProject().setProperty(propertyId, manifestInfo.getId());
        }
        if (propertyVersion != null) {
            Version version = manifestInfo.getVersion();
            getProject().setProperty(propertyVersion,
                    (version != null) ? version.toString() : ""); //$NON-NLS-1$
        }
        if (propertyVendor != null) {
            String value = manifestInfo.getVendor();
            getProject().setProperty(propertyVendor,
                    (value != null) ? value : ""); //$NON-NLS-1$
        }
        if (propertyPluginId != null) {
            String value = manifestInfo.getPluginId();
            getProject().setProperty(propertyPluginId,
                    (value != null) ? value : ""); //$NON-NLS-1$
        }
        if (propertyPluginVersion != null) {
            Version version = manifestInfo.getPluginVersion();
            getProject().setProperty(propertyPluginVersion,
                    (version != null) ? version.toString() : ""); //$NON-NLS-1$
        }
        if (propertyMatchingRule != null) {
            MatchingRule value = manifestInfo.getMatchingRule();
            getProject().setProperty(propertyMatchingRule,
                    (value != null) ? value.toCode() : ""); //$NON-NLS-1$
        }
    }
}
