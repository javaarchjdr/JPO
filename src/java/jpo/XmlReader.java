package jpo;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.swing.*;
import javax.swing.tree.*;

/*
XmlReader.java:  class that reads the xml file

Copyright (C) 2002  Richard Eigenmann.
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
 *  class that reads an XML collection and creates a tree of SortableDefaultMutableTreeNodes 
 */
public class XmlReader extends DefaultHandler {
	/**
	 *  temporary reference to the group node being read
	 */
	private SortableDefaultMutableTreeNode currentGroup;


	/**
	 *  temporary reference to the picture node being read
	 */
	private SortableDefaultMutableTreeNode currentPicture;
	

	/**
	 * 	variable used to interpret what the text is that is coming in 
	 *	through the parser.
	 */
	private int interpretChars;


	/**
	 *  Temporary variable to hold the GroupInfo of the group being created.
	 */
	private GroupInfo gi;	 

	/**
	 *   Constructor an XML parser that can read our picture list XML files.
	 *
	 *   @param  inputStream  The stream which is to be parsed
	 *   @param  startNode	The node that becomes the root node for the nodes being read. 
	 *  			Whether unsaved changes should be set or not depends entirely on 
	 *                      the context and is not set by the parser.
	 */
	public XmlReader ( InputStream inputStream, SortableDefaultMutableTreeNode startNode ) {

		BufferedInputStream bufferedInputStream = new BufferedInputStream ( inputStream );
		
		// Tools.log( "Reading: " + inputFile );
		currentGroup = startNode;
		// currentGroup.removeAllChildren();
		
		// Use the validating parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating( true );
		//factory.setNamespaceAware(true);
		try {

			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			//saxParser.getXMLReader().setEntityResolver( this );
			saxParser.parse( bufferedInputStream, this );

		} catch (SAXParseException spe) {
			// Error generated by the parser
			Tools.log("\n** Parsing error"
				+ ", line " + spe.getLineNumber()
				+ ", uri " + spe.getSystemId());
			Tools.log("   " + spe.getMessage() );

			// Use the contained exception, if any
			Exception  x = spe;
			if (spe.getException() != null)
				x = spe.getException();
			//x.printStackTrace();

		} catch (SAXException sxe) {
			// Error generated by this application
			// (or a parser-initialization error)
			Exception  x = sxe;
			if (sxe.getException() != null)
				x = sxe.getException();
			//x.printStackTrace();

		} catch (ParserConfigurationException pce) {
			Tools.log("XmlReader: Parser with specified options can't be built");
			//pce.printStackTrace();

		} catch (IOException ioe) {
			Tools.log("XmlReader: I/O error");
			//ioe.printStackTrace();
		}
		
		correctJarReferences( startNode );
		// new IntegrityChecker( startNode );
	}





	/**
	 *   method that gets invoked by the parser when a new element is discovered
	 */
	public void startElement(String namespaceURI,
                             String lName, // local name
                             String qName, // qualified name
                             Attributes attrs)
	throws SAXException {
		if ( ( qName == "collection" ) && ( attrs != null ) ) {
			gi = new GroupInfo( attrs.getValue( "collection_name" ) );
			gi.setLowresLocation( attrs.getValue( "collection_icon" ) );
			currentGroup.setUserObject( gi );
			currentGroup.setAllowEdits( attrs.getValue( "collection_protected" ).equals("No") );
		} else if ( qName == "group" ) {
			gi = new GroupInfo( attrs.getValue("group_name") );
			gi.setLowresLocation( attrs.getValue( "group_icon" ) );
			SortableDefaultMutableTreeNode nextCurrentGroup = 
				new SortableDefaultMutableTreeNode( gi );
			currentGroup.add( nextCurrentGroup );
			currentGroup = nextCurrentGroup;
		} else if (qName == "picture") {
			currentPicture = new SortableDefaultMutableTreeNode( new PictureInfo() );
			currentGroup.add( currentPicture );
		} else if (qName == "description") {
			interpretChars = Settings.DESCRIPTION;
		} else if (qName == "file_URL") {
			interpretChars = Settings.FILE_URL;
		} else if (qName == "file_lowres_URL") {
			interpretChars = Settings.FILE_LOWRES_URL;
		} else if (qName == "film_reference") {
			interpretChars = Settings.FILM_REFERENCE;
		} else if (qName == "CREATION_TIME") {
			interpretChars = Settings.CREATION_TIME;
		} else if (qName == "COMMENT") {
			interpretChars = Settings.COMMENT;
		} else if (qName == "PHOTOGRAPHER") {
			interpretChars = Settings.PHOTOGRAPHER;
		} else if (qName == "COPYRIGHT_HOLDER") {
			interpretChars = Settings.COPYRIGHT_HOLDER;
		} else if (qName == "ROTATION") {
			interpretChars = Settings.ROTATION;
		} else if (qName == "checksum") {
			interpretChars = Settings.CHECKSUM;
		} else if (qName == "categoryAssignment") {
			((PictureInfo) currentPicture.getUserObject()).addCategoryAssignment( attrs.getValue("index") );
		} else if (qName == "categories") {
			interpretChars = Settings.CATEGORIES;
		} else if (qName == "category") {
			temporaryCategoryIndex = attrs.getValue("index");
			interpretChars = Settings.CATEGORY;
		} else if (qName == "categoryDescription") {
			interpretChars = Settings.CATEGORY_DESCRIPTION;
		} else {
		    	Tools.log("XmlReader: Don't know what to do with ELEMENT: " + qName);
		}
	}


	/**
	 *  this field hold the value of the category index being parsed.
	 */
	private String temporaryCategoryIndex = "";
	
	/**
	 *  this field hold the texte of the category being parsed.
	 */
	private String temporaryCategory = "";


	/**
	 *  method that gets invoked by the parser when a end element is discovered; used
	 *  here to go back to the parent group if a </group> tag is found.
	 */
	public void endElement(String namespaceURI,
                           String sName, // simple name
                           String qName  // qualified name
                          )
	throws SAXException {
		if (qName == "group")
			currentGroup = (SortableDefaultMutableTreeNode) currentGroup.getParent();
		else if (qName == "ROTATION") {
			//Tools.log("ROTATION here");
			((PictureInfo) currentPicture.getUserObject()).parseRotation();			
		} else if (qName == "checksum") {
			//Tools.log("CHECKSUM here");
			((PictureInfo) currentPicture.getUserObject()).parseChecksum();
		} else if (qName == "categoryDescription") {
			currentGroup.addCategory( new Integer( Integer.parseInt( temporaryCategoryIndex ) ), temporaryCategory );
			temporaryCategory = "";
		}
	}




	/**
	 *   method invoked by the parser when characters are read between tags. The
	 *   variable interpretChars is set so that the characters can be put in the right place
	 */
	public void characters( char buf[], int offset, int len ) throws SAXException {
	        // Tools.log("CHARS:   "+ new String(buf, offset, len));
		String s = new String(buf, offset, len);
		// Tools.log(s);
		switch (interpretChars) {
			case Settings.DESCRIPTION:
				//((PictureInfo) currentPicture.getUserObject()).setDescription(s);
				//Tools.log ( "appending: " + s );
				((PictureInfo) currentPicture.getUserObject()).appendToDescription( s );
				break;
			case Settings.FILE_URL:
				//((PictureInfo) currentPicture.getUserObject()).setHighresLocation(s);
				((PictureInfo) currentPicture.getUserObject()).appendToHighresLocation( s );
				break;
			case Settings.FILE_LOWRES_URL:
				//((PictureInfo) currentPicture.getUserObject()).setLowresLocation(s);
				((PictureInfo) currentPicture.getUserObject()).appendToLowresLocation( s );
				break;
			case Settings.FILM_REFERENCE:
				//((PictureInfo) currentPicture.getUserObject()).setLowresLocation(s);
				((PictureInfo) currentPicture.getUserObject()).appendToFilmReference( s );
				break;
			case Settings.CREATION_TIME:
				((PictureInfo) currentPicture.getUserObject()).appendToCreationTime( s );
				break;
			case Settings.COMMENT:
				((PictureInfo) currentPicture.getUserObject()).appendToComment( s );
				break;
			case Settings.PHOTOGRAPHER:
				((PictureInfo) currentPicture.getUserObject()).appendToPhotographer( s );
				break;
			case Settings.COPYRIGHT_HOLDER:
				((PictureInfo) currentPicture.getUserObject()).appendToCopyrightHolder( s );
				break;
			case Settings.ROTATION:
				((PictureInfo) currentPicture.getUserObject()).appendToRotation( s );
				break;
			case Settings.CHECKSUM:
				((PictureInfo) currentPicture.getUserObject()).appendToChecksum( s );
				break;
			case Settings.CATEGORIES:
				Tools.log( "XmlReader: parsing string on CATEGORIES: " + s );
				break;
			case Settings.CATEGORY:
				Tools.log( "XmlReader: parsing string on CATEGORY: " + s );
				break;
			case Settings.CATEGORY_DESCRIPTION:
				//Tools.log( "XmlReader: category description concatenating: " + s );
				temporaryCategory = temporaryCategory.concat( s );
				break;
		}
	}



	public void ignorableWhitespace(char buf[], int offset, int len)
	throws SAXException
	{
		// Ignore it
	}


	public void processingInstruction(String target, String data) throws SAXException {
		nl();
		emit("PROCESS: ");
		emit("<?"+target+" "+data+"?>");
	}


	/**
	 *  try to resolve where the file belongs
	 */
	public InputSource resolveEntity (String publicId, String systemId) {
		//ClassLoader cl = this.getClass().getClassLoader();
		return  new InputSource( Settings.cl.getResourceAsStream( "jpo/collection.dtd" ) );
	}




	//===========================================================
	// SAX ErrorHandler methods
	//===========================================================

	// treat validation errors as fatal
	public void error(SAXParseException e) throws SAXParseException {
		throw e;
	}


	// dump warnings too
	public void warning(SAXParseException err) throws SAXParseException {
        	Tools.log("** Warning"
			+ ", line " + err.getLineNumber()
			+ ", uri " + err.getSystemId());
		Tools.log("   " + err.getMessage());
	}


	//===========================================================
	// Utility Methods ...
	//===========================================================

	// Wrap I/O exceptions in SAX exceptions, to
	// suit handler signature requirements
	private void emit(String s) throws SAXException {
	}
	
	

	// Start a new line
	// and indent the next line appropriately
	private void nl() throws SAXException {
		String lineEnd =  System.getProperty("line.separator");
	}
    
    
    
    
	/**
	 *  This method runs through all the URL strings and changes the 
	 *  jar:&excl; references with the path to the jar.
	 *  I am prepared to admit this is a sloppy way of building this. The
	 *  problem is that since the parser doesn't always return the whole 
	 *  URL in one go I could be reading fragments and those will not translate
	 *  well. That's also the reason for using append in the adding of
	 *  the data.
	 */
	 public void correctJarReferences ( SortableDefaultMutableTreeNode startNode ) {
		Enumeration kids = startNode.children();
		while (kids.hasMoreElements()) {
			SortableDefaultMutableTreeNode n = (SortableDefaultMutableTreeNode) kids.nextElement();
			
			if (n.getUserObject() instanceof PictureInfo) {
				PictureInfo pi = (PictureInfo) n.getUserObject();
				if (pi.getHighresLocation().startsWith("jar:!"))
					pi.setHighresLocation (
						pi.getHighresLocation().replaceFirst("jar:!", Settings.jarRoot )
					);
				if (pi.getLowresLocation().startsWith("jar:!"))
					pi.setLowresLocation (
						pi.getLowresLocation().replaceFirst("jar:!", Settings.jarRoot )
					);
			}
			
			if ( n.getChildCount() > 0 )
				correctJarReferences ( n );
		}

	 }
}
