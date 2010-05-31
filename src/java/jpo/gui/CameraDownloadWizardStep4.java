package jpo.gui;

import javax.swing.event.ChangeEvent;
import jpo.dataModel.Settings;
import jpo.*;
import java.awt.Component;
import net.javaprog.ui.wizard.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;

/*
CameraDownloadWizardStep4.java: the fourth step in the download from Camera Wizard

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
 *  The fourth step in the download from camera dialog asks for the storage location on the disk.
 */
public class CameraDownloadWizardStep4 extends AbstractStep {

    /**
     * The fourth step in the download from camera dialog asks for the storage location on the disk.
     * @param dataModel The data model where the settings are to be saved
     */
    public CameraDownloadWizardStep4(CameraDownloadWizardData dataModel) {
        //pass step title and description
        super(Settings.jpoResources.getString("DownloadCameraWizardStep4Title"), Settings.jpoResources.getString("DownloadCameraWizardStep4Description"));
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
        //return component shown to the user
        JPanel stepComponent = new JPanel();
        stepComponent.setLayout(new BoxLayout(stepComponent, BoxLayout.PAGE_AXIS));
        JLabel label1 = new JLabel(Settings.jpoResources.getString("DownloadCameraWizardStep4Text1"));
        label1.setAlignmentX(Component.LEFT_ALIGNMENT);
        stepComponent.add(label1);

        final DirectoryChooser dirChooser = new DirectoryChooser(Settings.jpoResources.getString("targetDirJLabel"),
                DirectoryChooser.DIR_MUST_BE_WRITABLE);
        dirChooser.setAlignmentX(Component.LEFT_ALIGNMENT);
        stepComponent.add(dirChooser);
        dirChooser.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                dataModel.targetDir = dirChooser.getDirectory();
            }
        });
        // get default value from the dirChooser
        dataModel.targetDir = dirChooser.getDirectory();

        return stepComponent;
    }

    /**
     *  Required by the AbstractSetp but not used.
     */
    public void prepareRendering() {
    }


}
