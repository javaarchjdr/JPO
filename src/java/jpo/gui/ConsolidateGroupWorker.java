package jpo.gui;

import java.io.File;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import jpo.dataModel.GroupInfo;
import jpo.dataModel.PictureInfo;
import jpo.dataModel.Settings;
import jpo.dataModel.SortableDefaultMutableTreeNode;
import jpo.dataModel.Tools;

/*
 ConsolidateGroup.java:  class that consolidated the pictures of a group in one directory

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
 * This class moves all pictures of a group node to a target directory.
 */
public class ConsolidateGroupWorker extends SwingWorker<String, String> {

    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( ConsolidateGroupWorker.class.getName() );

    /**
     * the directory where the pictures are to be moved to
     */
    private final File targetDirectoryHighres;

    /**
     * the node to start from
     */
    private final SortableDefaultMutableTreeNode startNode;
    /**
     * flag that indicates that the subgroups should also be considered
     */
    private final boolean recurseGroups;

    /**
     * Creates a Thread which runs the consolidation.
     *
     * @param targetDirectoryHighres	Where we want the files moved to
     * @param startNode	The node from which this is all to be built.
     * @param recurseGroups Flag indicating subgroups should be included if the
     * moveLowres flag is true
     * @param progGui A Progress Gui
     */
    public ConsolidateGroupWorker( File targetDirectoryHighres,
            SortableDefaultMutableTreeNode startNode, boolean recurseGroups,
            ProgressGui progGui ) {
        this.targetDirectoryHighres = targetDirectoryHighres;
        this.startNode = startNode;
        this.recurseGroups = recurseGroups;
        this.progGui = progGui;

        if ( !targetDirectoryHighres.exists() ) {
            LOGGER.severe( String.format( "Aborting because target directory %s doesn't exist", targetDirectoryHighres.getPath() ) );
            return;
        }
        if ( !targetDirectoryHighres.canWrite() ) {
            LOGGER.severe( String.format( "Aborting because directory %s can't be written to", targetDirectoryHighres.getPath() ) );
            return;
        }

        execute();
    }
    /**
     * This object holds a reference to the progress GUI for the user.
     */
    private final ProgressGui progGui;

    /**
     * The run method is fired by starting the thread. It creates a ProgressGui
     * and does the work.
     *
     * @return Integer.MAX_VALUE
     */
    @Override
    protected String doInBackground() {
        consolidateGroup( startNode );

        return "done";
    }

    @Override
    protected void process( List<String> messages ) {
        for ( String message : messages ) {
            progGui.progressIncrement();
        }
    }

    @Override
    protected void done() {
        progGui.switchToDoneMode();

        if ( errorCount > 0 ) {
            JOptionPane.showMessageDialog( progGui,
                    String.format( "Could not move %d pictures", errorCount ),
                    Settings.jpoResources.getString( "genericError" ),
                    JOptionPane.ERROR_MESSAGE );
        }

    }

    private int notMovedCount;
    private int movedCount;
    private int errorCount;

    /**
     * This method consolidates all the nodes of the supplied group.
     *
     * @param groupNode the Group whose nodes are to be consolidated.
     */
    private void consolidateGroup( SortableDefaultMutableTreeNode groupNode ) {
        List<SortableDefaultMutableTreeNode> nodes = groupNode.getChildPictureNodes( recurseGroups );
        System.out.println( "List Size: " + nodes.size() );
        for ( SortableDefaultMutableTreeNode node : nodes ) {
            PictureInfo pictureInfo = (PictureInfo) node.getUserObject();
            System.out.println( "node: " + pictureInfo.toString() );
            if ( needToMovePicture( pictureInfo, targetDirectoryHighres ) ) {
                if ( movePicture( pictureInfo, targetDirectoryHighres ) ) {
                    LOGGER.info( String.format( "Successfully Moved Highres file of node %s", pictureInfo.toString() ) );
                    movedCount++;
                    publish( String.format( "Consolidated node: %s", node.toString() ) );
                } else {
                    LOGGER.severe( String.format( "Could not move highres picture of node %s. Aborting.", node.toString() ) );
                    errorCount++;
                }
            } else {
                notMovedCount++;
                publish( String.format( "No need to move node: %s", node.toString() ) );
            }
        }
    }

    /**
     * Returns true if the picture needs to be moved, false if not
     *
     * @param pictureInfo the PictureInfo pointing to the image to move
     * @param targetDirectory the target directory to move it to
     * @return True if a move is needed False if not.
     */
    public static boolean needToMovePicture( PictureInfo pictureInfo, File targetDirectory ) {
        File pictureFile = pictureInfo.getImageFile();
        // don't move if the pictureInfo points at a null file
        if ( pictureFile == null ) {
            return false;
        }

        // don't move if the file is already in the correct directory
        File parentDirectory = pictureFile.getParentFile();
        if ( ( parentDirectory != null ) && ( parentDirectory.equals( targetDirectory ) ) ) {
            return false;
        }

        return true;
    }

    /**
     * This method moves a PictureInfo's highres file to the target directory if
     * it exists and can be moved necessary.
     *
     * @param pictureInfo the PictureInfopointing to the highres file to move
     * @param targetDirectory the target directory
     * @return True if a real move was done False if not.
     */
    public static boolean movePicture( PictureInfo pictureInfo, File targetDirectory ) {
        File pictureFile = pictureInfo.getImageFile();

        // don't move if the file is already in the correct directory but report true to move
        File parentDirectory = pictureFile.getParentFile();
        if ( ( parentDirectory != null ) && ( parentDirectory.equals( targetDirectory ) ) ) {
            return true;
        }

        File newFile = Tools.inventPicFilename( targetDirectory, pictureInfo.getImageFilename() );
        if ( Tools.moveFile( pictureFile, newFile ) ) {
            pictureInfo.setImageLocation( newFile );
            Tools.correctReferences( pictureFile, newFile );

            return true;
        } else {
            LOGGER.info( String.format( "Failed to move file %s to %s.", pictureFile.toString(), newFile.toString() ) );
            return false;
        }
    }

}