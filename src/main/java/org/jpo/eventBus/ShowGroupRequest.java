package org.jpo.eventBus;

import org.jpo.dataModel.SortableDefaultMutableTreeNode;

/*
 Copyright (C) 2008-2019,  Richard Eigenmann, Zürich
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
 * This request indicates that the user wants to see a set of thumbnails. GUI components are
 * expected to listen to this and act accordingly.
 * 
 * @author Richard Eigenmann
 */
public class ShowGroupRequest implements Request {

    private final SortableDefaultMutableTreeNode node;

    /**
     * A request to show the thumbnails of the group node
     * @param node The node with the thumbnails to show
     */
    public ShowGroupRequest( SortableDefaultMutableTreeNode node ) {
        this.node = node;
    }

    /**
     * Returns the node for which the thumbnails are to be shown.
     * @return the Node with the group
     */
    public SortableDefaultMutableTreeNode getNode() {
        return node;
    }

}
