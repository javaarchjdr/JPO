package jpo.gui;

import java.util.logging.Logger;
import jpo.dataModel.Tools;
import jpo.dataModel.Settings;
import jpo.*;
import net.javaprog.ui.wizard.*;
import javax.swing.*;


/*
CameraDownloadWizardStep1.java: the first step in the download from Camera Wizard

Copyright (C) 2007 - 2009  Richard Eigenmann.
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
 *  The first step in the download from camera dialog that pops up and informs the user that
 *  on the camera she just plugged in x new pictures were discovered. If no new pictures
 *  were found the Next button remains disabled and the user can only click the Cancel button.
 */
public class CameraDownloadWizardStep1
        extends AbstractStep {

    /**
     * Defines a logger for this class
     */
    private static Logger logger = Logger.getLogger( CameraDownloadWizardStep1.class.getName() );


    /**
     *  Constructor for the first step
     *  @param dataModel The data model for this wizard
     */
    public CameraDownloadWizardStep1( CameraDownloadWizardData dataModel ) {
        //pass step title and description
        super( Settings.jpoResources.getString( "DownloadCameraWizardStep1Title" ), Settings.jpoResources.getString( "DownloadCameraWizardStep1Description" ) );
        this.dataModel = dataModel;
    }

    /**
     *  Holds a reference to the data used by the wizard
     */
    private CameraDownloadWizardData dataModel = null;


    /**
     *  Returns the component that visualises the user interactable stuff for this step of the wizard.
     * @return
     */
    protected JComponent createComponent() {
        Tools.checkEDT();
        //return component shown to the user
        JPanel stepComponent = new JPanel();
        stepComponent.setLayout( new BoxLayout( stepComponent, BoxLayout.PAGE_AXIS ) );
        // say Camera xxx detected
        stepComponent.add( new JLabel( Settings.jpoResources.getString( "DownloadCameraWizardStep1Text1" ) + dataModel.getCamera().getDescription() + Settings.jpoResources.getString( "DownloadCameraWizardStep1Text2" ) ) );
        stepComponent.add( Box.createVerticalStrut( 8 ) );

        JLabel analysisLabel = new JLabel( Settings.jpoResources.getString( "DownloadCameraWizardStep1Text4" ) );
        stepComponent.add( analysisLabel );
        Thread t = new Thread( new SearchForPicturesThread( analysisLabel ) );
        t.start();

        return stepComponent;
    }


    /**
     *  Required by the AbstractSetp but not used.
     */
    public void prepareRendering() {
        setCanGoNext( false );
    }

    /**
     *  This inner class will search for the new pictures. It is implemented as a thread.
     *  When the new pictures have been found it changes the label to say that who many pictures were
     *  found. Only if there are more than 0 pictures then the setCanGoNext method is called which will
     *  allow the user to move forward. If there are no new pictures then the user can only cancel.
     */
    class SearchForPicturesThread
            implements Runnable {

        private JLabel progressJLabel;


        public SearchForPicturesThread( JLabel progressJLabel ) {
            this.progressJLabel = progressJLabel;
        }


        public void run() {
            logger.info( getClass().toString() + ".run: searching for the new pictures on the camera " + dataModel.getCamera().getDescription() );
            dataModel.setNewPictures( dataModel.getCamera().getNewPictures() );

            // now update the GUI on the EDT
            Runnable r = new Runnable() {

                public void run() {
                    if ( dataModel.getNewPictures().size() > 0 ) {
                        setCanGoNext( true );
                    } else {
                        setCanGoNext( false );
                    }
                    progressJLabel.setText( Integer.toString( dataModel.getNewPictures().size() ) + Settings.jpoResources.getString( "DownloadCameraWizardStep1Text3" ) );
                }
            };
            if ( SwingUtilities.isEventDispatchThread() ) {
                r.run();
            } else {
                SwingUtilities.invokeLater( r );
            }
        }
    }
}
