package org.jpo.eventBus;

import org.jpo.dataModel.NodeNavigatorInterface;

import java.awt.*;

/*
 Copyright (C) 2019,  Richard Eigenmann, Zürich
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
 * This request indicates that the group popup menu should be shown
 * 
 * @author Richard Eigenmann
 */
public class ShowPicturePopUpMenuRequest implements Request {

    private final NodeNavigatorInterface nodes;
    private final int index;
    private final Component invoker;
    private final int x;
    private final int y;

    /**
     * A request to show the group popup menu
     * @param nodes The nodes on which the request was invoked
     * @param index the current index
     * @param invoker The component on which the request was invoked
     * @param x the x coordinates for the popup window
     * @param y the y coordinates for the popup window
     */
    public ShowPicturePopUpMenuRequest(NodeNavigatorInterface nodes, int index, Component invoker, int x, int y ) {
        this.nodes = nodes;
        this.index = index;
        this.invoker = invoker;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the node
     * @return the Node with the group
     */
    public NodeNavigatorInterface getNodes() {
        return nodes;
    }

    /**
     * Returns the index
     * @return the index
     */
    public int getIndex() {return index;}

    /**
     * The Swing component on which the menu was invoked
     * @return the Swing component on which the menu was invoked
     */
    public Component getInvoker() { return invoker; }

    /**
     * The x coordinates for the menu
     * @return the x coordinates for the menu
     */
    public int getX() {return x;}

    /**
     * The y coordinates for the menu
     * @return the y coordinates for the menu
     */
    public int getY() {return y;}
}
