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
package org.java.plugin.tools.ant;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.java.plugin.registry.Library;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginFragment;
import org.java.plugin.registry.PluginPrerequisite;
import org.java.plugin.util.IoUtil;

/**
 * The Ant task to prepare classpath according to plug-in manifest(s)
 * declarations.
 * 
 * @version $Id: PathTask.java,v 1.3 2007/03/01 19:11:19 ddimon Exp $
 */
public class PathTask extends BaseJpfTask {
    private String pathId;
    private String pathIdRef;
    private String pluginId;
    private String pluginIds;
    private boolean followExports = true;
    
    /**
     * @param value the path ID to set
     */
    public void setPathId(final String value) {
        pathId = value;
    }
    
    /**
     * @param value the path ID reference to set
     */
    public void setPathIdRef(final String value) {
        pathIdRef = value;
    }
    
    /**
     * @param value the plug-in ID to set
     */
    public void setPluginId(final String value) {
        pluginId = value;
    }
    
    /**
     * @param value the plug-in ID's to set
     */
    public void setPluginIds(final String value) {
        pluginIds = value;
    }
    
    /**
     * @param value the follow exports flag to set
     */
    public void setFollowExports(final boolean value) {
        followExports = value;
    }
    
    /**
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {
        if ((pathId == null) && (pathIdRef == null)) {
            throw new BuildException(
                    "pathid or pathidref attribute must be set!", //$NON-NLS-1$
                    getLocation());
        }
        Set<String> ids = collectTargetIds();
        if (ids.isEmpty()) {
            throw new BuildException(
                    "pluginid or/and pluginids attribute must be set!", //$NON-NLS-1$
                    getLocation());
        }
        initRegistry(true);
        Set<String> processedIds = new HashSet<String>();
        Set<File> result = new HashSet<File>();
        for (PluginDescriptor descr : getRegistry().getPluginDescriptors()) {
            if (!ids.contains(descr.getId())) {
                continue;
            }
            processDescriptor(result, processedIds, descr, true);
        }
        for (PluginFragment fragment : getRegistry().getPluginFragments()) {
            if (!ids.contains(fragment.getId())) {
                continue;
            }
            processDescriptor(result, processedIds,
                    getRegistry().getPluginDescriptor(fragment.getPluginId()),
                    true);
        }
        Path path;
        if (pathIdRef != null) {
            Object ref = getProject().getReference(pathIdRef);
            if (!(ref instanceof Path)) {
                throw new BuildException(
                        "invalid reference " + pathIdRef //$NON-NLS-1$
                        + ", expected " + Path.class.getName() //$NON-NLS-1$
                        + ", found " + ref, //$NON-NLS-1$
                        getLocation());
            }
            path = (Path) ref;
        } else {
            path = new Path(getProject());
            getProject().addReference(pathId, path);
        }
        for (File file : result) {
            path.setLocation(file);
        }
        if (getVerbose()) {
            log("Collected path entries: " + result.size()); //$NON-NLS-1$
        }
    }

    private void processDescriptor(final Set<File> result,
            final Set<String> processedIds, final PluginDescriptor descr,
            final boolean includePrivate) {
        if (followExports && !includePrivate
                && processedIds.contains(descr.getId())) {
            return;
        }
        processedIds.add(descr.getId());
        for (Library lib : descr.getLibraries()) {
            if (followExports && !includePrivate
                    && lib.getExports().isEmpty()) {
                continue;
            }
            URL url = getPathResolver().resolvePath(lib, lib.getPath());
            File file = IoUtil.url2file(url);
            if (file != null) {
                result.add(file);
                if (getVerbose()) {
                    log("Collected file " + file //$NON-NLS-1$
                            + " from library " + lib.getUniqueId()); //$NON-NLS-1$
                }
            } else {
                log("Ignoring non-local URL " + url //$NON-NLS-1$
                        + " found in library " + lib.getUniqueId()); //$NON-NLS-1$
            }
        }
        for (PluginPrerequisite pre : descr.getPrerequisites()) {
            if (!pre.matches()) {
                continue;
            }
            processDescriptor(result, processedIds,
                    getRegistry().getPluginDescriptor(pre.getPluginId()),
                    false);
        }
    }

    private Set<String> collectTargetIds() {
        HashSet<String> result = new HashSet<String>();
        if (pluginId != null) {
            result.add(pluginId);
        }
        if (pluginIds != null) {
            for (StringTokenizer st =
                    new StringTokenizer(pluginIds, ",", false); //$NON-NLS-1$
                    st.hasMoreTokens();) {
                result.add(st.nextToken());
            }
        }
        return result;
    }
}
