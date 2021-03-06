package org.jpo.eventBus;

import org.jpo.dataModel.Settings.FieldCodes;
import org.jpo.dataModel.SortableDefaultMutableTreeNode;

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
 * This request indicates that the user wants to sort a group by the specified criteria
 * 
 * @author Richard Eigenmann
 */
public class SortGroupRequest implements Request {

    private final SortableDefaultMutableTreeNode node;
    private final FieldCodes sortCriteria;

    /**
     * A request to sort the group
     * @param node The node to which should be sorted
     * @param sortCriteria The sort criteria
     */
    public SortGroupRequest( SortableDefaultMutableTreeNode node, FieldCodes sortCriteria ) {
        this.node = node;
        this.sortCriteria = sortCriteria;
    }

    /**
     * Returns the node to which the collection should be added
     * @return the Node with the group
     */
    public SortableDefaultMutableTreeNode getNode() {
        return node;
    }

    /**
     * Returns the sort criteria
     * @return the sort criteria index
     */
    public FieldCodes getSortCriteria() {
        return sortCriteria;
    }
    
}
