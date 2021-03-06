package org.jpo.eventBus;

import org.jpo.dataModel.SortableDefaultMutableTreeNode;

import java.util.List;

/*
 Copyright (C) 2017  Richard Eigenmann.
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
 * This request indicates that the supplied picture nodes should be added
 * to the email selection
 *
 * @author Richard Eigenmann
 */
public class AddPictureNodesToEmailSelectionRequest implements Request {

    private final List<SortableDefaultMutableTreeNode> nodesList;

    /**
     * A request to add the nodes to the email selection
     *
     * @param nodesList The nodes to add
     */
    public AddPictureNodesToEmailSelectionRequest( List<SortableDefaultMutableTreeNode> nodesList ) {
        this.nodesList = nodesList;
    }

    /**
     * Returns the nodes whose pictures are to be added
     *
     * @return the Nodes
     */
    public List<SortableDefaultMutableTreeNode> getNodesList() {
        return nodesList;
    }

}
