package jpo.gui;

import jpo.dataModel.*;
import jpo.*;
import java.util.*;

/*
ArrayListBrower.java:  an implementation of the ThumbnailBrowserInterface for browsing pictures.

Copyright (C) 2006-2009  Richard Eigenmann, Zürich, Switzerland
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
 *  This class implements the ThumbnailBrowserInterface so that all the pictures of a specified
 *  ArrayList can be browsed sequentially.
 */
public class ArrayListBrowser extends ThumbnailBrowser {

        /**
     * Optional Constructor for an ArrayListBrowser.
     * @param title
     * @param nodes 
     */
    public ArrayListBrowser(String title, ArrayList<SortableDefaultMutableTreeNode> nodes) {
        setTitle( title );
        setArrayList( nodes );
    }

    /**
     * Optional Constructor for an ArrayListBrowser.
     */
    public ArrayListBrowser() {
        setTitle( "" );
        setArrayList( new ArrayList<SortableDefaultMutableTreeNode>() );
    }


    /**
     *   title for this ArrayList of images.
     */
    private String title;


    /**
     * Sets the title to be returned if any component tried to display the title of the list
     * @param newTitle
     */
    public void setTitle( String newTitle ) {
        title = newTitle;
    }


    /**
     *  returns the title
     * @return
     */
    public String getTitle() {
        if ( title != null ) {
            return title;
        } else {
            return "";
        }
    }


    /**
     *  Returns the number of pictures in this group. Starts at 1 like all arrays.
     */
    public int getNumberOfNodes() {
        return allPictures.size();
    }


    /**
     *  This method returns the node for the indicated position in the group.
     *
     *  @param index   The component index that is to be returned. The number is from 0 to
     *                 {@link #getNumberOfNodes}. If there are 3 nodes request getNode(0),
     *                 getNode(1) and getNode(2).
     */
    public SortableDefaultMutableTreeNode getNode( int index ) {
        if ( index >= getNumberOfNodes() ) {
            // it is perfectly legal to ask for a node beyond the available nodes
            return null;
        } else {
            return allPictures.get( index );
        }
    }

    /**
     *  This ArrayList holds a reference to each picture under the start group.
     */
    protected ArrayList<SortableDefaultMutableTreeNode> allPictures;


    /**
     *   sets the ArrayList
     *
     *  @param newArrayList   the new ArrayList with the nodes to display.
     */
    public void setArrayList( ArrayList<SortableDefaultMutableTreeNode> newArrayList ) {
        allPictures = newArrayList;
    }


    /**
     *  adds a node to the ArrayList
     *
     *  @param  addNode   a node to add to the allPictures ArrayList
     */
    public void addNode( SortableDefaultMutableTreeNode addNode ) {
        allPictures.add( addNode );
    }


    /**
     *  This method unregisters the TreeModelListener and sets the variables to null;
     */
    @Override
    public void cleanup() {
        super.cleanup();
        allPictures = null;
    }
}
