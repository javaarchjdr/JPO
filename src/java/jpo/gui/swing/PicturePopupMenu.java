package jpo.gui.swing;

import com.google.common.eventbus.Subscribe;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import jpo.EventBus.ConsolidateGroupRequest;
import jpo.EventBus.CopyLocationsChangedEvent;
import jpo.EventBus.CopyToDirRequest;
import jpo.EventBus.CopyToNewLocationRequest;
import jpo.EventBus.CopyToNewZipfileRequest;
import jpo.EventBus.CopyToZipfileRequest;
import jpo.EventBus.JpoEventBus;
import jpo.EventBus.MoveNodeDownRequest;
import jpo.EventBus.MoveNodeToBottomRequest;
import jpo.EventBus.MoveNodeToNodeRequest;
import jpo.EventBus.MoveNodeToTopRequest;
import jpo.EventBus.MoveNodeUpRequest;
import jpo.EventBus.RecentDropNodesChangedEvent;
import jpo.EventBus.RefreshThumbnailRequest;
import jpo.EventBus.RenamePictureRequest;
import jpo.EventBus.RotatePictureRequest;
import jpo.EventBus.SetPictureRotationRequest;
import jpo.EventBus.ShowCategoryUsageEditorRequest;
import jpo.EventBus.ShowGroupRequest;
import jpo.EventBus.ShowPictureInfoEditorRequest;
import jpo.EventBus.ShowPictureOnMapRequest;
import jpo.EventBus.ShowPictureRequest;
import jpo.EventBus.UserFunctionsChangedEvent;
import jpo.dataModel.NodeNavigatorInterface;
import jpo.dataModel.PictureInfo;
import jpo.dataModel.Settings;
import jpo.dataModel.SortableDefaultMutableTreeNode;
import jpo.dataModel.Tools;
import jpo.gui.ThumbnailQueueRequest;

/*
 PicturePopupMenu.java:  a popup menu for pictures

 Copyright (C) 2002 - 2015  Richard Eigenmann.
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
 * This class generates a popup menu on a picture node.
 *
 */
public class PicturePopupMenu extends JPopupMenu {

    /**
     * Defines a LOGGER for this class
     */
    private static final Logger LOGGER = Logger.getLogger( PicturePopupMenu.class.getName() );

    /**
     * array of menu items that allows the user to call up a user function
     *
     *
     */
    private final JMenuItem[] userFunctionsJMenuItems = new JMenuItem[Settings.maxUserFunctions];

    /**
     * array of menu items that allows the user to copy the picture to a
     * memorised file location
     *
     *
     */
    private final JMenuItem[] copyLocationsJMenuItems = new JMenuItem[Settings.MAX_MEMORISE];
    
    /**
     * array of menu items that remembers the zip file names
     *
     *
     */
    private final JMenuItem[] memorizedZipFileJMenuItems = new JMenuItem[Settings.MAX_MEMORISE];

    /**
     * a separator for the Move menu. Declared here because other class methods
     * want to turn on and off visible.
     */
    private final JSeparator movePictureNodeSeparator = new JSeparator();

    /**
     * This array of JMenuItems memorises the most recent drop locations and
     * allows The user to quickly select a recently used drop location for the
     * next drop.
     */
    private final JMenuItem[] recentDropNodes = new JMenuItem[Settings.MAX_DROPNODES];

    /**
     * Constructor for the PicturePopupMenu where we do have a
     * {@link PictureViewer} that should receive the picture.
     *
     * @param setOfNodes The set of nodes from which the popup picture is coming
     * @param idx	The picture of the set for which the popup is being shown.
     */
    public PicturePopupMenu( NodeNavigatorInterface setOfNodes, int idx ) {
        this.mySetOfNodes = setOfNodes;
        this.index = idx;
        this.popupNode = mySetOfNodes.getNode( index );
        JpoEventBus.getInstance().register( new RecentDropNodeChangedEventHandler() );
        JpoEventBus.getInstance().register( new CopyLocationsChangedEventHandler() );
        JpoEventBus.getInstance().register( new UserFunctionsChangedEventHandler() );

        initComponents();
    }

    /**
     * initialises the GUI components
     */
    private void initComponents() {
        String title = getTitle();
        setLabel( title );

        JMenuItem titleJMenuItem = new JMenuItem( title );
        titleJMenuItem.setEnabled( false );
        add( titleJMenuItem );

        addSeparator();

        JMenuItem showPictureMenuItem = new JMenuItem( Settings.jpoResources.getString( "pictureShowJMenuItemLabel" ) );
        showPictureMenuItem.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                JpoEventBus.getInstance().post( new ShowPictureRequest( popupNode ) );
            }
        } );
        add( showPictureMenuItem );

        JMenuItem showMapMenuItem = new JMenuItem( Settings.jpoResources.getString( "mapShowJMenuItemLabel" ) );
        showMapMenuItem.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                JpoEventBus.getInstance().post( new ShowPictureOnMapRequest( popupNode ) );

            }
        } );
        add( showMapMenuItem );

        JMenu navigateMenuItem = new JMenu( Settings.jpoResources.getString( "navigationJMenu" ) );
        SortableDefaultMutableTreeNode[] parentNodes = Settings.getPictureCollection().findParentGroups( popupNode );
        for ( SortableDefaultMutableTreeNode parentNode : parentNodes ) {
            JMenuItem navigateTargetRoute = new JMenuItem( parentNode.getUserObject().toString() );
            final SortableDefaultMutableTreeNode targetNode = parentNode;
            navigateTargetRoute.addActionListener( new ActionListener() {

                final SortableDefaultMutableTreeNode node = targetNode;

                @Override
                public void actionPerformed( ActionEvent e ) {
                    JpoEventBus.getInstance().post( new ShowGroupRequest( node ) );

                }
            } );
            navigateMenuItem.add( navigateTargetRoute );
        }

        add( navigateMenuItem );

        JMenuItem showCategoryUsageJMenuItemMenuItem = new JMenuItem( Settings.jpoResources.getString( "categoryUsagetJMenuItem" ) );
        showCategoryUsageJMenuItemMenuItem.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                    HashSet<SortableDefaultMutableTreeNode> hashSet = new HashSet<>();
                    hashSet.add( popupNode );
                    JpoEventBus.getInstance().post( new ShowCategoryUsageEditorRequest( hashSet ) );
                } else {
                    if ( !Settings.getPictureCollection().isSelected( popupNode ) ) {
                        Settings.getPictureCollection().clearSelection();
                        HashSet<SortableDefaultMutableTreeNode> hashSet = new HashSet<>();
                        hashSet.add( popupNode );
                        JpoEventBus.getInstance().post( new ShowCategoryUsageEditorRequest( hashSet ) );
                    } else {
                        HashSet<SortableDefaultMutableTreeNode> hs = new HashSet<>( Arrays.asList( Settings.getPictureCollection().getSelectedNodes() ) );
                        JpoEventBus.getInstance().post( new ShowCategoryUsageEditorRequest( hs ) );

                    }
                }

            }
        } );

        add( showCategoryUsageJMenuItemMenuItem );

        JMenuItem pictureMailSelectJMenuItem = new JMenuItem( Settings.jpoResources.getString( "pictureMailSelectJMenuItem" ) );

        pictureMailSelectJMenuItem.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed( ActionEvent e ) {
                        /*
                         * Adds a picture to the selection of pictures to be mailed.
                         *
                         * 1. If no nodes are selected, mail-select the node.
                         *
                         * 2. If multiple nodes are selected but the popup node is not one of them
                         * then mail-select the node
                         *
                         * 3. If multiple nodes are selected and the popup node is one of them then
                         * mail-select them all
                         */
                        if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                            popupNode.getPictureCollection().addToMailSelection( popupNode );
                        } else if ( !Settings.getPictureCollection().isSelected( popupNode ) ) {
                            popupNode.getPictureCollection().addToMailSelection( popupNode );
                        } else {
                            for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
                                if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                                    selectedNode.getPictureCollection().addToMailSelection( selectedNode );
                                }
                            }
                        }

                    }
                }
        );

        JMenuItem pictureMailUnSelectJMenuItem = new JMenuItem( Settings.jpoResources.getString( "pictureMailUnselectJMenuItem" ) );

        pictureMailUnSelectJMenuItem.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed( ActionEvent e ) {
                        popupNode.getPictureCollection().removeFromMailSelection( popupNode );

                    }
                }
        );

        JMenuItem pictureMailUnselectAllJMenuItem = new JMenuItem( Settings.jpoResources.getString( "pictureMailUnselectAllJMenuItem" ) );

        pictureMailUnselectAllJMenuItem.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed( ActionEvent e ) {
                        popupNode.getPictureCollection().clearMailSelection();
                    }
                }
        );

        if ( !popupNode.getPictureCollection()
                .isMailSelected( popupNode ) ) {
            add( pictureMailSelectJMenuItem );
        } else {
            add( pictureMailUnSelectJMenuItem );
        }

        if ( Settings.getPictureCollection()
                .countMailSelectedNodes() > 0 ) {
            add( pictureMailUnselectAllJMenuItem );
        }

        JMenu userFunctionsJMenu = new JMenu( Settings.jpoResources.getString( "userFunctionsJMenu" ) );
        for ( int i = 0;
                i < Settings.maxUserFunctions;
                i++ ) {
            final int userfunction = i;
            userFunctionsJMenuItems[i] = new JMenuItem();
            userFunctionsJMenuItems[i].addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    try {
                        Tools.runUserFunction( userfunction, (PictureInfo) popupNode.getUserObject() );
                    } catch ( ClassCastException | NullPointerException x ) {
                        LOGGER.severe( x.getMessage() );
                    }
                }
            } );
            userFunctionsJMenu.add( userFunctionsJMenuItems[i] );
        }

        add( userFunctionsJMenu );
        setUserFunctionLabels();

        if ( popupNode.getPictureCollection()
                .getAllowEdits() ) {
            JMenu rotationMenu = new JMenu( Settings.jpoResources.getString( "rotation" ) );

            JMenuItem rotate90JMenuItem = new JMenuItem( Settings.jpoResources.getString( "rotate90" ) );
            rotate90JMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    JpoEventBus.getInstance().post( new RotatePictureRequest( popupNode, 90, ThumbnailQueueRequest.HIGH_PRIORITY ) );
                }
            } );
            rotationMenu.add( rotate90JMenuItem );

            JMenuItem rotate180JMenuItem = new JMenuItem( Settings.jpoResources.getString( "rotate180" ) );
            rotate180JMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    JpoEventBus.getInstance().post( new RotatePictureRequest( popupNode, 180, ThumbnailQueueRequest.HIGH_PRIORITY ) );
                }
            } );
            rotationMenu.add( rotate180JMenuItem );

            JMenuItem rotate270JMenuItem = new JMenuItem( Settings.jpoResources.getString( "rotate270" ) );
            rotate270JMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    JpoEventBus.getInstance().post( new RotatePictureRequest( popupNode, 270, ThumbnailQueueRequest.HIGH_PRIORITY ) );
                }
            } );
            rotationMenu.add( rotate270JMenuItem );

            JMenuItem rotate0JMenuItem = new JMenuItem( Settings.jpoResources.getString( "rotate0" ) );
            rotate0JMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    JpoEventBus.getInstance().post( new SetPictureRotationRequest( popupNode, 0f, ThumbnailQueueRequest.HIGH_PRIORITY ) );
                }
            } );
            rotationMenu.add( rotate0JMenuItem );
            add( rotationMenu );
        }

        JMenuItem pictureRefreshJMenuItem = new JMenuItem( Settings.jpoResources.getString( "pictureRefreshJMenuItem" ) );

        pictureRefreshJMenuItem.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed( ActionEvent e ) {
                        if ( !Settings.getPictureCollection().isSelected( popupNode ) ) {
                            JpoEventBus.getInstance().post( new RefreshThumbnailRequest( popupNode, ThumbnailQueueRequest.HIGH_PRIORITY ) );
                        } else {
                            JpoEventBus.getInstance().post( new RefreshThumbnailRequest( Settings.getPictureCollection().getSelectedNodesAsList(), ThumbnailQueueRequest.HIGH_PRIORITY ) );
                        }
                    }
                }
        );
        add( pictureRefreshJMenuItem );

        if ( popupNode.getPictureCollection()
                .getAllowEdits() ) {
            add( new MoveSubmenu() );
        }

        add( new CopySubmenu() );

        // remove node
        if ( popupNode.getPictureCollection()
                .getAllowEdits() ) {
            JMenuItem pictureNodeRemove = new JMenuItem( Settings.jpoResources.getString( "pictureNodeRemove" ) );
            pictureNodeRemove.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    SortableDefaultMutableTreeNode actionNode = mySetOfNodes.getNode( index );
                    if ( ( Settings.getPictureCollection().countSelectedNodes() > 1 ) && ( Settings.getPictureCollection().isSelected( actionNode ) ) ) {
                        for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
                            if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                                selectedNode.deleteNode();
                            }
                        }
                    } else {
                        popupNode.deleteNode();
                    }

                }
            } );
            add( pictureNodeRemove );

            JMenu fileOperationsJMenu = new JMenu( Settings.jpoResources.getString( "FileOperations" ) );
            add( fileOperationsJMenu );

            JMenuItem fileRenameJMenuItem = new JMenuItem( Settings.jpoResources.getString( "fileRenameJMenuItem" ) );
            fileRenameJMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                        JpoEventBus.getInstance().post( new RenamePictureRequest( popupNode ) );
                    } else {
                        for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
                            if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                                JpoEventBus.getInstance().post( new RenamePictureRequest( selectedNode ) );
                            }
                        }
                    }
                }
            } );
            fileOperationsJMenu.add( fileRenameJMenuItem );

            // Delete
            JMenuItem fileDeleteJMenuItem = new JMenuItem( Settings.jpoResources.getString( "fileDeleteJMenuItem" ) );
            fileDeleteJMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    SortableDefaultMutableTreeNode actionNode = mySetOfNodes.getNode( index );
                    if ( ( Settings.getPictureCollection().countSelectedNodes() > 1 ) && ( Settings.getPictureCollection().isSelected( actionNode ) ) ) {
                        multiDeleteDialog();
                    } else {
                        fileDelete( actionNode );
                    }

                }
            } );
            fileOperationsJMenu.add( fileDeleteJMenuItem );
        }

        JMenuItem showPictureInfoEditorMenuItem = new JMenuItem( Settings.jpoResources.getString( "pictureEditJMenuItemLabel" ) );
        showPictureInfoEditorMenuItem.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                JpoEventBus.getInstance().post( new ShowPictureInfoEditorRequest( popupNode ) );
            }
        } );
        add( showPictureInfoEditorMenuItem );

        JMenuItem consolidateHereMenuItem = new JMenuItem( "Consolidate Here" );

        consolidateHereMenuItem.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed( ActionEvent e
                    ) {
                        JpoEventBus.getInstance().post(
                                new ConsolidateGroupRequest(
                                        (SortableDefaultMutableTreeNode) popupNode.getParent(),
                                        ( (PictureInfo) popupNode.getUserObject() ).getHighresFile().getParentFile() )
                        );
                    }
                }
        );
        add( consolidateHereMenuItem );

    }

    /**
     * Returns the title for the popup Menu. If nodes are selected and the
     * selected node is one of them it returns the number of selected nodes.
     * Otherwise it returns the description of the picture.
     *
     * @return the title for the popup menu
     */
    private String getTitle() {
        String title;
        if ( ( Settings.getPictureCollection().countSelectedNodes() > 1 ) && ( Settings.getPictureCollection().isSelected( popupNode ) ) ) {
            title = String.format( "%d nodes", Settings.getPictureCollection().countSelectedNodes() );
        } else {
            Object userObject = popupNode.getUserObject();
            if ( userObject instanceof PictureInfo ) {
                String description = ( (PictureInfo) userObject ).getDescription();
                title = description.length() > 25
                        ? title = description.substring( 0, 25 ) + "..." : description;
            } else {
                title = "Picture Popup Menu";
            }
        }
        return title;
    }

    /**
     * Brings up an are you sure dialog and then deletes the file.
     *
     * TODO: Refactor this out of here!
     *
     * @param nodeToDelete The node to be deleted
     */
    public void fileDelete( SortableDefaultMutableTreeNode nodeToDelete ) {
        Object userObj = nodeToDelete.getUserObject();
        if ( !( userObj instanceof PictureInfo ) ) {
            return;
        }

        PictureInfo pi = (PictureInfo) userObj;
        File highresFile = pi.getHighresFile();
        if ( highresFile == null ) {
            return;
        }

        int option = JOptionPane.showConfirmDialog(
                Settings.anchorFrame,
                Settings.jpoResources.getString( "FileDeleteLabel" ) + highresFile.toString() + "\n" + Settings.jpoResources.getString( "areYouSure" ),
                Settings.jpoResources.getString( "FileDeleteTitle" ),
                JOptionPane.OK_CANCEL_OPTION );

        if ( option == 0 ) {
            boolean ok = false;

            if ( highresFile.exists() ) {
                ok = highresFile.delete();
                if ( !ok ) {
                    LOGGER.log( Level.INFO, "File deleted failed on: {0}", highresFile.toString() );
                }
            }

            nodeToDelete.deleteNode();

            if ( !ok ) {
                JOptionPane.showMessageDialog( Settings.anchorFrame,
                        Settings.jpoResources.getString( "fileDeleteError" ) + highresFile.toString(),
                        Settings.jpoResources.getString( "genericError" ),
                        JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /**
     * Multi delete dialog
     */
    private void multiDeleteDialog() {
        JTextArea textArea = new JTextArea();
        textArea.setText( "" );
        for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
            if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                textArea.append( ( (PictureInfo) selectedNode.getUserObject() ).getHighresLocation() + "\n" );
            }
        }
        textArea.append( Settings.jpoResources.getString( "areYouSure" ) );

        int option = JOptionPane.showConfirmDialog(
                Settings.anchorFrame, //very annoying if the main window is used as it forces itself into focus.
                textArea,
                Settings.jpoResources.getString( "FileDeleteLabel" ),
                JOptionPane.OK_CANCEL_OPTION );

        if ( option == 0 ) {
            for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
                PictureInfo pi;
                if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                    pi = (PictureInfo) selectedNode.getUserObject();
                    boolean ok = false;

                    File highresFile = pi.getHighresFile();
                    if ( highresFile.exists() ) {
                        ok = highresFile.delete();
                        if ( !ok ) {
                            LOGGER.log( Level.INFO, "File deleted failed on: {0}", highresFile.toString() );
                        }
                    }

                    selectedNode.deleteNode();

                    if ( !ok ) {
                        JOptionPane.showMessageDialog( Settings.anchorFrame,
                                Settings.jpoResources.getString( "fileDeleteError" ) + highresFile.toString(),
                                Settings.jpoResources.getString( "genericError" ),
                                JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
            Settings.getPictureCollection().clearSelection();
        }

    }

    /**
     * The node the popup menu was created for
     */
    private final SortableDefaultMutableTreeNode popupNode;
    /**
     * Reference to the {@link NodeNavigatorInterface} which indicates the nodes
     * being displayed.
     */
    private final NodeNavigatorInterface mySetOfNodes;

    /**
     * Index of the {@link #mySetOfNodes} being popped up.
     */
    private int index;  // default is 0

    /**
     * Handler for the RecentDropNodeChangedEvent
     */
    private class RecentDropNodeChangedEventHandler {

        /**
         * Handle the event by updating the submenu items
         *
         * @param event
         */
        @Subscribe
        public void handleRecentDropNodeChangedEventHandler( RecentDropNodesChangedEvent event ) {
            SwingUtilities.invokeLater( new Runnable() {

                @Override
                public void run() {
                    recentDropNodesChanged();
                }
            } );

        }
    }

    /**
     * Here we receive notification that the nodes have been updated. The method
     * then updates the menu items for the drop targets. Those that are null are
     * not shown. If no drop targets are shown at all the separator in the
     * submenu is not shown either.
     */
    public void recentDropNodesChanged() {
        boolean dropNodesVisible = false;
        for ( int i = 0; i < Settings.MAX_DROPNODES; i++ ) {
            if ( Settings.recentDropNodes[i] != null ) {
                recentDropNodes[i].setText(
                        Settings.jpoResources.getString( "recentDropNodePrefix" ) + Settings.recentDropNodes[i].toString() );
                recentDropNodes[i].setVisible( true );
                dropNodesVisible = true;
            } else {
                recentDropNodes[i].setVisible( false );
            }
        }
        if ( dropNodesVisible ) {
            movePictureNodeSeparator.setVisible( true );
        } else {
            movePictureNodeSeparator.setVisible( false );

        }
    }

    /**
     * Handler for the CopyLocationsChangedEvent
     */
    private class CopyLocationsChangedEventHandler {

        /**
         * Handle the event by updating the submenu items
         *
         * @param event
         */
        @Subscribe
        public void handleCopyLocationsChangedEvent( CopyLocationsChangedEvent event ) {
            SwingUtilities.invokeLater( new Runnable() {

                @Override
                public void run() {
                    copyLocationsChanged();
                }
            } );

        }
    }

    /**
     * Here we receive notification that the copy locations were updated and
     * then go and update the targets on the menu.
     */
    public void copyLocationsChanged() {
        for ( int i = 0; i < Settings.copyLocations.length; i++ ) {
            if ( Settings.copyLocations[i] != null ) {
                copyLocationsJMenuItems[i].setText( Settings.copyLocations[i] );
                copyLocationsJMenuItems[i].setVisible( true );
            } else {
                copyLocationsJMenuItems[i].setVisible( false );

            }
        }
    }

    /**
     * Handler for the UserFunctionsChangedEvent
     */
    private class UserFunctionsChangedEventHandler {

        /**
         * Handle the event by updating the submenu items
         *
         * @param event
         */
        @Subscribe
        public void handleUserFunctionsChangedEvent( UserFunctionsChangedEvent event ) {
            SwingUtilities.invokeLater( new Runnable() {

                @Override
                public void run() {
                    setUserFunctionLabels();
                }
            } );

        }
    }

    /**
     * This method populates the user functions sub entries on the menu.
     *
     */
    private void setUserFunctionLabels() {
        for ( int i = 0; i < Settings.maxUserFunctions; i++ ) {
            if ( ( Settings.userFunctionNames[i] != null ) && ( Settings.userFunctionNames[i].length() > 0 ) && ( Settings.userFunctionCmd[i] != null ) && ( Settings.userFunctionCmd[i].length() > 0 ) ) {
                userFunctionsJMenuItems[i].setText( Settings.userFunctionNames[i] );
                userFunctionsJMenuItems[i].setVisible( true );
            } else {
                userFunctionsJMenuItems[i].setVisible( false );
            }
        }
    }


    private class MoveSubmenu extends JMenu {

        public MoveSubmenu() {
            setText( Settings.jpoResources.getString( "moveNodeJMenuLabel" ) );

            for ( int i = 0; i < Settings.MAX_DROPNODES; i++ ) {
                final int dropnode = i;
                recentDropNodes[i] = new JMenuItem();
                recentDropNodes[i].addActionListener( new ActionListener() {

                    /**
                     * Moves the selected nodes to the picked destination. If no
                     * nodes are in the selection then the popup node is moved.
                     * If the node for the popup is not in the selection then
                     * this node only is moved. After the move of the selected
                     * nodes they are cleared.
                     *
                     * @param event
                     */
                    @Override
                    public void actionPerformed( ActionEvent event ) {
                        SortableDefaultMutableTreeNode targetNode = Settings.recentDropNodes[dropnode];
                        List<SortableDefaultMutableTreeNode> movingNodes = new ArrayList<>();
                        if ( ( Settings.getPictureCollection().countSelectedNodes() > 0 ) && ( Settings.getPictureCollection().isSelected( popupNode ) ) ) {
                            movingNodes.addAll( Settings.getPictureCollection().getSelectedNodesAsList() );
                            Settings.getPictureCollection().clearSelection();
                        } else {
                            movingNodes.add( popupNode );
                        }
                        JpoEventBus.getInstance().post( new MoveNodeToNodeRequest( movingNodes, targetNode ) );

                        Settings.memorizeGroupOfDropLocation( Settings.recentDropNodes[dropnode] );
                        JpoEventBus.getInstance().post( new RecentDropNodesChangedEvent() );
                    }
                } );
                add( recentDropNodes[i] );
            }
            add( movePictureNodeSeparator );
            recentDropNodesChanged();

            JMenuItem movePictureToTopJMenuItem = new JMenuItem( Settings.jpoResources.getString( "movePictureToTopJMenuItem" ) );
            movePictureToTopJMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent event ) {
                    if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                        JpoEventBus.getInstance().post( new MoveNodeToTopRequest( popupNode ) );
                    } else {
                        for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
                            if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                                JpoEventBus.getInstance().post( new MoveNodeToTopRequest( selectedNode ) );
                            }
                        }
                    }
                }
            } );
            add( movePictureToTopJMenuItem );

            JMenuItem movePictureUpJMenuItem = new JMenuItem( Settings.jpoResources.getString( "movePictureUpJMenuItem" ) );
            movePictureUpJMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                        JpoEventBus.getInstance().post( new MoveNodeUpRequest( popupNode ) );
                    } else {
                        for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
                            if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                                JpoEventBus.getInstance().post( new MoveNodeUpRequest( selectedNode ) );
                            }
                        }
                    }
                }
            } );
            add( movePictureUpJMenuItem );

            JMenuItem movePictureDownJMenuItem = new JMenuItem( Settings.jpoResources.getString( "movePictureDownJMenuItem" ) );
            movePictureDownJMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                        JpoEventBus.getInstance().post( new MoveNodeDownRequest( popupNode ) );
                    } else {
                        for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
                            if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                                JpoEventBus.getInstance().post( new MoveNodeDownRequest( selectedNode ) );
                                selectedNode.moveNodeDown();
                            }
                        }
                    }
                }
            } );
            add( movePictureDownJMenuItem );

            JMenuItem movePictureToBottomJMenuItem = new JMenuItem( Settings.jpoResources.getString( "movePictureToBottomJMenuItem" ) );
            movePictureToBottomJMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                        JpoEventBus.getInstance().post( new MoveNodeToBottomRequest( popupNode ) );
                    } else {
                        for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
                            if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                                JpoEventBus.getInstance().post( new MoveNodeToBottomRequest( selectedNode ) );
                            }
                        }
                    }
                }
            } );
            add( movePictureToBottomJMenuItem );

            JMenuItem indentJMenuItem = new JMenuItem( Settings.jpoResources.getString( "indentJMenuItem" ) );
            indentJMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                        popupNode.indentNode();
                    } else {
                        for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
                            if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                                selectedNode.indentNode();
                            }
                        }
                    }
                }
            } );
            add( indentJMenuItem );

            JMenuItem outdentJMenuItem = new JMenuItem( Settings.jpoResources.getString( "outdentJMenuItem" ) );
            outdentJMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                        popupNode.outdentNode();
                    } else {
                        for ( SortableDefaultMutableTreeNode selectedNode : Settings.getPictureCollection().getSelectedNodes() ) {
                            if ( selectedNode.getUserObject() instanceof PictureInfo ) {
                                selectedNode.outdentNode();
                            }
                        }
                    }
                }
            } );
            add( outdentJMenuItem );
        }

    }

    private class CopySubmenu extends JMenu {

        public CopySubmenu() {
            setText( Settings.jpoResources.getString( "copyImageJMenuLabel" ) );

            final JMenuItem copyToNewLocationJMenuItem = new JMenuItem( Settings.jpoResources.getString( "copyToNewLocationJMenuItem" ) );
            copyToNewLocationJMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                        SortableDefaultMutableTreeNode[] nodes = new SortableDefaultMutableTreeNode[1];
                        nodes[0] = popupNode;
                        JpoEventBus.getInstance().post( new CopyToNewLocationRequest( nodes ) );
                    } else {
                        JpoEventBus.getInstance().post( new CopyToNewLocationRequest( Settings.getPictureCollection().getSelectedNodes() ) );
                    }
                }
            } );
            add( copyToNewLocationJMenuItem );

            addSeparator();

            for ( int i = 0; i < Settings.MAX_MEMORISE; i++ ) {
                final int item = i;
                copyLocationsJMenuItems[i] = new JMenuItem();
                copyLocationsJMenuItems[i].addActionListener( new ActionListener() {

                    @Override
                    public void actionPerformed( ActionEvent ae ) {
                        if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                            SortableDefaultMutableTreeNode[] nodes = new SortableDefaultMutableTreeNode[1];
                            nodes[0] = popupNode;
                            JpoEventBus.getInstance().post( new CopyToDirRequest( nodes, new File( Settings.copyLocations[item] ) ) );
                        } else {
                            JpoEventBus.getInstance().post( new CopyToDirRequest( Settings.getPictureCollection().getSelectedNodes(), new File( Settings.copyLocations[item] ) ) );
                        }
                    }
                } );
                add( copyLocationsJMenuItems[i] );
            }
            copyLocationsChanged();

            addSeparator();

            final JMenuItem copyToNewZipfileJMenuItem = new JMenuItem( Settings.jpoResources.getString( "copyToNewZipfileJMenuItem" ) );
            copyToNewZipfileJMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                        SortableDefaultMutableTreeNode[] nodes = new SortableDefaultMutableTreeNode[1];
                        nodes[0] = popupNode;
                        JpoEventBus.getInstance().post( new CopyToNewZipfileRequest( nodes ) );
                    } else {
                        JpoEventBus.getInstance().post( new CopyToNewZipfileRequest( Settings.getPictureCollection().getSelectedNodes() ) );
                    }
                }
            } );
            add( copyToNewZipfileJMenuItem );

            for ( int i = 0; i < Settings.MAX_MEMORISE; i++ ) {
                final int item = i;
                memorizedZipFileJMenuItems[i] = new JMenuItem();
                memorizedZipFileJMenuItems[i].addActionListener( new ActionListener() {

                    @Override
                    public void actionPerformed( ActionEvent ae ) {
                        if ( Settings.getPictureCollection().countSelectedNodes() < 1 ) {
                            SortableDefaultMutableTreeNode[] nodes = new SortableDefaultMutableTreeNode[1];
                            nodes[0] = popupNode;
                            JpoEventBus.getInstance().post( new CopyToZipfileRequest( nodes, new File( Settings.memorizedZipFiles[item] ) ) );
                        } else {
                            JpoEventBus.getInstance().post( new CopyToZipfileRequest( Settings.getPictureCollection().getSelectedNodes(), new File( Settings.memorizedZipFiles[item] ) ) );
                        }
                    }
                } );
                add( memorizedZipFileJMenuItems[i] );
                if ( Settings.memorizedZipFiles[i] != null ) {
                    memorizedZipFileJMenuItems[i].setText( Settings.memorizedZipFiles[i] );
                    memorizedZipFileJMenuItems[i].setVisible( true );
                } else {
                    memorizedZipFileJMenuItems[i].setVisible( false );
                }
            }
        }
    }

}
