/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpotestground;

import java.util.logging.Logger;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 *
 * @author richi
 */
public class DebuggingDefaultTreeModel
        extends DefaultTreeModel {

    public DebuggingDefaultTreeModel( TreeNode root ) {
        super( root );
    }

    /**
     * Defines a logger for this class
     */
    private static Logger logger = Logger.getLogger( DebuggingDefaultTreeModel.class.getName() );


    @Override
    public void nodesWereRemoved( TreeNode node,
            int[] childIndices,
            Object[] removedChildren ) {
        logger.info( String.format( "Listeners: %d, Node: %s, childIndices %d, removedChildren: %d",getTreeModelListeners().length, node.toString(), childIndices.length, removedChildren.length ) );
        super.nodesWereRemoved( node, childIndices, removedChildren );
    }


    @Override
    protected void fireTreeNodesRemoved(Object source,
                                    Object[] path,
                                    int[] childIndices,
                                    Object[] children) {
        logger.info( String.format( "Listeners: %d, Source: %s, path: %d, childIndices %d, children: %d",getTreeModelListeners().length, source.toString(), path.length, childIndices.length, children.length ) );
        super.fireTreeNodesRemoved(source, path, childIndices, children );
    }

}