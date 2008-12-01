package org.java.plugin.tools.docgen;

import java.io.IOException;
import java.io.InputStreamReader;

import org.onemind.commons.java.util.FileUtils;
import org.onemind.jxp.CachedJxpPage;
import org.onemind.jxp.CachingPageSource;
import org.onemind.jxp.JxpPage;
import org.onemind.jxp.JxpPageNotFoundException;
import org.onemind.jxp.JxpPageParseException;
import org.onemind.jxp.parser.AstJxpDocument;
import org.onemind.jxp.parser.JxpParser;
import org.onemind.jxp.parser.ParseException;

/**
 * JXP page source configured to load templates from the classpath.
 * @version $Id$
 */
final class ClassPathPageSource extends CachingPageSource {
    private final String base;
    private final ClassLoader cl;
    private final String encoding;

    ClassPathPageSource(final String aBase, final String anEncoding) {
        super();
        base = aBase;
        encoding = anEncoding;
        cl = getClass().getClassLoader();
    }

    /**
     * @see org.onemind.jxp.CachingPageSource#loadJxpPage(java.lang.String)
     */
    @Override
    protected CachedJxpPage loadJxpPage(final String id)
            throws JxpPageNotFoundException {
        if (!hasJxpPage(id)) {
            throw new JxpPageNotFoundException("page " + id + " not found"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return new CachedJxpPage(this, id);
    }

    /**
     * @see org.onemind.jxp.CachingPageSource#parseJxpDocument(
     *      org.onemind.jxp.JxpPage)
     */
    @Override
    protected AstJxpDocument parseJxpDocument(final JxpPage page)
            throws JxpPageParseException {
        try {
            JxpParser parser;
            if (encoding == null) {
                parser = new JxpParser(cl.getResourceAsStream(
                        getStreamName(page.getName())));
            } else {
                parser = new JxpParser(new InputStreamReader(
                        cl.getResourceAsStream(getStreamName(page.getName())),
                        encoding));
            }
            return parser.JxpDocument();
        } catch (IOException ioe) {
            throw new JxpPageParseException("problem parsing page " //$NON-NLS-1$
                    + page.getName() + ": " + ioe.getMessage(), ioe); //$NON-NLS-1$
        } catch (ParseException pe) {
            throw new JxpPageParseException("problem parsing page " //$NON-NLS-1$
                    + page.getName() + ": " + pe.getMessage(), pe); //$NON-NLS-1$
        }
    }

    /**
     * @see org.onemind.jxp.JxpPageSource#hasJxpPage(java.lang.String)
     */
    @Override
    public boolean hasJxpPage(final String id) {
        if (isJxpPageCached(id)) {
            return true;
        }
        return cl.getResource(getStreamName(id)) != null;
    }
    
    private String getStreamName(final String pageName) {
        return FileUtils.concatFilePath(base, pageName);
    }
}
