package jpo.gui;

import jpo.dataModel.NodeNavigatorInterface;
import jpo.dataModel.UserFunctionsChangeListener;
import jpo.dataModel.Tools;
import jpo.dataModel.CopyLocationsChangeListener;
import jpo.dataModel.Settings;
import jpo.dataModel.SortableDefaultMutableTreeNode;
import jpo.dataModel.PictureInfo;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import jpo.dataModel.RecentDropNodeListener;

/*
PicturePopupMenu.java:  a popup menu for pictures

Copyright (C) 2002 - 2010  Richard Eigenmann.
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
 *  This class generates a popup menu over a picture node.
 *
 */
public class PicturePopupMenu
        extends JPopupMenu
        implements ActionListener,
        RecentDropNodeListener,
        CopyLocationsChangeListener,
        UserFunctionsChangeListener {

    /**
     * Defines a logger for this class
     */
    private static Logger logger = Logger.getLogger( PicturePopupMenu.class.getName() );

    /**
     *  array of menu items that allows the user to call up a user function
     *
     **/
    private JMenuItem[] userFunctionsJMenuItems = new JMenuItem[Settings.maxUserFunctions];

    /**
     *  array of menu items that allows the user to copy the picture to a memorised file location
     *
     **/
    private JMenuItem[] copyLocationsJMenuItems = new JMenuItem[Settings.MAX_MEMORISE];

    /**
     *  a separator for the Move menu. Declared here because other class methods want to turn on and off visible.
     */
    private JSeparator movePictureNodeSeparator = new JSeparator();

    /**
     *  This array of JMenuItems memorises the most recent drop locations and allows
     *  The user to quickly select a recently used drop location for the next drop.
     */
    private JMenuItem[] recentDropNodes = new JMenuItem[Settings.MAX_DROPNODES];


    /**
     * Constructor for the PicturePopupMenu where we do have a {@link PictureViewer} that should
     * receive the picture.
     * TODO: Fix the way this is being called because the whole business of
     * figuring out whether this is a single node or multi node is silly
     *
     * @param  setOfNodes   The set of nodes from which the popup picture is coming
     * @param  idx		The picture of the set for which the popup is being shown.
     */
    public PicturePopupMenu( NodeNavigatorInterface setOfNodes, int idx ) {
        this.mySetOfNodes = setOfNodes;
        this.index = idx;
        this.popupNode = mySetOfNodes.getNode( index );

        // Title
        String title = "Picture Popup Menu";
        if ( ( Settings.pictureCollection.countSelectedNodes() > 1 ) && ( Settings.pictureCollection.isSelected( popupNode ) ) ) {
            title = String.format( "%d nodes", Settings.pictureCollection.countSelectedNodes() );
        } else {
            Object uo = popupNode.getUserObject();
            if ( uo instanceof PictureInfo ) {
                title = ( (PictureInfo) uo ).getDescription();
            }
            if ( title.length() > 25 ) {
                logger.info( String.format( "Length: %d", title.length() ) );
                title = title.substring( 0, 25 ) + "...";
                logger.info( String.format( "Length: %d", title.length() ) );
            }

        }

        setLabel( title );
        JMenuItem menuTitle = new JMenuItem( title );
        menuTitle.setEnabled( false );
        add( menuTitle );
        addSeparator();

        // Show Picture button
        JMenuItem pictureShowJMenuItem = new JMenuItem( Settings.jpoResources.getString( "pictureShowJMenuItemLabel" ) );
        pictureShowJMenuItem.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                requestShowPicture();
            }
        } );
        add( pictureShowJMenuItem );

        // Navigate to menu
        SortableDefaultMutableTreeNode[] parentNodes = Settings.pictureCollection.findParentGroups( popupNode );
        JMenu navigationJMenu = new JMenu( Settings.jpoResources.getString( "navigationJMenu" ) );
        for ( int i = 0; i < parentNodes.length; i++ ) {
            JMenuItem navigateToRootNode = new JMenuItem( parentNodes[i].getUserObject().toString() );
            final SortableDefaultMutableTreeNode targetNode = parentNodes[i];
            navigateToRootNode.addActionListener( new ActionListener() {

                final SortableDefaultMutableTreeNode node = targetNode;


                public void actionPerformed( ActionEvent e ) {
                    Jpo.positionToNode( node );
                }
            } );
            navigationJMenu.add( navigateToRootNode );
        }
        add( navigationJMenu );



        // Categories
        JMenuItem categoryUsagetJMenuItem = new JMenuItem( Settings.jpoResources.getString( "categoryUsagetJMenuItem" ) );
        categoryUsagetJMenuItem.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                requestCategories();
            }
        } );
        add( categoryUsagetJMenuItem );


        JMenuItem pictureMailSelectJMenuItem = new JMenuItem();
        JMenuItem pictureMailUnselectAllJMenuItem = new JMenuItem( Settings.jpoResources.getString( "pictureMailUnselectAllJMenuItem" ) );
        if ( popupNode.getPictureCollection().isMailSelected( popupNode ) ) {
            pictureMailSelectJMenuItem.setText( Settings.jpoResources.getString( "pictureMailUnselectJMenuItem" ) );
            pictureMailSelectJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    requestSelectForEmail();

                }
            } );
            add( pictureMailSelectJMenuItem );

            pictureMailUnselectAllJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    popupNode.getPictureCollection().clearMailSelection();
                }
            } );
            add( pictureMailUnselectAllJMenuItem );
        } else {
            pictureMailSelectJMenuItem.setText( Settings.jpoResources.getString( "pictureMailSelectJMenuItem" ) );
            pictureMailSelectJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
                        popupNode.getPictureCollection().addToMailSelected( popupNode );
                    } else {
                        Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
                        SortableDefaultMutableTreeNode n;
                        while ( selection.hasMoreElements() ) {
                            n = selection.nextElement();
                            if ( n.getUserObject() instanceof PictureInfo ) {
                                n.getPictureCollection().addToMailSelected( n );
                            }
                        }
                    }
                }
            } );
            add( pictureMailSelectJMenuItem );
        }
        JMenu userFunctionsJMenu = new JMenu( Settings.jpoResources.getString( "userFunctionsJMenu" ) );
        for ( int i = 0;
                i < Settings.maxUserFunctions;
                i++ ) {
            userFunctionsJMenuItems[i] = new JMenuItem();
            userFunctionsJMenuItems[i].addActionListener( this );
            userFunctionsJMenu.add( userFunctionsJMenuItems[i] );
        }
        add( userFunctionsJMenu );
        userFunctionsChanged();


        JMenu rotationJMenu = new JMenu( Settings.jpoResources.getString( "rotation" ) );
        if ( popupNode.getPictureCollection().getAllowEdits() ) {
            add( rotationJMenu );
            JMenuItem rotate90JMenuItem = new JMenuItem( Settings.jpoResources.getString( "rotate90" ) );
            rotate90JMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    rotatePicture( 90 );
                }
            } );
            JMenuItem rotate180JMenuItem = new JMenuItem( Settings.jpoResources.getString( "rotate180" ) );
            rotate180JMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    rotatePicture( 180 );
                }
            } );
            JMenuItem rotate270JMenuItem = new JMenuItem( Settings.jpoResources.getString( "rotate270" ) );
            rotate270JMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    rotatePicture( 270 );
                }
            } );
            JMenuItem rotate0JMenuItem = new JMenuItem( Settings.jpoResources.getString( "rotate0" ) );
            rotate0JMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    noRotation();
                }
            } );
            rotationJMenu.add( rotate90JMenuItem );
            rotationJMenu.add( rotate180JMenuItem );
            rotationJMenu.add( rotate270JMenuItem );
            rotationJMenu.add( rotate0JMenuItem );
        }
        JMenuItem pictureRefreshJMenuItem = new JMenuItem( Settings.jpoResources.getString( "pictureRefreshJMenuItem" ) );

        pictureRefreshJMenuItem.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                if ( !Settings.pictureCollection.isSelected( popupNode ) ) {
                    Settings.pictureCollection.clearSelection();
                    popupNode.refreshThumbnail();
                } else {
                    Object[] o = Settings.pictureCollection.getSelectedNodes();
                    for ( int i = 0; i < o.length; i++ ) {
                        ( (SortableDefaultMutableTreeNode) o[i] ).refreshThumbnail();
                    }
                }
            }
        } );
        add( pictureRefreshJMenuItem );


        if ( popupNode.getPictureCollection().getAllowEdits() ) {
            JMenu movePictureNodeJMenu = new JMenu( Settings.jpoResources.getString( "moveNodeJMenuLabel" ) );
            add( movePictureNodeJMenu );

            for ( int i = 0; i < Settings.MAX_DROPNODES; i++ ) {
                recentDropNodes[i] = new JMenuItem();
                recentDropNodes[i].addActionListener( this );
                movePictureNodeJMenu.add( recentDropNodes[i] );
            }
            movePictureNodeJMenu.add( movePictureNodeSeparator );
            recentDropNodesChanged();

            JMenuItem movePictureToTopJMenuItem = new JMenuItem( Settings.jpoResources.getString( "movePictureToTopJMenuItem" ) );
            movePictureToTopJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
                        popupNode.moveNodeToTop();
                    } else {
                        Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
                        SortableDefaultMutableTreeNode n;
                        while ( selection.hasMoreElements() ) {
                            n = selection.nextElement();
                            if ( n.getUserObject() instanceof PictureInfo ) {
                                n.moveNodeToTop();
                            }
                        }
                    }
                }
            } );
            movePictureNodeJMenu.add( movePictureToTopJMenuItem );

            JMenuItem movePictureUpJMenuItem = new JMenuItem( Settings.jpoResources.getString( "movePictureUpJMenuItem" ) );
            movePictureUpJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
                        popupNode.moveNodeUp();
                    } else {
                        Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
                        SortableDefaultMutableTreeNode n;
                        while ( selection.hasMoreElements() ) {
                            n = selection.nextElement();
                            if ( n.getUserObject() instanceof PictureInfo ) {
                                n.moveNodeUp();
                            }
                        }
                    }
                }
            } );
            movePictureNodeJMenu.add( movePictureUpJMenuItem );

            JMenuItem movePictureDownJMenuItem = new JMenuItem( Settings.jpoResources.getString( "movePictureDownJMenuItem" ) );
            movePictureDownJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
                        popupNode.moveNodeDown();
                    } else {
                        Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
                        SortableDefaultMutableTreeNode n;
                        while ( selection.hasMoreElements() ) {
                            n = selection.nextElement();
                            if ( n.getUserObject() instanceof PictureInfo ) {
                                n.moveNodeDown();
                            }
                        }
                    }
                }
            } );
            movePictureNodeJMenu.add( movePictureDownJMenuItem );

            JMenuItem movePictureToBottomJMenuItem = new JMenuItem( Settings.jpoResources.getString( "movePictureToBottomJMenuItem" ) );
            movePictureToBottomJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
                        popupNode.moveNodeToBottom();
                    } else {
                        Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
                        SortableDefaultMutableTreeNode n;
                        while ( selection.hasMoreElements() ) {
                            n = selection.nextElement();
                            if ( n.getUserObject() instanceof PictureInfo ) {
                                n.moveNodeToBottom();
                            }
                        }
                    }
                }
            } );
            movePictureNodeJMenu.add( movePictureToBottomJMenuItem );

            JMenuItem indentJMenuItem = new JMenuItem( Settings.jpoResources.getString( "indentJMenuItem" ) );
            indentJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
                        popupNode.indentNode();
                    } else {
                        Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
                        SortableDefaultMutableTreeNode n;
                        while ( selection.hasMoreElements() ) {
                            n = selection.nextElement();
                            if ( n.getUserObject() instanceof PictureInfo ) {
                                TreeNodeController.copyToNewLocation( n );
                            }
                        }
                    }
                }
            } );
            movePictureNodeJMenu.add( indentJMenuItem );

            JMenuItem outdentJMenuItem = new JMenuItem( Settings.jpoResources.getString( "outdentJMenuItem" ) );
            outdentJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
                        popupNode.outdentNode();
                    } else {
                        Enumeration selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
                        SortableDefaultMutableTreeNode n;
                        while ( selection.hasMoreElements() ) {
                            n = (SortableDefaultMutableTreeNode) selection.nextElement();
                            if ( n.getUserObject() instanceof PictureInfo ) {
                                TreeNodeController.copyToNewLocation( n );
                            }
                        }
                    }
                }
            } );
            movePictureNodeJMenu.add( outdentJMenuItem );

        }


        JMenu copyImageJMenu = new JMenu( Settings.jpoResources.getString( "copyImageJMenuLabel" ) );
        add( copyImageJMenu );

        JMenuItem copyToNewLocationJMenuItem = new JMenuItem( Settings.jpoResources.getString( "copyToNewLocationJMenuItem" ) );
        copyToNewLocationJMenuItem.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
                    TreeNodeController.copyToNewLocation( popupNode );
                } else {
                    Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
                    SortableDefaultMutableTreeNode n;
                    while ( selection.hasMoreElements() ) {
                        n = selection.nextElement();
                        if ( n.getUserObject() instanceof PictureInfo ) {
                            TreeNodeController.copyToNewLocation( n );
                        }
                    }
                }
            }
        } );
        copyImageJMenu.add( copyToNewLocationJMenuItem );

        copyImageJMenu.addSeparator();



        for ( int i = 0; i < Settings.MAX_MEMORISE; i++ ) {
            copyLocationsJMenuItems[i] = new JMenuItem();
            copyLocationsJMenuItems[i].addActionListener( this );
            copyImageJMenu.add( copyLocationsJMenuItems[i] );
        }
        copyLocationsChanged();

        // remove node
        if ( popupNode.getPictureCollection().getAllowEdits() ) {
            JMenuItem pictureNodeRemove = new JMenuItem( Settings.jpoResources.getString( "pictureNodeRemove" ) );
            pictureNodeRemove.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    requestRemoveNode();

                }
            } );
            add( pictureNodeRemove );

            JMenu fileOperationsJMenu = new JMenu( Settings.jpoResources.getString( "FileOperations" ) );
            add( fileOperationsJMenu );

            JMenuItem fileRenameJMenuItem = new JMenuItem( Settings.jpoResources.getString( "fileRenameJMenuItem" ) );
            fileRenameJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
                        TreeNodeController.fileRename( popupNode );
                    } else {
                        Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
                        SortableDefaultMutableTreeNode n;
                        while ( selection.hasMoreElements() ) {
                            n = selection.nextElement();
                            if ( n.getUserObject() instanceof PictureInfo ) {
                                TreeNodeController.fileRename( n );
                            }
                        }
                    }
                }
            } );
            fileOperationsJMenu.add( fileRenameJMenuItem );

            // Delete
            JMenuItem fileDeleteJMenuItem = new JMenuItem( Settings.jpoResources.getString( "fileDeleteJMenuItem" ) );
            fileDeleteJMenuItem.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent e ) {
                    requestDelete();
                }
            } );
            fileOperationsJMenu.add( fileDeleteJMenuItem );
        }
        
        // Properties
        JMenuItem picturePropertiesMenuItem = new JMenuItem( Settings.jpoResources.getString( "pictureEditJMenuItemLabel" ) );
        picturePropertiesMenuItem.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                requestProperties();
            }
        } );
        add( picturePropertiesMenuItem );
    }


    /**
     * Request to remove a node. What happens depends on the selection:
     * If the node on which the popup was performed is one of a selection of
     * nodes, the multi-delete dialog is opened. If, however it is not part
     * of a potentially exisitng selection then only the specified node will
     * be deleted.
     */
    private void requestRemoveNode() {
        SortableDefaultMutableTreeNode actionNode = mySetOfNodes.getNode( index );
        if ( ( Settings.pictureCollection.countSelectedNodes() > 1 ) && ( Settings.pictureCollection.isSelected( actionNode ) ) ) {
            Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
            SortableDefaultMutableTreeNode n;
            while ( selection.hasMoreElements() ) {
                n = selection.nextElement();
                if ( n.getUserObject() instanceof PictureInfo ) {
                    n.deleteNode();
                }
            }
        } else {
            popupNode.deleteNode();
        }
    }


    /**
     * the Delete menu was clicked. What happens depends on the selection:
     * If the node on which the popup was performed is one of a selection of
     * nodes, the multi-delete dialog is opened. If, however it is not part
     * of a potentially exisitng selection then only the specified node will
     * be deleted.
     */
    private void requestDelete() {
        SortableDefaultMutableTreeNode actionNode = mySetOfNodes.getNode( index );
        if ( ( Settings.pictureCollection.countSelectedNodes() > 1 ) && ( Settings.pictureCollection.isSelected( actionNode ) ) ) {
            multiDeleteDialog();
        } else {
            fileDelete( actionNode );
        }
    }


    /**
     * Brings up an are you sure dialog and then deletes the file.
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
            File lowresFile = pi.getLowresFile();
            if ( ( lowresFile != null ) && ( lowresFile.exists() ) ) {
                ok = lowresFile.delete();
                if ( !ok ) //logger.info("File deleted: " + lowresFile.toString() );
                // else
                {
                    logger.info( "File deleted failed on: " + lowresFile.toString() );
                }
            }


            if ( highresFile.exists() ) {
                ok = highresFile.delete();
                if ( !ok ) //logger.info("File deleted: " + highresFile.toString() );
                //else
                {
                    logger.info( "File deleted failed on: " + highresFile.toString() );
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
        Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
        SortableDefaultMutableTreeNode n;
        while ( selection.hasMoreElements() ) {
            n = selection.nextElement();
            if ( n.getUserObject() instanceof PictureInfo ) {
                textArea.append( ( (PictureInfo) n.getUserObject() ).getHighresLocation() + "\n" );
            }
        }
        textArea.append( Settings.jpoResources.getString( "areYouSure" ) );


        int option = JOptionPane.showConfirmDialog(
                Settings.anchorFrame, //very annoying if the main window is used as it forces itself into focus.
                textArea,
                Settings.jpoResources.getString( "FileDeleteLabel" ),
                JOptionPane.OK_CANCEL_OPTION );

        if ( option == 0 ) {
            selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
            PictureInfo pi;
            while ( selection.hasMoreElements() ) {
                n = selection.nextElement();
                if ( n.getUserObject() instanceof PictureInfo ) {
                    pi = (PictureInfo) n.getUserObject();
                    boolean ok = false;
                    File lowresFile = pi.getLowresFile();
                    if ( ( lowresFile != null ) && ( lowresFile.exists() ) ) {
                        ok = lowresFile.delete();
                        if ( !ok ) //logger.info("File deleted: " + lowresFile.toString() );
                        // else
                        {
                            logger.info( "File deleted failed on: " + lowresFile.toString() );
                        }
                    }

                    File highresFile = pi.getHighresFile();
                    if ( highresFile.exists() ) {
                        ok = highresFile.delete();
                        if ( !ok ) //logger.info("File deleted: " + highresFile.toString() );
                        //else
                        {
                            logger.info( "File deleted failed on: " + highresFile.toString() );
                        }
                    }

                    n.deleteNode();

                    if ( !ok ) {
                        JOptionPane.showMessageDialog( Settings.anchorFrame,
                                Settings.jpoResources.getString( "fileDeleteError" ) + highresFile.toString(),
                                Settings.jpoResources.getString( "genericError" ),
                                JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
            Settings.pictureCollection.clearSelection();
        }

    }

    //  Controller type Stuff
    /**
     *  The node the popup menu was created for
     */
    private final SortableDefaultMutableTreeNode popupNode;

    /**
     *  Reference to the {@link NodeNavigatorInterface} which indicates the nodes being displayed.
     */
    private NodeNavigatorInterface mySetOfNodes = null;

    /**
     *  Index of the {@link #mySetOfNodes} being popped up.
     */
    private int index = 0;


    /**
     * The "Show Picture" menu button calls this function
     */
    private void requestShowPicture() {
        Jpo.browsePictures( popupNode );
    }


    /**
     * Show the Properties GUI
     */
    private void requestProperties() {
        new PictureInfoEditor( mySetOfNodes, index );
    }


    /**
     * handle the Categories click
     */
    private void requestCategories() {
        if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
            TreeNodeController.showCategoryUsageGUI( popupNode );
        } else {
            if ( !Settings.pictureCollection.isSelected( popupNode ) ) {
                Settings.pictureCollection.clearSelection();
                TreeNodeController.showCategoryUsageGUI( popupNode );
            } else {
                CategoryUsageJFrame cujf = new CategoryUsageJFrame();
                cujf.setSelection( Settings.pictureCollection.getSelectedNodesAsVector() );
            }
        }
    }


    /**
     * request to select for Email
     */
    private void requestSelectForEmail() {
        if ( Settings.pictureCollection.countSelectedNodes() < 1 ) {
            popupNode.getPictureCollection().removeFromMailSelection( popupNode );
        } else {
            Enumeration<SortableDefaultMutableTreeNode> selection = Settings.pictureCollection.getSelectedNodesAsVector().elements();
            SortableDefaultMutableTreeNode n;
            while ( selection.hasMoreElements() ) {
                n = selection.nextElement();
                if ( n.getUserObject() instanceof PictureInfo ) {
                    n.getPictureCollection().removeFromMailSelection( n );
                }
            }
        }
    }


    /**
     * Switches the picture back to no rotation
     */
    private void noRotation() {
        Object o = popupNode.getUserObject();
        PictureInfo pi = (PictureInfo) o;
        pi.setRotation( 0 );
        popupNode.refreshThumbnail();
    }


    /**
     * Rotates the picture by the indicated angle
     * @param angle 0..360 degrees
     */
    private void rotatePicture( int angle ) {
        Object o = popupNode.getUserObject();
        PictureInfo pi = (PictureInfo) o;
        pi.rotate( angle );
        popupNode.refreshThumbnail();
    }


    /**
     *  Here we receive notification that the nodes have been updated. The method
     *  then updates the menu items for the drop targets. Those that are null are
     *  not shown. If no drop targets are shown at all the seerator in the submenu
     *  is not shown either.
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
     *  Here we receive notification that the copy locations were updated and then go
     *  and update the targets on the menu.
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
     *  Here we receive notification that the user Functions were updated and then go
     *  and update the menu.
     */
    public void userFunctionsChanged() {
        for ( int i = 0; i < Settings.maxUserFunctions; i++ ) {
            if ( ( Settings.userFunctionNames[i] != null ) && ( Settings.userFunctionNames[i].length() > 0 ) && ( Settings.userFunctionCmd[i] != null ) && ( Settings.userFunctionCmd[i].length() > 0 ) ) {
                userFunctionsJMenuItems[i].setText( Settings.userFunctionNames[i] );
                userFunctionsJMenuItems[i].setVisible( true );
            } else {
                userFunctionsJMenuItems[i].setVisible( false );
            }
        }
    }


    /**
     *  The PicturePopupMenu routes menu click events for the recent drop node, copy locations
     *  and user Functions here for processing. These are all array based lists.
     *
     * @param e
     */
    public void actionPerformed( ActionEvent e ) {
        //  was a recentDropNode picked?
        for ( int i = 0; i < Settings.MAX_DROPNODES; i++ ) {
            if ( ( recentDropNodes[i] != null ) && ( e.getSource().hashCode() == recentDropNodes[i].hashCode() ) ) {

                SortableDefaultMutableTreeNode nextChild =
                        (SortableDefaultMutableTreeNode) ( (SortableDefaultMutableTreeNode) popupNode.getParent() ).getChildAfter( popupNode );

                popupNode.moveToLastChild( Settings.recentDropNodes[i] );
                Settings.memorizeGroupOfDropLocation( Settings.recentDropNodes[i] );

                return;
            }
        }

        //  was a copyLocationsJMenuItems item picked?
        for ( int i = 0; i < Settings.MAX_MEMORISE; i++ ) {
            if ( ( copyLocationsJMenuItems[i] != null ) && ( e.getSource().hashCode() == copyLocationsJMenuItems[i].hashCode() ) ) {
                popupNode.validateAndCopyPicture( new File( Settings.copyLocations[i] ) );
                Settings.memorizeCopyLocation( Settings.copyLocations[i] );
                return;
            }
        }

        //  was a userFunctionsJMenuItems item chosen?
        for ( int i = 0; i < Settings.maxUserFunctions; i++ ) {
            if ( ( userFunctionsJMenuItems[i] != null ) && ( e.getSource().hashCode() == userFunctionsJMenuItems[i].hashCode() ) ) {
                try {
                    Tools.runUserFunction( i, (PictureInfo) popupNode.getUserObject() );
                } catch ( ClassCastException x ) {
                    // Well, it was the wrong type
                } catch ( NullPointerException x ) {
                    // Well, it wasn't a good node anyway.
                }
                return;
            }
        }
    }


    /**
     * Intercepting this method to attach the change listeners when the menu
     * becomes visible so that they can also be removed when the menu goes away
     * again.
     */
    @Override
    protected void firePopupMenuWillBecomeVisible() {
        logger.info( "Adding listeners" );
        Settings.addRecentDropNodeListener( this );
        Settings.addCopyLocationsChangeListener( this );
        Settings.addUserFunctionsChangeListener( this );
        super.firePopupMenuWillBecomeVisible();
    }


    /**
     * Intercepting this method to attach the change listeners when the menu
     * becomes visible so that they can also be removed when the menu goes away
     * again.
     */
    @Override
    protected void firePopupMenuWillBecomeInvisible() {
        logger.info( "Removing Listeners" );
        Settings.removeRecentDropNodeListener( this );
        Settings.removeCopyLocationsChangeListener( this );
        Settings.removeUserFunctionsChangeListener( this );
        super.firePopupMenuWillBecomeInvisible();
    }
}