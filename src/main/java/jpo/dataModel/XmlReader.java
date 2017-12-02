package jpo.dataModel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import jpo.EventBus.JpoEventBus;
import jpo.EventBus.RemoveOldLowresThumbnailsRequest;
import jpo.gui.LabelFrame;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/*
 XmlReader.java:  class that reads the xml file

 Copyright (C) 2002 - 2017  Richard Eigenmann.
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or any later version. This program is distributed
 in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details. You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 The license is in gpl.txt.
 See http://www.gnu.org/copyleft/gpl.html for the details.
 */
/**
 * class that reads an XML collection and creates a tree of
 * SortableDefaultMutableTreeNodes
 */
public class XmlReader {
    
    private LabelFrame loadProgressGui = new LabelFrame( Settings.jpoResources.getString( "jpo.dataModel.XmlReader.loadProgressGuiTitle" ) );

    /**
     * Defines a LOGGER for this class
     */
    private static final Logger LOGGER = Logger.getLogger( XmlReader.class.getName() );


    /**
     * Constructor an XML parser that can read our picture list XML files.
     *
     * @param inputStream The stream which is to be parsed
     * @param startNode	The node that becomes the root node for the nodes being
     * read.
     */
    public XmlReader( InputStream inputStream, SortableDefaultMutableTreeNode startNode ) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream( inputStream );

        final StringBuilder lowresUrls = new StringBuilder();

        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating( true );
        SAXParser saxParser;
        try {
            saxParser = factory.newSAXParser();
        } catch ( ParserConfigurationException | SAXException ex ) {
            LOGGER.log( Level.SEVERE, null, ex );
            return;
        }

        try {
            saxParser.parse( bufferedInputStream, new SaxEventHandler( startNode, loadProgressGui, lowresUrls ) );
        } catch ( SAXParseException spe ) {
            // Error generated by the parser
            LOGGER.log( Level.INFO, "\n** Parsing error" + ", line {0}, uri {1}", new Object[]{ spe.getLineNumber(), spe.getSystemId() } );
            LOGGER.log( Level.INFO, "   {0}", spe.getMessage() );

            // Use the contained exception, if any
            Exception x = spe;
            if ( spe.getException() != null ) {
                x = spe.getException();
            }

        } catch ( SAXException sxe ) {
            // Error generated by this application
            // (or a parser-initialization error)
            LOGGER.severe( "SAXException: " + sxe.getMessage() );
            Exception x = sxe;
            if ( sxe.getException() != null ) {
                x = sxe.getException();
                LOGGER.severe( "Embedded Exception: " + x.getMessage() );
            }
        } catch ( IOException ex ) {
            LOGGER.severe( "IOException: " + ex.getMessage() );
        }

        correctJarReferences( startNode );
        // new IntegrityChecker( startNode );
        loadProgressGui.getRid();
        loadProgressGui = null;

        if ( lowresUrls.length() > 1 ) {
            LOGGER.info( String.format( "lowresUrls length is %d", lowresUrls.length() ) );
            JpoEventBus.getInstance().post( new RemoveOldLowresThumbnailsRequest( lowresUrls ) );

        }
    }

    /**
     * This method runs through all the URL strings and changes the jar:
     * references with the path to the jar. 
     *
     * @param startNode Start Node
     */
    public static void correctJarReferences( SortableDefaultMutableTreeNode startNode ) {
        Enumeration kids = startNode.children();
        while ( kids.hasMoreElements() ) {
            SortableDefaultMutableTreeNode n = (SortableDefaultMutableTreeNode) kids.nextElement();

            if ( n.getUserObject() instanceof PictureInfo ) {
                PictureInfo pi = (PictureInfo) n.getUserObject();
                if ( pi.getImageLocation().startsWith( "jar:!" ) ) {
                    pi.setImageLocation(
                            pi.getImageLocation().replaceFirst( "jar:!", Settings.jarRoot ) );
                }
            }

            if ( n.getChildCount() > 0 ) {
                correctJarReferences( n );
            }
        }

    }
}
