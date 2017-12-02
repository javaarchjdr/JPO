package jpo.dataModel;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

/*
 DuplicatesQuery.java:  Finds duplicates and adds them to a query object

 Copyright (C) 2010-2014  Richard Eigenmann.
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
 * This class finds duplicates and adds them to a query object
 *
 * @author Richard Eigenmann
 */
public class DuplicatesQuery
        implements Query {

    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( DuplicatesQuery.class.getName() );

    /**
     * The number of entries found
     *
     * @return the number of entries found
     */
    @Override
    public int getNumberOfResults() {
        if ( searchResults == null ) {
            extractSearchResults();
        }
        return searchResults.size();
    }

    /**
     * Returns the element specified in the index
     *
     * @param index The element to be returned
     * @return returns the node or null if the query is not right.
     */
    @Override
    public SortableDefaultMutableTreeNode getIndex( int index ) {
        if ( index >= getNumberOfResults() ) // forces execute of query if not yet executed
        {
            return null;
        } else {
            return searchResults.get( index );
        }
    }

    /**
     * Returns a title for the query
     *
     * @return The title for the query
     */
    @Override
    public String getTitle() {
        return "Duplicates";
    }

    /**
     * returns a the title for the search that can be used to display the search
     * results under. The JTree asks for toString()
     */
    @Override
    public String toString() {
        return getTitle();
    }

    /**
     * Refreshes the search results
     */
    @Override
    public void refresh() {
        LOGGER.info( "refresh called" );
        extractSearchResults();
    }

    /**
     * ResultSet so that the query is not reexecuted every time a user clicks
     */
    private List<SortableDefaultMutableTreeNode> searchResults;

    /**
     * Finds the duplicates
     */
    private void extractSearchResults() {
        List<SortableDefaultMutableTreeNode> results = new ArrayList<>();
        List<SortableDefaultMutableTreeNode> nodeList = new ArrayList<>();
        SortableDefaultMutableTreeNode pictureNode;
        for ( Enumeration e = Settings.getPictureCollection().getRootNode().preorderEnumeration(); e.hasMoreElements(); ) {
            pictureNode = (SortableDefaultMutableTreeNode) e.nextElement();
            if ( pictureNode.getUserObject() instanceof PictureInfo ) {
                nodeList.add( pictureNode );
            }
        }
        LOGGER.info( String.format( "Build list of %d picture nodes", nodeList.size() ) );

        SortableDefaultMutableTreeNode baseNode;
        PictureInfo baseNodePictureInfo;
        SortableDefaultMutableTreeNode compareNode;
        PictureInfo compareNodePictureInfo;
        for ( int i = 0; i < nodeList.size(); i++ ) {
            baseNode = nodeList.get( i );
            for ( int j = i + 1; j < nodeList.size(); j++ ) {
                compareNode = nodeList.get( j );
                baseNodePictureInfo = (PictureInfo) baseNode.getUserObject();
                compareNodePictureInfo = (PictureInfo) compareNode.getUserObject();
                if ( ( baseNodePictureInfo.getImageLocation().equals( compareNodePictureInfo.getImageLocation() ) )
                        || ( ( baseNodePictureInfo.getChecksum() != Long.MIN_VALUE ) && ( baseNodePictureInfo.getChecksum() == compareNodePictureInfo.getChecksum() ) ) ) {
                    LOGGER.info( String.format( "Found a duplicate: %s = %s", baseNode.toString(), compareNode.toString() ) );
                    results.add( baseNode );
                    results.add( compareNode );
                }
            }
        }
        searchResults = results;
    }
}