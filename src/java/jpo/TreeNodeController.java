package jpo;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import javax.swing.*;

/*
TreeNodeController.java: This class should handle all the interactive GUI stuff for a Tree Node
 
Copyright (C) 2007  Richard Eigenmann.
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
 *   This class should handle all the interactive GUI stuff for a Tree Node. Formerly the
 *   SortableDefaultMutableTreeNode was doing data stuff and GUI stuff. It should really
 *   only do data stuff.
 *
 *   @author  Richard Eigenmann
 */
public class TreeNodeController {

    /**
     *  This method brings up a Filechooser and then loads the images off the specified flat file.
     * @param  targetNode   The node at which to add the flat file.
     */
    public static void addFlatFile( SortableDefaultMutableTreeNode targetNode ) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
        jFileChooser.setApproveButtonText( Settings.jpoResources.getString( "fileOpenButtonText" ) );
        jFileChooser.setDialogTitle( Settings.jpoResources.getString( "addFlatFileTitle" ) );
        jFileChooser.setCurrentDirectory( Settings.getMostRecentCopyLocation() );
        
        int returnVal = jFileChooser.showOpenDialog( Settings.anchorFrame );
        if( returnVal == JFileChooser.APPROVE_OPTION ) {
            File chosenFile = jFileChooser.getSelectedFile();
            
            try {
                targetNode.addFlatFile( chosenFile );
            } catch (IOException e) {
                Tools.log( "IOException " + e.getMessage() );
                JOptionPane.showMessageDialog( Settings.anchorFrame,
                        e.getMessage() ,
                        Settings.jpoResources.getString("genericError"),
                        JOptionPane.ERROR_MESSAGE );
            }
            
        }
    }
    

    /**
     *   brings up a file chooser
     *   and allows the user to specify where the image should to be copied to and then
     *   copies it.
     *
     *
     */
    public static void copyToNewLocation( SortableDefaultMutableTreeNode node ) {
        URL originalUrl;
        if ( ! ( node.getUserObject() instanceof PictureInfo ) ) {
            Tools.log( "SDMTN.copyToNewLocation: inkoked on a non PictureInfo type node! Aborted." );
            return;
        }
        try {
            originalUrl = ((PictureInfo) node.getUserObject()).getHighresURL();
        } catch ( MalformedURLException x ) {
            Tools.log("MarformedURLException trapped on: " + ((PictureInfo) node.getUserObject()).getHighresLocation() + "\nReason: " + x.getMessage());
            JOptionPane.showMessageDialog(
                    Settings.anchorFrame,
                    "MarformedURLException trapped on:\n" + ((PictureInfo) node.getUserObject()).getHighresLocation() + "\nReason: " + x.getMessage(),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JFileChooser jFileChooser = new JFileChooser();
        
        jFileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        jFileChooser.setApproveButtonText( Settings.jpoResources.getString( "CopyImageDialogButton" ) );
        jFileChooser.setDialogTitle( Settings.jpoResources.getString( "CopyImageDialogTitle" ) + originalUrl );
        jFileChooser.setCurrentDirectory( Settings.getMostRecentCopyLocation() );
        
        int returnVal = jFileChooser.showSaveDialog( Settings.anchorFrame );
        if( returnVal == JFileChooser.APPROVE_OPTION ) {
            node.validateAndCopyPicture( jFileChooser.getSelectedFile() );
        }
    }
    

    /**
     *   Bring up a Dialog where the user can input a new name for a file and rename it.
     */
    public static void fileRename( SortableDefaultMutableTreeNode node ) {
        Object userObject = node.getUserObject();
        if ( ! ( userObject instanceof PictureInfo ) )
            return;
        
        PictureInfo pi = (PictureInfo) userObject;
        File highresFile = pi.getHighresFile();
        if ( highresFile == null )
            return;
        
        Object object =  Settings.jpoResources.getString("FileRenameLabel1")
        + highresFile.toString()
        + Settings.jpoResources.getString("FileRenameLabel2") ;
        String selectedValue = JOptionPane.showInputDialog(
                Settings.anchorFrame, 					// parent component
                object, 						// message
                highresFile.toString() );				// initialSelectionValue
        if ( selectedValue != null ) {
            File newName = new File( selectedValue );
            if ( highresFile.renameTo( newName ) ) {
                Tools.log("Sucessufully renamed: " + highresFile.toString() + " to: " + selectedValue);
                try {
                    pi.setHighresLocation( newName.toURI().toURL() );
                } catch ( MalformedURLException x ) {
                    Tools.log("Caught a MalformedURLException because of: " + x.getMessage() );
                }
            } else {
                Tools.log("Rename failed from : " + highresFile.toString() + " to: " + selectedValue);
            }
        }
    }

    
    /**
     *  This function brings up a PictureInfoEditor of a GroupInfoEditor
     */
    public static void showEditGUI( SortableDefaultMutableTreeNode node ) {
        if ( node.getUserObject() instanceof PictureInfo ) {
            new PictureInfoEditor( node );
        } else if ( node.getUserObject() instanceof GroupInfo ) {
            new GroupInfoEditor( node );
        } else {
            Tools.log( "TreeNodecontroller.showEditGUI: doesn't know what kind of editor to use. Irngoring request.");
        }
    }
    
    

    /**
     *  This function opens the CateGoryUsageEditor.
     */
    public static void showCategoryUsageGUI( SortableDefaultMutableTreeNode node ) {
        //Tools.log("SDMTN.showCategoryUsageGUI invoked");
        if ( node.getUserObject() instanceof PictureInfo ) {
            CategoryUsageJFrame cujf = new CategoryUsageJFrame();
            Vector nodes = new Vector();
            nodes.add( node );
            cujf.setSelection( nodes );
        } else  if ( node.getUserObject() instanceof GroupInfo ) {
            CategoryUsageJFrame cujf = new CategoryUsageJFrame();
            cujf.setGroupSelection( node, false );
        } else{
            Tools.log( "SortableDefaultMutableTreeNode.showCategoryUsageGUI: doesn't know what kind of editor to use. Ignoring request.");
        }
    }
    
    
    
    
}
