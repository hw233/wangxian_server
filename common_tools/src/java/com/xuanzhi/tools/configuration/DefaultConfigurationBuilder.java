/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package com.xuanzhi.tools.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * A DefaultConfigurationBuilder builds <code>Configuration</code>s from XML,
 * via a SAX2 compliant parser.
 *
 * <p>
 * XML namespace support is optional, and disabled by default to preserve
 * backwards-compatibility. To enable it, pass the {@link
 * #DefaultConfigurationBuilder(boolean)} constructor the flag <code>true</code>, or pass
 * a namespace-enabled <code>XMLReader</code> to the {@link
 * #DefaultConfigurationBuilder(XMLReader)} constructor.
 * </p>
 * <p>
 * The mapping from XML namespaces to {@link Configuration} namespaces is pretty
 * straightforward, with one caveat: attribute namespaces are (deliberately) not
 * supported. Enabling namespace processing has the following effects:</p>
 * <ul>
 *  <li>Attributes starting with <code>xmlns:</code> are interpreted as
 *  declaring a prefix:namespaceURI mapping, and won't result in the creation of
 *  <code>xmlns</code>-prefixed attributes in the <code>Configuration</code>.
 *  </li>
 *  <li>
 *  Prefixed XML elements, like <tt>&lt;doc:title xmlns:doc="http://foo.com"&gt;,</tt>
 *  will result in a <code>Configuration</code> with <code>{@link
 *  Configuration#getName getName()}.equals("title")</code> and <code>{@link
 *  Configuration#getNamespace getNamespace()}.equals("http://foo.com")</code>.
 *  </li>
 * </ul>
 *
 * @author <a href="mailto:fede@apache.org">Federico Barbieri</a>
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:bloritsch@apache.org">Berin Loritsch</a>
 */
public class DefaultConfigurationBuilder
{
    private SAXConfigurationHandler               m_handler;
    private XMLReader                             m_parser;

    /**
     * Create a Configuration Builder with a default XMLReader that ignores
     * namespaces.  In order to enable namespaces, use either the constructor
     * that has a boolean or that allows you to pass in your own
     * namespace-enabled XMLReader.
     */
    public DefaultConfigurationBuilder()
    {
        this( false );
    }

    /**
     * Create a Configuration Builder, specifying a flag that determines
     * namespace support.
     *
     * @param enableNamespaces If <code>true</code>,  a namespace-aware
     * <code>SAXParser</code> is used. If <code>false</code>, the default JAXP
     * <code>SAXParser</code> (without namespace support) is used.
     */
    public DefaultConfigurationBuilder( final boolean enableNamespaces )
    {
        //yaya the bugs with some compilers and final variables ..
        try
        {
            final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

            if ( enableNamespaces )
            {
                saxParserFactory.setNamespaceAware(true);
            }

            final SAXParser saxParser = saxParserFactory.newSAXParser();
            this.setParser(saxParser.getXMLReader());
        }
        catch( final Exception se )
        {
            throw new Error( "Unable to setup SAX parser" + se );
        }
    }

    /**
     * Create a Configuration Builder with your own XMLReader.
     */
    public DefaultConfigurationBuilder( XMLReader parser )
    {
        this.setParser(parser);
    }

    /**
     * Internally sets up the XMLReader
     */
    private void setParser(XMLReader parser)
    {
        m_parser = parser;

        m_handler = getHandler();

        m_parser.setContentHandler( m_handler );
        m_parser.setErrorHandler( m_handler );
    }

    /**
     * Get a SAXConfigurationHandler for your configuration reading.
     */
    protected SAXConfigurationHandler getHandler()
    {
        try
        {
            if ( m_parser.getFeature( "http://xml.org/sax/features/namespaces" ) )
            {
                return new NamespacedSAXConfigurationHandler();
            }
        }
        catch ( Exception e )
        {
            // ignore error and fall through to the non-namespaced version
        }

        return new SAXConfigurationHandler();
    }

    /**
     * Build a configuration object from a file using a filename.
     */
    public Configuration buildFromFile( final String filename )
        throws SAXException, IOException, ConfigurationException
    {
        return buildFromFile( new File( filename ) );
    }

    /**
     * Build a configuration object from a file using a File object.
     */
    public Configuration buildFromFile( final File file )
        throws SAXException, IOException, ConfigurationException
    {
        synchronized(this)
        {
            m_handler.clear();
            m_parser.parse( file.toURL().toString() );
            return m_handler.getConfiguration();
        }
    }

    /**
     * Build a configuration object using an InputStream.
     */
    public Configuration build( final InputStream inputStream )
        throws SAXException, IOException, ConfigurationException
    {
        return build( new InputSource( inputStream ) );
    }

    /**
     * Build a configuration object using an URI
     */
    public Configuration build( final String uri )
        throws SAXException, IOException, ConfigurationException
    {
        return build( new InputSource( uri ) );
    }

    /**
     * Build a configuration object using an XML InputSource object
     */
    public Configuration build( final InputSource input )
        throws SAXException, IOException, ConfigurationException
    {
        synchronized(this)
        {
            m_handler.clear();
            m_parser.parse( input );
            return m_handler.getConfiguration();
        }
    }
}
