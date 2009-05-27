package jpo.gui;

import jpo.dataModel.Settings;
import jpo.dataModel.SortableDefaultMutableTreeNode;
import jpo.*;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/*
ConsolidateGroupJFrame.java:  creates a GUI for consolidation

Copyright (C) 2002, 2009  Richard Eigenmann.
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
 *   Creates a GUI that allows the user to specify into which directory 
 *   he or she would like images to be moved physically.
 */
public class ConsolidateGroupJFrame extends JFrame {

    /**
     *  The node from which to start the export
     */
    private SortableDefaultMutableTreeNode startNode;
    /**
     *  The extended JTextField that holds the directory that the highres images of the group is
     *  to be consolidated to
     **/
    private DirectoryChooser highresTargetDirJTextField =
            new DirectoryChooser( Settings.jpoResources.getString( "highresTargetDirJTextField" ),
            DirectoryChooser.DIR_MUST_BE_WRITABLE );
    /**
     *  The extended JTextField that holds the directory that the lowres images of the group is
     *  to be consolidated to
     **/
    private DirectoryChooser lowresTargetDirJTextField =
            new DirectoryChooser( Settings.jpoResources.getString( "lowresTargetDirJTextField" ),
            DirectoryChooser.DIR_MUST_BE_WRITABLE );
    /**
     *  Tickbox that indicates whether pictures or the current group only
     *  should be consolidated or whether the subgroups (if any) should
     *  be included.
     **/
    private JCheckBox recurseSubgroupsJCheckBox = new JCheckBox( Settings.jpoResources.getString( "RecurseSubgroupsLabel" ) );
    /**
     *  Tickbox that indicates whether pictures or the current group only
     *  should be consolidated or whether the subgroups (if any) should
     *  be included.
     **/
    private JCheckBox lowresJCheckBox = new JCheckBox( Settings.jpoResources.getString( "lowresJCheckBox" ) );

    /**
     *   Creates a GUI that allows the user to specify into which directory
     *   he or she would like images to be moved physically.
     *
     *   @param startNode  The group node that the user wants the consolidation to be done on.
     */
    public ConsolidateGroupJFrame( SortableDefaultMutableTreeNode startNode ) {
        super( Settings.jpoResources.getString( "ConsolidateGroupJFrameHeading" ) );
        this.startNode = startNode;

        setSize( 460, 300 );
        setLocationRelativeTo( Settings.anchorFrame );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        addWindowListener( new WindowAdapter() {

            @Override
            public void windowClosing( WindowEvent e ) {
                getRid();
            }
        } );

        JPanel contentJPanel = new javax.swing.JPanel();
        contentJPanel.setLayout( new GridBagLayout() );

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;


        JLabel consolidateGroupBlaBlaJLabel = new JLabel( Settings.jpoResources.getString( "ConsolidateGroupBlaBlaLabel" ) );
        consolidateGroupBlaBlaJLabel.setPreferredSize( new Dimension( 700, 80 ) );
        consolidateGroupBlaBlaJLabel.setMinimumSize( new Dimension( 600, 80 ) );
        consolidateGroupBlaBlaJLabel.setMaximumSize( new Dimension( 800, 100 ) );
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;
        constraints.insets = new Insets( 4, 4, 4, 4 );
        contentJPanel.add( consolidateGroupBlaBlaJLabel, constraints );

        JLabel targetDirJLabel = new JLabel( Settings.jpoResources.getString( "genericTargetDirText" ) );
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;
        constraints.insets = new Insets( 4, 4, 4, 4 );
        contentJPanel.add( targetDirJLabel, constraints );

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.weightx = 0.8;
        constraints.insets = new Insets( 4, 4, 4, 4 );
        contentJPanel.add( highresTargetDirJTextField, constraints );
        highresTargetDirJTextField.getDirectoryJTextField().getDocument().addDocumentListener( new DocumentListener() {

            public void insertUpdate( DocumentEvent e ) {
                setLowresLocation();
            }

            public void removeUpdate( DocumentEvent e ) {
                setLowresLocation();
            }

            public void changedUpdate( DocumentEvent e ) {
                setLowresLocation();
            }
        } );
        highresTargetDirJTextField.getDirectoryJTextField().addFocusListener( new FocusListener() {

            public void focusGained( FocusEvent e ) {
            }

            public void focusLost( FocusEvent e ) {
                setLowresLocation();
            }
        } );


        recurseSubgroupsJCheckBox.setSelected( true );
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;
        constraints.insets = new Insets( 4, 4, 4, 4 );
        contentJPanel.add( recurseSubgroupsJCheckBox, constraints );


        final JLabel targetLowresDirJLabel = new JLabel( Settings.jpoResources.getString( "genericTargetDirText" ) );

        lowresJCheckBox.setSelected( true );
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;
        constraints.insets = new Insets( 4, 4, 4, 4 );
        contentJPanel.add( lowresJCheckBox, constraints );
        lowresJCheckBox.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                setLowresLocation();
                lowresTargetDirJTextField.setVisible( lowresJCheckBox.isSelected() );
                targetLowresDirJLabel.setVisible( lowresJCheckBox.isSelected() );
            }
        } );


        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;
        constraints.insets = new Insets( 4, 4, 4, 4 );
        contentJPanel.add( targetLowresDirJLabel, constraints );

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.weightx = 0.8;
        constraints.insets = new Insets( 4, 4, 4, 4 );
        contentJPanel.add( lowresTargetDirJTextField, constraints );


        // create a JPanel for the buttons
        JPanel buttonJPanel = new JPanel();

        // add the consolidate button
        final JButton consolidateJButton = new JButton( Settings.jpoResources.getString( "ConsolidateButton" ) );
        consolidateJButton.setPreferredSize( new Dimension( 120, 25 ) );
        consolidateJButton.setMinimumSize( Settings.defaultButtonDimension );
        consolidateJButton.setMaximumSize( new Dimension( 120, 25 ) );
        consolidateJButton.setDefaultCapable( true );
        this.getRootPane().setDefaultButton( consolidateJButton );
        consolidateJButton.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                consolidateToDirectory();
                getRid();
            }
        } );

        buttonJPanel.add( consolidateJButton );

        // add the cancel button
        final JButton cancelJButton = new JButton( Settings.jpoResources.getString( "genericCancelText" ) );
        cancelJButton.setPreferredSize( Settings.defaultButtonDimension );
        cancelJButton.setMinimumSize( Settings.defaultButtonDimension );
        cancelJButton.setMaximumSize( Settings.defaultButtonDimension );
        cancelJButton.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                getRid();
            }
        } );
        buttonJPanel.add( cancelJButton );

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.weightx = 0.5;
        constraints.insets = new Insets( 4, 4, 4, 4 );
        contentJPanel.add( buttonJPanel, constraints );

        setContentPane( contentJPanel );

        pack();
        setVisible( true );
    }

    /**
     * This method sets the lowres location to the highres location with an additional /Lowres at the end.
     * It is typically invoked when the highres location changes.
     */
    private void setLowresLocation() {
        lowresTargetDirJTextField.setFile(
                new File( highresTargetDirJTextField.getFile(), "/Lowres/" ) );
    }

    /**
     *  method that get's rid of this JFrame
     */
    private void getRid() {
        setVisible( false );
        dispose();
    }

    /**
     *  method that outputs the selected group to a directory
     */
    private void consolidateToDirectory() {
        File exportDirectory = highresTargetDirJTextField.getFile();

        if ( !exportDirectory.exists() ) {
            try {
                exportDirectory.mkdirs();
            } catch ( SecurityException e ) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        Settings.anchorFrame,
                        Settings.jpoResources.getString( "ConsolidateCreateDirFailure" ) + exportDirectory + "\n" + e.getMessage(),
                        Settings.jpoResources.getString( "genericError" ),
                        JOptionPane.ERROR_MESSAGE );
                return;
            }
        }

        File lowresDirectory = lowresTargetDirJTextField.getFile();

        if ( ( lowresJCheckBox.isSelected() ) && ( !lowresDirectory.exists() ) ) {
            try {
                lowresDirectory.mkdirs();
            } catch ( SecurityException e ) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        Settings.anchorFrame,
                        Settings.jpoResources.getString( "ConsolidateCreateDirFailure" ) + exportDirectory + "\n" + e.getMessage(),
                        Settings.jpoResources.getString( "genericError" ),
                        JOptionPane.ERROR_MESSAGE );
                return;
            }
        }


        new ConsolidateGroupThread(
                exportDirectory,
                startNode,
                recurseSubgroupsJCheckBox.isSelected(),
                lowresJCheckBox.isSelected(),
                lowresDirectory );
        Settings.memorizeCopyLocation( exportDirectory.toString() );

    }
} 
