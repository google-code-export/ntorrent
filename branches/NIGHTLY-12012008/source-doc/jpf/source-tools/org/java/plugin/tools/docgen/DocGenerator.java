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
package org.java.plugin.tools.docgen;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.java.plugin.PathResolver;
import org.java.plugin.registry.Documentation;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.Identity;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginElement;
import org.java.plugin.registry.PluginFragment;
import org.java.plugin.registry.PluginPrerequisite;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.util.IoUtil;
import org.onemind.jxp.FilePageSource;
import org.onemind.jxp.JxpProcessingContext;
import org.onemind.jxp.JxpProcessor;

/**
 * Tool class to generate documentation for plug-ins using <a
 * href="http://jxp.sourceforge.net" target="_new">JXP</a> templates.
 * 
 * @version $Id$
 */
public final class DocGenerator {
    private static String getRelativePath(final int level) {
        StringBuilder result = new StringBuilder();
        if (level > 0) {
            for (int i = 0; i < level; i++) {
                if (i > 0) {
                    result.append("/"); //$NON-NLS-1$
                }
                result.append(".."); //$NON-NLS-1$
            }
        } else {
            result.append("."); //$NON-NLS-1$
        }
        return result.toString();
    }

    private final PluginRegistry registry;

    private final PathResolver pathResolver;

    private JxpProcessor processor;

    private Collection<PluginDescriptor> allPluginDescriptors;

    private Collection<PluginFragment> allPluginFragments;

    private Collection<ExtensionPoint> allExtensionPoints;

    private Collection<Extension> allExtensions;

    private String documentationOverview;

    private String stylesheet;

    private String outputEncoding = "UTF-8"; //$NON-NLS-1$

    /**
     * Constructs generator configured to use pre-defined set of templates.
     * 
     * @param aRegistry
     *            plug-ins registry
     * @param aPathResolver
     *            path resolver
     * @throws Exception
     *             if an error has occurred
     */
    public DocGenerator(final PluginRegistry aRegistry,
            final PathResolver aPathResolver) throws Exception {
        this(aRegistry, aPathResolver, DocGenerator.class.getName().substring(
                0, DocGenerator.class.getName().lastIndexOf('.')).replace('.',
                '/')
                + "/templates/", null); //$NON-NLS-1$
    }

    /**
     * Constructs generator configured to use custom templates available in the
     * classpath.
     * 
     * @param aRegistry
     *            plug-ins registry
     * @param aPathResolver
     *            path resolver
     * @param templatesPath
     *            path to templates (should be available in classpath)
     * @param templatesEncoding
     *            templates characters encoding, if <code>null</code>, system
     *            default will be used
     * @throws Exception
     *             if an error has occurred
     */
    public DocGenerator(final PluginRegistry aRegistry,
            final PathResolver aPathResolver, final String templatesPath,
            final String templatesEncoding) throws Exception {
        this(aRegistry, aPathResolver, new JxpProcessor(
                new ClassPathPageSource(templatesPath, templatesEncoding)));
    }

    /**
     * Constructs generator configured to use custom templates located somewhere
     * in the local file system.
     * 
     * @param aRegistry
     *            plug-ins registry
     * @param aPathResolver
     *            path resolver
     * @param templatesFolder
     *            folder with templates
     * @param templatesEncoding
     *            templates characters encoding, if <code>null</code>, system
     *            default will be used
     * @throws Exception
     *             if an error has occurred
     */
    public DocGenerator(final PluginRegistry aRegistry,
            final PathResolver aPathResolver, final File templatesFolder,
            final String templatesEncoding) throws Exception {
        // TODO: use character encoding when that will be possible in JXP
        // library
        this(aRegistry, aPathResolver, new JxpProcessor(new FilePageSource(
                templatesFolder.getCanonicalPath())));
    }

    private DocGenerator(final PluginRegistry aRegistry,
            final PathResolver aPathResolver, final JxpProcessor proc) {
        registry = aRegistry;
        pathResolver = aPathResolver;
        processor = proc;
        allPluginDescriptors = getAllPluginDescriptors();
        allPluginFragments = getAllPluginFragments();
        allExtensionPoints = getAllExtensionPoints();
        allExtensions = getAllExtensions();
    }

    /**
     * @return documentation overview HTML content
     */
    public String getDocumentationOverview() {
        return documentationOverview;
    }

    /**
     * @param aDocumentationOverview
     *            documentation overview HTML content
     */
    public void setDocumentationOverview(final String aDocumentationOverview) {
        this.documentationOverview = aDocumentationOverview;
    }

    /**
     * @return CSS style sheet content
     */
    public String getStylesheet() {
        return stylesheet;
    }

    /**
     * @param aStylesheet
     *            CSS style sheet content
     */
    public void setStylesheet(final String aStylesheet) {
        this.stylesheet = aStylesheet;
    }

    /**
     * @return output files encoding name
     */
    public String getOutputEncoding() {
        return outputEncoding;
    }

    /**
     * @param encoding
     *            output files encoding name (default is UTF-8)
     */
    public void setOutputEncoding(final String encoding) {
        this.outputEncoding = encoding;
    }

    private void processTemplateFile(final Map<String, Object> ctx,
            final String template, final File outFile) throws Exception {
        Writer out = new OutputStreamWriter(new BufferedOutputStream(
                new FileOutputStream(outFile, false)), outputEncoding);
        try {
            processor.process(template, new JxpProcessingContext(out, ctx));
        } finally {
            out.close();
        }
    }

    private void processTemplateContent(final Map<String, Object> ctx,
            final String template, final File outFile) throws Exception {
        File tmpFile = File.createTempFile("~jpf-jxp", null); //$NON-NLS-1$
        tmpFile.deleteOnExit();
        Writer tmpOut = new OutputStreamWriter(new BufferedOutputStream(
                new FileOutputStream(tmpFile, false)), "UTF-8"); //$NON-NLS-1$
        try {
            tmpOut.write(template);
        } finally {
            tmpOut.close();
        }
        Writer out = new OutputStreamWriter(new BufferedOutputStream(
                new FileOutputStream(outFile, false)), outputEncoding);
        try {
            JxpProcessor proc = new JxpProcessor(new FilePageSource(tmpFile
                    .getParentFile().getCanonicalPath()));
            // TODO: use character encoding when that will be possible in JXP
            // library (UTF-8 in this case)
            proc.process(tmpFile.getName(), new JxpProcessingContext(out, ctx));
        } finally {
            tmpFile.delete();
            out.close();
        }
    }

    /**
     * Generates documentation for all registered plug-ins.
     * 
     * @param destDir
     *            target folder
     * @throws Exception
     *             if an error has occurred
     */
    public void generate(final File destDir) throws Exception {
        // generating index page
        Map<String, Object> ctx = createConext(0);
        processTemplateFile(ctx, "index.jxp", //$NON-NLS-1$
                new File(destDir, "index.html")); //$NON-NLS-1$
        // generating style sheet file
        generateCss(destDir);
        // generating menu page
        ctx = createConext(0);
        processTemplateFile(ctx, "menu.jxp", //$NON-NLS-1$
                new File(destDir, "menu.html")); //$NON-NLS-1$
        // generating overview page
        ctx = createConext(0);
        if (documentationOverview != null) {
            ctx.put("overview", documentationOverview.replaceAll( //$NON-NLS-1$
                    "(?i)(?d)(?m).*<body>(.*)</body>.*", "$1")); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            ctx.put("overview", ""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        processTemplateFile(ctx, "overview.jxp", //$NON-NLS-1$
                new File(destDir, "overview.html")); //$NON-NLS-1$
        // generating "all plug-ins" page
        ctx = createConext(0);
        processTemplateFile(ctx, "allplugins.jxp", //$NON-NLS-1$
                new File(destDir, "allplugins.html")); //$NON-NLS-1$
        // generating "all plug-in fragments" page
        ctx = createConext(0);
        processTemplateFile(ctx, "allfragments.jxp", //$NON-NLS-1$
                new File(destDir, "allfragments.html")); //$NON-NLS-1$
        // generating "all extension points" page
        ctx = createConext(0);
        processTemplateFile(ctx, "allextpoints.jxp", //$NON-NLS-1$
                new File(destDir, "allextpoints.html")); //$NON-NLS-1$
        // generating "all extensions" page
        ctx = createConext(0);
        processTemplateFile(ctx, "allexts.jxp", //$NON-NLS-1$
                new File(destDir, "allexts.html")); //$NON-NLS-1$
        // generating tree page
        ctx = createConext(0);
        processTemplateFile(ctx, "tree.jxp", //$NON-NLS-1$
                new File(destDir, "tree.html")); //$NON-NLS-1$
        // per plug-in generation
        for (PluginDescriptor descriptor : registry.getPluginDescriptors()) {
            generateForPluginDescriptor(destDir, descriptor);
        }
    }

    private void generateCss(final File destDir) throws Exception {
        final Map<String, Object> ctx = createConext(0);
        if (stylesheet == null) {
            processTemplateFile(ctx, "stylesheet.jxp", //$NON-NLS-1$
                    new File(destDir, "stylesheet.css")); //$NON-NLS-1$
        } else {
            processTemplateContent(ctx, stylesheet, new File(destDir,
                    "stylesheet.css")); //$NON-NLS-1$
        }
    }

    private void generateForPluginDescriptor(final File baseDir,
            final PluginDescriptor descr) throws Exception {
        File destDir = new File(baseDir, descr.getId());
        destDir.mkdirs();
        File srcDocsFolder = IoUtil.url2file(pathResolver.resolvePath(descr,
                descr.getDocsPath()));
        if ((srcDocsFolder != null) && srcDocsFolder.isDirectory()) {
            File destDocsFolder = new File(destDir, "extra"); //$NON-NLS-1$
            destDocsFolder.mkdir();
            IoUtil.copyFolder(srcDocsFolder, destDocsFolder, true);
        }
        List<PluginDescriptor> dependedPlugins = new LinkedList<PluginDescriptor>();
        for (PluginDescriptor dependedDescr : registry.getPluginDescriptors()) {
            if (dependedDescr.getId().equals(descr.getId())) {
                continue;
            }
            for (PluginPrerequisite pre : dependedDescr.getPrerequisites()) {
                if (pre.getPluginId().equals(descr.getId()) && pre.matches()) {
                    dependedPlugins.add(dependedDescr);
                    break;
                }
            }
        }
        Map<String, Object> ctx = createConext(1);
        ctx.put("descriptor", descr); //$NON-NLS-1$
        ctx.put("dependedPlugins", dependedPlugins); //$NON-NLS-1$
        processTemplateFile(ctx, "plugin.jxp", //$NON-NLS-1$
                new File(destDir, "index.html")); //$NON-NLS-1$
        // per plug-in fragment generation
        for (PluginFragment fragment : descr.getFragments()) {
            generateForPluginFragment(baseDir, fragment);
        }
        // generating extension points
        if (!descr.getExtensionPoints().isEmpty()) {
            File extPointsDir = new File(destDir, "extp"); //$NON-NLS-1$
            extPointsDir.mkdir();
            for (ExtensionPoint extPoint : descr.getExtensionPoints()) {
                ctx = createConext(3);
                ctx.put("extPoint", extPoint); //$NON-NLS-1$
                File dir = new File(extPointsDir, extPoint.getId());
                dir.mkdir();
                processTemplateFile(ctx, "extpoint.jxp", //$NON-NLS-1$
                        new File(dir, "index.html")); //$NON-NLS-1$
            }
        }
        // generating extensions
        if (!descr.getExtensions().isEmpty()) {
            File extsDir = new File(destDir, "ext"); //$NON-NLS-1$
            extsDir.mkdir();
            for (Extension ext : descr.getExtensions()) {
                ctx = createConext(3);
                ctx.put("ext", ext); //$NON-NLS-1$
                File dir = new File(extsDir, ext.getId());
                dir.mkdir();
                processTemplateFile(ctx, "ext.jxp", //$NON-NLS-1$
                        new File(dir, "index.html")); //$NON-NLS-1$
            }
        }
    }

    private void generateForPluginFragment(final File baseDir,
            final PluginFragment fragment) throws Exception {
        final File destDir = new File(baseDir, fragment.getId());
        destDir.mkdirs();
        Map<String, Object> ctx = createConext(1);
        ctx.put("fragment", fragment); //$NON-NLS-1$
        processTemplateFile(ctx, "fragment.jxp", //$NON-NLS-1$
                new File(destDir, "index.html")); //$NON-NLS-1$
    }

    private Map<String, Object> createConext(final int level) {
        final Map<String, Object> result = new HashMap<String, Object>();
        String relativePath = getRelativePath(level);
        result.put("tool", new Tool(relativePath)); //$NON-NLS-1$
        result.put("relativePath", relativePath); //$NON-NLS-1$
        result.put("registry", registry); //$NON-NLS-1$
        result.put("allPluginDescriptors", allPluginDescriptors); //$NON-NLS-1$
        result.put("allPluginFragments", allPluginFragments); //$NON-NLS-1$
        result.put("allExtensionPoints", allExtensionPoints); //$NON-NLS-1$
        result.put("allExtensions", allExtensions); //$NON-NLS-1$
        return result;
    }

    private Collection<PluginDescriptor> getAllPluginDescriptors() {
        final List<PluginDescriptor> result = new LinkedList<PluginDescriptor>();
        result.addAll(registry.getPluginDescriptors());
        Collections.sort(result, new IdentityComparator());
        return Collections.unmodifiableCollection(result);
    }

    private Collection<PluginFragment> getAllPluginFragments() {
        final List<PluginFragment> result = new LinkedList<PluginFragment>();
        result.addAll(registry.getPluginFragments());
        Collections.sort(result, new IdentityComparator());
        return Collections.unmodifiableCollection(result);
    }

    private Collection<ExtensionPoint> getAllExtensionPoints() {
        final List<ExtensionPoint> result = new LinkedList<ExtensionPoint>();
        for (PluginDescriptor descriptor : registry.getPluginDescriptors()) {
            result.addAll(descriptor.getExtensionPoints());
        }
        Collections.sort(result, new IdentityComparator());
        return Collections.unmodifiableCollection(result);
    }

    private Collection<Extension> getAllExtensions() {
        final List<Extension> result = new LinkedList<Extension>();
        for (PluginDescriptor descriptor : registry.getPluginDescriptors()) {
            result.addAll(descriptor.getExtensions());
        }
        Collections.sort(result, new IdentityComparator());
        return Collections.unmodifiableCollection(result);
    }

    /**
     * Utility class to be used from JXP templates.
     * 
     * @version $Id$
     */
    public static final class Tool {
        private String relativePath;

        protected Tool(final String aRelativePath) {
            this.relativePath = aRelativePath;
        }

        /**
         * @param ref
         *            documentation reference element
         * @return link to be used in "href" attribute
         */
        public String getLink(final Documentation.Reference<?> ref) {
            if (isAbsoluteUrl(ref.getRef())) {
                return ref.getRef();
            }
            String id;
            Identity idt = ref.getDeclaringIdentity();
            if (idt instanceof PluginElement) {
                PluginElement<?> element = (PluginElement) idt;
                PluginFragment fragment = element.getDeclaringPluginFragment();
                if (fragment != null) {
                    id = fragment.getId();
                } else {
                    id = element.getDeclaringPluginDescriptor().getId();
                }
            } else {
                id = idt.getId();
            }
            return relativePath + "/" + id + "/extra/" + ref.getRef(); //$NON-NLS-1$ //$NON-NLS-2$
        }

        /**
         * @param url
         *            an URL to check
         * @return <code>true</code> if given link is an absolute URL
         */
        public boolean isAbsoluteUrl(final String url) {
            try {
                String protocol = new URL(url).getProtocol();
                return (protocol != null) && (protocol.length() > 0);
            } catch (MalformedURLException e) {
                return false;
            }
        }

        /**
         * Substitutes all ${relativePath} variables with their values.
         * 
         * @param text
         *            text to be processed
         * @return processed documentation text
         */
        public String processDocText(final String text) {
            if ((text == null) || (text.length() == 0)) {
                return ""; //$NON-NLS-1$
            }
            return text.replaceAll("(?d)(?m)\\$\\{relativePath\\}", //$NON-NLS-1$
                    relativePath);
        }
    }

    static final class IdentityComparator implements
            Comparator<Identity> {
        /**
         * @param o1 first object to compare
         * @param o2 second object to compare
         * @return comparison result
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(final Identity o1, final Identity o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }
}
