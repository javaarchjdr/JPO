package org.jpo.gui;

import org.jpo.eventBus.CopyLocationsChangedEvent;
import org.jpo.eventBus.JpoEventBus;
import org.jpo.dataModel.GroupInfo;
import org.jpo.dataModel.Settings;
import org.jpo.dataModel.SortableDefaultMutableTreeNode;
import org.jpo.dataModel.Tools;
import org.jpo.gui.swing.CategoryJScrollPane;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;


/*
 Copyright (C) 2002, 2019  Richard Eigenmann.
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
 * This controller class brings up a Filechooser which allows the user to select
 * pictures and directories. If the user clicks OK the pictures and
 * subdirectories are added to the previously indicated group node.
 */
public class PictureFileChooser
        implements PropertyChangeListener {

    /**
     * We use the java filechooser and customise it.
     */
    private final JFileChooser jFileChooser = new JFileChooser();

    /**
     * Construct the PictureFileChooser and show it.
     *
     * @param startNode The node to which the selected pictures are to be added.
     * It must be a GroupInfo Node.
     */
    public PictureFileChooser( final SortableDefaultMutableTreeNode startNode ) {
        Tools.checkEDT();

        if ( !( startNode.getUserObject() instanceof GroupInfo ) ) {
            JOptionPane.showMessageDialog(
                    Settings.anchorFrame,
                    Settings.jpoResources.getString( "notGroupInfo" ),
                    Settings.jpoResources.getString( "genericError" ),
                    JOptionPane.ERROR_MESSAGE );
            return;
        }

        initComponents();

        if ( jFileChooser.showOpenDialog( Settings.anchorFrame ) == JFileChooser.APPROVE_OPTION ) {
            final File[] chosenFiles = jFileChooser.getSelectedFiles();
            Settings.memorizeCopyLocation( jFileChooser.getCurrentDirectory().getPath() );
            JpoEventBus.getInstance().post( new CopyLocationsChangedEvent() );

            Settings.showThumbOnFileChooser = showThumbnailJCheckBox.isSelected();

            PictureAdder pictureAdder = new PictureAdder( startNode, chosenFiles, newOnlyJCheckBox.isSelected(), recurseJCheckBox.isSelected(), retainDirectoriesJCheckBox.isSelected(), categoryJScrollPane.getSelectedCategories() );
            pictureAdder.execute();
        }
    }

    /**
     * This method is invoked from the FileChooser and creates the thumbnail.
     *
     * See <a href="http://java.sun.com/developer/JDCTechTips/index.html">Core
     * Java Technologies Tech Tips</a> for the March 16 2004 issue on Preview
     * panels in the JFileChooser.
     *
     * @param changeEvent The event from the FileChooser that changed
     */
    @Override
    public void propertyChange( PropertyChangeEvent changeEvent ) {
        String changeName = changeEvent.getPropertyName();
        if ( changeName.equals( JFileChooser.SELECTED_FILE_CHANGED_PROPERTY ) ) {
            File file = (File) changeEvent.getNewValue();
            if ( ( file != null ) && ( showThumbnailJCheckBox.isSelected() ) ) {
                ImageIcon icon = new ImageIcon( file.getPath() );
                if ( icon.getIconWidth() > OPTIONS_PANEL_DIMENSION.getWidth() ) {
                    icon = new ImageIcon( icon.getImage().getScaledInstance( (int) OPTIONS_PANEL_DIMENSION.getWidth(), -1,
                            Image.SCALE_DEFAULT ) );
                    if ( icon.getIconHeight() > OPTIONS_PANEL_DIMENSION.getHeight() ) {
                        icon = new ImageIcon( icon.getImage().getScaledInstance( -1, (int) OPTIONS_PANEL_DIMENSION.getHeight(),
                                Image.SCALE_DEFAULT ) );
                    }
                }
                final ImageIcon setIcon = icon;
                Tools.checkEDT();
                thumbnailJLabel.setIcon( setIcon );
            }
        }
    }

    /**
     * Checkbox that allows the user to specify that pictures already in the
     * collection should be ignored.
     */
    private final JCheckBox showThumbnailJCheckBox = new JCheckBox( Settings.jpoResources.getString( "showThumbnailJCheckBox" ) );

    /**
     * preferred size of accessory panel
     */
    private static final Dimension OPTIONS_PANEL_DIMENSION = new Dimension( 200, 180 );

    /**
     * Checkbox that allows the user to specify whether pictures in the
     * subdirectories should be added or not.
     */
    private final JCheckBox recurseJCheckBox = new JCheckBox( Settings.jpoResources.getString( "recurseJCheckBox" ) );

    /**
     *
     * Checkbox that allows the user to specify that pictures already in the
     * collection should be ignored.
     */
    private final JCheckBox newOnlyJCheckBox = new JCheckBox( Settings.jpoResources.getString( "newOnlyJCheckBox" ) );

    /**
     * Checkbox that allows the user to specify whether directory structures
     * should be retained
     */
    private final JCheckBox retainDirectoriesJCheckBox = new JCheckBox( Settings.jpoResources.getString( "retainDirectoriesJCheckBox" ) );

    /**
     * This component shows the thumbnail. It is a JLabel as we can thus use the
     * ImageIcon to display the picture.
     */
    private final JLabel thumbnailJLabel = new JLabel();

    /**
     * Allow the user to choose categories
     */
    private final CategoryJScrollPane categoryJScrollPane = new CategoryJScrollPane();

    /**
     * Creates the GUI components for the PictureFileChooser controller
     */
    private void initComponents() {
        recurseJCheckBox.setSelected( true );
        newOnlyJCheckBox.setSelected( true );
        showThumbnailJCheckBox.setSelected( Settings.showThumbOnFileChooser );
        retainDirectoriesJCheckBox.setSelected( true );

        JPanel optionsJPanel = new JPanel();
        optionsJPanel.setLayout( new BoxLayout( optionsJPanel, BoxLayout.Y_AXIS ) );
        optionsJPanel.add( recurseJCheckBox, BorderLayout.WEST );
        optionsJPanel.add( newOnlyJCheckBox, BorderLayout.WEST );
        optionsJPanel.add( showThumbnailJCheckBox, BorderLayout.WEST );
        optionsJPanel.add( retainDirectoriesJCheckBox, BorderLayout.WEST );
        optionsJPanel.setPreferredSize( OPTIONS_PANEL_DIMENSION );
        thumbnailJLabel.setPreferredSize( OPTIONS_PANEL_DIMENSION );

        categoryJScrollPane.loadCategories( Settings.getPictureCollection().getCategoryIterator() );

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 0 ) );
        tabbedPane.add( Settings.jpoResources.getString( "pictureAdderOptionsTab" ), optionsJPanel );
        tabbedPane.add( Settings.jpoResources.getString( "pictureAdderThumbnailTab" ), thumbnailJLabel );
        tabbedPane.add( Settings.jpoResources.getString( "pictureAdderCategoryTab" ), categoryJScrollPane );

        jFileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
        jFileChooser.setMultiSelectionEnabled( true );
        jFileChooser.setApproveButtonText( Settings.jpoResources.getString( "fileChooserAddButtonLabel" ) );
        jFileChooser.setDialogTitle( Settings.jpoResources.getString( "PictureAdderDialogTitle" ) );
        jFileChooser.setAccessory( tabbedPane );
        jFileChooser.setCurrentDirectory( Settings.getMostRecentCopyLocation() );
        jFileChooser.addPropertyChangeListener( this );
    }
}
