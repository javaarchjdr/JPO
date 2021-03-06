package org.jpo.gui;

import com.google.common.eventbus.Subscribe;
import org.apache.commons.io.FileUtils;
import org.jpo.eventBus.*;
import org.jpo.dataModel.*;
import org.jpo.gui.swing.CollectionJTree;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Copyright (C) 2002 - 2019 Richard Eigenmann, Zurich, Switzerland This
 * program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA. The license is in gpl.txt. See
 * http://www.gnu.org/copyleft/gpl.html for the details.
 */
/**
 * This class deals with the tree representation of the picture collection
 */
public class CollectionJTreeController {

    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( CollectionJTreeController.class.getName() );

    /**
     * The Controller class for the tree representation of the picture collection.
     *
     * @param pictureCollection the PictureCollection to control
     */
    public CollectionJTreeController(PictureCollection pictureCollection) {
        Tools.checkEDT();
        collectionJTree.setModel( pictureCollection.getTreeModel() );
        collectionJTree.setEditable( true );
        collectionJTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        collectionJTree.setTransferHandler( new MyTransferHandler() );
        collectionJTree.setDragEnabled( true );
        collectionJTree.setDropMode( DropMode.ON_OR_INSERT );
        ToolTipManager.sharedInstance().registerComponent( collectionJTree );

        collectionJScrollPane.setMinimumSize( Settings.JPO_NAVIGATOR_JTABBEDPANE_MINIMUM_SIZE );
        collectionJScrollPane.setPreferredSize( Settings.jpoNavigatorJTabbedPanePreferredSize );

        CollectionMouseAdapter mouseAdapter = new CollectionMouseAdapter();
        collectionJTree.addMouseListener( mouseAdapter );
        registerOnEventBus();
    }

    private void registerOnEventBus() {
        JpoEventBus.getInstance().register( this );
    }

    /**
     * When the tree receives a GroupSelectionEvent it will expand the treepath
     * to show the node that was selected.
     *
     * @param event The GroupSelectionEvent
     */
    @Subscribe
    public void handleGroupSelectionEvent(GroupSelectionEvent event ) {
        expandAndScroll( event.getNode() );
    }

    /**
     * When the tree receives a ShowGroupRequest it will expand the treepath to
     * show the node that was selected.
     *
     * @param request The ShowGroupRequest
     */
    @Subscribe
    public void handleShowGroupRequest( ShowGroupRequest request ) {
        expandAndScroll( request.getNode() );
    }

    /**
     * Expands the nodes and scroll the tree so that the indicated node is
     * visible.
     *
     * @param node The node
     */
    private void expandAndScroll( SortableDefaultMutableTreeNode node ) {
        final TreePath tp = new TreePath( node.getPath() );
        Runnable r = () -> {
            collectionJTree.expandPath( tp );
            collectionJTree.scrollPathToVisible( tp );
            collectionJTree.setSelectionPath( tp );
        };
        if ( SwingUtilities.isEventDispatchThread() ) {
            r.run();
        } else {
            SwingUtilities.invokeLater( r );
        }
    }

    private class MyTransferHandler
            extends TransferHandler {

        /**
         * This method is used to query what actions are supported by the source
         * component
         *
         * @param component the Object to query
         * @return COPY_OR_MOVE for this TransferHandler
         */
        @Override
        public int getSourceActions( JComponent component ) {
            return COPY_OR_MOVE;
        }

        /**
         * This method bundles up the data to be exported into a Transferable
         * object in preparation for the transfer.
         *
         * @param component The component
         * @return a transferable
         */
        @Override
        protected Transferable createTransferable( JComponent component ) {
            TreePath selected = collectionJTree.getSelectionPath();
            SortableDefaultMutableTreeNode node = (SortableDefaultMutableTreeNode) selected.getLastPathComponent();
            if ( node.isRoot() ) {
                LOGGER.info( "The Root node must not be dragged. Dragging disabled." );
                return null;
            }
            List<SortableDefaultMutableTreeNode> transferableNodes = new ArrayList<>();
            transferableNodes.add( node );
            return new JpoTransferable( transferableNodes );
        }

        /**
         * This method is called repeatedly during a drag gesture and returns
         * true if the area below the cursor can accept the transfer, or false
         * if the transfer will be rejected.
         *
         * @param support The transfer support object
         * @return true if the import is OK
         */
        @Override
        public boolean canImport( TransferSupport support ) {
            return support.isDataFlavorSupported( JpoTransferable.jpoNodeFlavor );
        }

        /**
         * This method is called on a successful drop (or paste) and initiates
         * the transfer of data to the target component. This method returns
         * true if the import was successful and false otherwise.
         *
         * @param support the TransferSupport
         * @return true if successful
         */
        @Override
        @SuppressWarnings({"unchecked"})
        public boolean importData( TransferSupport support ) {
            JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
            SortableDefaultMutableTreeNode targetNode = (SortableDefaultMutableTreeNode) dropLocation.getPath().getLastPathComponent();
            LOGGER.info( String.format( "Choosing node %s as target for path %s, ChildIndex: %d", targetNode.toString(), dropLocation.getPath(), dropLocation.getChildIndex() ) );

            int actionType = support.getDropAction();
            if ( !( ( actionType == TransferHandler.COPY ) || ( actionType == TransferHandler.MOVE ) ) ) {
                LOGGER.info( String.format( "The event has an odd Action Type: %d. Drop rejected. Copy is %d; Move is %d", actionType, TransferHandler.COPY, TransferHandler.MOVE ) );
                return false;
            }

            //SortableDefaultMutableTreeNode sourceNode;
            //Object[] arrayOfNodes;
            List<SortableDefaultMutableTreeNode> transferableNodes;

            try {
                Transferable t = support.getTransferable();
                Object o = t.getTransferData( JpoTransferable.jpoNodeFlavor );
                //arrayOfNodes = (Object[]) o;
                transferableNodes = (List<SortableDefaultMutableTreeNode>) o;
            } catch ( java.awt.datatransfer.UnsupportedFlavorException x ) {
                LOGGER.log( Level.INFO, "Caught an UnsupportedFlavorException: message: {0}", x.getMessage() );
                return false;
            } catch ( java.io.IOException x ) {
                LOGGER.log( Level.INFO, "Caught an IOException: message: {0}", x.getMessage() );
                return false;
            } catch ( ClassCastException x ) {
                LOGGER.log( Level.INFO, "Caught an ClassCastException: message: {0}", x.getMessage() );
                return false;
            }

            for ( SortableDefaultMutableTreeNode sourceNode : transferableNodes ) {
                //sourceNode = (SortableDefaultMutableTreeNode) arrayOfNode;
                if ( targetNode.isNodeAncestor( sourceNode ) ) {
                    JOptionPane.showMessageDialog( Settings.anchorFrame,
                            Settings.jpoResources.getString( "moveNodeError" ),
                            Settings.jpoResources.getString( "genericError" ),
                            JOptionPane.ERROR_MESSAGE );
                    return false;
                }
            }

            // The drop is a valid one.
            //  memorise the group of the drop location.
            SortableDefaultMutableTreeNode groupOfDropLocation;
            if ( targetNode.getUserObject() instanceof GroupInfo ) {
                groupOfDropLocation = targetNode;
            } else {
                // the parent must be a group node
                groupOfDropLocation = targetNode.getParent();
            }
            if ( ( groupOfDropLocation != null ) && ( groupOfDropLocation.getUserObject() instanceof GroupInfo ) ) {
                Settings.memorizeGroupOfDropLocation( groupOfDropLocation );
                JpoEventBus.getInstance().post( new RecentDropNodesChangedEvent() );
            } else {
                LOGGER.info( "Failed to find the group of the drop location. Not memorizing." );
            }

            transferableNodes.forEach( (sourceNode ) -> {
                //sourceNode = (SortableDefaultMutableTreeNode) arrayOfNode;
                if ( actionType == TransferHandler.MOVE ) {
                    if ( dropLocation.getChildIndex() == -1 ) {
                        if ( targetNode.getUserObject() instanceof GroupInfo ) {
                            // append to end of group if dropping on a group node
                            sourceNode.moveToLastChild( targetNode );
                        } else {
                            // dropping on a PictureInfo
                            sourceNode.moveBefore( targetNode );
                        }
                    } else {
                        //index was supplied by the JTree notification
                        sourceNode.moveToIndex( targetNode, dropLocation.getChildIndex() );
                    }
                } else {
                    // Copy
                    SortableDefaultMutableTreeNode cloneNode = sourceNode.getClone();
                    if ( dropLocation.getChildIndex() == -1 ) {
                        if ( targetNode.getUserObject() instanceof GroupInfo ) {
                            targetNode.add( cloneNode );
                        } else {
                            // dropping onto a picture
                            cloneNode.moveBefore( targetNode );
                        }
                    } else {
                        cloneNode.moveToIndex( targetNode, dropLocation.getChildIndex() );
                    }
                }
            } );
            return true;
        }
    }


    /**
     * The private reference to the JTree representing the collection
     */
    private final JTree collectionJTree = new CollectionJTree() {

        @Override
        public String getToolTipText( MouseEvent mouseEvent ) {
            if ( collectionJTree.getRowForLocation( mouseEvent.getX(), mouseEvent.getY() ) == -1 ) {
                return null;
            }
            TreePath curPath = collectionJTree.getPathForLocation( mouseEvent.getX(), mouseEvent.getY() );
            SortableDefaultMutableTreeNode node = (SortableDefaultMutableTreeNode) Objects.requireNonNull(curPath).getLastPathComponent();
            Object userObject = node.getUserObject();
            String toolTip = "";
            if ( userObject instanceof GroupInfo ) {
                GroupInfo groupInfo = (GroupInfo) userObject;
                toolTip = String.format( "<html>Group: %s</html>", groupInfo.getGroupName() );
            } else if ( userObject instanceof PictureInfo ) {
                final PictureInfo pictureInfo = (PictureInfo) userObject;
                File highresFile = pictureInfo.getImageFile();
                String fileSize = highresFile == null ? "no file" : FileUtils.byteCountToDisplaySize( highresFile.length() );
                toolTip = String.format( "<html>Picture: %s<br>%s %s</html>", pictureInfo.getDescription(), Settings.jpoResources.getString( "CollectionSizeJLabel" ), fileSize );
            }
            return toolTip;
        }
    };


    /**
     * The private reference to the JScrollPane that holds the JTree.
     */
    private final JScrollPane collectionJScrollPane = new JScrollPane( collectionJTree );

    /**
     * Returns the JScrollPane that holds the JTree.
     *
     * @return the JScrollPane that holds the tree
     */
    public JScrollPane getJScrollPane() {
        return collectionJScrollPane;
    }

    /**
     * This class decides what to do with mouse events on the JTree. Since there
     * is so much logic tied to what we are trying to do in the context of the
     * JTree being the top left component here which might not be desirable in a
     * different context this is kept as an inner class of the
     * CollectionJTreeController. the groupPopupJPopupMenu menu must exist.
     *
     */
    private static class CollectionMouseAdapter
            extends MouseAdapter {

        /**
         * Handle click events on the tree. Find out what node was clicked.
         * If it was a single click on a group show the group.
         * If it was multi-click open the (first) picture.
         */
        @Override
        public void mouseClicked( MouseEvent e ) {
            TreePath clickPath = ( (JTree) e.getSource() ).getPathForLocation( e.getX(), e.getY() );
            if ( clickPath == null ) { // this happens
                return;
            }
            SortableDefaultMutableTreeNode clickNode = (SortableDefaultMutableTreeNode) clickPath.getLastPathComponent();

            if ( e.getClickCount() == 1 && ( !e.isPopupTrigger() ) ) {
                if ( clickNode.getUserObject() instanceof GroupInfo ) {
                    JpoEventBus.getInstance().post( new ShowGroupRequest( clickNode ) );
                }
            } else if ( e.getClickCount() > 1 && ( !e.isPopupTrigger() ) ) {
                JpoEventBus.getInstance().post( new ShowPictureRequest( clickNode ) );
            }
        }

        /**
         * Override the mousePressed event.
         */
        @Override
        public void mousePressed( MouseEvent e ) {
            maybeShowPopup( e );
        }

        /**
         * Override the mouseReleased event.
         */
        @Override
        public void mouseReleased( MouseEvent e ) {
            maybeShowPopup( e );
        }

        /**
         * This method figures out whether a popup window should be displayed
         * and displays it.
         *
         * @param e	The MouseEvent that was trapped.
         */
        private void maybeShowPopup( MouseEvent e ) {
            if ( e.isPopupTrigger() ) {
                TreePath popupPath = ( (JTree) e.getSource() ).getPathForLocation( e.getX(), e.getY() );
                if ( popupPath == null ) {
                    return;
                } // happens

                final SortableDefaultMutableTreeNode popupNode = (SortableDefaultMutableTreeNode) popupPath.getLastPathComponent();
                ( (JTree) e.getSource() ).setSelectionPath( popupPath );
                Object nodeInfo = popupNode.getUserObject();

                if ( nodeInfo instanceof GroupInfo ) {
                    JpoEventBus.getInstance().post(new ShowGroupPopUpMenuRequest( popupNode, e.getComponent(), e.getX(), e.getY() ));
                } else if ( nodeInfo instanceof PictureInfo ) {
                    SingleNodeNavigator sb = new SingleNodeNavigator( popupNode );
                    JpoEventBus.getInstance().post(new ShowPicturePopUpMenuRequest( sb, 0, e.getComponent(), e.getX(), e.getY() ));
                }
            }
        }
    }
}
