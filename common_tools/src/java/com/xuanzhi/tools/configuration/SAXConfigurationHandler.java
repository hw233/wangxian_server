/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package com.xuanzhi.tools.configuration;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A SAXConfigurationHandler helps build Configurations out of sax events.
 *
 * @author <a href="mailto:fede@apache.org">Federico Barbieri</a>
 * @author <a href="mailto:donaldp@apache.org">Peter Donald</a>
 */
public class SAXConfigurationHandler
    extends DefaultHandler
    implements ErrorHandler
{
    private final ArrayList              m_elements        = new ArrayList();
    private Configuration                m_configuration;
    private Locator                      m_locator;

    public Configuration getConfiguration()
    {
        return m_configuration;
    }

    public void clear()
    {
        m_elements.clear();
        m_locator = null;
    }

    public void setDocumentLocator( final Locator locator )
    {
        m_locator = locator;
    }

    public void characters( final char[] ch, int start, int end )
        throws SAXException
    {
        String value = (new String( ch, start, end )).trim();

        if( value.equals( "" ) )
        {
            return;
        }

        final DefaultConfiguration configuration =
            (DefaultConfiguration)m_elements.get( m_elements.size() - 1 );

        if( 0 != configuration.getChildCount() )
        {
            throw new SAXException( "Not allowed to define mixed content in the " +
                                    "element " + configuration.getName() + " at " +
                                    configuration.getLocation() );
        }

        value = configuration.getValue( "" ) + value;
        configuration.setValue( value );
    }

    public void endElement( final String namespaceURI,
                            final String localName,
                            final String rawName )
        throws SAXException
    {
        final int location = m_elements.size() - 1;
        final Object object = m_elements.remove( location );

        if( 0 == location )
        {
            m_configuration = (Configuration)object;
        }
    }

    protected DefaultConfiguration createConfiguration( final String localName,
                                                        final String location )
    {
        return new DefaultConfiguration( localName, location );
    }

    public void startElement( final String namespaceURI,
                              final String localName,
                              final String rawName,
                              final Attributes attributes )
        throws SAXException
    {
        final DefaultConfiguration configuration =
            createConfiguration( rawName, getLocationString() );
        final int size = m_elements.size() - 1;

        if( size > -1 )
        {
            final DefaultConfiguration parent =
                (DefaultConfiguration)m_elements.get( size );

            if( null != parent.getValue( null ) )
            {
                throw new SAXException( "Not allowed to define mixed content in the " +
                                        "element " + parent.getName() + " at " +
                                        parent.getLocation() );
            }

            parent.addChild( configuration );
        }

        m_elements.add( configuration );

        final int attributesSize = attributes.getLength();

        for( int i = 0; i < attributesSize; i++ )
        {
            final String name = attributes.getQName( i );
            final String value = attributes.getValue( i );
            configuration.setAttribute( name, value );
        }
    }

    /**
     * This just throws an exception on a parse error.
     */
    public void error( final SAXParseException exception )
        throws SAXException
    {
        throw exception;
    }

    /**
     * This just throws an exception on a parse error.
     */
    public void warning( final SAXParseException exception )
        throws SAXException
    {
        throw exception;
    }

    /**
     * This just throws an exception on a parse error.
     */
    public void fatalError( final SAXParseException exception )
        throws SAXException
    {
        throw exception;
    }

    protected String getLocationString()
    {
        if( null == m_locator )
        {
            return "Unknown";
        }
        else
        {
            return
                m_locator.getSystemId() + ":" +
                m_locator.getLineNumber() + ":" +
                m_locator.getColumnNumber();
        }
    }
}
