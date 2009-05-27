package jpo.dataModel;

import java.io.File;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/*
NodeStatistics.java: A Class that returns statistics about the node

Copyright (C) 2002-2009  Richard Eigenmann.
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
 * A Class that analyses the statistics about the supplied node
 * @author richi
 */
public class NodeStatistics {

    private DefaultMutableTreeNode myNode;


    /**
     * Constructs a NodeStatistics Object for the supplied node
     * @param nodeToAnalyse
     */
    public NodeStatistics( DefaultMutableTreeNode nodeToAnalyse ) {
        setNode( nodeToAnalyse );

    }


    /**
     * Sets the node to analyses
     * @param nodeToAnalyse
     */
    public void setNode( DefaultMutableTreeNode nodeToAnalyse ) {
        myNode = nodeToAnalyse;
    }


    /**
     * Returns the node being analysed.
     * @return
     */
    public DefaultMutableTreeNode getNode() {
        return myNode;
    }


    /**
     * Call this method when we are done with the stats and release the link
     * to the node under analysis.
     */
    public void getRid() {
        myNode = null;
    }


    public int getNumberOfNodes() {
        return countNodes( myNode );
    }


    /**
     * Recursive method that loops through the child nodes to find the
     * number nodes
     * @param start
     * @return
     */
    private static int countNodes( TreeNode start ) {
        int count = 1;
        TreeNode n;
        Enumeration nodes = start.children();
        while ( nodes.hasMoreElements() ) {
            n = (TreeNode) nodes.nextElement();
            if ( n.getChildCount() > 0 ) {
                count += countNodes( n );
            } else {
                count++;
            }
        }
        return count;
    }


    public String getNumberOfNodesString() {
        return Settings.jpoResources.getString( "CollectionNodeCountLabel" ) + Integer.toString( getNumberOfNodes() );
    }


    public int getNumberOfGroups() {
        return countGroups( myNode );
    }


    /**
     * Recursive method that counts the number of groups in the subtree
     * excluding the starting one
     * @param startNode
     * @return the number of GroupInfo carrying nodes minus the start node.
     */
    private static int countGroups( TreeNode startNode ) {
        int count = 0;
        DefaultMutableTreeNode n;
        Enumeration nodes = startNode.children();
        while ( nodes.hasMoreElements() ) {
            try {
                n = (DefaultMutableTreeNode) nodes.nextElement();
                if ( n.getUserObject() instanceof GroupInfo ) {
                    count++;
                }
                if ( n.getChildCount() > 0 ) {
                    count += countGroups( n );
                }
            } catch ( ClassCastException ex ) {
                // ignore if we get a cast error from the nextElement function;
            }
        }
        return count;
    }


    public String getNumberOfGroupsString() {
        return Settings.jpoResources.getString( "CollectionGroupCountLabel" ) + Integer.toString( getNumberOfGroups() );
    }


    public int getNumberOfPictures() {
        return countPictures( myNode, true );
    }


    public String getNumberOfPicturesString() {
        return Settings.jpoResources.getString( "CollectionPictureCountLabel" ) + Integer.toString( getNumberOfPictures() );
    }


    /**
     *   Returns the number of PictureInfo nNodes in a subtree. Useful for progress monitors. If called with
     *   a null start node it returns 0. If called with a node that is actually a Query object it
     *   asks the Query for the count.
     *
     *   @param startNode	the node from which to count
     *   @param recurseSubgroups  indicator to say whether the next levels of groups should be counted too or not.
     *   @return the number of PictureInfo nodes
     */
    public static int countPictures( DefaultMutableTreeNode startNode, boolean recurseSubgroups ) {
        if ( startNode == null ) {
            return 0;
        }

        if ( startNode.getUserObject() instanceof Query ) {
            return ( (Query) startNode.getUserObject() ).getNumberOfResults();
        }

        int count = 0;
        DefaultMutableTreeNode n;
        Enumeration nodes = startNode.children();
        while ( nodes.hasMoreElements() ) {
            try {
                n = (DefaultMutableTreeNode) nodes.nextElement();
                if ( n.getUserObject() instanceof PictureInfo ) {
                    count++;
                }
                if ( recurseSubgroups && ( n.getChildCount() > 0 ) ) {
                    count += countPictures( n, true );
                }
            } catch ( ClassCastException ex ) {
                //ignore failing cast from nextElement()
            }
        }
        return count;
    }



    /**
     * Returns the bytes of the pictures underneath the supplied node
     * @return
     */
    public long getSizeOfPictures() {
        return sizeOfPicturesLong( myNode );
    }


    public String getSizeOfPicturesString() {
        return Settings.jpoResources.getString( "CollectionSizeJLabel" ) + Tools.fileSizeToString( getSizeOfPictures() );
    }

        /**
     * Returns a nicely formatted string of the bytes the picture files in the subtree occupy.
     * @param startNode
     * @return The number of bytes
     *
    public static String sizeOfPictures( DefaultMutableTreeNode startNode ) {
        long size = sizeOfPicturesLong( startNode );
        return fileSizeToString( size );
    }/


    /**
     * Returns the number of bytes the picture files in the subtree occupy.
     * If the node holds a query the query is enumerated.
     *
     * @param startNode   The node for which to add up the size of the pictures
     * @return  The number of bytes
     */
    private static long sizeOfPicturesLong( DefaultMutableTreeNode startNode ) {
        if ( startNode == null ) {
            return 0;
        }

        long size = 0;
        File testfile;
        DefaultMutableTreeNode n;


        if ( startNode.getUserObject() instanceof Query ) {
            Query q = (Query) startNode.getUserObject();
            for ( int i = 0; i < q.getNumberOfResults(); i++ ) {
                n = (DefaultMutableTreeNode) q.getIndex( i );
                if ( n.getUserObject() instanceof PictureInfo ) {
                    testfile = ( (PictureInfo) n.getUserObject() ).getHighresFile();
                    if ( testfile != null ) {
                        size += testfile.length();
                    }
                }
            }
        } else {
            Enumeration nodes = startNode.children();
            while ( nodes.hasMoreElements() ) {
                n = (DefaultMutableTreeNode) nodes.nextElement();
                if ( n.getUserObject() instanceof PictureInfo ) {
                    testfile = ( (PictureInfo) n.getUserObject() ).getHighresFile();
                    if ( testfile != null ) {
                        size += testfile.length();
                    }
                }
                if ( n.getChildCount() > 0 ) {
                    size += sizeOfPicturesLong( n );
                }
            }
        }

        return size;
    }


}
