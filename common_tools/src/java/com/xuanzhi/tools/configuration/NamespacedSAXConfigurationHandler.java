/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package com.xuanzhi.tools.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A SAXConfigurationHandler helps build Configurations out of sax events,
 * including namespace information.
 *
 * @author <a href="mailto:fede@apache.org">Federico Barbieri</a>
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public class NamespacedSAXConfigurationHandler
    extends SAXConfigurationHandler
{
    private final ArrayList              m_elements         = new ArrayList();
    private final ArrayList              m_prefixes         = new ArrayList();
    private Configuration                m_configuration;
    private Locator                      m_locator;
    private NamespaceSupport             m_namespaceSupport = new NamespaceSupport();

    public Configuration getConfiguration()
    {
        return m_configuration;
    }

    public void clear()
    {
        m_elements.clear();
        Iterator i = m_prefixes.iterator();
        while ( i.hasNext() )
        {
            ( (ArrayList) i.next() ).clear();
        }
        m_prefixes.clear();
        m_locator = null;
    }

    public void setDocumentLocator( final Locator locator )
    {
        m_locator = locator;
    }

    public void startDocument()
        throws SAXException
    {
        m_namespaceSupport.reset();
        super.startDocument();
    }

    public void endDocument()
        throws SAXException
    {
        super.endDocument();
        m_namespaceSupport.reset();
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
        final ArrayList prefixes = (ArrayList) m_prefixes.remove( location );

        final Iterator i = prefixes.iterator();
        while ( i.hasNext() )
        {
            endPrefixMapping( (String) i.next() );
        }
        prefixes.clear();

        if( 0 == location )
        {
            m_configuration = (Configuration)object;
        }

        m_namespaceSupport.popContext();
    }

    protected DefaultConfiguration createConfiguration( final String localName,
                                                        final String namespaceURI,
                                                        final String location )
    {
        String prefix = m_namespaceSupport.getPrefix( namespaceURI );
        if (prefix == null) prefix = "";
        return new DefaultConfiguration( localName, location, namespaceURI, prefix );
    }

    public void startElement( final String namespaceURI,
                              final String localName,
                              final String rawName,
                              final Attributes attributes )
        throws SAXException
    {
        m_namespaceSupport.pushContext();
        final ArrayList prefixes = new ArrayList();
        AttributesImpl componentAttr = new AttributesImpl();

        for (int i = 0; i < attributes.getLength(); i++)
        {
            if ( attributes.getQName(i).startsWith("xmlns") )
            {
                prefixes.add( attributes.getLocalName(i) );
                this.startPrefixMapping( attributes.getLocalName(i),
                                         attributes.getValue(i) );
            }
            else
            {
                componentAttr.addAttribute( attributes.getURI( i ),
                                            attributes.getLocalName( i ),
                                            attributes.getQName( i ),
                                            attributes.getType( i ),
                                            attributes.getValue( i ) );
            }
        }

        final DefaultConfiguration configuration =
            createConfiguration( localName, namespaceURI, getLocationString() );
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
        m_prefixes.add( prefixes );

        final int attributesSize = componentAttr.getLength();

        for( int i = 0; i < attributesSize; i++ )
        {
            final String name = componentAttr.getQName( i );
            final String value = componentAttr.getValue( i );
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

    public void startPrefixMapping(String prefix, String uri)
        throws SAXException
    {
        m_namespaceSupport.declarePrefix( prefix, uri );
        super.startPrefixMapping( prefix, uri );
    }
}
