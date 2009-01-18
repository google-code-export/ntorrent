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
package org.java.plugin.tools.ant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.tools.ant.BuildException;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginFragment;
import org.java.plugin.registry.Version;
import org.java.plugin.util.IoUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <p>
 * This class can upgrade all version and plugin-version tags in all plugin
 * manifest files, to the latest version specified in a text file (in Java
 * properties format). This class also handles updating the build number in the
 * specified file.
 * <p>
 * This class will only upgrade 'version' and 'plugin-version' tags that already
 * exist in the manifest files, so it won't add any to the manifest files.
 * <p>
 * This class tracks plug-in modification timestamp's and keep them together
 * with versions info in the given text file. The actual plug-in version will be
 * upgraded only if plug-in timestamp changes.
 * 
 * @author Jonathan Giles
 * @author Dmitry Olshansky
 */
public class VersionUpdateTask extends BaseJpfTask {
    private File versionsFile;
    private boolean alterReferences = false;
    private boolean timestampVersion = false;
    
    /**
     * @param value <code>true</code> if version references should be upgraded
     */
    public final void setAlterReferences(final boolean value) {
        alterReferences = value;
    }

    /**
     * @param value file where to store versioning related info
     */
    public void setVersionsFile(final File value) {
        versionsFile = value;
    }
    
    /**
     * @param value if <code>true</code>, the plug-in timestamp will be included
     *              into version {@link Version#getName() name} attribute
     */
    public void setTimestampVersion(final boolean value) {
        timestampVersion = value;
    }

    /**
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {
        if (versionsFile == null) {
            throw new BuildException("versionsfile attribute must be set!", //$NON-NLS-1$
                    getLocation());
        }
        initRegistry(true);
        // reading contents of versions file
        if (getVerbose()) {
            log("Loading versions file " + versionsFile); //$NON-NLS-1$
        }
        Properties versions = new Properties();
        if (versionsFile.exists()) {
            try {
                InputStream in =
                    new BufferedInputStream(new FileInputStream(versionsFile));
                try {
                    versions.load(in);
                } finally {
                    in.close();
                }
            } catch (IOException ioe) {
                throw new BuildException("failed reading versions file " //$NON-NLS-1$
                        + versionsFile, ioe, getLocation());
            }
        } else {
            log("Versions file " + versionsFile //$NON-NLS-1$
                    + " not found, new one will be created."); //$NON-NLS-1$
        }
        Map<String, PluginInfo> infos = new HashMap<String, PluginInfo>();
        // collecting manifests
        for (PluginDescriptor descr : getRegistry().getPluginDescriptors()) {
            File manifestFile = IoUtil.url2file(descr.getLocation());
            if (manifestFile == null) {
                throw new BuildException(
                        "non-local plug-in manifest URL given " //$NON-NLS-1$
                        + descr.getLocation());
            }
            URL homeUrl = getPathResolver().resolvePath(descr, "/"); //$NON-NLS-1$
            File homeFile = IoUtil.url2file(homeUrl);
            if (homeFile == null) {
                throw new BuildException(
                        "non-local plug-in home URL given " //$NON-NLS-1$
                        + homeUrl);
            }
            try {
                infos.put(descr.getId(), new PluginInfo(descr.getId(),
                        versions, manifestFile, homeFile, descr.getVersion()));
            } catch (ParseException pe) {
                throw new BuildException("failed parsing versions data " //$NON-NLS-1$
                        + " for manifest " + manifestFile, pe, getLocation()); //$NON-NLS-1$
            }
            if (getVerbose()) {
                log("Collected manifest file " + manifestFile); //$NON-NLS-1$
            }
        }
        for (PluginFragment descr : getRegistry().getPluginFragments()) {
            File manifestFile = IoUtil.url2file(descr.getLocation());
            if (manifestFile == null) {
                throw new BuildException(
                        "non-local plug-in fragment manifest URL given " //$NON-NLS-1$
                        + descr.getLocation());
            }
            URL homeUrl = getPathResolver().resolvePath(descr, "/"); //$NON-NLS-1$
            File homeFile = IoUtil.url2file(homeUrl);
            if (homeFile == null) {
                throw new BuildException(
                        "non-local plug-in fragment home URL given " //$NON-NLS-1$
                        + homeUrl);
            }
            try {
                infos.put(descr.getId(), new PluginInfo(descr.getId(),
                        versions, manifestFile, homeFile, descr.getVersion()));
            } catch (ParseException pe) {
                throw new BuildException("failed parsing versions data " //$NON-NLS-1$
                        + " for manifest " + manifestFile, pe, getLocation()); //$NON-NLS-1$
            }
            if (getVerbose()) {
                log("Populated manifest file " + manifestFile); //$NON-NLS-1$
            }
        }
        // processing manifest versions
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setValidating(false);
        DocumentBuilder builder;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            throw new BuildException("can't obtain XML document builder ", //$NON-NLS-1$
                    pce, getLocation());
        }
        if (getVerbose()) {
            log("Processing versions"); //$NON-NLS-1$
        }
        for (PluginInfo info : infos.values()) {
            try {
                info.processVersion(versions, builder, timestampVersion);
            } catch (Exception e) {
                throw new BuildException("failed processing manifest " //$NON-NLS-1$
                        + info.getManifestFile(), e, getLocation());
            }
        }
        if (alterReferences) {
            if (getVerbose()) {
                log("Processing version references"); //$NON-NLS-1$
            }
            for (PluginInfo info : infos.values()) {
                try {
                    info.processVersionReferences(versions, builder);
                } catch (Exception e) {
                    throw new BuildException("failed processing manifest " //$NON-NLS-1$
                            + info.getManifestFile(), e, getLocation());
                }
            }
        }
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException tce) {
            throw new BuildException("can't obtain XML document transformer ", //$NON-NLS-1$
                    tce, getLocation());
        } catch (TransformerFactoryConfigurationError tfce) {
            throw new BuildException(
                    "can't obtain XML document transformer factory ", //$NON-NLS-1$
                    tfce, getLocation());
        }
        transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); //$NON-NLS-1$
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no"); //$NON-NLS-1$
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                "-//JPF//Java Plug-in Manifest 1.0"); //$NON-NLS-1$
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                "http://jpf.sourceforge.net/plugin_1_0.dtd"); //$NON-NLS-1$
        if (getVerbose()) {
            log("Saving manifests"); //$NON-NLS-1$
        }
        for (PluginInfo info : infos.values()) {
            try {
                info.save(versions, transformer);
            } catch (Exception e) {
                throw new BuildException("failed saving manifest " //$NON-NLS-1$
                        + info.getManifestFile(), e, getLocation());
            }
        }
        // saving versions
        if (getVerbose()) {
            log("Saving versions file " + versionsFile); //$NON-NLS-1$
        }
        try {
            OutputStream out =
                new BufferedOutputStream(new FileOutputStream(versionsFile));
            try {
                versions.store(out, "Plug-in and plug-in fragment versions file"); //$NON-NLS-1$
            } finally {
                out.close();
            }
        } catch (IOException ioe) {
            throw new BuildException("failed writing versions file " //$NON-NLS-1$
                    + versionsFile, ioe, getLocation());
        }
        log("Plug-in versions update done"); //$NON-NLS-1$
    }
    
    static final class PluginInfo {
        private static Date getTimestamp(final File file,
                final Date parentTimestamp) {
            Date result;
            if (parentTimestamp == null) {
                result = new Date(file.lastModified());
            } else {
                result = new Date(Math.max(parentTimestamp.getTime(),
                        file.lastModified()));
            }
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    result = getTimestamp(files[i], result);
                }
            }
            return result;
        }
        
        private static Version upgradeVersion(final Version ver) {
            if (ver == null) {
                return new Version(0, 0, 1, null);
            }
            return new Version(ver.getMajor(), ver.getMinor(),
                    ver.getBuild() + 1, ver.getName());
        }

        private static Version upgradeVersion(final Version ver,
                final Date timestamp) {
            DateFormat dtf =
                new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH); //$NON-NLS-1$
            if (ver == null) {
                return new Version(0, 0, 1, dtf.format(timestamp));
            }
            return new Version(ver.getMajor(), ver.getMinor(),
                    ver.getBuild() + 1, dtf.format(timestamp));
        }

        private final String id;
        private final File manifestFile;
        private final Version oldVersion;
        private final Date oldTimestamp;
        private final DateFormat dtf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH); //$NON-NLS-1$
        private Document doc;
        private Version newVersion;
        private Date currentTimestamp;
        
        PluginInfo(final String anId, final Properties versions,
                final File aMnifestFile, final File homeFile,
                final Version currentVersion) throws ParseException {
            id = anId;
            String versionStr = versions.getProperty(anId, null);
            String timestampStr = versions.getProperty(anId + ".timestamp", null); //$NON-NLS-1$
            if (versionStr != null) {
                oldVersion = Version.parse(versionStr);
            } else {
                oldVersion = currentVersion;
            }
            if (timestampStr != null) {
                oldTimestamp = dtf.parse(timestampStr);
            } else {
                oldTimestamp = null;
            }
            manifestFile = aMnifestFile;
            currentTimestamp = getTimestamp(homeFile, null);
            versions.setProperty(id + ".timestamp", //$NON-NLS-1$
                    dtf.format(currentTimestamp));
        }
        
        File getManifestFile() {
            return manifestFile;
        }
        
        void processVersion(final Properties versions,
                final DocumentBuilder builder, final boolean timestampVersion)
                throws Exception {
            if (IoUtil.compareFileDates(oldTimestamp, currentTimestamp)) {
                newVersion = oldVersion;
            } else {
                newVersion = !timestampVersion ? upgradeVersion(oldVersion)
                        : upgradeVersion(oldVersion, currentTimestamp);
            }
            versions.setProperty(id, newVersion.toString());
            if (oldVersion.equals(newVersion)) {
                return;
            }
            doc = builder.parse(manifestFile);
            Element root = doc.getDocumentElement();
            if (root.hasAttribute("version")) { //$NON-NLS-1$
                root.setAttribute("version", newVersion.toString()); //$NON-NLS-1$
            }
        }
        
        void processVersionReferences(final Properties versions,
                final DocumentBuilder builder) throws Exception {
            if (doc == null) {
                doc = builder.parse(manifestFile);
            }
            Element root = doc.getDocumentElement();
            if ("plugin-fragment".equals(root.getNodeName())) { //$NON-NLS-1$
                processVersionReference(versions, root);
            }
            NodeList imports = root.getElementsByTagName("import"); //$NON-NLS-1$
            for (int i = 0; i < imports.getLength(); i++) {
                processVersionReference(versions, (Element) imports.item(i));
            }
        }
        
        private void processVersionReference(final Properties versions,
                final Element elm) {
            if (!elm.hasAttribute("plugin-version")) { //$NON-NLS-1$
                return;
            }
            String version = versions.getProperty(
                    elm.getAttribute("plugin-id"), id); //$NON-NLS-1$
            if (version != null) {
                elm.setAttribute("plugin-version", version); //$NON-NLS-1$
            }
        }

        void save(final Properties versions, final Transformer transformer)
                throws Exception {
            if (doc == null) {
                return;
            }
            long modified = manifestFile.lastModified();
            transformer.transform(new DOMSource(doc),
                    new StreamResult(manifestFile));
            manifestFile.setLastModified(modified);
        }
    }
}
