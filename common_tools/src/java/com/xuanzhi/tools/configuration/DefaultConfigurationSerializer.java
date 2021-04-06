/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package com.xuanzhi.tools.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * A ConfigurationSerializer serializes configurations via SAX2 compliant parser.
 *
 * @author <a href="mailto:bloritsch@apache.org">Berin Loritsch</a>
 */
public class DefaultConfigurationSerializer
{
    private SAXTransformerFactory m_tfactory;
    private TransformerHandler    m_handler;
    private OutputStream          m_out;
    private Properties            m_format = new Properties();
    private NamespaceSupport      m_namespaceSupport = new NamespaceSupport();

    /**
     * Build a ConfigurationSerializer
     */
    public DefaultConfigurationSerializer()
    {
        getTransformerFactory();
    }

    /**
     * Internally set the output strream we will be using.
     */
    protected void setOutputStream( final OutputStream out )
    {
        try
        {
            m_out = out;
            m_handler = getTransformerFactory().newTransformerHandler();
            m_format.put(OutputKeys.METHOD,"xml");
			m_format.put(OutputKeys.INDENT,"yes");
			
			if(m_format.get(OutputKeys.ENCODING) == null) {
				m_format.put(OutputKeys.ENCODING,System.getProperty("file.encoding"));
			}
			
            //m_format.put(OutputKeys.ENCODING,"UTF-8");
			
			m_handler.setResult(new StreamResult(out));
            m_handler.getTransformer().setOutputProperties( m_format );
        } 
        catch( final Exception e )
        {
            throw new RuntimeException( e.toString() );
        }
    }

    /**
     * Get the SAXTransformerFactory so we can get a serializer without being
     * tied to one vendor.
     */
    protected SAXTransformerFactory getTransformerFactory()
    {
        if(m_tfactory == null)
        {
            m_tfactory = (SAXTransformerFactory) TransformerFactory.newInstance();
        }

        return m_tfactory;
    }

    /**
     * Start the serialization process.  The output stream <strong>must</strong>
     * be set before calling this method.
     */
    protected void serialize( final Configuration source )
        throws SAXException, ConfigurationException
    {
        m_namespaceSupport.reset();
        m_handler.startDocument();
        serializeElement(source);
        m_handler.endDocument();
    }

    /**
     * Serialize each Configuration element.  This method is called recursively.
     */
    protected void serializeElement( final Configuration element )
        throws SAXException, ConfigurationException
    {
        m_namespaceSupport.pushContext();

        AttributesImpl attr = new AttributesImpl();
        String[] attrNames = element.getAttributeNames();

        if (null != attrNames)
        {
            for (int i = 0; i < attrNames.length; i++)
            {
                attr.addAttribute("", // namespace URI
                                  attrNames[i], // local name
                                  attrNames[i], // qName
                                  "CDATA",  // type
                                   element.getAttribute(attrNames[i], "") // value
                                 );
            }
        }

        final String nsURI = element.getNamespace();
        String nsPrefix = "";

        if ( element instanceof AbstractConfiguration )
        {
            nsPrefix = ((AbstractConfiguration) element).getPrefix();
        }
        // nsPrefix is guaranteed to be non-null at this point.

        boolean nsWasDeclared = false;

        final String existingURI = m_namespaceSupport.getURI( nsPrefix );

        // ie, there is no existing URI declared for this prefix or we're
        // remapping the prefix to a different URI
        if ( existingURI == null || !existingURI.equals( nsURI ) )
        {
            nsWasDeclared = true;
            if (nsPrefix.equals("") && nsURI.equals(""))
            {
                // implicit mapping; don't need to declare
            }
            else if (nsPrefix.equals(""))
            {
                // (re)declare the default namespace
                attr.addAttribute("", "xmlns", "xmlns", "CDATA", nsURI);
            }
            else
            {
                // (re)declare a mapping from nsPrefix to nsURI
                attr.addAttribute("", "xmlns:"+nsPrefix, "xmlns:"+nsPrefix, "CDATA", nsURI);
            }
            m_handler.startPrefixMapping( nsPrefix, nsURI );
            m_namespaceSupport.declarePrefix( nsPrefix, nsURI );
        }

        String localName = element.getName();
        String qName = element.getName();
        if ( nsPrefix == null || nsPrefix.length() == 0 )
        {
            qName = localName;
        }
        else
        {
            qName = nsPrefix + ":" + localName;
        }

        m_handler.startElement(nsURI, localName, qName, attr);

        String value = element.getValue(null);

        if (null == value)
        {
            Configuration[] children = element.getChildren();

            for (int i = 0; i < children.length; i++)
            {
                serializeElement(children[i]);
            }
        }
        else
        {
            m_handler.characters(value.toCharArray(), 0, value.length());
        }

        m_handler.endElement(nsURI, localName, qName);

        if ( nsWasDeclared )
        {
            m_handler.endPrefixMapping( nsPrefix );
        }

        m_namespaceSupport.popContext();
    }

    /**
     * Serialize the configuration object to a file using a filename.
     */
    public void serializeToFile( final String filename, final Configuration source )
        throws SAXException, IOException, ConfigurationException
    {
        serializeToFile( new File( filename ), source );
    }

    /**
     * Serialize the configuration object to a file using a File object.
     */
    public void serializeToFile( final File file, final Configuration source )
        throws SAXException, IOException, ConfigurationException
    {
    	FileOutputStream out = new FileOutputStream(file);
    	try{
    		serialize(out, source );
    	}finally{
    		if(out != null) out.close();
    	}
    }
    


    /**
     * Serialize the configuration object to a file using a File object.
     */
    public void serializeToFile( final File file, final Configuration source , final String encoding)
        throws SAXException, IOException, ConfigurationException
    {
    	this.m_format.put(OutputKeys.ENCODING, encoding);
    	FileOutputStream out = new FileOutputStream(file);
    	try{
    		serialize(out, source );
    	}finally{
    		if(out != null) out.close();
    	}
    }

    /**
     * Serialize the configuration object to an output stream.
     */
    public void serialize( final OutputStream outputStream, final Configuration source )
        throws SAXException, IOException, ConfigurationException
    {
        synchronized(this)
        {
            setOutputStream( outputStream );
            serialize( source );
        }
    }

    /**
     * Serialize the configuration object to an output stream derived from an
     * URI.  The URI must be resolveable by the <code>java.net.URL</code> object.
     */
    public void serialize( final String uri, final Configuration source )
        throws SAXException, IOException, ConfigurationException
    {
    	OutputStream out = new URL( uri ).openConnection().getOutputStream();
    	try{
    		serialize(out, source );
    	}finally{
    		if(out != null) out.close();
    	}
    }
}
