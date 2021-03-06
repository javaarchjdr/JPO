package org.jpo.gui;

import com.google.common.eventbus.Subscribe;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jpo.eventBus.*;
import org.jpo.cache.JpoCache;
import org.jpo.cache.ThumbnailCreationFactory;
import org.jpo.cache.QUEUE_PRIORITY;
import org.jpo.dataModel.*;
import org.jpo.dataModel.Settings.FieldCodes;
import org.jpo.export.GenerateWebsiteWizard;
import org.jpo.export.PicasaUploadRequest;
import org.jpo.export.PicasaUploaderWizard;
import org.jpo.export.WebsiteGenerator;
import org.jpo.gui.swing.*;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.jpo.gui.swing.ResizableJFrame.WindowSize.WINDOW_LEFT;
import static org.jpo.gui.swing.ResizableJFrame.WindowSize.WINDOW_RIGHT;

/*
 Copyright (C) 2014-2019  Richard Eigenmann.
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
 * This class handles all the Application Events. It uses the Google Guava
 * EventBus
 *
 * @author Richard Eigenmann
 */
@SuppressWarnings("UnstableApiUsage")
public class ApplicationEventHandler {

    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(ApplicationEventHandler.class.getName());

    /**
     * This class handles most of the events flying around the JPO application
     */
    public ApplicationEventHandler() {
        JpoEventBus.getInstance().register(this);
    }

    /**
     * Handles the application startup by posting an {@link OpenMainWindowRequest},
     * starting the {@link StartCameraWatchDaemonRequest}, starting the
     * {@link StartThumbnailCreationFactoryRequest}. If an autoLoad is defined in the Settings it
     * will load that or start a new collection with {@link StartNewCollectionRequest}.
     *
     * @param request the startup request
     * @see OpenMainWindowRequest
     * @see ApplicationEventHandler#handleOpenMainWindowRequest(OpenMainWindowRequest)
     * @see StartCameraWatchDaemonRequest
     * @see ApplicationEventHandler#handleStartCameraWatchDaemonRequest(StartCameraWatchDaemonRequest)
     * @see StartThumbnailCreationFactoryRequest
     * @see ApplicationEventHandler#handleStartThumbnailCreationFactoryRequest(StartThumbnailCreationFactoryRequest)
     * @see FileLoadRequest
     * @see ApplicationEventHandler#handleFileLoadRequest(FileLoadRequest)
     * @see StartNewCollectionRequest
     * @see ApplicationEventHandler#handleStartNewCollectionRequest(StartNewCollectionRequest)
     */
    @Subscribe
    public void handleApplicationStartupRequest(ApplicationStartupRequest request) {
        LOGGER.info("------------------------------------------------------------\n      Starting JPO");

        Settings.loadSettings();

        // JpoEventBus.getInstance().register( new DebugEventListener() );
        JpoEventBus.getInstance().post(new OpenMainWindowRequest());
        JpoEventBus.getInstance().post(new StartCameraWatchDaemonRequest());

        for (int i = 1; i <= Settings.numberOfThumbnailCreationThreads; i++) {
            JpoEventBus.getInstance().post(new StartThumbnailCreationFactoryRequest());
        }

        if ((Settings.autoLoad != null) && (Settings.autoLoad.length() > 0)) {
            File xmlFile = new File(Settings.autoLoad);
            JpoEventBus.getInstance().post(new FileLoadRequest(xmlFile));
        } else {
            JpoEventBus.getInstance().post(new StartNewCollectionRequest());
        }
    }


    /**
     * Start a ThumbnailCreationFactory
     *
     * @param request the request
     */
    @Subscribe
    public void handleStartThumbnailCreationFactoryRequest(StartThumbnailCreationFactoryRequest request) {
        new ThumbnailCreationFactory(Settings.ThumbnailCreationThreadPollingTime);
    }


    /**
     * Opens the MainWindow on the EDT thread by constructing a {@link MainWindow}. We then fire a
     * {@link LoadDockablesPositionsRequest}. We connect the picture collection with the {@link MainAppModelListener}
     *
     * @param request The request
     * @see MainWindow
     * @see LoadDockablesPositionsRequest
     * @see MainAppModelListener
     */
    @Subscribe
    public void handleOpenMainWindowRequest(OpenMainWindowRequest request) {
        try {
            // Activate OpenGL performance improvements
            //System.setProperty( "sun.java2d.opengl", "true" );
            SwingUtilities.invokeAndWait(
                    () -> {
                        new MainWindow();
                        JpoEventBus.getInstance().post(new LoadDockablesPositionsRequest());
                        Settings.getPictureCollection().getTreeModel().addTreeModelListener(new MainAppModelListener());
                    }
            );
        } catch (InterruptedException | InvocationTargetException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The user wants to find duplicates
     *
     * @param request The request
     */
    @Subscribe
    public void handleFindDuplicatesRequest(FindDuplicatesRequest request) {
        DuplicatesQuery duplicatesQuery = new DuplicatesQuery();
        Settings.getPictureCollection().addQueryToTreeModel(duplicatesQuery);
        JpoEventBus.getInstance().post(new ShowQueryRequest(duplicatesQuery));
    }

    /**
     * Creates an IntegrityChecker that does it's magic on the collection.
     *
     * @param request The request
     */
    @Subscribe
    public void handleCheckIntegrityRequest(CheckIntegrityRequest request) {
        new IntegrityCheckerJFrame(Settings.getPictureCollection().getRootNode());
    }

    /**
     * Creates a {@link SettingsDialog} where the user can edit Application wide
     * settings.
     *
     * @param request The request
     */
    @Subscribe
    public void handleEditSettingsRequest(EditSettingsRequest request) {
        new SettingsDialog(true);
    }

    /**
     * Opens up the Camera Editor GUI. See {@link CamerasEditor}
     *
     * @param request the request object
     */
    @Subscribe
    public void handleEditCamerasRequest(EditCamerasRequest request) {
        new CamerasEditor();
    }

    /**
     * Opens up the Camera Editor GUI. See {@link CamerasEditor}
     *
     * @param request the request object
     */
    @Subscribe
    public void handleSendEmailRequest(SendEmailRequest request) {
        new EmailerGui();
    }

    /**
     * Shuts down JPO no questions asked. Wrap it as a next request with a
     * UnsavedUpdatesDialogRequest
     *
     * @param request The request
     */
    @Subscribe
    public void handleCloseApplicationRequest(CloseApplicationRequest request) {
        JpoEventBus.getInstance().post(new SaveDockablesPositionsRequest());
        if (Settings.unsavedSettingChanges) {
            Settings.writeSettings();
        }

        JpoCache.getInstance().shutdown();

        LOGGER.info("Exiting JPO\n------------------------------------------------------------");

        System.exit(0);
    }

    /**
     * Creates a {@link ReconcileJFrame} which lets the user specify a directory
     * whose pictures are then compared against the current collection. Allows
     * the user to reconcile pictures in a directory with those in his
     * collection.
     *
     * @param request The request
     */
    @Subscribe
    public void handleCheckDirectoriesRequest(CheckDirectoriesRequest request) {
        new ReconcileJFrame(Settings.getPictureCollection().getRootNode());
    }

    /**
     * Starts a double panel slide show
     *
     * @param request The request
     */
    @Subscribe
    public void handleStartDoublePanelSlideshowRequest(StartDoublePanelSlideshowRequest request) {
        Tools.checkEDT();
        SortableDefaultMutableTreeNode rootNode = request.getNode();
        PictureViewer p1 = new PictureViewer();
        p1.switchWindowMode(WINDOW_LEFT);
        PictureViewer p2 = new PictureViewer();
        p2.switchWindowMode(WINDOW_RIGHT);
        RandomNavigator rb1 = new RandomNavigator(rootNode.getChildPictureNodes(true), String.format("Randomised pictures from %s", rootNode.toString()));
        RandomNavigator rb2 = new RandomNavigator(rootNode.getChildPictureNodes(true), String.format("Randomised pictures from %s", rootNode.toString()));
        p1.showNode(rb1, 0);
        p1.startAdvanceTimer(10);
        p2.showNode(rb2, 0);
        p2.startAdvanceTimer(10);
    }

    /**
     * When we see a ShowPictureRequest this method will open a {@link PictureViewer}
     * and will tell it to show the {@link FlatGroupNavigator} based on the pictures
     * parent node starting at the current position
     *
     * @param request the {@link ShowPictureRequest}
     */
    @Subscribe
    public void handleShowPictureRequest(ShowPictureRequest request) {
        SortableDefaultMutableTreeNode node = request.getNode();
        Object userObject = node.getUserObject();

        final NodeNavigatorInterface navigator;
        int index = 0;

        if (userObject instanceof PictureInfo) {
            navigator = new FlatGroupNavigator(node.getParent());
            for (int i = 0; i < navigator.getNumberOfNodes(); i++) {
                if (navigator.getNode(i).equals(node)) {
                    index = i;
                    i = navigator.getNumberOfNodes();
                }
            }
        } else if (userObject instanceof GroupInfo && node.hasChildPictureNodes()) {
            navigator = new FlatGroupNavigator(node);
        } else {
            return; // should only be receiving PictureInfo or GroupInfo with child pictures
        }
        final int myIndex = index;
        SwingUtilities.invokeLater(() -> {
            PictureViewer pictureViewer = new PictureViewer();
            pictureViewer.showNode(navigator, myIndex);
        });

    }

    /**
     * When the app sees a ShowPictureInfoEditorRequest it will open the
     * PictureInfoEditor for the supplied node
     *
     * @param request The request
     */
    @Subscribe
    public void handleShowPictureInfoEditorRequest(ShowPictureInfoEditorRequest request) {
        new PictureInfoEditor(request.getNode());
    }

    /**
     * When the app sees a ShowGroupInfoEditorRequest it will open the
     * PictureInfoEditor for the supplied node
     *
     * @param request The request
     */
    @Subscribe
    public void handleShowGroupInfoEditorRequest(ShowGroupInfoEditorRequest request) {
        new GroupInfoEditor(request.getNode());
    }

    /**
     * When the app sees a OpenSearchDialog it will open the QueryJFrame
     *
     * @param request The request
     */
    @Subscribe
    public void handleOpenSearchDialogRequest(OpenSearchDialogRequest request) {
        if (!(request.getStartNode().getUserObject() instanceof GroupInfo)) {
            LOGGER.log(Level.INFO, "Method can only be invoked on GroupInfo nodes! Ignoring request. You are on node: {0}", this.toString());
            JOptionPane.showMessageDialog(
                    Settings.anchorFrame,
                    "Method can only be invoked on GroupInfo nodes! Ignoring request. You are on node: " + this.toString(),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        FindJPanel findPanel = new FindJPanel();
        Object[] options = {"OK", "Cancel"};
        int result = JOptionPane.showOptionDialog(
                Settings.anchorFrame,
                findPanel,
                Settings.jpoResources.getString("searchDialogTitle"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null
        );
        if (result == JOptionPane.OK_OPTION) {

            TextQuery textQuery = new TextQuery(findPanel.getSearchArgument());
            textQuery.setStartNode(request.getStartNode());

            if (findPanel.getSearchByDate()) {
                textQuery.setLowerDateRange(Tools.parseDate(findPanel.getLowerDate()));
                textQuery.setUpperDateRange(Tools.parseDate(findPanel.getHigherDate()));

                if ((textQuery.getLowerDateRange() != null) && (textQuery.getUpperDateRange() != null) && (textQuery.getLowerDateRange().compareTo(textQuery.getUpperDateRange()) > 0)) {
                    JOptionPane.showMessageDialog(
                            Settings.anchorFrame,
                            Settings.jpoResources.getString("dateRangeError"),
                            Settings.jpoResources.getString("genericError"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Settings.getPictureCollection().addQueryToTreeModel(textQuery);
            JpoEventBus.getInstance().post(new ShowQueryRequest(textQuery));
        }
    }

    /**
     * When the app sees a ShowCategoryUsageEditorRequest it will open the
     * CategoryUsageEditor for the supplied node
     *
     * @param request The request
     */
    @Subscribe
    public void handleShowCategoryUsageEditorRequest(ShowCategoryUsageEditorRequest request) {
        new CategoryUsageJFrame(request);
    }

    /**
     * When the app sees a ShowAutoAdvanceDialog it needs to show the Auto Advance dialog
     *
     * @param request The request
     */
    @Subscribe
    public void handleShowAutoAdvanceDialog(ShowAutoAdvanceDialogRequest request) {
        new AutoAdvanceDialog(request);
    }

    /**
     * @param request the request
     */
    @Subscribe
    public static void handleMoveToNewLocationRequest(MoveToNewLocationRequest request) {
        JFileChooser jFileChooser = new JFileChooser();

        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser.setDialogTitle(Settings.jpoResources.getString("MoveImageDialogTitle"));
        jFileChooser.setCurrentDirectory(Settings.getMostRecentCopyLocation());

        int returnVal = jFileChooser.showDialog(Settings.anchorFrame, Settings.jpoResources.getString("MoveImageDialogButton"));
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        JpoEventBus.getInstance().post(new MoveToDirRequest(request.getNodes(), jFileChooser.getSelectedFile()));
    }

    /**
     * Bring up a Dialog where the user can input a new name for a file and
     * rename it. If the target file already exists and would overwrite the existing file
     * A new name is suggested that the user can accept or abort the rename.
     *
     * @param request the request
     */
    @Subscribe
    public static void handleRenamePictureRequest(@NonNull RenamePictureRequest request) {
        SortableDefaultMutableTreeNode node = request.getNode();
        PictureInfo pi = (PictureInfo) node.getUserObject();

        File imageFile = pi.getImageFile();
        if (imageFile == null) {
            return;
        }

        Object object = Settings.jpoResources.getString("FileRenameLabel1")
                + imageFile.toString()
                + Settings.jpoResources.getString("FileRenameLabel2");
        String selectedValue = JOptionPane.showInputDialog(Settings.anchorFrame,
                object,
                imageFile.toString());
        if (selectedValue != null) {
            File newName = new File(selectedValue);

            if (newName.exists()) {
                File alternativeNewName = Tools.inventPicFilename(newName.getParentFile(), newName.getName());
                int alternativeAnswer = JOptionPane.showConfirmDialog(Settings.anchorFrame,
                        String.format(Settings.jpoResources.getString("FileRenameTargetExistsText"), newName.toString(), alternativeNewName.toString()),
                        Settings.jpoResources.getString("FileRenameTargetExistsTitle"),
                        JOptionPane.OK_CANCEL_OPTION);
                if (alternativeAnswer == JOptionPane.OK_OPTION) {
                    newName = alternativeNewName;
                } else {
                    LOGGER.log(Level.INFO, "File exists and new name was not accepted by user");
                    return;
                }
            }
            JpoEventBus.getInstance().post(new RenameFileRequest(request.getNode(), newName.getName()));
        }
    }


    /**
     * Bring up a Dialog where the user can input a new name for a file and
     * rename it. If the target file already exists and would overwrite the existing file
     * A new name is suggested that the user can accept or abort the rename.
     *
     * @param request the request
     */
    @Subscribe
    public static void handleRenameFileRequest(@NonNull RenameFileRequest request) {
        PictureInfo pi = (PictureInfo) request.getNode().getUserObject();
        LOGGER.info(String.format("Renaming node %s (%s) to new filename: %s", request.getNode().toString(), pi.getImageFile().getPath(), request.getNewFileName()));
        File imageFile = pi.getImageFile();
        String newName = request.getNewFileName();
        File newFile = new File(imageFile.getParentFile(), newName);
        if (imageFile.renameTo(newFile)) {
            LOGGER.log(Level.INFO, "Successfully renamed: {0} to: {1}", new Object[]{imageFile.toString(), newName});
            pi.setImageLocation(newFile);
            request.getNode().getPictureCollection().setUnsavedUpdates();
        } else {
            LOGGER.log(Level.INFO, "Rename failed from : {0} to: {1}", new Object[]{imageFile.toString(), newName});
        }

    }


    /**
     * When the app sees a ChooseAndAddCollectionRequest it will open the a
     * chooser dialog and will add the collection to the supplied node
     *
     * @param request the request
     */
    @Subscribe
    public void handleChooseAndAddCollectionRequest(ChooseAndAddCollectionRequest request) {
        File fileToLoad = chooseXmlFile();
        if (fileToLoad != null) {
            JpoEventBus.getInstance().post(new AddCollectionToGroupRequest(request.getNode(), fileToLoad));
        }

    }

    /**
     * When the app sees a ShowGroupAsTableRequest it will open the the group in
     * a table.
     *
     * @param request the request
     */
    @Subscribe
    public void handleShowGroupAsTableRequest(ShowGroupAsTableRequest request) {
        TableJFrame tableJFrame = new TableJFrame(request.getNode());
        tableJFrame.pack();
        tableJFrame.setVisible(true);
    }

    /**
     * Brings up a dialog where the user can select the collection to be loaded.
     * Then fires a {@link FileLoadRequest}.
     * <p>
     * Enclose this request in an {@link UnsavedUpdatesDialogRequest} if you
     * care about unsaved changes as this request will not check for unsaved
     * changes
     *
     * @param request the request
     */
    @Subscribe
    public void handleFileLoadDialogRequest(FileLoadDialogRequest request) {
        final File fileToLoad = chooseXmlFile();
        JpoEventBus.getInstance().post(new FileLoadRequest(fileToLoad));
    }

    /**
     * Method that chooses an xml file or returns null
     *
     * @return the xml file or null
     */
    private static File chooseXmlFile() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.setApproveButtonText(Settings.jpoResources.getString("fileOpenButtonText"));
        jFileChooser.setDialogTitle(Settings.jpoResources.getString("fileOpenHeading"));
        jFileChooser.setFileFilter(new XmlFilter());
        jFileChooser.setCurrentDirectory(Settings.getMostRecentCopyLocation());

        int returnVal = jFileChooser.showOpenDialog(Settings.anchorFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return jFileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    /**
     * Loads the file by calling
     * {@link PictureCollection#fileLoad}. If there is a problem
     * creates a new collection.
     * <p>
     * Remember to wrap this request in an UnsavedUpdatesDialogRequest if you
     * care about unsaved changes as this request will not check for unsaved
     * changes
     *
     * @param request the request
     */
    @Subscribe
    public void handleFileLoadRequest(FileLoadRequest request) {
        final File fileToLoad = request.getFileToLoad();
        new Thread("FileLoadRequest") {

            @Override
            public void run() {
                try {
                    Settings.getPictureCollection().fileLoad(fileToLoad);
                    Settings.pushRecentCollection(fileToLoad.toString());
                    JpoEventBus.getInstance().post(new RecentCollectionsChangedEvent());
                    JpoEventBus.getInstance().post(new ShowGroupRequest(Settings.getPictureCollection().getRootNode()));
                } catch (final FileNotFoundException ex) {

                    SwingUtilities.invokeLater(() -> {
                                LOGGER.log(Level.INFO, "FileNotFoundException: {0}", ex.getMessage());
                                JOptionPane.showMessageDialog(Settings.anchorFrame,
                                        ex.getMessage(),
                                        Settings.jpoResources.getString("genericError"),
                                        JOptionPane.ERROR_MESSAGE);
                                JpoEventBus.getInstance().post(new StartNewCollectionRequest());
                            }
                    );
                }
            }
        }.start();
    }

    /**
     * Clears the collection and starts a new one. Remember to wrap this request
     * in an UnsavedUpdatesDialogRequest if you care about unsaved changes as
     * this request will not check for unsaved changes
     *
     * @param event the event
     */
    @Subscribe
    public void handleStartNewCollectionRequest(StartNewCollectionRequest event) {
        SwingUtilities.invokeLater(
                () -> {
                    Settings.getPictureCollection().clearCollection();
                    JpoEventBus.getInstance().post(new ShowGroupRequest(Settings.getPictureCollection().getRootNode()));
                }
        );
    }

    /**
     * Calls the {@link org.jpo.dataModel.PictureCollection#fileSave} method that
     * saves the current collection under it's present name and if it was never
     * saved before brings up a popup window.
     *
     * @param request The request
     */
    @Subscribe
    public void handleFileSaveRequest(FileSaveRequest request) {
        if (Settings.getPictureCollection().getXmlFile() == null) {
            FileSaveAsRequest fileSaveAsRequest = new FileSaveAsRequest();
            fileSaveAsRequest.setOnSuccessNextRequest(request.getOnSuccessNextRequest());
            JpoEventBus.getInstance().post(fileSaveAsRequest);
        } else {
            LOGGER.info(String.format("Saving under the name: %s", Settings.getPictureCollection().getXmlFile()));
            Settings.getPictureCollection().fileSave();
            JpoEventBus.getInstance().post(new AfterFileSaveRequest(Settings.getPictureCollection().getXmlFile().toString()));
            if (request.getOnSuccessNextRequest() != null) {
                JpoEventBus.getInstance().post(request.getOnSuccessNextRequest());
            }
        }
    }

    /**
     * method that saves the entire index in XML format. It prompts for the
     * filename first.
     *
     * @param request the request
     */
    @Subscribe
    public void handleFileSaveAsRequest(FileSaveAsRequest request) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        jFileChooser.setDialogTitle(Settings.jpoResources.getString("fileSaveAsTitle"));
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setFileFilter(new XmlFilter());
        if (Settings.getPictureCollection().getXmlFile() != null) {
            jFileChooser.setCurrentDirectory(Settings.getPictureCollection().getXmlFile());
        } else {
            jFileChooser.setCurrentDirectory(Settings.getMostRecentCopyLocation());
        }

        int returnVal = jFileChooser.showSaveDialog(Settings.anchorFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File chosenFile = jFileChooser.getSelectedFile();
            chosenFile = Tools.correctFilenameExtension("xml", chosenFile);
            if (chosenFile.exists()) {
                int answer = JOptionPane.showConfirmDialog(Settings.anchorFrame,
                        Settings.jpoResources.getString("confirmSaveAs"),
                        Settings.jpoResources.getString("genericWarning"),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (answer == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }

            Settings.getPictureCollection().setXmlFile(chosenFile);
            Settings.getPictureCollection().fileSave();

            Settings.memorizeCopyLocation(chosenFile.getParent());
            JpoEventBus.getInstance().post(new CopyLocationsChangedEvent());
            Settings.pushRecentCollection(chosenFile.toString());
            JpoEventBus.getInstance().post(new RecentCollectionsChangedEvent());
            JpoEventBus.getInstance().post(new AfterFileSaveRequest(Settings.getPictureCollection().getXmlFile().toString()));
            if (request.getOnSuccessNextRequest() != null) {
                JpoEventBus.getInstance().post(request.getOnSuccessNextRequest());
            }
        }
    }

    /**
     * Brings up the dialog after a file save and allows the saved collection to
     * be set as the default start up collection.
     *
     * @param request the request
     */
    @Subscribe
    public void handleAfterFileSaveRequest(AfterFileSaveRequest request) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(Settings.jpoResources.getString("collectionSaveBody") + Settings.getPictureCollection().getXmlFile().toString()));
        JCheckBox setAutoload = new JCheckBox(Settings.jpoResources.getString("setAutoload"));
        if (Settings.autoLoad != null && ((new File(Settings.autoLoad)).compareTo(Settings.getPictureCollection().getXmlFile()) == 0)) {
            setAutoload.setSelected(true);
        }
        panel.add(setAutoload);
        JOptionPane.showMessageDialog(Settings.anchorFrame,
                panel,
                Settings.jpoResources.getString("collectionSaveTitle"),
                JOptionPane.INFORMATION_MESSAGE);

        if (setAutoload.isSelected()) {
            Settings.autoLoad = request.getAutoLoadCollectionFile();
            Settings.writeSettings();
        }
    }

    /**
     * Handles the request to add a collection supplied as a file to the
     * supplied group node
     *
     * @param request the request
     */
    @Subscribe
    public void handleAddCollectionToGroupRequest(AddCollectionToGroupRequest request) {
        LOGGER.info("Starting");
        Tools.checkEDT();
        SortableDefaultMutableTreeNode popupNode = request.getNode();
        File fileToLoad = request.getCollectionFile();

        SortableDefaultMutableTreeNode newNode = popupNode.addGroupNode("New Group");
        try {
            PictureCollection.fileLoad(fileToLoad, newNode);
        } catch (FileNotFoundException x) {
            LOGGER.log(Level.INFO, "{0}.fileToLoad: FileNotFoundException: {1}", new Object[]{this.getClass().toString(), x.getMessage()});
            JOptionPane.showMessageDialog(Settings.anchorFrame,
                    "File not found:\n" + fileToLoad.getPath(),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
        }
        newNode.getPictureCollection().setUnsavedUpdates(true);
        JpoEventBus.getInstance().post(new ShowGroupRequest(newNode));
    }

    /**
     * when the App sees this request it will sort the group by the criteria
     *
     * @param request The node on which the request was made
     */
    @Subscribe
    public void handleSortGroupRequest(SortGroupRequest request) {
        SortableDefaultMutableTreeNode popupNode = request.getNode();
        FieldCodes sortCriteria = request.getSortCriteria();
        popupNode.sortChildren(sortCriteria);
        List<SortableDefaultMutableTreeNode> nodes = new ArrayList<>();
        nodes.add(popupNode);
        JpoEventBus.getInstance().post(new RefreshThumbnailRequest(nodes, QUEUE_PRIORITY.MEDIUM_PRIORITY));
    }

    /**
     * when the App sees an AddEmptyGroup request it will sort the group by the
     * criteria
     *
     * @param request The node on which the request was made
     */
    @Subscribe
    public void handleAddEmptyGroupRequest(AddEmptyGroupRequest request) {
        SortableDefaultMutableTreeNode node = request.getNode();
        if (!(node.getUserObject() instanceof GroupInfo)) {
            LOGGER.warning(String.format("node %s is of type %s instead of GroupInfo. Proceeding anyway.", node.getUserObject().toString(), node.getUserObject().getClass().toString()));
        }
        SortableDefaultMutableTreeNode newNode = node.addGroupNode("New Group");
        Settings.memorizeGroupOfDropLocation(newNode);
        JpoEventBus.getInstance().post(new RecentDropNodesChangedEvent());
        JpoEventBus.getInstance().post(new ShowGroupRequest(newNode));
    }

    /**
     * The App will respond to this request by opening the Export to HTML wizard
     *
     * @param request the request
     */
    @Subscribe
    public void handleExportGroupToHtmlRequest(ExportGroupToHtmlRequest request) {
        SortableDefaultMutableTreeNode nodeToExport = request.getNode();
        new GenerateWebsiteWizard(nodeToExport);
    }

    /**
     * The Creates the Website
     *
     * @param request the request
     */
    @Subscribe
    public void handleGenerateWebsiteRequest(GenerateWebsiteRequest request) {
        WebsiteGenerator h = new WebsiteGenerator(request);
        SwingUtilities.invokeLater(h);
    }

    /**
     * The App will respond to this request by creating a FlatFileDistiller
     *
     * @param request the request
     */
    @Subscribe
    public void handleExportGroupFlatFileRequest(ExportGroupToFlatFileRequest request) {
        new FlatFileDistiller(request);
    }

    /**
     * Opens a dialog asking for the name of the new collection
     *
     * @param request the request
     */
    @Subscribe
    public void handleExportGroupToNewCollectionRequest(ExportGroupToNewCollectionRequest request) {
        new CollectionDistillerJFrame(request);
    }

    /**
     * Fulfill the export to new collection request
     *
     * @param request the request
     */
    @Subscribe
    public void handleExportGroupToCollectionRequest(ExportGroupToCollectionRequest request) {
        new JpoWriter(request);
    }

    /**
     * When the app receives the ExportGroupToPicasaRequest the dialog will be
     * opened to export the pictures to Picasa
     *
     * @param request the request
     */
    @Subscribe
    public void handleExportGroupToPicasaRequest(ExportGroupToPicasaRequest request) {
        SortableDefaultMutableTreeNode groupNode = request.getNode();
        PicasaUploadRequest myRequest = new PicasaUploadRequest();
        myRequest.setNode(groupNode);
        new PicasaUploaderWizard(myRequest);
    }

    /**
     * Adds the pictures in the supplied group to the email selection
     *
     * @param request the request
     */
    @Subscribe
    public void handleAddGroupToEmailSelectionRequest(final AddGroupToEmailSelectionRequest request) {
        final SortableDefaultMutableTreeNode groupNode = request.getNode();
        SortableDefaultMutableTreeNode n;
        for (final Enumeration e = groupNode.breadthFirstEnumeration(); e.hasMoreElements(); ) {
            n = (SortableDefaultMutableTreeNode) e.nextElement();
            if (n.getUserObject() instanceof PictureInfo) {
                Settings.getPictureCollection().addToMailSelection(n);
            }
        }
    }

    /**
     * Adds the picture nodes in the supplied request to the email selection
     *
     * @param request the request
     */
    @Subscribe
    public void handleAddPictureModesToEmailSelectionRequest(final AddPictureNodesToEmailSelectionRequest request) {
        final List<SortableDefaultMutableTreeNode> nodesList = request.getNodesList();
        for (final SortableDefaultMutableTreeNode n : nodesList) {
            if (n.getUserObject() instanceof PictureInfo) {
                Settings.getPictureCollection().addToMailSelection(n);
            }
        }
    }

    /**
     * Removes the picture nodes in the supplied request from the email
     * selection
     *
     * @param request the request
     */
    @Subscribe
    public void handleRemovePictureModesFromEmailSelectionRequest(final RemovePictureNodesFromEmailSelectionRequest request) {
        final List<SortableDefaultMutableTreeNode> nodesList = request.getNodesList();
        for (final SortableDefaultMutableTreeNode n : nodesList) {
            if (n.getUserObject() instanceof PictureInfo) {
                Settings.getPictureCollection().removeFromMailSelection(n);
            }
        }
    }

    /**
     * Clears the the email selection
     *
     * @param request the request
     */
    @Subscribe
    public void handleClearEmailSelectionRequest(ClearEmailSelectionRequest request) {
        Settings.getPictureCollection().clearMailSelection();
    }

    /**
     * Opens the consolidate group dialog
     *
     * @param request The request
     */
    @Subscribe
    public void handleConsolidateGroupDialogRequest(ConsolidateGroupDialogRequest request) {
        new ConsolidateGroupController(request);
    }

    /**
     * Consolidates the files
     *
     * @param request The request
     */
    @Subscribe
    public void handleConsolidateGroupRequest(ConsolidateGroupRequest request) {
        new ConsolidateGroupWorker(
                request.getTargetDir(),
                request.getNode(),
                request.getRecurseSubgroups(),
                new ProgressGui(NodeStatistics.countPictures(request.getNode(), request.getRecurseSubgroups()),
                        Settings.jpoResources.getString("ConsolidateProgBarTitle"),
                        ""));
    }


    /**
     * Brings up a JFileChooser to select the target location and then copies
     * the images to the target location
     *
     * @param request The request
     */
    @Subscribe
    public void handleCopyToNewLocationRequest(CopyToNewLocationRequest request) {
        JFileChooser jFileChooser = new JFileChooser();

        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser.setApproveButtonText(Settings.jpoResources.getString("CopyImageDialogButton"));
        jFileChooser.setDialogTitle(Settings.jpoResources.getString("CopyImageDialogTitle"));
        jFileChooser.setCurrentDirectory(Settings.getMostRecentCopyLocation());

        int returnVal = jFileChooser.showSaveDialog(Settings.anchorFrame);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        JpoEventBus.getInstance().post(new CopyToDirRequest(request.getNodes(), jFileChooser.getSelectedFile()));
    }

    /**
     * Copies the pictures of the supplied nodes to the target directory
     *
     * @param request The request
     */
    @Subscribe
    public static void handleCopyToDirRequest(CopyToDirRequest request) {
        if (!request.getTargetLocation().canWrite()) {
            JOptionPane.showMessageDialog(Settings.anchorFrame,
                    Settings.jpoResources.getString("htmlDistCanWriteError"),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int picsCopied = 0;
        for (SortableDefaultMutableTreeNode node : request.getNodes()) {
            if (node.getUserObject() instanceof PictureInfo) {
                if (node.validateAndCopyPicture(request.getTargetLocation())) {
                    picsCopied++;
                }
            } else {
                LOGGER.info(String.format("Skipping non PictureInfo node %s", node.toString()));
            }
        }
        JOptionPane.showMessageDialog(Settings.anchorFrame,
                String.format(Settings.jpoResources.getString("copyToNewLocationSuccess"), picsCopied, request.getNodes().size()),
                Settings.jpoResources.getString("genericInfo"),
                JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Moves the pictures of the supplied nodes to the target directory
     *
     * @param request The request
     */
    @Subscribe
    public static void handleMoveToDirRequest(MoveToDirRequest request) {
        if (!request.getTargetLocation().isDirectory()) {
            JOptionPane.showMessageDialog(Settings.anchorFrame,
                    Settings.jpoResources.getString("htmlDistIsDirError"),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!request.getTargetLocation().canWrite()) {
            JOptionPane.showMessageDialog(Settings.anchorFrame,
                    Settings.jpoResources.getString("htmlDistCanWriteError"),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int picsMoved = 0;
        for (SortableDefaultMutableTreeNode node : request.getNodes()) {
            Object userObject = node.getUserObject();
            if (userObject instanceof PictureInfo) {
                if (ConsolidateGroupWorker.movePicture((PictureInfo) userObject, request.getTargetLocation())) {
                    picsMoved++;
                }
            } else {
                LOGGER.info(String.format("Skipping non PictureInfo node %s", node.toString()));
            }
        }
        JOptionPane.showMessageDialog(Settings.anchorFrame,
                String.format(Settings.jpoResources.getString("moveToNewLocationSuccess"), picsMoved, request.getNodes().size()),
                Settings.jpoResources.getString("genericInfo"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Brings up a JFileChooser to select the target zip file and then copies
     * the images there
     * <p>
     * TODO: Refactor to use a list
     *
     * @param request The request
     */
    @Subscribe
    public void handleCopyToNewZipfileRequest(CopyToNewZipfileRequest request) {
        final JFileChooser jFileChooser = new JFileChooser();

        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // TODO: internationalise this Settings.jpoResources.getString( "CopyImageDialogButton" )
        jFileChooser.setApproveButtonText("Select");
        jFileChooser.setDialogTitle("Pick the zipfile to which the pictures should be added");
        jFileChooser.setCurrentDirectory(Settings.getMostRecentCopyLocation());

        int returnVal = jFileChooser.showDialog(Settings.anchorFrame, "Select");
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final File chosenFile = jFileChooser.getSelectedFile();
        Settings.memorizeZipFile(chosenFile.getPath());
        //copyToZipfile( request.getNodes(), chosenFile );

        JpoEventBus.getInstance().post(new CopyToZipfileRequest(request.getNodes(), chosenFile));
    }

    /**
     * Copies the pictures of the supplied nodes to the target zipfile, creating
     * it if need be. This method does append to the zipfile by writing to a
     * temporary file and then copying the old zip file over to this one as the
     * API doesn't support directly appending to a zip file.
     *
     * @param request The request
     */
    @Subscribe
    public void handleCopyToZipfileRequest(CopyToZipfileRequest request) {

        File tempfile = new File(request.getTargetZipfile().getAbsolutePath() + ".org.jpo.temp");
        int picsCopied = 0;
        PictureInfo pictureInfo;
        File sourceFile;
        try (ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(tempfile)) {
            zipArchiveOutputStream.setLevel(9);
            for (SortableDefaultMutableTreeNode node : request.getNodes()) {
                if (node.getUserObject() instanceof PictureInfo) {
                    pictureInfo = (PictureInfo) node.getUserObject();
                    sourceFile = pictureInfo.getImageFile();
                    LOGGER.info(String.format("Processing file %s", sourceFile.toString()));

                    ZipArchiveEntry entry = new ZipArchiveEntry(sourceFile, sourceFile.getName());
                    zipArchiveOutputStream.putArchiveEntry(entry);

                    try (FileInputStream fis = new FileInputStream(sourceFile)) {
                        streamcopy(fis, zipArchiveOutputStream);
                    }
                    zipArchiveOutputStream.closeArchiveEntry();

                    picsCopied++;

                } else {
                    LOGGER.info(String.format("Skipping non PictureInfo node %s", node.toString()));
                }
            }

            if (request.getTargetZipfile().exists()) {
                // copy the old entries over
                try (
                        final ZipFile oldzip = new ZipFile(request.getTargetZipfile())) {
                    final Enumeration entries = oldzip.getEntries();
                    while (entries.hasMoreElements()) {
                        final ZipArchiveEntry e = (ZipArchiveEntry) entries.nextElement();
                        LOGGER.info(String.format("streamcopy: %s", e.getName()));
                        zipArchiveOutputStream.putArchiveEntry(e);
                        if (!e.isDirectory()) {
                            streamcopy(oldzip.getInputStream(e), zipArchiveOutputStream);
                        }
                        zipArchiveOutputStream.closeArchiveEntry();
                    }
                }
            }
            zipArchiveOutputStream.finish();
        } catch (final IOException ex) {
            LOGGER.severe(ex.getMessage());
            boolean ok = tempfile.delete();
            if (!ok) {
                LOGGER.severe("could not delete tempfile: " + tempfile.toString());
            }
        }

        if (request.getTargetZipfile().exists()) {
            LOGGER.info(String.format("Deleting old file %s", request.getTargetZipfile().getAbsolutePath()));
            boolean ok = request.getTargetZipfile().delete();
            if (!ok) {
                LOGGER.severe(String.format("Failed to delete file %s", request.getTargetZipfile().getAbsolutePath()));
            }
        }
        LOGGER.info(String.format("Renaming temp file %s to %s", tempfile.getAbsolutePath(), request.getTargetZipfile().getAbsolutePath()));
        boolean ok = tempfile.renameTo(request.getTargetZipfile());
        if (!ok) {
            LOGGER.severe(String.format("Failed to rename temp file %s to %s", tempfile.getAbsolutePath(), request.getTargetZipfile().getAbsolutePath()));
        }

        JOptionPane.showMessageDialog(Settings.anchorFrame,
                String.format("Copied %d files of %d to zipfile %s", picsCopied, request.getNodes().size(), request.getTargetZipfile().toString()),
                Settings.jpoResources.getString("genericInfo"),
                JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Copies an input stream to an output stream
     *
     * @param input  the input stream
     * @param output the output stream
     * @throws IOException The exception it can throw
     */
    private static void streamcopy(InputStream input, OutputStream output) throws IOException {
        // 4MB buffer
        byte[] buffer = new byte[4096 * 1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    /**
     * Copies the supplied picture nodes to the system clipboard
     *
     * @param request The request
     */
    @Subscribe
    public void handleCopyImageToClipboardRequest(CopyImageToClipboardRequest request) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        JpoTransferable transferable = new JpoTransferable(request.getNodes());
        clipboard.setContents(transferable, (Clipboard clipboard1, Transferable contents) -> LOGGER.info("Lost Ownership of clipboard - not an issue"));
    }

    /**
     * Copies the path(s) of the supplied picture node(s) to the system clipboard
     *
     * @param request The request
     */
    @Subscribe
    public void handleCopyPathToClipboardRequest(CopyPathToClipboardRequest request) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringBuilder sb = new StringBuilder();
        for (SortableDefaultMutableTreeNode s : request.getNodes()) {
            Object o = s.getUserObject();
            if (o instanceof PictureInfo) {
                PictureInfo pi = (PictureInfo) o;
                sb.append(pi.getImageFile().getAbsoluteFile().toString());
                sb.append(System.lineSeparator());
            }
        }
        StringSelection stringSelection = new StringSelection(sb.toString());
        clipboard.setContents(stringSelection, (Clipboard clipboard1, Transferable contents) -> LOGGER.info("Lost Ownership of clipboard - not an issue"));
    }


    /**
     * Moves the node to the first position in the group
     *
     * @param request The node on which the request was made
     */
    @Subscribe
    public void handleMoveNodeToTopRequest(MoveNodeToTopRequest request) {
        SortableDefaultMutableTreeNode popupNode = request.getNode();
        popupNode.moveNodeToTop();
        List<SortableDefaultMutableTreeNode> nodes = new ArrayList<>();
        nodes.add(popupNode.getParent());
        JpoEventBus.getInstance().post(new RefreshThumbnailRequest(nodes, QUEUE_PRIORITY.MEDIUM_PRIORITY));
    }

    /**
     * Moves the node up one position
     *
     * @param request the request
     */
    @Subscribe
    public void handleMoveNodeUpRequest(MoveNodeUpRequest request) {
        SortableDefaultMutableTreeNode popupNode = request.getNode();
        popupNode.moveNodeUp();
        List<SortableDefaultMutableTreeNode> nodes = new ArrayList<>();
        nodes.add(popupNode.getParent());
        JpoEventBus.getInstance().post(new RefreshThumbnailRequest(nodes, QUEUE_PRIORITY.MEDIUM_PRIORITY));
    }

    /**
     * Moves the node down one position
     *
     * @param request the request
     */
    @Subscribe
    public void handleMoveNodeDownRequest(MoveNodeDownRequest request) {
        SortableDefaultMutableTreeNode popupNode = request.getNode();
        popupNode.moveNodeDown();
        List<SortableDefaultMutableTreeNode> nodes = new ArrayList<>();
        nodes.add(popupNode.getParent());
        JpoEventBus.getInstance().post(new RefreshThumbnailRequest(nodes, QUEUE_PRIORITY.MEDIUM_PRIORITY));
    }

    /**
     * Moves the node to the last position
     *
     * @param request the request
     */
    @Subscribe
    public void handleMoveNodeToBottomRequest(MoveNodeToBottomRequest request) {
        SortableDefaultMutableTreeNode popupNode = request.getNode();
        popupNode.moveNodeToBottom();
        List<SortableDefaultMutableTreeNode> nodes = new ArrayList<>();
        nodes.add(popupNode.getParent());
        JpoEventBus.getInstance().post(new RefreshThumbnailRequest(nodes, QUEUE_PRIORITY.MEDIUM_PRIORITY));
    }

    /**
     * Indents the nodes
     *
     * @param request the request
     */
    @Subscribe
    public void handleMoveIndentRequest(MoveIndentRequest request) {
        List<SortableDefaultMutableTreeNode> nodes = request.getNodes();
        for (SortableDefaultMutableTreeNode node : nodes) {
            node.indentNode();
        }
        // ToDo: Figure out what to refresh. New Group node for instance
        //JpoEventBus.getInstance().post( new RefreshThumbnailRequest( (SortableDefaultMutableTreeNode) popupNode.getParent(), ThumbnailQueueRequest.MEDIUM_PRIORITY ) );
    }

    /**
     * Outdents the nodes
     *
     * @param request the request
     */
    @Subscribe
    public void handleMoveOutdentRequest(MoveOutdentRequest request) {
        List<SortableDefaultMutableTreeNode> nodes = request.getNodes();
        for (SortableDefaultMutableTreeNode node : nodes) {
            node.outdentNode();
        }
        // ToDo: Figure out what to refresh. New Group node for instance
        // ToDo: Could also delete the left over group node if it is empty
        //JpoEventBus.getInstance().post( new RefreshThumbnailRequest( (SortableDefaultMutableTreeNode) popupNode.getParent(), ThumbnailQueueRequest.MEDIUM_PRIORITY ) );
    }

    /**
     * Removes the supplied node from it's parent
     *
     * @param request The request
     */
    @Subscribe
    public void handleRemoveNodeRequest(RemoveNodeRequest request) {
        List<SortableDefaultMutableTreeNode> nodesToRemove = request.getNodes();
        SortableDefaultMutableTreeNode firstParentNode = nodesToRemove.get(0).getParent();
        for (SortableDefaultMutableTreeNode deleteNode : nodesToRemove) {
            deleteNode.deleteNode();
        }
        JpoEventBus.getInstance().post(new ShowGroupRequest(firstParentNode));
    }

    /**
     * Deletes the file and the node
     *
     * @param request the request the request
     */
    @Subscribe
    public void handleDeleteNodeFileRequest(DeleteNodeFileRequest request) {
        SortableDefaultMutableTreeNode node = request.getNode();
        Object userObj = node.getUserObject();
        if (!(userObj instanceof PictureInfo)) {
            return;
        }

        PictureInfo pi = (PictureInfo) userObj;
        File highresFile = pi.getImageFile();
        if (highresFile == null) {
            return;
        }

        int option = JOptionPane.showConfirmDialog(
                Settings.anchorFrame,
                Settings.jpoResources.getString("FileDeleteLabel") + highresFile.toString() + "\n" + Settings.jpoResources.getString("areYouSure"),
                Settings.jpoResources.getString("FileDeleteTitle"),
                JOptionPane.OK_CANCEL_OPTION);

        if (option == 0) {
            boolean ok = false;

            if (highresFile.exists()) {
                ok = highresFile.delete();
                if (!ok) {
                    LOGGER.log(Level.INFO, "File deleted failed on: {0}", highresFile.toString());
                }
            }

            node.deleteNode();

            if (!ok) {
                JOptionPane.showMessageDialog(Settings.anchorFrame,
                        Settings.jpoResources.getString("fileDeleteError") + highresFile.toString(),
                        Settings.jpoResources.getString("genericError"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Deletes the file and the node
     *
     * @param request the request
     */
    @Subscribe
    public void handleDeleteMultiNodeFileRequest(DeleteMultiNodeFileRequest request) {
        List<SortableDefaultMutableTreeNode> nodes = request.getNodes();
        JTextArea textArea = new JTextArea();
        textArea.setText("");
        for (SortableDefaultMutableTreeNode selectedNode : nodes) {
            if (selectedNode.getUserObject() instanceof PictureInfo) {
                textArea.append(((PictureInfo) selectedNode.getUserObject()).getImageLocation() + "\n");
            }
        }
        textArea.append(Settings.jpoResources.getString("areYouSure"));

        int option = JOptionPane.showConfirmDialog(
                Settings.anchorFrame, //very annoying if the main window is used as it forces itself into focus.
                textArea,
                Settings.jpoResources.getString("FileDeleteLabel"),
                JOptionPane.OK_CANCEL_OPTION);

        if (option == 0) {
            for (SortableDefaultMutableTreeNode selectedNode : nodes) {
                PictureInfo pi;
                if (selectedNode.getUserObject() instanceof PictureInfo) {
                    pi = (PictureInfo) selectedNode.getUserObject();
                    boolean ok = false;

                    File highresFile = pi.getImageFile();
                    if (highresFile.exists()) {
                        ok = highresFile.delete();
                        if (!ok) {
                            LOGGER.log(Level.INFO, "File deleted failed on: {0}", highresFile.toString());
                        }
                    }

                    selectedNode.deleteNode();

                    if (!ok) {
                        JOptionPane.showMessageDialog(Settings.anchorFrame,
                                Settings.jpoResources.getString("fileDeleteError") + highresFile.toString(),
                                Settings.jpoResources.getString("genericError"),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            Settings.getPictureCollection().clearSelection();
        }
    }

    /**
     * Handles the request to open a recent collection
     * <p>
     * Remember to wrap this request in an UnsavedUpdatesDialogRequest if you
     * care about unsaved changes as this request will not check for unsaved
     * changes
     *
     * @param request the request
     */
    @Subscribe
    public void handleOpenRecentCollectionRequest(OpenRecentCollectionRequest request) {
        final int i = request.getIndex();

        new Thread("OpenRecentCollectionRequest") {

            @Override
            public void run() {
                final File fileToLoad = new File(Settings.recentCollections[i]);
                try {
                    Settings.getPictureCollection().fileLoad(fileToLoad);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ApplicationEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                    LOGGER.log(Level.INFO, "FileNotFoundException: {0}", ex.getMessage());
                    JOptionPane.showMessageDialog(Settings.anchorFrame,
                            ex.getMessage(),
                            Settings.jpoResources.getString("genericError"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JpoEventBus.getInstance().post(new ShowGroupRequest(Settings.getPictureCollection().getRootNode()));

                Settings.pushRecentCollection(fileToLoad.toString());
                JpoEventBus.getInstance().post(new RecentCollectionsChangedEvent());
            }
        }.start();
    }

    /**
     * Brings up a chooser to pick files and add them to the group.
     *
     * @param request the Request
     */
    @Subscribe
    public void handleChooseAndAddPicturesToGroupRequest(ChooseAndAddPicturesToGroupRequest request) {
        new PictureFileChooser(request.getNode());
    }

    /**
     * Brings up a chooser to pick a flat file and add them to the group.
     *
     * @param request the Request
     */
    @Subscribe
    public void handleChooseAndAddFlatfileRequest(ChooseAndAddFlatfileRequest request) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
        jFileChooser.setApproveButtonText(Settings.jpoResources.getString("fileOpenButtonText"));
        jFileChooser.setDialogTitle(Settings.jpoResources.getString("addFlatFileTitle"));
        jFileChooser.setCurrentDirectory(Settings.getMostRecentCopyLocation());

        int returnVal = jFileChooser.showOpenDialog(Settings.anchorFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File chosenFile = jFileChooser.getSelectedFile();
            JpoEventBus.getInstance().post(new AddFlatFileRequest(request.getNode(), chosenFile));

        }
    }

    /**
     * Handles the request to add a flat file to a node
     *
     * @param request the Request
     */
    @Subscribe
    public void handleAddFlatFileRequest(AddFlatFileRequest request) {
        new FlatFileReader(request);
    }

    /**
     * Moves the movingNode into the last child position of the target node
     *
     * @param request the request
     */
    @Subscribe
    public void handleMoveNodeToNodeRequest(MoveNodeToNodeRequest request) {
        List<SortableDefaultMutableTreeNode> movingNodes = request.getMovingNodes();
        SortableDefaultMutableTreeNode targetGroup = request.getTargetNode();
        for (SortableDefaultMutableTreeNode movingNode : movingNodes) {
            movingNode.moveToLastChild(targetGroup);
        }
    }

    /**
     * Opens the License window
     *
     * @param request the request
     */
    @Subscribe
    public void handleOpenLicenceFrameRequest(OpenLicenceFrameRequest request) {
        new LicenseWindow();
    }

    /**
     * Opens the Help About window
     *
     * @param request the request
     */
    @Subscribe
    public void handleHelpAboutFrameRequest(OpenHelpAboutFrameRequest request) {
        new HelpAboutWindow();
    }

    /**
     * Opens the Privacy window
     *
     * @param request the request
     */
    @Subscribe
    public void handleOpenPrivacyFrameRequest(OpenPrivacyFrameRequest request) {
        new PrivacyJFrame();
    }

    /**
     * Starts the Camera Watch Daemon
     *
     * @param request the request
     */
    @Subscribe
    public void handleStartCameraWatchDaemonRequest(StartCameraWatchDaemonRequest request) {
        new CameraWatchDaemon();
    }

    /**
     * Brings the unsaved updates dialog if there are unsaved updates and then
     * fires the next request. Logic is: if unsavedChanges then show dialog
     * submit next request
     * <p>
     * The dialog has choices: 0 : discard unsaved changes and go to next
     * request 1 : fire save request then send next request 2 : fire save-as
     * request then send next request 3 : cancel - don't proceed with next
     * request
     *
     * @param request the request
     */
    @Subscribe
    public void handleUnsavedUpdatesDialogRequest(UnsavedUpdatesDialogRequest request) {
        Tools.checkEDT();
        if (Settings.getPictureCollection().getUnsavedUpdates()) {
            Object[] options = {
                    Settings.jpoResources.getString("discardChanges"),
                    Settings.jpoResources.getString("genericSaveButtonLabel"),
                    Settings.jpoResources.getString("FileSaveAsMenuItemText"),
                    Settings.jpoResources.getString("genericCancelText")};
            int option = JOptionPane.showOptionDialog(
                    Settings.anchorFrame,
                    Settings.jpoResources.getString("unsavedChanges"),
                    Settings.jpoResources.getString("genericWarning"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (option) {
                case 0:
                    JpoEventBus.getInstance().post(request.getNextRequest());
                    break;
                case 1:
                    FileSaveRequest fileSaveRequest = new FileSaveRequest();
                    fileSaveRequest.setOnSuccessNextRequest(request.getNextRequest());
                    JpoEventBus.getInstance().post(fileSaveRequest);
                    break;
                case 2:
                    FileSaveAsRequest fileSaveAsRequest = new FileSaveAsRequest();
                    fileSaveAsRequest.setOnSuccessNextRequest(request.getNextRequest());
                    JpoEventBus.getInstance().post(fileSaveAsRequest);
                    break;
            }
        } else {
            JpoEventBus.getInstance().post(request.getNextRequest());
        }

    }

    /**
     * Handles the RefreshThumbnailRequest
     *
     * @param request The request
     */
    @Subscribe
    public void handleRefreshThumbnailRequest(RefreshThumbnailRequest request) {
        for (SortableDefaultMutableTreeNode node : request.getNodes()) {
            if (node.isRoot()) {
                LOGGER.fine("Ignoring the request for a thumbnail refresh on the Root Node as the query for it's parent's children will fail");
                return;
            }
            LOGGER.fine(String.format("refreshing the thumbnail on the node %s%nAbout to create the thumbnail", this.toString()));
            ThumbnailController t = new ThumbnailController(new Thumbnail(), Settings.thumbnailSize);
            t.setNode(new SingleNodeNavigator(node), 0);
        }
    }

    /**
     * Handles the RotatePictureRequest request
     *
     * @param request The request
     */
    @Subscribe
    public void handleRotatePictureRequestRequest(RotatePictureRequest request) {
        PictureInfo pictureInfo = (PictureInfo) request.getNode().getUserObject();
        pictureInfo.rotate(request.getAngle());
        LOGGER.info("Changed the rotation");
        List<SortableDefaultMutableTreeNode> nodes = new ArrayList<>();
        nodes.add(request.getNode());
        nodes.add(request.getNode().getParent());
        JpoEventBus.getInstance().post(new RefreshThumbnailRequest(nodes, request.getPriority()));
    }

    /**
     * Handles the SetPictureRotationRequest request by setting the rotation and
     * calling the refresh thumbnails methods
     *
     * @param request The request
     */
    @Subscribe
    public void handleSetPictureRotationRequest(SetPictureRotationRequest request) {
        PictureInfo pictureInfo = (PictureInfo) request.getNode().getUserObject();
        pictureInfo.setRotation(request.getAngle());
        List<SortableDefaultMutableTreeNode> nodes = new ArrayList<>();
        nodes.add(request.getNode());
        nodes.add(request.getNode().getParent());
        JpoEventBus.getInstance().post(new RefreshThumbnailRequest(nodes, request.getPriority()));
    }

    /**
     * Handles the OpenCategoryEditorRequest request
     *
     * @param request The request
     */
    @Subscribe
    public void handleOpenCategoryEditorRequest(OpenCategoryEditorRequest request) {
        new CategoryEditorJFrame();
    }

    /**
     * Handles the ShowGroupPopUpMenuRequest request
     *
     * @param request The request
     */
    @Subscribe
    public void handleShowGroupPopUpMenuRequest(ShowGroupPopUpMenuRequest request) {
        Runnable r = () -> {
            GroupPopupMenu groupPopupMenu = new GroupPopupMenu(request.getNode());
            groupPopupMenu.show(request.getInvoker(), request.getX(), request.getY());
        };
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    /**
     * Handles the ShowPicturePopUpMenuRequest request
     *
     * @param request The request
     */
    @Subscribe
    public void handleShowPicturePopUpMenuRequest(ShowPicturePopUpMenuRequest request) {
        Runnable r = () -> {
            PicturePopupMenu picturePopupMenu = new PicturePopupMenu(request.getNodes(), request.getIndex());
            picturePopupMenu.show(request.getInvoker(), request.getX(), request.getY());
        };
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }

    }

    /**
     * Handles the RunUserFunctionRequest request
     *
     * @param request The request
     */
    @Subscribe
    public void handleRunUserFunctionRequest(RunUserFunctionRequest request) {
        try {
            runUserFunction(request.getUserFunctionIndex(), request.getPictureInfo());
        } catch (ClassCastException | NullPointerException x) {
            LOGGER.severe(x.getMessage());
        }

    }


    /**
     * This method fires up a user function if it can. User functions are only
     * valid on PictureInfo nodes.
     *
     * @param userFunction The user function to be executed in the array
     *                     Settings.userFunctionCmd
     * @param myObject     The PictureInfo upon which the user function should be
     *                     executed.
     */
    private static void runUserFunction(int userFunction, PictureInfo myObject) {
        if ((userFunction < 0) || (userFunction >= Settings.maxUserFunctions)) {
            LOGGER.info("Error: called with an out of bounds index");
            return;
        }
        String command = Settings.userFunctionCmd[userFunction];
        if ((command == null) || (command.length() == 0)) {
            LOGGER.log(Level.INFO, "Command {0} is not properly defined", Integer.toString(userFunction));
            return;
        }

        String filename = (myObject).getImageFile().toString();
        command = command.replaceAll("%f", filename);

        String escapedFilename = filename.replaceAll("\\s", "\\\\\\\\ ");
        command = command.replaceAll("%e", escapedFilename);

        try {
            URL pictureURL = myObject.getImageFile().toURI().toURL();
            command = command.replaceAll("%u", pictureURL.toString());
        } catch (MalformedURLException x) {
            LOGGER.log(Level.SEVERE, "Could not substitute %u with the URL: {0}", x.getMessage());
            return;
        }


        LOGGER.log(Level.INFO, "Command to run is: {0}", command);
        try {
            // Had big issues here because the simple exec (String) calls a StringTokenizer
            // which messes up the filename parameters
            int blank = command.indexOf(' ');
            if (blank > -1) {
                String[] cmdarray = new String[2];
                cmdarray[0] = command.substring(0, blank);
                cmdarray[1] = command.substring(blank + 1);
                Runtime.getRuntime().exec(cmdarray);
            } else {
                String[] cmdarray = new String[1];
                cmdarray[0] = command;
                Runtime.getRuntime().exec(cmdarray);
            }
        } catch (IOException x) {
            LOGGER.log(Level.INFO, "Runtime.exec collapsed with and IOException: {0}", x.getMessage());
        }
    }


    /**
     * Handles the RemoveOldLowresThumbnailsRequest request
     *
     * @param request The request with the lowres urls to remove
     */
    @Subscribe
    public void handleRemoveOldLowresThumbnailsRequest(final RemoveOldLowresThumbnailsRequest request) {
        SwingUtilities.invokeLater(
                () -> new ClearThumbnailsJFrame(request.getLowresUrls())
        );
    }

    /**
     * Handles the OpenFileExplorerRequest request
     *
     * @param request The request
     */
    @Subscribe
    public void handleOpenFileExplorerRequest(final OpenFileExplorerRequest request) {
        try {
            Desktop.getDesktop().open(request.getDirectory());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * Inner class that monitors the collection for changes and figures out
     * whether the root node changed and asks the application to change the
     * title of the Window accordingly
     */
    private static class MainAppModelListener
            implements TreeModelListener {

        @Override
        public void treeNodesChanged(TreeModelEvent e) {
            TreePath tp = e.getTreePath();
            LOGGER.fine(String.format("The main app model listener trapped a tree node change event on the tree path: %s", tp.toString()));
            if (tp.getPathCount() == 1) { //if the root node sent the event
                LOGGER.fine("Since this is the root node we will update the ApplicationTitle");

                updateApplicationTitle();
            }
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
            // ignore
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
            // ignore, the root can't be removed ... Really?
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            TreePath tp = e.getTreePath();
            if (tp.getPathCount() == 1) { //if the root node sent the event
                updateApplicationTitle();
            }
        }

        /**
         * Sets the application title to the default title based on the
         * Resourcebundle string ApplicationTitle and the file name of the
         * loaded xml file if any.
         */
        private void updateApplicationTitle() {
            final File xmlFile = Settings.getPictureCollection().getXmlFile();
            if (xmlFile != null) {
                JpoEventBus.getInstance().post(new UpdateApplicationTitleRequest(Settings.jpoResources.getString("ApplicationTitle") + ":  " + xmlFile.toString()));
            } else {
                JpoEventBus.getInstance().post(new UpdateApplicationTitleRequest(Settings.jpoResources.getString("ApplicationTitle")));
            }
        }
    }

}

