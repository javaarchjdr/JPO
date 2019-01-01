package jpo.gui.swing;

import com.google.common.eventbus.Subscribe;
import jpo.EventBus.*;
import jpo.dataModel.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/*
 Copyright (C) 2017-2018  Richard Eigenmann.
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
 * Tests for the PicturePopupMenu Class
 *
 * @author Richard Eigenmann
 */
public class PicturePopupMenuTest {

    /*
     * Note these tests are burdened with reflection to get at the inner
     * workings of the popup menu. Should I open up the fields in the popup menu
     * class? I think not because other classes don't need to see into the inner
     * workings of the popup menu. With the exception of this one that has to
     * make sure the details of the class are working properly.
     *
     */
    final private PictureInfo myPictureInfo = new PictureInfo();
    final private GroupInfo myGroupInfo = new GroupInfo("Parent Group");
    final private SortableDefaultMutableTreeNode myNode = new SortableDefaultMutableTreeNode(myPictureInfo);
    final private SortableDefaultMutableTreeNode myParentNode = new SortableDefaultMutableTreeNode(myGroupInfo);

    final private SingleNodeNavigator myNavigator = new SingleNodeNavigator(myNode);
    private PicturePopupMenu myPicturePopupMenu;

    private JMenuItem title;
    private JMenuItem showPicture;
    private JMenuItem showMap;
    private JMenu navigateTo;
    private JMenuItem navigateTo_0;
    private JMenuItem categories;
    private JMenuItem selectForEmail;
    private JMenuItem unselectForEmail;
    private JMenuItem clearEmailSelection;
    private JMenu userFunction;
    private JMenuItem userFunction_0;
    private JMenuItem userFunction_1;
    private JMenuItem userFunction_2;
    private JMenu rotation;
    private JMenuItem rotate90;
    private JMenuItem rotate180;
    private JMenuItem rotate270;
    private JMenuItem rotate0;
    private JMenuItem refreshThumbnail;
    private JMenu move;
    private JMenuItem moveToTop;
    private JMenuItem moveUp;
    private JMenuItem moveDown;
    private JMenuItem moveToBottom;
    private JMenuItem moveIndent;
    private JMenuItem moveOutdent;
    private JMenu copyImage;
    private JMenuItem copyImageChooseTargetDir;
    private JMenuItem copyImageToZipFile;
    private JMenuItem copyToClipboard;
    private JMenuItem removeNode;
    private JMenu fileOperations;
    private JMenu renameJMenu;
    private JMenu moveImage;
    private JMenuItem moveToNewLocation;
    private JMenuItem fileOperationsRename;
    private JMenuItem fileoperationsDelete;
    private JMenuItem properties;
    private JMenuItem consolidateHere;

    /**
     * Creates the objects for testing. Runs on the EDT.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        myPictureInfo.setDescription("My Picture");
        try {
            File temp = File.createTempFile("JPO-Unit-Test", ".jpg");
            temp.deleteOnExit();
            myPictureInfo.setImageLocation(temp);
        } catch (IOException ex) {
            Logger.getLogger(PicturePopupMenuTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        myParentNode.add(myNode);
        Settings.getPictureCollection().getRootNode().add(myParentNode);

        myPicturePopupMenu = new PicturePopupMenu(myNavigator, 0);

        SwingUtilities.invokeAndWait(() -> {
            title = (JMenuItem) myPicturePopupMenu.getComponent(0);
            showPicture = (JMenuItem) myPicturePopupMenu.getComponent(2);
            showMap = (JMenuItem) myPicturePopupMenu.getComponent(3);
            navigateTo = (JMenu) myPicturePopupMenu.getComponent(4);
            navigateTo_0 = navigateTo.getItem(0);
            categories = (JMenuItem) myPicturePopupMenu.getComponent(5);
            selectForEmail = (JMenuItem) myPicturePopupMenu.getComponent(6);
            unselectForEmail = (JMenuItem) myPicturePopupMenu.getComponent(7);
            clearEmailSelection = (JMenuItem) myPicturePopupMenu.getComponent(8);
            userFunction = (JMenu) myPicturePopupMenu.getComponent(9);
            userFunction_0 = userFunction.getItem(0);
            userFunction_1 = userFunction.getItem(1);
            userFunction_2 = userFunction.getItem(2);
            rotation = (JMenu) myPicturePopupMenu.getComponent(10);
            rotate90 = rotation.getItem(0);
            rotate180 = rotation.getItem(1);
            rotate270 = rotation.getItem(2);
            rotate0 = rotation.getItem(3);
            refreshThumbnail = (JMenuItem) myPicturePopupMenu.getComponent(11);
            move = (JMenu) myPicturePopupMenu.getComponent(12);
            moveToTop = move.getItem(Settings.MAX_DROPNODES + 1);
            moveUp = move.getItem(Settings.MAX_DROPNODES + 2);
            moveDown = move.getItem(Settings.MAX_DROPNODES + 3);
            moveToBottom = move.getItem(Settings.MAX_DROPNODES + 4);
            moveIndent = move.getItem(Settings.MAX_DROPNODES + 5);
            moveOutdent = move.getItem(Settings.MAX_DROPNODES + 6);
            copyImage = (JMenu) myPicturePopupMenu.getComponent(13);
            copyImageChooseTargetDir = copyImage.getItem(0);
            copyImageToZipFile = copyImage.getItem(12);
            copyToClipboard = copyImage.getItem(13);
            removeNode = (JMenuItem) myPicturePopupMenu.getComponent(14);
            fileOperations = (JMenu) myPicturePopupMenu.getComponent(15);
            moveImage = (JMenu) fileOperations.getItem(2);
            moveToNewLocation = moveImage.getItem(0);
            renameJMenu = (JMenu) fileOperations.getItem(3);
            fileOperationsRename = renameJMenu.getItem(0);
            fileoperationsDelete = fileOperations.getItem(4);
            properties = (JMenuItem) myPicturePopupMenu.getComponent(16);
            consolidateHere = (JMenuItem) myPicturePopupMenu.getComponent(17);
        });

    }

    /**
     * Test that out Group Node was created for the correct node.
     */
    @Test
    public void testRememberingPopupNode() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    Field popupNodeField;
                    popupNodeField = PicturePopupMenu.class.getDeclaredField("popupNode");
                    popupNodeField.setAccessible(true);
                    SortableDefaultMutableTreeNode verifyNode = (SortableDefaultMutableTreeNode) popupNodeField.get(myPicturePopupMenu);
                    PictureInfo verifyPictureInfo = (PictureInfo) verifyNode.getUserObject();
                    assertEquals(myPictureInfo, verifyPictureInfo);
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(PicturePopupMenuTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(PicturePopupMenuTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Get the children
     */
    @Test
    public void testGetChildren() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                assertEquals("My Picture", title.getText());
                assertEquals("Show Picture", showPicture.getText());
                assertEquals("Show Map", showMap.getText());
                assertEquals("Navigate to", navigateTo.getText());
                assertEquals("Categories", categories.getText());
                assertEquals("Select for email", selectForEmail.getText());
                assertEquals("User Function", userFunction.getText());
                assertEquals("Rotation", rotation.getText());
                assertEquals("Rotate Right 90", rotate90.getText());
                assertEquals("Rotate 180", rotate180.getText());
                assertEquals("Rotate Left 270", rotate270.getText());
                assertEquals("No Rotation", rotate0.getText());
                assertEquals("Refresh Thumbnail", refreshThumbnail.getText());
                assertEquals("Move", move.getText());
                assertEquals("to Top", moveToTop.getText());
                assertEquals("Up", moveUp.getText());
                assertEquals("Down", moveDown.getText());
                assertEquals("indent", moveIndent.getText());
                assertEquals("outdent", moveOutdent.getText());
                assertEquals("to Bottom", moveToBottom.getText());
                assertEquals("Copy Image", copyImage.getText());
                assertEquals("choose target directory", copyImageChooseTargetDir.getText());
                assertEquals("to zip file", copyImageToZipFile.getText());
                assertEquals("Copy to Clipboard", copyToClipboard.getText());
                assertEquals("Remove Node", removeNode.getText());
                assertEquals("File operations", fileOperations.getText());
                assertEquals("Properties", properties.getText());
                assertEquals("Consolidate Here", consolidateHere.getText());
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(PicturePopupMenuTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int showPictureEventCount = 0;

    /**
     * Test clicking showPicture
     */
    @Test
    public void testShowPicture() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleShowPictureRequest(ShowPictureRequest request) {
                //SortableDefaultMutableTreeNode node = request.getNode();
                //Object userObject = node.getUserObject();
                showPictureEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, showPictureEventCount);
        showPicture.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, showPictureEventCount);
    }

    private int showMapEventCount = 0;

    /**
     * Test clicking showMap
     */
    @Test
    public void testShowMap() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleShowPictureOnMapRequest(ShowPictureOnMapRequest request) {
                showMapEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, showMapEventCount);
        showMap.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, showMapEventCount);
    }

    private int navigateToEventCount = 0;

    @Test
    public void testNavigateTo() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleShowGroupRequest(ShowGroupRequest request) {
                navigateToEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, navigateToEventCount);
        navigateTo_0.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, navigateToEventCount);
    }

    private int categoriesEventCount = 0;

    @Test
    public void testCategories() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleShowCategoryUsageEditorRequest(ShowCategoryUsageEditorRequest request) {
                categoriesEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, categoriesEventCount);
        categories.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, categoriesEventCount);
    }

    private int addToMailSelectEventCount = 0;

    @Test
    public void testSelectForEmail() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleAddPictureModesToEmailSelectionRequest(AddPictureNodesToEmailSelectionRequest request) {
                addToMailSelectEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, addToMailSelectEventCount);
        selectForEmail.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, addToMailSelectEventCount);
    }

    private int removeFromMailSelectEventCount = 0;

    @Test
    public void testUnselectForEmail() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleRemovePictureModesFromEmailSelectionRequest(RemovePictureNodesFromEmailSelectionRequest request) {
                removeFromMailSelectEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, removeFromMailSelectEventCount);
        unselectForEmail.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, removeFromMailSelectEventCount);
    }

    private int clearEmailSelectionEventCount = 0;

    @Test
    public void testClearEmailSelection() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleClearEmailSelectionRequest(ClearEmailSelectionRequest request) {
                clearEmailSelectionEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, clearEmailSelectionEventCount);
        clearEmailSelection.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, clearEmailSelectionEventCount);
    }

    // TODO: Test the selection logic of the Select for EMail and unselect for EMail
    private int userFunctionEventCount = 0;

    @Test
    public void testUserFunctions() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleRunUserFunctionRequest(RunUserFunctionRequest request) {
                userFunctionEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, userFunctionEventCount);
        userFunction_0.doClick();
        userFunction_1.doClick();
        userFunction_2.doClick();
        assertEquals("After clicking on the node the event count should be 3", 3, userFunctionEventCount);
    }

    private int rotationEventCount = 0;

    @Test
    public void testRotation() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleRotatePictureRequestRequest(RotatePictureRequest request) {
                rotationEventCount++;
            }

            @Subscribe
            public void handleSetPictureRotationRequest(SetPictureRotationRequest request) {
                rotationEventCount++;
            }

        });
        assertEquals("Before clicking on the node the event count should be 0", 0, rotationEventCount);
        rotate90.doClick();
        rotate180.doClick();
        rotate270.doClick();
        rotate0.doClick();
        assertEquals("After clicking on the node the event count should be 4", 4, rotationEventCount);
    }

    private int refreshEventCount = 0;

    @Test
    public void testRefresh() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleRefreshThumbnailRequest(RefreshThumbnailRequest request) {
                refreshEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, refreshEventCount);
        refreshThumbnail.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, refreshEventCount);
    }

    private int moveEventCount = 0;

    @Test
    public void testMove() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleMoveNodeToTopRequest(MoveNodeToTopRequest request) {
                moveEventCount++;
            }

            @Subscribe
            public void handleMoveNodeUpRequest(MoveNodeUpRequest request) {
                moveEventCount++;
            }

            @Subscribe
            public void handleMoveNodeDownRequest(MoveNodeDownRequest request) {
                moveEventCount++;
            }

            @Subscribe
            public void handleMoveNodeToBottomRequest(MoveNodeToBottomRequest request) {
                moveEventCount++;
            }

            @Subscribe
            public void handleMoveIndentRequest(MoveIndentRequest request) {
                moveEventCount++;
            }

            @Subscribe
            public void handleMoveOutdentRequest(MoveOutdentRequest request) {
                moveEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, moveEventCount);
        moveToTop.doClick();
        moveDown.doClick();
        moveUp.doClick();
        moveToBottom.doClick();
        moveIndent.doClick();
        moveOutdent.doClick();
        assertEquals("After clicking on the node the event count should be 1", 6, moveEventCount);
    }

    private int clipboardEventCount = 0;

    @Test
    public void testCopyToClipboard() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleCopyToClipboardRequest(CopyToClipboardRequest request) {
                clipboardEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, clipboardEventCount);
        copyToClipboard.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, clipboardEventCount);
    }

    private int removeEventCount = 0;

    @Test
    public void testRemoveNode() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleRemoveNodeRequest(RemoveNodeRequest request) {
                removeEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, removeEventCount);
        removeNode.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, removeEventCount);
    }

    private int moveToNewLocationEventCount = 0;

    @Test
    public void testMoveToNewLocation() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleMoveToNewLocation(MoveToNewLocationRequest request) {
                moveToNewLocationEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, moveToNewLocationEventCount);
        moveToNewLocation.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, moveToNewLocationEventCount);
    }


    private int renameEventCount = 0;

    @Test
    public void testFileRename() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleRenamePictureRequest(RenamePictureRequest request) {
                renameEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, renameEventCount);
        fileOperationsRename.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, renameEventCount);
    }

    private int deleteEventCount = 0;

    @Test
    public void testFileDelete() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleDeleteNodeFileRequest(DeleteNodeFileRequest request) {
                deleteEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, deleteEventCount);
        fileoperationsDelete.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, deleteEventCount);
    }

    private int propertiesEventCount = 0;

    @Test
    public void properties() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleShowPictureInfoEditorRequest(ShowPictureInfoEditorRequest request) {
                propertiesEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, propertiesEventCount);
        properties.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, propertiesEventCount);
    }

    private int consolidateHereEventCount = 0;

    @Test
    public void consolidateHere() {
        JpoEventBus.getInstance().register(new Object() {
            @Subscribe
            public void handleConsolidateGroupRequest(ConsolidateGroupRequest request) {
                consolidateHereEventCount++;
            }
        });
        assertEquals("Before clicking on the node the event count should be 0", 0, consolidateHereEventCount);
        consolidateHere.doClick();
        assertEquals("After clicking on the node the event count should be 1", 1, consolidateHereEventCount);
    }

    @Test
    public void testReplaceEscapedSpaces() {
        String s = "filename%20extension.jpg";
        Optional<String> o = PicturePopupMenu.replaceEscapedSpaces(s);
        assert (o.isPresent());
        assertEquals("filename extension.jpg", o.get());
    }

    @Test
    public void testRplaceDoubleEscapedSpaces() {
        String s = "filename%20%20extension.jpg";
        Optional<String> o = PicturePopupMenu.replaceEscapedSpaces(s);
        assert (o.isPresent());
        assertEquals("filename extension.jpg", o.get());
    }

    @Test
    public void testReplaceNoEscapedSpaces() {
        String s = "filenameExtension.jpg";
        Optional<String> o = PicturePopupMenu.replaceEscapedSpaces(s);
        assertFalse(o.isPresent());
    }

    @Test
    public void testReplaceNoEscapedSpacesSingeChars() {
        String s1 = "filename2Extension.jpg";
        Optional<String> o1 = PicturePopupMenu.replaceEscapedSpaces(s1);
        if (o1.isPresent()) {
            fail(String.format("There wasn't supposed to be anything that changed between\n%s and\n%s", s1, o1.get()));
        }

        String s2 = "filename0Extension_.jpg";
        Optional<String> o2 = PicturePopupMenu.replaceEscapedSpaces(s2);
        if (o2.isPresent()) {
            fail(String.format("There wasn't supposed to be anything that changed between\n%s and\n%s", s2, o2.get()));
        }

        String s3 = "filename%Extension.jpg";
        Optional<String> o3 = PicturePopupMenu.replaceEscapedSpaces(s3);
        if (o3.isPresent()) {
            fail(String.format("There wasn't supposed to be anything that changed between\n%s and\n%s", s3, o3.get()));
        }
    }

    @Test
    public void testReplaceUnderscore() {
        String s = "filename_extension.jpg";
        Optional<String> o = PicturePopupMenu.replaceUnderscore(s);
        assert (o.isPresent());
        assertEquals("filename extension.jpg", o.get());
    }

    @Test
    public void testReplaceDoubleUnderscores() {
        String s = "filename__extension.jpg";
        Optional<String> o = PicturePopupMenu.replaceUnderscore(s);
        assert (o.isPresent());
        assertEquals("filename extension.jpg", o.get());
    }

    @Test
    public void testReplaceNoUnderscores() {
        String s = "filenameExtension%20.jpg";
        Optional<String> o = PicturePopupMenu.replaceUnderscore(s);
        assertFalse(o.isPresent());
    }

}
