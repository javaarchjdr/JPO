package jpo.dataModel;

import java.util.List;

/*
 Copyright (C) 2017 - 2017  Richard Eigenmann.
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
 *
 * @author Richard Eigenmann
 */
public class StaticNodesQuery implements Query {

    private final List<SortableDefaultMutableTreeNode> nodes;

    /**
     * Creates a static nodes query
     * @param title the title
     * @param nodes  the nodes
     */
    public StaticNodesQuery( String title, List<SortableDefaultMutableTreeNode> nodes ) {
        this.title = title;
        this.nodes = nodes;
    }

    @Override
    public int getNumberOfResults() {
        return nodes.size();
    }

    @Override
    public SortableDefaultMutableTreeNode getIndex( int index ) {
        return nodes.get( index );
    }

    private String title;
    
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void refresh() {
        // do nothing.
    }

}
