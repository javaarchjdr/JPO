package jpo.gui;

import jpo.dataModel.SortableDefaultMutableTreeNode;
import java.util.*;
import java.util.logging.Logger;

/*
ThumbnailBrowser.java:  an implementation of the ThumbnailBrowserInterface for browsing pictures sequentially.

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
 *  This class implements the RelayoutListener functionality required by the  ThumbnailBrowserInterface
 *  but the other methods need to be implements by the exentding class.
 */
public abstract class ThumbnailBrowser
        implements ThumbnailBrowserInterface {

    /**
     * Logger for this class
     */
    protected Logger logger = Logger.getLogger( ThumbnailBrowser.class.getName() );


    /**
     *  The implementing class must return the title for the images being shown.
     */
    public abstract String getTitle();


    /**
     *  The implementing class must return the number of nodes it contains.
     */
    public abstract int getNumberOfNodes();


    /**
     *  This method returns the node for the indicated position in the group.
     *
     *  @param index   The component index that is to be returned. The number is from 0 to
     *                 {@link #getNumberOfNodes}. If there are 3 nodes request getNode(0),
     *                 getNode(1) and getNode(2).
     */
    public abstract SortableDefaultMutableTreeNode getNode( int index );


    /**
     *  This method unregisters the TreeModelListener and sets the variables to null;
     *  a super must be called to ensure that the ThumbnailBrowser cleans up the relayoutListeners
     */
    public void cleanup() {
        relayoutListeners.clear();
    }

    /**
     *  This ArrayList holds the reference to the listeners that need to be notified if there is a structural change.
     *  Observer pattern.
     */
    private Vector<RelayoutListener> relayoutListeners = new Vector<RelayoutListener>();


    /**
     *  method to register a ThumbnailJScrollPane as a listener
     */
    public void addRelayoutListener( RelayoutListener listener ) {
        logger.fine( String.format( "adding relayout listener: %s", listener.toString() ) );
        relayoutListeners.add( listener );
        logger.fine( String.format( "We now have %d relayout listeners.", relayoutListeners.size() ) );
    }


    /**
     *  method to remove a ThumbnailJScrollPane as a listener
     */
    public void removeRelayoutListener( RelayoutListener listener ) {
        logger.fine( String.format( "removing relayout listener: %s", listener.toString() ) );
        relayoutListeners.remove( listener );
        logger.fine( String.format( "We now have %d relayout listeners.", relayoutListeners.size() ) );
    }


    /**
     * Method that notifies the relayoutListeners of a structural change that they need to
     * respond to.
     */
    public void notifyRelayoutListeners() {
        logger.fine( String.format( "notifying %d relayout listeners.", relayoutListeners.size() ) );
        for ( RelayoutListener relayoutListener : relayoutListeners ) {
            relayoutListener.assignThumbnails();
        }
    }
}
