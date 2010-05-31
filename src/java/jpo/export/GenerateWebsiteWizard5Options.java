package jpo.export;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jpo.dataModel.Settings;
import net.javaprog.ui.wizard.AbstractStep;


/*
GenerateWebsiteWizard5Options.java: Ask about the options

Copyright (C) 2008  Richard Eigenmann.
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
 * Asks all the questions we need to know in regard to the options for the website
 *
 * @author Richard Eigenmann
 */
public class GenerateWebsiteWizard5Options extends AbstractStep {

    /**
     * The link to the values that this panel should change
     */
    private final HtmlDistillerOptions options;

    /**
     * This step asks about the options that we require to generate a website
     * @param options
     */
    public GenerateWebsiteWizard5Options( HtmlDistillerOptions options ) {
        super( Settings.jpoResources.getString( "HtmlDistOptions" ), Settings.jpoResources.getString( "HtmlDistOptions" ) );
        this.options = options;

        // load the options into the GUI components
        switch ( options.getPictureNaming() ) {
            case HtmlDistillerOptions.PICTURE_NAMING_BY_HASH_CODE:
                hashcodeRadioButton.setSelected( true );
                originalNameRadioButton.setSelected( false );
                sequentialRadioButton.setSelected( false );
                sequentialStartJSpinner.setEnabled( false );
                break;
            case HtmlDistillerOptions.PICTURE_NAMING_BY_ORIGINAL_NAME:
                hashcodeRadioButton.setSelected( false );
                originalNameRadioButton.setSelected( true );
                sequentialRadioButton.setSelected( false );
                sequentialStartJSpinner.setEnabled( false );
                break;
            case HtmlDistillerOptions.PICTURE_NAMING_BY_SEQUENTIAL_NUMBER:
                hashcodeRadioButton.setSelected( false );
                originalNameRadioButton.setSelected( false );
                sequentialRadioButton.setSelected( true );
                sequentialStartJSpinner.setEnabled( true );
                break;
        }
        ( (SpinnerNumberModel) ( sequentialStartJSpinner.getModel() ) ).setValue( options.getSequentialStartNumber() );
        generateRobotsJCheckBox.setSelected( options.isWriteRobotsTxt() );

    }
    /**
     * Radio Button to indicate that the java hash code should be used to get the image name
     */
    private JRadioButton hashcodeRadioButton = new JRadioButton( Settings.jpoResources.getString( "hashcodeRadioButton" ) );
    /**
     * Radio Button to indicate that the original name should be used to get the image name
     */
    private JRadioButton originalNameRadioButton = new JRadioButton( Settings.jpoResources.getString( "originalNameRadioButton" ) );
    /**
     * Radio Button to indicate that a sequential number should be used to get the image name
     */
    private JRadioButton sequentialRadioButton = new JRadioButton( Settings.jpoResources.getString( "sequentialRadioButton" ) );
    /**
     * Allow the user to specify a start number for the sequential numbering, 1..999999999, start 1, increment 1
     * Requested by Jay Christopherson, Nov 2008
     */
    private final JSpinner sequentialStartJSpinner = new JSpinner( new SpinnerNumberModel( 1, 1, 999999999, 1 ) );
    /**
     *  Tickbox that indicates whether to write a robots.txt
     **/
    private JCheckBox generateRobotsJCheckBox = new JCheckBox( Settings.jpoResources.getString( "generateRobotsJCheckBox" ) );

    /**
     * Creates the GUI widgets
     * @return The component to be shown
     */
    @Override
    protected JComponent createComponent() {
        JPanel wizardPanel = new JPanel();
        wizardPanel.setLayout( new BoxLayout( wizardPanel, BoxLayout.PAGE_AXIS ) );
        wizardPanel.setAlignmentX( Component.LEFT_ALIGNMENT );

        ButtonGroup bg = new ButtonGroup();
        bg.add( hashcodeRadioButton );
        wizardPanel.add( hashcodeRadioButton );

        bg.add( originalNameRadioButton );
        wizardPanel.add( originalNameRadioButton );

        final JLabel sequentialStartLabel = new JLabel( Settings.jpoResources.getString( "sequentialRadioButtonStart" ) );
        bg.add( sequentialRadioButton );
        wizardPanel.add( sequentialRadioButton );
        ChangeListener radioButtonChangeListener = new ChangeListener() {

            public void stateChanged( ChangeEvent arg0 ) {
                if ( hashcodeRadioButton.isSelected() ) {
                    options.setPictureNaming( HtmlDistillerOptions.PICTURE_NAMING_BY_HASH_CODE );
                } else if ( originalNameRadioButton.isSelected() ) {
                    options.setPictureNaming( HtmlDistillerOptions.PICTURE_NAMING_BY_ORIGINAL_NAME );
                } else {
                    options.setPictureNaming( HtmlDistillerOptions.PICTURE_NAMING_BY_SEQUENTIAL_NUMBER );
                }
                if ( sequentialStartJSpinner.isEnabled() != sequentialRadioButton.isSelected() ) {
                    sequentialStartJSpinner.setEnabled( sequentialRadioButton.isSelected() );
                    sequentialStartLabel.setEnabled( sequentialRadioButton.isSelected() );
                }
            }
        };
        hashcodeRadioButton.addChangeListener( radioButtonChangeListener );
        originalNameRadioButton.addChangeListener( radioButtonChangeListener );
        sequentialRadioButton.addChangeListener( radioButtonChangeListener );
        sequentialStartLabel.setEnabled( false );
        sequentialStartJSpinner.setEnabled( false );

        JPanel sequentialNumberJPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        sequentialNumberJPanel.setMaximumSize( GenerateWebsiteWizard.normalComponentSize );
        sequentialNumberJPanel.setAlignmentX( Container.LEFT_ALIGNMENT );
        sequentialNumberJPanel.add( sequentialStartLabel );
        sequentialStartJSpinner.addChangeListener( new ChangeListener() {

            public void stateChanged( ChangeEvent arg0 ) {
                options.setSequentialStartNumber( ( (SpinnerNumberModel) sequentialStartJSpinner.getModel() ).getNumber().intValue() );
            }
        } );
        sequentialNumberJPanel.add( sequentialStartJSpinner );
        wizardPanel.add( sequentialNumberJPanel );

        generateRobotsJCheckBox.addChangeListener( new ChangeListener() {

            public void stateChanged( ChangeEvent arg0 ) {
                options.setWriteRobotsTxt( generateRobotsJCheckBox.isSelected() );
            }
        } );
        wizardPanel.add( generateRobotsJCheckBox );


        return wizardPanel;
    }

    /**
     * Required but not used here
     */
    public void prepareRendering() {
    }
}