package jpo; 

import java.util.*;
import javax.swing.event.*; 
import javax.swing.tree.*; 
 
/*
GroupBrower.java:  an implementation of the ThumbnailBrowserInterface for browsing groups.

Copyright (C) 2002-2006  Richard Eigenmann.
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
 *  Thuis class implements the ThumbnailBrowserInterface in the specific manner that is required for 
 *  displaying Groups in the Thumbnail JScrollPane.
 */

public class GroupBrowser implements ThumbnailBrowserInterface, TreeModelListener {

	/**
	 *  A reference to the current group that shall be browsed
	 */
	private SortableDefaultMutableTreeNode myNode;
	
	
	/**
	 *  Constructs a new Group Browser object
	 */
	public GroupBrowser () {
	}

	/**
	 *  Constructs a new Group Browser object for a specific group node
	 */
	public GroupBrowser ( SortableDefaultMutableTreeNode node ) {
		setNode ( node );
	}
	
	/**
	 *  call this method to specify the node that this GroupBrowser should refer to. The node is validated
	 *  that it's payload is of type GroupInfo.
	 *
	 *  @param  node   The SortableDefaultMutableTreeNode that refers to the Group that should be displayed.
	 */
	public void setNode ( SortableDefaultMutableTreeNode node ) {
		// deregister from prior TreeModelListener (could be that the new group is from a different tree)
		if ( myNode != null ) {
			myNode.getPictureCollection().getTreeModel().removeTreeModelListener( this );
		}
		
		// validate that we are dealing with a GroupInfo node
		if ( ! ( node.getUserObject() instanceof GroupInfo ) ) {
			myNode = null;
		} else {
			myNode = node;
			// register this component so that it receives notifications from the Model
			myNode.getPictureCollection().getTreeModel().addTreeModelListener( this );
		}
	}

	/**
	 *  returns the name of the Group being displayed
	 */
	public String getTitle() {
		if ( myNode != null ) {
			return myNode.toString();
		} else {
			return "<no group>";
		}
	}


	/**
	 *  On a group we return the number of children in the group.
	 */
	public int getNumberOfNodes() {
		if ( myNode != null ) {
			return myNode.getChildCount();
		} else {
			return 0;
		}
	}



	/**
	 *  This method returns the SDMTN node for the indicated position in the group
	 *  If there are more Thumbnails than nodes in the group it returns null.
	 *
	 *  @param index   The component index that is to be returned.
	 */
 	public SortableDefaultMutableTreeNode getNode( int index ) {
		if ( myNode == null ) { return null; }
		if  ( index >= getNumberOfNodes() )
			return null;
		else 
			return (SortableDefaultMutableTreeNode) myNode.getChildAt( index );
	}


	/**
	 *  This method unregisters the TreeModelListener and sets the variables to null;
	 */
	public void cleanup () {
		if ( myNode != null ) {
			myNode.getPictureCollection().getTreeModel().removeTreeModelListener( this );
			myNode = null;
		}
		relayoutListeners.clear();
	}

	



	/**
	 *  A vector that holds all the listeners that need to be notified if there is a structural change.
	 */
	private Vector relayoutListeners = new Vector();


	/**
	 *  method to register a ThumbnailJScrollPane as a listener
	 */
	public void addRelayoutListener ( ThumbnailJScrollPane listener ) {
		relayoutListeners.add( listener );
	}


	/**
	 *  method to remove a ThumbnailJScrollPane as a listener
	 */
	public void removeRelayoutListener ( ThumbnailJScrollPane listener ) {
		relayoutListeners.remove( listener );
	}


	/**
	 * Method that notifies the relayoutListeners of a structural change that they need to 
	 * respond to.
	 */
	private void notifyRelayoutListeners() {
		Vector nonmodifiedVector = (Vector) relayoutListeners.clone();

		Enumeration e = nonmodifiedVector.elements();
		while ( e.hasMoreElements() ) {
			((ThumbnailJScrollPane) e.nextElement()).layoutThumbnailsInThread();
		}
	}


	/** 
	 *   This method is defined by the TreeModelListener interface and gives the 
	 *   JThumnailScrollPane a notification that some nodes changed in a non dramatic way.
	 *   The nodes that were changed have their Constraints reevaluated and a revalidate
	 *   is called to update the screen.
	 */
	public void treeNodesChanged (TreeModelEvent e) {
		Tools.log("GroupBrowser.treeNodesChanged: " + e.toString() );
		if ( myNode == null ) {
			Tools.log("GroupBrowser.treeNodesChanged: ERROR! This should not have been called as there is not group showing and therefore there should be no tree listener firing off. Ignoring notification.");
			return;
		}

		// don't get excited and force a relayout unless the inserted node is part 
		// of the current group
		TreePath myPath = new TreePath( myNode.getPath() );
		if ( myPath.equals( e.getTreePath() ) ) {
			Tools.log("GroupBrowser.treeNodesChanged: The changed node's parent is the group being shown. We must therefore relayout the children; myNode: " + myPath.toString() + " comparison:" +  ((SortableDefaultMutableTreeNode) e.getTreePath().getLastPathComponent() ).toString() );
			notifyRelayoutListeners(); 
		}
	}
	
	/** 
	 *   This method is defined by the TreeModelListener interface and gives the 
	 *   JThumnailScrollPane a notification if additional nodes were inserted.
	 *   The additional nodes are added and the existing nodes are reevaluated
	 *   as to whether they are at the right place. Revalidate is called to update
	 *   the screen.
	 */
	public void treeNodesInserted (TreeModelEvent e) {
		Tools.log("GroupBrowser.treeNodesInserted: " + e.toString() );
		if ( myNode == null ) {
			Tools.log("GroupBrowser.treeNodesInserted: ERROR! This should not have been called as there is not group showing and therefore there should be no tree listener firing off. Ignoring notification.");
			return;
		}

		// don't get excited and force a relayout unless the inserted node is part 
		// of the current group
		TreePath myPath = new TreePath( myNode.getPath() );
		if ( myPath.equals( e.getTreePath() ) ) {
			Tools.log("GroupBrowser.treeNodesInserted: The inserted node's parent is the group being shown. We must therefore relayout the children; myNode: " + myPath.toString() + " comparison:" +  ((SortableDefaultMutableTreeNode) e.getTreePath().getLastPathComponent() ).toString() );
			notifyRelayoutListeners(); 
		}
	}

	/** 
	 *   This method is defined by the TreeModelListener interface and gives the 
	 *   JThumnailScrollPane a notification that some nodes were removed. It steps
	 *   through all the Thumbnail Components and makes sure they all are at the correct
	 *   location. The dead ones are removed.
	 */
	public void treeNodesRemoved ( TreeModelEvent e ) {
		Tools.log("GroupBrowser.treeNodesRemoved: " + e.toString() );
		if ( myNode == null ) {
			Tools.log("GroupBrowser.treeNodesRemoved: ERROR! This should not have been called as there is not group showing and therefore there should be no tree listener firing off. Ignoring notification.");
			return;
		}
		
		// if the current node is part of the tree that was deleted then we need to 
		//  reposition the group at the parent node that remains.
		if ( SortableDefaultMutableTreeNode.wasNodeDeleted( myNode, e ) ) {
			Tools.log("GroupBrowser.treeNodesRemoved: determined that a child node of the currently displaying node was deleted and therefore executing a setNode on the parent that remains.");
			setNode( (SortableDefaultMutableTreeNode) e.getTreePath().getLastPathComponent() );
		} else {
			// don't get excited and force a relayout unless the partent of the deleted 
			// node is the current group
			TreePath myPath = new TreePath( myNode.getPath() );
			if ( myPath.equals( e.getTreePath() ) ) {
				Tools.log("GroupBrowser.treeNodesRemoved: The removed node's parent is the group being shown. We must therefore relayout the children; myNode: " + myPath.toString() + " comparison:" +  ((SortableDefaultMutableTreeNode) e.getTreePath().getLastPathComponent() ).toString() );
				notifyRelayoutListeners(); 
			}
		}
	}


	/** 
	 *   This method is defined by the TreeModelListener interface and gives the 
	 *   JThumnailScrollPane a notification if there was a massive structure change in the 
	 *   tree. In this event all laying out shall stop and the group should be laid out from 
	 *   scratch.
	 */
	public void treeStructureChanged (TreeModelEvent e) {
		Tools.log("GroupBrowser.treeStructureChanged: " + e.toString() );
		if ( myNode == null ) {
			Tools.log("GroupBrowser.treeStructureChanged: ERROR! This should not have been called as there is not group showing and therefore there should be no tree listener firing off. Ignoring notification.");
			return;
		}
		if ( myNode.isNodeDescendant( (SortableDefaultMutableTreeNode) e.getTreePath().getLastPathComponent() ) ){
			notifyRelayoutListeners(); 
		}
	}

	
	
}
