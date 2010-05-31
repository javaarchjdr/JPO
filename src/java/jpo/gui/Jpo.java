package jpo.gui;

import jpo.dataModel.FlatGroupNavigator;
import jpo.dataModel.RandomNavigator;
import jpo.dataModel.GroupNavigator;
import jpo.dataModel.NodeNavigatorInterface;
import jpo.gui.swing.ResizableJFrame;
import jpo.gui.swing.MainWindow;
import jpo.dataModel.Tools;
import jpo.dataModel.Settings;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeModelEvent;
import jpo.dataModel.SortableDefaultMutableTreeNode;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.util.logging.Handler;
import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import jpo.dataModel.DuplicatesQuery;
import jpo.dataModel.GroupInfo;
import jpo.dataModel.PictureInfo;
import jpo.dataModel.QueryNavigator;
import jpo.dataModel.TextQuery;
import jpo.gui.swing.CollectionJTree;
import jpotestground.CheckThreadViolationRepaintManager;


/*
Jpo.java:  The collection controller object of the JPO application

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
 * This is the collection controller the Java Picture Organizer application that lets
 * a user view a collection of pictures in as thumbnails, in a separate window
 * or in a full sized mode.<p>
 *
 *
 * <p><img src=../Overview.png border=0><p>
 *
 * It uses a list of pictures (PictureList file) to create a hierarchical model of
 * <code>SortableDefaultMutableTreeNode</code>s that represent the structure of the collection.
 * Each node has an associated object of {@link GroupInfo} or {@link PictureInfo} type.
 *
 * The Jpo class creates the following main objects:
 *
 * The {@link CollectionJTreeController} visualises the model and allows the user to
 * expand and collapse branches of the tree with the mouse. If a node is clicked this generates
 * a <code>valueChanged</code> event from the model which is sent to all listening objects.<p>
 *
 * Listening objects are the thumbnail pane which displays the group if a node of type
 * <code>GroupInfo</code> has been selected.<p>
 *
 * This listener architecture allows fairly easy expansion of the application
 * since all that is required is that any additional objects that need to be change the picture
 * or need to be informed of a change can connect to the model in this manner and
 * need no other controls.
 *
 * @author Richard Eigenmann, richard.eigenmann@gmail.com
 * @version 0.9
 * @see CollectionJTree
 * @see ThumbnailPanelController
 * @see PictureViewer
 * @since JDK1.6.0
 */
public class Jpo
        implements ApplicationMenuInterface {

    /**
     *   the main method is the entry point for this application (or any)
     *   Java application. No parameter passing is used in the Jpo application. <p>
     *
     *   The method verifies that the user has the correct Java Virtual Machine (> 1.4.0)
     *   and then created a new {@link Jpo} object.
     */
    private MainWindow mainWindow;

    /**
     * Defines a logger for this class
     */
    private static Logger logger = Logger.getLogger( Jpo.class.getName() );


    /**
     *  Constructor for the Jpo application that creates the main JFrame, attaches an
     *  {@link ApplicationJMenuBar}, adds a JSplitPane to which it adds the {@link CollectionJTreeController}
     *  on the left side and a {@link ThumbnailPanelController} on the right side.
     */
    public Jpo() {

        // set up logging level
        Handler[] handlers =
                Logger.getLogger( "" ).getHandlers();
        for ( int index = 0; index < handlers.length; index++ ) {
            handlers[index].setLevel( Level.FINEST );
        }
        Settings.loadSettings();

        logger.info( "------------------------------------------------------------\n      Starting JPO" );

        //Toolkit.getDefaultToolkit().getSystemEventQueue().push(
        //       new TracingEventQueue() );
        RepaintManager.setCurrentManager( new CheckThreadViolationRepaintManager() );


        // does this give us any performance gains?? RE 7.6.2004
        javax.imageio.ImageIO.setUseCache( false );
        try {
            // Activate OpenGL performance improvements
            System.setProperty( "sun.java2d.opengl", "true" );
            SwingUtilities.invokeAndWait( new Runnable() {

                public void run() {
                    collectionJTreeController = new CollectionJTreeController( Jpo.this );
                    searchesJTree = new QueriesJTree();
                    thumbnailJScrollPane = new ThumbnailPanelController();
                    infoPanelController = new InfoPanelController();
                    mainWindow = new MainWindow( Jpo.this, collectionJTreeController.getJScrollPane(), searchesJTree.getJScrollPane(), thumbnailJScrollPane, infoPanelController.getInfoPanel() );
                    mainWindow.addWindowListener( new WindowAdapter() {

                        @Override
                        public void windowClosing( WindowEvent e ) {
                            mainWindow.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
                            closeJpo();
                        }
                    } );
                }
            } );
        } catch ( InterruptedException ex ) {
            logger.log( Level.SEVERE, null, ex );
        } catch ( InvocationTargetException ex ) {
            logger.log( Level.SEVERE, null, ex );
        }

        Settings.pictureCollection.getTreeModel().addTreeModelListener( new MainAppModelListener() );
        if ( !loadAutoloadCollection() ) {
            requestFileNew();
        }
        new CameraWatchDaemon( this );

    }

    /**
     *  This object does all the tree work. It can load and save the nodes of the tree, listens to
     *  events happening on the tree and calls back with any actions that should be performed.
     *
     * @see CollectionJTreeController
     */
    public static CollectionJTreeController collectionJTreeController;

    /**
     * This object does the controller work for the Queries.
     */
    public static QueriesJTree searchesJTree;

    /**
     *  This object holds all the thumbnails and deals with all the thumbnail events.
     * ToDo: Make this private again when the wordlist browser is properly integrated
     **/
    public static ThumbnailPanelController thumbnailJScrollPane;

    /**
     * The InfoPanelController
     */
    private static InfoPanelController infoPanelController;

    /**
     *  This Vector allows us to keep track of the number of ThumbnailCreationThreads
     *  we have fired off. Could be enhanced to dynamically start more or less.
     */
    private static Vector<ThumbnailCreationFactory> thumbnailFactories = new Vector<ThumbnailCreationFactory>();


    /**
     *  static initializer for the ThumbnailCreationThreads
     */
    static {
        for ( int i = 1; i <= Settings.numberOfThumbnailCreationThreads; i++ ) {
            thumbnailFactories.add( new ThumbnailCreationFactory() );
        }
    }


    /**
     *  This method looks if it can find a file called autostartJarPicturelist.xml in the classpath;
     *  failing that it loads the file indicated in Settings.autoLoad.
     *  @return returns whether this was successfull or not.
     */
    public boolean loadAutoloadCollection() {
        /* Settings.jarAutostartList = ClassLoader.getSystemResource( "autostartJarPicturelist.xml" );


        if ( Settings.jarAutostartList != null ) {
        Settings.jarRoot = Settings.jarAutostartList.toString().substring( 0, Settings.jarAutostartList.toString().indexOf( "!" ) + 1 );
        logger.info( "Autoloading: " + Settings.jarAutostartList.toString() );
        try {
        Settings.pictureCollection.getRootNode().streamLoad( Settings.jarAutostartList.openStream() );
        thumbnailJScrollPane.show( new GroupNavigator( Settings.pictureCollection.getRootNode() ) );
        } catch ( IOException x ) {
        logger.info( Settings.jarAutostartList.toString() + " could not be loaded\nReason: " + x.getMessage() );
        }


        } else*/
        if ( ( Settings.autoLoad != null ) && ( Settings.autoLoad.length() > 0 ) ) {
            File xmlFile = new File( Settings.autoLoad );
            logger.fine( "File to Autoload: " + Settings.autoLoad );
            if ( xmlFile.exists() ) {
                try {
                    Settings.pictureCollection.fileLoad( xmlFile );
                } catch ( FileNotFoundException ex ) {
                    Logger.getLogger( Jpo.class.getName() ).log( Level.SEVERE, null, ex );
                    return false;
                }
                positionToNode( Settings.pictureCollection.getRootNode() );
                return true;
            } else {
                logger.fine( String.format( "File %s doesn't exist. not loading", xmlFile.toString() ) );
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * Brings up a QueryJFrame GUI.
     * @param startSearchNode
     */
    public void find( SortableDefaultMutableTreeNode startSearchNode ) {
        new QueryJFrame( startSearchNode, this );
    }


    /**
     *  method that is invoked when the Jpo application is to be closed. Checks if
     *  the main application window size should be saved and saves if necessary.
     *  also checks for unsaved changes before closing the application.
     */
    public void closeJpo() {
        if ( checkUnsavedUpdates() ) {
            return;
        }

        if ( Settings.unsavedSettingChanges ) {
            Settings.writeSettings();
        }

        logger.info( "Exiting JPO\n------------------------------------------------------------" );

        System.exit( 0 );
    }


    /**
     *   Call to do the File|New function
     */
    public void requestFileNew() {
        Runnable r = new Runnable() {

            public void run() {
                if ( checkUnsavedUpdates() ) {
                    return;
                }
                Settings.pictureCollection.clearCollection();
                positionToNode( Settings.pictureCollection.getRootNode() );
            }
        };
        SwingUtilities.invokeLater( r );
    }


    /**
     *   The {@link ApplicationMenuInterface} calls here when the user
     *   wants to add pictures to the root node of the collection.
     */
    public void requestAddPictures() {
        chooseAndAddPicturesToGroup( Settings.pictureCollection.getRootNode() );
    }


    /**
     *   Brings up a dialog where the user can select the collection
     *   to be loaded. Calls {@link SortableDefaultMutableTreeNode#fileLoad}
     */
    public void requestFileLoad() {
        if ( checkUnsavedUpdates() ) {
            return;
        }
        final File fileToLoad = Tools.chooseXmlFile();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    Settings.pictureCollection.fileLoad( fileToLoad );
                } catch ( FileNotFoundException ex ) {
                    logger.info( this.getClass().toString() + ".requestFileLoad: FileNotFoundExecption: " + ex.getMessage() );
                    JOptionPane.showMessageDialog( Settings.anchorFrame,
                            ex.getMessage(),
                            Settings.jpoResources.getString( "genericError" ),
                            JOptionPane.ERROR_MESSAGE );

                    return;
                }
                positionToNode( Settings.pictureCollection.getRootNode() );
            }
        };
        t.start();
    }


    /**
     * A call to this method with a GroupInfo node will position the JTree to the
     * node and will open the node in the ThumbnailPanel
     * @param displayNode must be of a node of type GroupInfo
     */
    public static void positionToNode(
            SortableDefaultMutableTreeNode displayNode ) {
        if ( displayNode == null ) {
            logger.severe( "I've been told to position to a null node!" );
            Thread.dumpStack();
            return;
        }
        if ( !( displayNode.getUserObject() instanceof GroupInfo ) ) {
            logger.severe( "We can only position to GroupInfo nodes!" );
            Thread.dumpStack();
            return;
        }
        collectionJTreeController.setSelectedNode( displayNode );
        showThumbnails( new GroupNavigator( displayNode ) );
        infoPanelController.showInfo( displayNode );
    }


    /**
     * A call to this method with a ThumbnailBrowser will show the
     * nodes of in the Thumbnail pane.
     * @param nodeSet must be of a node of type GroupInfo
     */
    public static void showThumbnails( NodeNavigatorInterface nodeSet ) {
        if ( nodeSet == null ) {
            logger.severe( "I've been told to showThumbnails on a null set!" );
            Thread.dumpStack();
            return;
        }
        thumbnailJScrollPane.show( nodeSet );
        //infoPanelController.show(nodeSet);
    }


    /**
     * Switches the left panel to the queries, expands to the right place and
     * shows the numbnails of the query.
     * @param node The query node to be displayed
     */
    public void showQuery( DefaultMutableTreeNode node ) {
        mainWindow.tabToSearches();
        searchesJTree.setSelectedNode( node );
    }


    /**
     * A call to this method will result in the InfoPanel updatings it's display
     * @param node the Node for which to show the info
     */
    public static void showInfo( SortableDefaultMutableTreeNode node ) {
        if ( node == null ) {
            logger.severe( "I've been told to show Info on a null node!" );
            Thread.dumpStack();
            return;
        }
        infoPanelController.showInfo( node );
    }


    /**
     * Opens a Picture Browser for the specified node. A PictureInfo node
     * will open directly and a GroupInfo node will open at it's first child node.
     * @param node
     */
    public static void browsePictures( SortableDefaultMutableTreeNode node ) {
        Object o = node.getUserObject();
        if ( o instanceof PictureInfo ) {
            //node.showLargePicture();
            FlatGroupNavigator sb = new FlatGroupNavigator( (SortableDefaultMutableTreeNode) node.getParent() );
            int index = 0;
            for ( int i = 0; i < sb.getNumberOfNodes(); i++ ) {
                if ( sb.getNode( i ).equals( node ) ) {
                    index = i;
                    i = sb.getNumberOfNodes();
                }
            }
            PictureViewer pictureViewer = new PictureViewer();
            pictureViewer.show( sb, index );
        } else if ( o instanceof GroupInfo ) {
            SortableDefaultMutableTreeNode firstPicNode = node.findFirstPicture();
            if ( firstPicNode != null ) {
                //firstPicNode.showLargePicture();
                FlatGroupNavigator sb = new FlatGroupNavigator( node );
                PictureViewer pictureViewer = new PictureViewer();
                pictureViewer.show( sb, 0 );
            } else {
                JOptionPane.showMessageDialog( Settings.anchorFrame,
                        Settings.jpoResources.getString( "noPicsForSlideshow" ),
                        Settings.jpoResources.getString( "genericError" ),
                        JOptionPane.ERROR_MESSAGE );
            }
        }

    }


    /**
     *   Requests a recently loaded collection to be loaded. The index
     *   of which recently opened file to load is supplied from the
     *   {@link ApplicationJMenuBar} through the interface method
     *   {@link ApplicationMenuInterface#requestOpenRecent}.
     */
    public void requestOpenRecent( final int i ) {
        if ( checkUnsavedUpdates() ) {
            return;
        }
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    Settings.pictureCollection.fileLoad( new File( Settings.recentCollections[i] ) );
                } catch ( FileNotFoundException ex ) {
                    Logger.getLogger( Jpo.class.getName() ).log( Level.SEVERE, null, ex );
                    logger.info( this.getClass().toString() + ".requestFileLoad: FileNotFoundExecption: " + ex.getMessage() );
                    JOptionPane.showMessageDialog( Settings.anchorFrame,
                            ex.getMessage(),
                            Settings.jpoResources.getString( "genericError" ),
                            JOptionPane.ERROR_MESSAGE );
                    return;
                }
                positionToNode( Settings.pictureCollection.getRootNode() );
            }
        };
        t.start();
    }


    /**
     * Checks for unsaved changes in the data model, pops up a dialog and does the save if so indicated by the user.
     *  
     * @return Returns true if the user want to cancel the close.
     */
    public boolean checkUnsavedUpdates() {
        Tools.checkEDT();
        if ( Settings.pictureCollection.getUnsavedUpdates() ) {
            Object[] options = {
                Settings.jpoResources.getString( "discardChanges" ),
                Settings.jpoResources.getString( "genericSaveButtonLabel" ),
                Settings.jpoResources.getString( "FileSaveAsMenuItemText" ),
                Settings.jpoResources.getString( "genericCancelText" ) };
            int option = JOptionPane.showOptionDialog(
                    Settings.anchorFrame,
                    Settings.jpoResources.getString( "unsavedChanges" ),
                    Settings.jpoResources.getString( "genericWarning" ),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0] );

            switch ( option ) {
                case 0:
                    return false;
                case 1:
                    requestFileSave();
                    return Settings.pictureCollection.getUnsavedUpdates();
                case 2:
                    fileSaveAs();
                    return Settings.pictureCollection.getUnsavedUpdates();
                case 3:
                    return true;
            }
        }
        return false;
    }


    /**
     *   Calls the {@link jpo.dataModel.PictureCollection#fileSave} method that saves the
     *   current collection under it's present name and if it was never
     *   saved before brings up a popup window.
     */
    public void requestFileSave() {
        if ( Settings.pictureCollection.getXmlFile() == null ) {
            fileSaveAs();
        } else {
            logger.info( String.format( "xml file is not null and reads: %s", Settings.pictureCollection.getXmlFile() ) );
            Settings.pictureCollection.fileSave();
            afterFileSaveDialog();
        }
    }


    /**
     *   Ask whether the file should be opened by default.
     */
    public void afterFileSaveDialog() {
        JPanel p = new JPanel();
        p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
        p.add( new JLabel( Settings.jpoResources.getString( "collectionSaveBody" ) + Settings.pictureCollection.getXmlFile().toString() ) );
        JCheckBox setAutoload = new JCheckBox( Settings.jpoResources.getString( "setAutoload" ) );
        if ( ( !( Settings.autoLoad == null ) ) && ( ( new File( Settings.autoLoad ) ).compareTo( Settings.pictureCollection.getXmlFile() ) == 0 ) ) {
            setAutoload.setSelected( true );
        }
        p.add( setAutoload );
        JOptionPane.showMessageDialog( Settings.anchorFrame,
                p,
                Settings.jpoResources.getString( "collectionSaveTitle" ),
                JOptionPane.INFORMATION_MESSAGE );
        if ( setAutoload.isSelected() ) {
            Settings.autoLoad = Settings.pictureCollection.getXmlFile().toString();
            Settings.writeSettings();
        }
    }


    /**
     *   Calls the {@link #fileSaveAs} method to bring up
     *   a filechooser where the user can select the filename to
     *   save under.
     */
    public void requestFileSaveAs() {
        fileSaveAs();
    }


    /**
     *   method that saves the entire index in XML format. It prompts for the
     *   filename first.
     */
    public void fileSaveAs() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
        jFileChooser.setDialogType( JFileChooser.SAVE_DIALOG );
        jFileChooser.setDialogTitle( Settings.jpoResources.getString( "fileSaveAsTitle" ) );
        jFileChooser.setMultiSelectionEnabled( false );
        jFileChooser.setFileFilter( new XmlFilter() );
        if ( Settings.pictureCollection.getXmlFile() != null ) {
            jFileChooser.setCurrentDirectory( Settings.pictureCollection.getXmlFile() );
        } else {
            jFileChooser.setCurrentDirectory( Settings.getMostRecentCopyLocation() );
        }

        int returnVal = jFileChooser.showSaveDialog( Settings.anchorFrame );
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            File chosenFile = jFileChooser.getSelectedFile();
            chosenFile = Tools.correctFilenameExtension( "xml", chosenFile );
            if ( chosenFile.exists() ) {
                int answer = JOptionPane.showConfirmDialog( Settings.anchorFrame,
                        Settings.jpoResources.getString( "confirmSaveAs" ),
                        Settings.jpoResources.getString( "genericWarning" ),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE );
                if ( answer == JOptionPane.CANCEL_OPTION ) {
                    return;
                }
            }

            Settings.pictureCollection.setXmlFile( chosenFile );
            Settings.pictureCollection.fileSave();

            Settings.memorizeCopyLocation( chosenFile.getParent() );
            Settings.pushRecentCollection( chosenFile.toString() );
            afterFileSaveDialog();
        }
    }


    /**
     *   Calls {@link #closeJpo} to shut down the application.
     */
    public void requestExit() {
        closeJpo();
    }


    /**
     *   Calls {@link #find} to bring up a find dialog box.
     */
    public void openFindDialog() {
        find( Settings.pictureCollection.getRootNode() );
    }


    /**
     *   Creates a {@link ReconcileJFrame} which lets the user
     *   specify a directory whose pictures are then compared
     *   against the current collection.
     */
    public void requestCheckDirectories() {
        new ReconcileJFrame( Settings.pictureCollection.getRootNode() );
    }


    /**
     *  Creates an IntegrityChecker that does it's magic on the collection.
     */
    public void requestCheckIntegrity() {
        new IntegrityChecker( Settings.pictureCollection.getRootNode() );
    }


    /**
     *   Creates a {@link SettingsDialog} where the user can edit
     *   Application wide settings.
     */
    public void requestEditSettings() {
        new SettingsDialog( true );
    }


    /**
     *   opens up the Camera Editor GUI. See {@link CamerasEditor}
     */
    public void requestEditCameras() {
        new CamerasEditor();
    }


    /**
     *   calls up the Pictureviewer
     */
    public void performSlideshow() {
        PictureViewer p1 = new PictureViewer();
        p1.switchWindowMode( ResizableJFrame.WINDOW_LEFT );
        //p1.switchDecorations( true );
        PictureViewer p2 = new PictureViewer();
        p2.switchWindowMode( ResizableJFrame.WINDOW_RIGHT );
        //p2.switchDecorations( true );
        RandomNavigator rb1 = new RandomNavigator( Settings.pictureCollection.getRootNode().getChildPictureNodes( true ), String.format( "Randomised pictures from %s", Settings.pictureCollection.getRootNode().toString() ) );
        RandomNavigator rb2 = new RandomNavigator( Settings.pictureCollection.getRootNode().getChildPictureNodes( true ), String.format( "Randomised pictures from %s", Settings.pictureCollection.getRootNode().toString() ) );
        p1.show( rb1, 0 );
        p1.startAdvanceTimer( 10 );
        p2.show( rb2, 0 );
        p2.startAdvanceTimer( 10 );
    }


    /**
     *  Opens up a Year Browser
     */
    public void requestYearBrowser() {
        new YearsBrowserController( Settings.pictureCollection.getRootNode() );
    }


    /**
     *  Opens up a Year Browser
     */
    public void requestYearlyAnalyis() {
        new YearlyAnalysisGuiController( Settings.pictureCollection.getRootNode() );
    }


    /**
     * The user wants to find duplicates
     */
    public void requestFindDuplicates() {
        DuplicatesQuery q = new DuplicatesQuery();
        DefaultMutableTreeNode newNode = Settings.pictureCollection.addQueryToTreeModel( q );
        showQuery( newNode );
        QueryNavigator queryBrowser = new QueryNavigator( q );
        showThumbnails( queryBrowser );
    }


    /**
     * Calling this method brings up a filechooser which allows pictures and directories
     * to be selected that are then added to the supplied node.
     * @param groupNode  The group nodde to which to add the pictures or subdirectories
     */
    public void chooseAndAddPicturesToGroup(
            SortableDefaultMutableTreeNode groupNode ) {
        PictureFileChooser pa = new PictureFileChooser( groupNode );
    }

    private class MainAppModelListener
            implements TreeModelListener {

        public void treeNodesChanged( TreeModelEvent e ) {
            TreePath tp = e.getTreePath();
            logger.fine( String.format( "The main app model listener trapped a tree node change event on the tree path: %s", tp.toString() ) );
            if ( tp.getPathCount() == 1 ) { //if the root node sent the event
                logger.fine( "Since this is the root node we will update the ApplicationTitle" );

                updateApplicationTitle();
            }
        }


        public void treeNodesInserted( TreeModelEvent e ) {
            // ignore
        }


        public void treeNodesRemoved( TreeModelEvent e ) {
            // ignore, the root can't be removed ... Really?
        }


        public void treeStructureChanged( TreeModelEvent e ) {
            TreePath tp = e.getTreePath();
            if ( tp.getPathCount() == 1 ) { //if the root node sent the event
                updateApplicationTitle();
            }
        }
    }


    /**
     * Sets the application title to the default tile based on the Resourcebundle string
     * ApplicationTitle and the file name of the loaded xml file if any.
     */
    private void updateApplicationTitle() {
        final File xmlFile = Settings.pictureCollection.getXmlFile();
        if ( xmlFile != null ) {
            mainWindow.updateApplicationTitleEDT( Settings.jpoResources.getString( "ApplicationTitle" ) + ":  " + xmlFile.toString() );
        } else {
            mainWindow.updateApplicationTitleEDT( Settings.jpoResources.getString( "ApplicationTitle" ) );
        }
    }


    /**
     *  Requests that a collection be added at this point in the tree
     *  @param popupNode The node at which to add
     *  @param fileToLoad The collection file to load
     *  @see GroupPopupInterface
     */
    public void requestAddCollection( SortableDefaultMutableTreeNode popupNode,
            File fileToLoad ) {
        SortableDefaultMutableTreeNode newNode = popupNode.addGroupNode( "New Group" );
        try {
            newNode.fileLoad( fileToLoad );
        } catch ( FileNotFoundException x ) {
            logger.info( this.getClass().toString() + ".fileToLoad: FileNotFoundExecption: " + x.getMessage() );
            JOptionPane.showMessageDialog( Settings.anchorFrame,
                    "File not found:\n" + fileToLoad.getPath(),
                    Settings.jpoResources.getString( "genericError" ),
                    JOptionPane.ERROR_MESSAGE );
        }
        newNode.getPictureCollection().setUnsavedUpdates( true );
        positionToNode( newNode );
        collectionJTreeController.expandPath( new TreePath( newNode.getPath() ) );
    }
}