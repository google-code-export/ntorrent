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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginFragment;
import org.java.plugin.registry.PluginPrerequisite;
import org.java.plugin.registry.UniqueIdentity;
import org.java.plugin.util.IoUtil;

/**
 * The Ant task to sort plug-ins and plug-in fragments in correct build order.
 * 
 * @version $Id: SortTask.java,v 1.2 2007/05/13 16:10:12 ddimon Exp $
 */
public class SortTask extends BaseJpfTask {
    /**
     * Put plug-in directory into output path.
     */
    public static final String MODE_DIR = "DIR"; //$NON-NLS-1$
    
    /**
     * Put plug-in's <code>build.xml</code> file into output path.
     */
    public static final String MODE_BUILD = "BUILD"; //$NON-NLS-1$
    
    /**
     * Put original plug-in manifest file into output path.
     */
    public static final String MODE_MANIFEST = "MANIFEST"; //$NON-NLS-1$
    
    private String pathId;
    private String pathIdRef;
    private String pathMode = MODE_MANIFEST;
    private boolean reverse;
    
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
     * @param value the output path mode to set (DIR, BUILD, MANIFEST)
     */
    public void setPathMode(final String value) {
        pathMode = value;
    }
    
    /**
     * @param value sets the reverse sort order
     */
    public void setReverse(final boolean value) {
        reverse = value;
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
        initRegistry(true);
        List<PluginDescriptor> descriptors = new ArrayList<PluginDescriptor>(
                getRegistry().getPluginDescriptors());
        reorder(descriptors);
        Collection<PluginFragment> fragments =
            getRegistry().getPluginFragments();
        List<UniqueIdentity> manifests = new ArrayList<UniqueIdentity>(
                descriptors.size() + fragments.size());
        manifests.addAll(descriptors);
        for (PluginFragment fragment : fragments) {
            int p = manifests.indexOf(
                    getRegistry().getPluginDescriptor(fragment.getPluginId()));
            if (p == -1) {
                p = manifests.size() - 1;
            }
            manifests.add(p + 1, fragment);
        }
        if (reverse) {
            Collections.reverse(manifests);
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
        int count = 0;
        for (UniqueIdentity idt : manifests) {
            URL url;
            if (idt instanceof PluginFragment) {
                url = ((PluginFragment) idt).getLocation();
            } else {
                url = ((PluginDescriptor) idt).getLocation();
            }
            File file = getResultFile(url);
            if (file == null) {
                continue;
            }
            path.setLocation(file);
            count++;
            if (getVerbose()) {
                log("Collected file " + file //$NON-NLS-1$
                        + " for manifest " + url //$NON-NLS-1$
                        + " (" + idt + ")"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        if (getVerbose()) {
            log("Collected path entries: " + count); //$NON-NLS-1$
        }
    }

    /**
     * @param manifestUrl plug-in or plug-in fragment manifest URL
     * @return file to be included in result path
     */
    protected File getResultFile(final URL manifestUrl) {
        File manifestFile = IoUtil.url2file(manifestUrl);
        if (manifestFile == null) {
            log("Ignoring non-local URL " + manifestUrl); //$NON-NLS-1$
            return null;
        }
        if (MODE_MANIFEST.equalsIgnoreCase(pathMode)) {
            return manifestFile;
        }
        if (MODE_DIR.equalsIgnoreCase(pathMode)) {
            return manifestFile.getParentFile();
        }
        if (MODE_BUILD.equalsIgnoreCase(pathMode)) {
            return new File(manifestFile.getParentFile(), "build.xml"); //$NON-NLS-1$
        }
        return manifestFile;
    }
    
    protected void reorder(final List<PluginDescriptor> descriptors) {
        for (int i = 0; i < descriptors.size(); i++) {
            for (int j = i + 1; j < descriptors.size(); j++) {
                if (isDepends(descriptors.get(i), descriptors.get(j))) {
                    Collections.swap(descriptors, i, j);
                    i = -1;
                    break;
                }
            }
        }
    }

    private boolean isDepends(final PluginDescriptor descr1,
            final PluginDescriptor descr2) {
        // Circular (mutual) dependencies are treated as absence of dependency
        // at all.
        Set<PluginDescriptor> pre1 = new HashSet<PluginDescriptor>();
        Set<PluginDescriptor> pre2 = new HashSet<PluginDescriptor>();
        collectPrerequisites(descr1, pre1);
        collectPrerequisites(descr2, pre2);
        return pre1.contains(descr2) && !pre2.contains(descr1);
    }
    
    private void collectPrerequisites(final PluginDescriptor descr,
            final Set<PluginDescriptor> result) {
        for (PluginPrerequisite pre : descr.getPrerequisites()) {
            if (!pre.matches()) {
                continue;
            }
            PluginDescriptor descriptor =
                getRegistry().getPluginDescriptor(pre.getPluginId());
            if (result.add(descriptor)) {
                collectPrerequisites(descriptor, result);
            }
        }
    }
}
