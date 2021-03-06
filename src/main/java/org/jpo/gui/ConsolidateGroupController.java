package org.jpo.gui;

import org.jpo.eventBus.ConsolidateGroupDialogRequest;
import org.jpo.eventBus.ConsolidateGroupRequest;
import org.jpo.eventBus.CopyLocationsChangedEvent;
import org.jpo.eventBus.JpoEventBus;
import org.jpo.dataModel.GroupInfo;
import org.jpo.dataModel.Settings;
import org.jpo.gui.swing.ConsolidateGroupJFrame;

import javax.swing.*;
import java.io.File;
import java.util.logging.Logger;

/*
 Copyright (C) 2017 -2019  Richard Eigenmann.
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
 * Controller to consolidate pictures of a node into a directory.
 */
public class ConsolidateGroupController implements ConsolidateGroupActionCallback {

    /**
     * The request to consolidate is memorised here
     */
    private final ConsolidateGroupDialogRequest request;

    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(ConsolidateGroupController.class.getName());

    /**
     * Creates a GUI that allows the user to customise the parameters of the
     * desired picture consolidation.
     *
     * @param request The details of the request
     */
    public ConsolidateGroupController(ConsolidateGroupDialogRequest request) {
        this.request = request;

        Object userObject = request.getNode().getUserObject();
        if (!(userObject instanceof GroupInfo)) {
            LOGGER.info(String.format("Node %s is not a group", request.getNode().toString()));
            JOptionPane.showMessageDialog(
                    Settings.anchorFrame,
                    Settings.jpoResources.getString("ConsolidateFailure"),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        ConsolidateGroupJFrame consolidateGroupJFrame = new ConsolidateGroupJFrame(this );

        if (request.getTargetDir() != null) {
            consolidateGroupJFrame.setTargetDir(request.getTargetDir());
        }
    }

    /**
     * method that outputs the selected group to a directory
     * @param targetDirectory target directory
     */
    @Override
    public void consolidateGroupCallback(File targetDirectory, boolean recurseSubgroups) {
        if (!targetDirectory.exists()) {
            try {
                if (!targetDirectory.mkdirs()) {
                    JOptionPane.showMessageDialog(Settings.anchorFrame,
                            String.format(Settings.jpoResources.getString("ConsolidateCreateDirFailure"), targetDirectory),
                            Settings.jpoResources.getString("genericError"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (SecurityException e) {
                JOptionPane.showMessageDialog(Settings.anchorFrame,
                        String.format(Settings.jpoResources.getString("ConsolidateCreateDirFailure"), targetDirectory),
                        Settings.jpoResources.getString("genericError"),
                        JOptionPane.ERROR_MESSAGE);
                LOGGER.severe(String.format("SecurityException when creating directory %s. Reason: %s", targetDirectory, e.getMessage()));
                return;
            }
        }

        if (!targetDirectory.canWrite()) {
            JOptionPane.showMessageDialog(Settings.anchorFrame,
                    String.format(Settings.jpoResources.getString("ConsolidateCantWrite"), targetDirectory),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Settings.memorizeCopyLocation(targetDirectory.toString());

        JpoEventBus.getInstance().post(new ConsolidateGroupRequest(request.getNode(), targetDirectory, recurseSubgroups));

        JpoEventBus.getInstance().post(new CopyLocationsChangedEvent());

    }

}
