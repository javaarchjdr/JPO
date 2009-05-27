package jpo.gui;

import jpo.dataModel.RecentFilesChangeListener;
import jpo.dataModel.Settings;
import jpo.*;
import java.awt.event.*;
import javax.swing.*;

/*
ApplicationJMenuBar.java:  main menu for the application
 
Copyright (C) 2002 -2009 Richard Eigenmann.
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
 *   This class deals with the visible components of the Jpo main menu. It creates the widgets and connects the
 *   actions back to the JPO application controller which is the Jpo class. It goes back through an
 *   Interface but since the main menu is so tightly tied to the application this decoupling just adds another
 *   class and can be done away with.
 *   The object that creates this menu must connect this menu to the Notifier for a locale change.
 *
 */

public class ApplicationJMenuBar extends JMenuBar
        implements RecentFilesChangeListener, LocaleChangeListener  {
    
    
    /**
     *  The File menu which is part of the JMenuBar for the Jpo application.
     **/
    private final JMenu FileJMenu = new JMenu();;
    
    
    /**
     *  The Edit menu which is part of the JMenuBar for the Jpo application.
     *
     **/
    private final JMenu EditJMenu = new JMenu();
    
    
    /**
     *  The Tools menu which is part of the JMenuBar for the Jpo application.
     *
     **/
    private final JMenu ToolsMenu = new JMenu();
    
    
    
    /**
     *  The Action menu which is part of the JMenuBar for the Jpo application.
     **/
    private final JMenu actionJMenu = new JMenu();
    
    
    /**
     *  Menu item that will request a Action | Send Email
     **/
    private final JMenuItem emailJMenuItem = new JMenuItem();
    
    
    /**
     *  The g menu which is part of the JMenuBar for the Jpo application.
     **/
    private final JMenu HelpJMenu = new JMenu();
    
    
    /**
     *  Menu item that will request a File|New operation.
     **/
    private final JMenuItem FileNewJMenuItem = new JMenuItem();
    
    
    /**
     *  Menu item that will request a File|Add operation.
     **/
    private final JMenuItem FileAddJMenuItem = new JMenuItem();
    
    /**
     *  Menu item that will request a File|Add from Camera operation.
     **/
    private final JMenuItem FileCameraJMenuItem = new JMenuItem();
    

    /**
     *  Menu item that allows the user to load a collection.
     **/
    private final JMenuItem FileLoadJMenuItem = new JMenuItem();
    
    
    /**
     *  Menu item that allows the user to load a collection recently used.
     **/
    private final JMenu FileOpenRecentJMenu= new JMenu();
    
    
    /**
     *   An array of recently opened collections.
     */
    private final JMenuItem []  recentOpenedfileJMenuItem = new JMenuItem[ Settings.recentFiles ];
    
    
    /**
     *  Menu item that allows the user to save the picture list.
     **/
    private final JMenuItem FileSaveJMenuItem = new JMenuItem();
    
    
    
    /**
     *  Menu item that allows the user to save the picture list to a new file.
     **/
    private final JMenuItem FileSaveAsJMenuItem = new JMenuItem();
    
    
    
    /**
     *  Menu item that allows the user to close the application.
     **/
    private final JMenuItem FileExitJMenuItem = new JMenuItem();
    
    
    
    /**
     *  Menu item that allows the user to search for pictures.
     **/
    private final JMenuItem EditFindJMenuItem  = new JMenuItem();
    
    
    /**
     *  Menu item that allows the user to check his directories
     **/
    private final JMenuItem EditCheckDirectoriesJMenuItem;
    
    
    /**
     *  Menu item that allows the user to change the application settings.
     **/
    private final JMenuItem EditCollectionPropertiesJMenuItem = new JMenuItem();
    
    /**
     *  Menu item that allows the user to have the collection integrity checked.
     **/
    private final JMenuItem EditCheckIntegrityJMenuItem = new JMenuItem();
    
    
    /**
     *  Menu item that allows the user to set up his cameras.
     **/
    private final JMenuItem EditCamerasJMenuItem = new JMenuItem();
    
    
    /**
     *  Menu item that allows the user to change the application settings.
     **/
    private final JMenuItem EditSettingsJMenuItem = new JMenuItem();
    
    /**
     *  Menu item that allows the user to change the categories.
     **/
    private final JMenuItem EditCategoriesJMenuItem = new JMenuItem();
    
    /**
     *  Menu item that pops up an automatic slide show
     */
    private final JMenuItem RandomSlideshowJMenuItem = new JMenuItem();
    
    /**
     *  Menu item that brings up the Help About screen
     */
    private final JMenuItem HelpAboutJMenuItem = new JMenuItem();
    
    /**
     *  Menu item to bring up the license
     */
    private final JMenuItem HelpLicenseJMenuItem = new JMenuItem();
    
    
    /**
     *  Object that must implement the functions dealing with the user
     *  request.
     */
    private ApplicationMenuInterface caller;
    
    
    
    /**
     *  Creates a menu object for use in the main frame of the application.
     *
     *  @param caller  The object that is going to get the requests.
     */
    public ApplicationJMenuBar( final ApplicationMenuInterface caller ) {
        this.caller = caller;
        
        //Build the file menu.
        FileJMenu.setMnemonic(KeyEvent.VK_F);
        add( FileJMenu );
        
        
        FileNewJMenuItem.setMnemonic( KeyEvent.VK_N );
        FileNewJMenuItem.setAccelerator( KeyStroke.getKeyStroke( 'N', java.awt.event.InputEvent.CTRL_MASK ) );
        FileNewJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestFileNew();
            }
        } );
        FileJMenu.add( FileNewJMenuItem );
        
        FileAddJMenuItem.setMnemonic( KeyEvent.VK_A );
        FileAddJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestFileAdd();
            }
        } );
        FileJMenu.add( FileAddJMenuItem );
        
        FileCameraJMenuItem.setMnemonic( KeyEvent.VK_C );
        FileCameraJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestFileAddFromCamera();
            }
        } );
        FileJMenu.add( FileCameraJMenuItem );


        FileLoadJMenuItem.setMnemonic(KeyEvent.VK_O);
        FileLoadJMenuItem.setAccelerator( KeyStroke.getKeyStroke( 'O', java.awt.event.InputEvent.CTRL_MASK ) );
        FileLoadJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestFileLoad();
            }
        } );
        FileJMenu.add( FileLoadJMenuItem );
        
        
        FileOpenRecentJMenu.setMnemonic(KeyEvent.VK_R);
        FileJMenu.add( FileOpenRecentJMenu );
        
        for ( int i = 0; i < Settings.recentFiles; i++ ) {
            recentOpenedfileJMenuItem[ i ] = new JMenuItem();
            final int index = i;  // the anonymous innter class needs a final variable
            recentOpenedfileJMenuItem[ i ].addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    caller.requestOpenRecent( index );
                }
            } );
            recentOpenedfileJMenuItem[ i ].setVisible( false );
            recentOpenedfileJMenuItem[ i ].setAccelerator( KeyStroke.getKeyStroke( "control " + Integer.toString(i).substring(1,1) ) );
            FileOpenRecentJMenu.add( recentOpenedfileJMenuItem[ i ] );
        }
        Settings.addRecentFilesChangeListener( this );
        
        
        FileSaveJMenuItem.setMnemonic(KeyEvent.VK_S);
        FileSaveJMenuItem.setAccelerator( KeyStroke.getKeyStroke( 'S', java.awt.event.InputEvent.CTRL_MASK ) );
        FileSaveJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestFileSave();
            }
        } );
        FileJMenu.add( FileSaveJMenuItem );
        
        
        FileSaveAsJMenuItem.setMnemonic( KeyEvent.VK_A );
        FileSaveAsJMenuItem.setAccelerator( KeyStroke.getKeyStroke( 'A', java.awt.event.InputEvent.ALT_MASK ) );
        FileSaveAsJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestFileSaveAs();
            }
        } );
        FileJMenu.add( FileSaveAsJMenuItem );
        
        
        
        FileExitJMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK ) );
        FileExitJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestExit();
            }
        } );
        FileJMenu.add( FileExitJMenuItem );
        
        
        //Build the Edit menu.
        EditJMenu.setMnemonic( KeyEvent.VK_E );
        add( EditJMenu );
        
        
        EditFindJMenuItem.setMnemonic( KeyEvent.VK_F );
        EditFindJMenuItem.setAccelerator( KeyStroke.getKeyStroke( 'F', java.awt.event.InputEvent.CTRL_MASK ) );
        EditFindJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestEditFind();
            }
        } );
        EditJMenu.add( EditFindJMenuItem );
        
        
        EditCheckDirectoriesJMenuItem = new JMenuItem();
        EditCheckDirectoriesJMenuItem.setMnemonic( KeyEvent.VK_D );
        EditCheckDirectoriesJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestCheckDirectories();
            }
        } );
        EditJMenu.add( EditCheckDirectoriesJMenuItem );
        
        
        EditCollectionPropertiesJMenuItem.setMnemonic( KeyEvent.VK_D );
        EditCollectionPropertiesJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestCollectionProperties();
            }
        } );
        EditJMenu.add( EditCollectionPropertiesJMenuItem );
        
        EditCheckIntegrityJMenuItem.setMnemonic( KeyEvent.VK_C );
        EditCheckIntegrityJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestCheckIntegrity();
            }
        } );
        EditJMenu.add( EditCheckIntegrityJMenuItem );
        
        
        EditCamerasJMenuItem.setMnemonic( KeyEvent.VK_D );
        EditCamerasJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestEditCameras();
            }
        } );
        EditJMenu.add( EditCamerasJMenuItem );
        
        
        EditCategoriesJMenuItem.setMnemonic( KeyEvent.VK_D );
        EditCategoriesJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                new CategoryEditorJFrame();
            }
        } );
        EditJMenu.add( EditCategoriesJMenuItem );
        
        
        
        EditSettingsJMenuItem.setMnemonic( KeyEvent.VK_S );
        EditSettingsJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.requestEditSettings();
            }
        } );
        EditJMenu.add( EditSettingsJMenuItem );
        
        
        
        
        // Build the Action menu
        emailJMenuItem.setMnemonic( KeyEvent.VK_E );
        emailJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                new EmailerJFrame();
            }
        } );
        actionJMenu.add( emailJMenuItem );
        
        
        RandomSlideshowJMenuItem.setMnemonic( KeyEvent.VK_S );
        RandomSlideshowJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                caller.performSlideshow();
            }
        } );
        actionJMenu.add( RandomSlideshowJMenuItem );
        
        actionJMenu.setMnemonic( KeyEvent.VK_A );
        add( actionJMenu );
        
        // Build the Help menu.
        HelpJMenu.setMnemonic( KeyEvent.VK_H );
        add( HelpJMenu );
        
        
        HelpAboutJMenuItem.setMnemonic(KeyEvent.VK_A);
        HelpAboutJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                new HelpAboutWindow();
            }
        } );
        HelpJMenu.add( HelpAboutJMenuItem );
        
        HelpLicenseJMenuItem.setMnemonic(KeyEvent.VK_L);
        HelpLicenseJMenuItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                new LicenseWindow();
            }
        } );
        HelpJMenu.add( HelpLicenseJMenuItem );
        
        // register an interest in the locale changes
        Settings.addLocaleChangeListener( this );
        setMenuTexts();
        recentFilesChanged();
    }
    
    /**
     *  This menu sets the texts of the menu in the language defined by the locale.
     *  The application needs to call this method when the user changes the Locale in the Settings editor.
     */
    public void setMenuTexts() {
        FileJMenu.setText( Settings.jpoResources.getString("FileMenuText") );
        FileNewJMenuItem.setText( Settings.jpoResources.getString("FileNewJMenuItem") );
        FileOpenRecentJMenu.setText( Settings.jpoResources.getString("FileOpenRecentItemText") );
        FileLoadJMenuItem.setText( Settings.jpoResources.getString("FileLoadMenuItemText") );
        FileAddJMenuItem.setText( Settings.jpoResources.getString("FileAddMenuItemText") );
        FileCameraJMenuItem.setText( Settings.jpoResources.getString("FileCameraJMenuItem") );
        FileSaveJMenuItem.setText( Settings.jpoResources.getString("FileSaveMenuItemText") );
        FileSaveAsJMenuItem.setText( Settings.jpoResources.getString("FileSaveAsMenuItemText") );
        FileExitJMenuItem.setText( Settings.jpoResources.getString("FileExitMenuItemText") );
        
        EditJMenu.setText( Settings.jpoResources.getString("EditJMenuText") );
        EditFindJMenuItem.setText( Settings.jpoResources.getString("EditFindJMenuItemText") );
        EditCheckDirectoriesJMenuItem.setText( Settings.jpoResources.getString("EditCheckDirectoriesJMenuItemText") );
        EditCollectionPropertiesJMenuItem.setText( Settings.jpoResources.getString("EditCollectionPropertiesJMenuItem") );
        EditCheckIntegrityJMenuItem.setText( Settings.jpoResources.getString("EditCheckIntegrityJMenuItem") );
        EditCamerasJMenuItem.setText( Settings.jpoResources.getString("EditCamerasJMenuItem") );
        EditCategoriesJMenuItem.setText( Settings.jpoResources.getString("EditCategoriesJMenuItem") );
        EditSettingsJMenuItem.setText( Settings.jpoResources.getString("EditSettingsMenuItemText") );
        
        
        actionJMenu.setText( Settings.jpoResources.getString("actionJMenu") );
        emailJMenuItem.setText( Settings.jpoResources.getString("emailJMenuItem") );
        RandomSlideshowJMenuItem.setText( Settings.jpoResources.getString("RandomSlideshowJMenuItem") );
        
        HelpJMenu.setText( Settings.jpoResources.getString("HelpJMenuText") );
        HelpAboutJMenuItem.setText( Settings.jpoResources.getString("HelpAboutMenuItemText") );
        HelpLicenseJMenuItem.setText( Settings.jpoResources.getString("HelpLicenseMenuItemText") );
        
    }
    
    
    /**
     *  This method handles the change of the application locale. In this class it simply makes sure the
     *  menus are reloaded.
     */
    public void localeChanged() {
        setMenuTexts();
    }
    
    
    /**
     *  Sets up the menu entries in the File|OpenRecent sub menu from the recentCollections
     *  in Settings. Can be called by the interface from the listener on the Settings object.
     */
    public void recentFilesChanged() {
        for ( int i = 0; i < Settings.recentCollections.length; i++ ) {
            if ( Settings.recentCollections[ i ] != null ) {
                recentOpenedfileJMenuItem[ i ].setText( Integer.toString( i+1 ) + ": " + Settings.recentCollections[ i ] );
                recentOpenedfileJMenuItem[ i ].setVisible( true );
            } else {
                recentOpenedfileJMenuItem[ i ].setVisible( false );
            }
        }
    }
    
    
}

