package org.jpo.dataModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jpo.eventBus.CopyLocationsChangedEvent;
import org.jpo.eventBus.JpoEventBus;
import org.jpo.dataModel.Settings.FieldCodes;
import org.jpo.gui.JpoTransferable;
import org.jpo.gui.ProgressGui;
import org.jpo.gui.SourcePicture;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.jpo.dataModel.Tools.copyBufferedStream;


/*
 Copyright (C) 2003 - 2019  Richard Eigenmann, Zurich, Switzerland
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
 * This is the main data structure object for the JPO Collection. Holds a
 * reference to either a PictureInfo or GroupInfo object in its getUserObject.
 * <p>
 * It extends the DefaultMutableTreeNode with the Comparable Interface that
 * allows our nodes to be compared.
 */
public class SortableDefaultMutableTreeNode
        extends DefaultMutableTreeNode
        implements Comparable, PictureInfoChangeListener {

    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(SortableDefaultMutableTreeNode.class.getName());

    /**
     * Constructor for a new node.
     */
    public SortableDefaultMutableTreeNode() {
        super();
    }

    /**
     * Constructor for a new node.
     *
     * @param userObject User Object
     */
    public SortableDefaultMutableTreeNode(GroupInfo userObject) {
        this((Object) userObject);
    }

    /**
     * Constructor for a new node.
     *
     * @param userObject User Object
     */
    public SortableDefaultMutableTreeNode(PictureInfo userObject) {
        this((Object) userObject);
    }

    /**
     * Constructor for a new node including a user object. The user object must
     * be a PictureInfo or GroupInfo object. Set to private so that this constructor
     */
    private SortableDefaultMutableTreeNode(Object userObject) {
        super();
        setUserObject(userObject);
    }

    /**
     * returns the collection associated with this node
     *
     * @return the picture collection.
     */
    public PictureCollection getPictureCollection() {
        return Settings.getPictureCollection();
    }

    /**
     * Call this method to sort the Children of a node by a field.
     *
     * @param sortCriteria The criteria by which the pictures should be sorted.
     */
    public void sortChildren(FieldCodes sortCriteria) {
        Tools.checkEDT();  // because of removeAllChildren
        synchronized (this.getRoot()) {
            int childCount = getChildCount();
            SortableDefaultMutableTreeNode[] childNodes = new SortableDefaultMutableTreeNode[childCount];
            for (int i = 0; i < childCount; i++) {
                childNodes[i] = (SortableDefaultMutableTreeNode) getChildAt(i);
            }

            // sort the array
            sortfield = sortCriteria;
            Arrays.sort(childNodes);

            //Remove all children from the node
            getPictureCollection().setSendModelUpdates(false);
            removeAllChildren();
            for (SortableDefaultMutableTreeNode childNode : childNodes) {
                add(childNode);
            }
        }
        getPictureCollection().setUnsavedUpdates();
        getPictureCollection().setSendModelUpdates(true);

        // tell the collection that the structure changed
        LOGGER.fine(String.format("Sending node structure changed event on node %s after sort", this.toString()));
        getPictureCollection().sendNodeStructureChanged(this);
    }

    /**
     * This field records the field by which the group is to be sorted. This is
     * not very elegant as a second sort could run at the same time and clobber
     * this global variable. But that's not very likely on a single user app
     * like this.
     */
    private static FieldCodes sortfield;

    /**
     * Overridden method to allow sorting of nodes. It uses the static global
     * variable sortfield to figure out what to compare on.
     *
     * @param o the object to compare to
     * @return the usual compareTo value used for sorting.
     */
    @Override
    public int compareTo(@NonNull Object o) {
        Object myObject = getUserObject();
        Object otherObject = ((DefaultMutableTreeNode) o).getUserObject();

        if ((myObject instanceof GroupInfo) && (otherObject instanceof GroupInfo) && (sortfield == FieldCodes.DESCRIPTION)) {
            return ((GroupInfo) myObject).getGroupName().compareTo(((GroupInfo) otherObject).getGroupName());
        }

        if ((myObject instanceof GroupInfo) && (otherObject instanceof PictureInfo) && (sortfield == FieldCodes.DESCRIPTION)) {
            return ((GroupInfo) myObject).getGroupName().compareTo(((PictureInfo) otherObject).getDescription());
        }

        if ((myObject instanceof PictureInfo) && (otherObject instanceof GroupInfo) && (sortfield == FieldCodes.DESCRIPTION)) {
            return ((PictureInfo) myObject).getDescription().compareTo(((GroupInfo) otherObject).getGroupName());
        }

        if ((myObject instanceof GroupInfo) || (otherObject instanceof GroupInfo)) {
            // we can't compare Groups against the other types of field other than the description.
            return 0;
        }

        // at this point there can only two PictureInfo Objects
        if ((myObject instanceof PictureInfo) && (otherObject instanceof PictureInfo)) {
            PictureInfo myPi = (PictureInfo) myObject;
            PictureInfo otherPi = (PictureInfo) otherObject;
            switch (sortfield) {
                case FILM_REFERENCE:
                    return myPi.getFilmReference().compareTo(otherPi.getFilmReference());
                case CREATION_TIME:
                    return myPi.getCreationTime().compareTo(otherPi.getCreationTime());
                case COMMENT:
                    return myPi.getComment().compareTo(otherPi.getComment());
                case PHOTOGRAPHER:
                    return myPi.getPhotographer().compareTo(otherPi.getPhotographer());
                case COPYRIGHT_HOLDER:
                    return myPi.getCopyrightHolder().compareTo(otherPi.getCopyrightHolder());
                default:  // case DESCRIPTION
                    return myPi.getDescription().compareTo(otherPi.getDescription());
            }
        } else {
            LOGGER.severe("We are not supposed to hit this else branch!");
            Thread.dumpStack();
            return 0;
        }
    }

    /**
     * Returns the first node with a picture before the current one in the tree.
     * It uses the getPreviousNode method of DefaultMutableTreeNode.
     *
     * @return the first node with a picture in preorder traversal or null if
     * none found.
     */
    public SortableDefaultMutableTreeNode getPreviousPicture() {
        synchronized (this.getRoot()) {
            DefaultMutableTreeNode prevNode = getPreviousNode();
            while ((prevNode != null) && (!(prevNode.getUserObject() instanceof PictureInfo))) {
                prevNode = prevNode.getPreviousNode();
            }
            return (SortableDefaultMutableTreeNode) prevNode;
        }
    }

    /**
     * Returns the next node with a picture found after current one in the tree.
     * This can be in another Group. It uses the getNextNode method of the
     * DefaultMutableTreeNode.
     *
     * @return The SortableDefaultMutableTreeNode that represents the next
     * picture. If no picture can be found it returns null.
     */
    public SortableDefaultMutableTreeNode getNextPicture() {
        synchronized (this.getRoot()) {
            DefaultMutableTreeNode nextNode = getNextNode();
            while ((nextNode != null) && (!(nextNode.getUserObject() instanceof PictureInfo))) {
                nextNode = nextNode.getNextNode();
            }
            return (SortableDefaultMutableTreeNode) nextNode;
        }
    }

    /**
     * Returns the next node with a picture found after current one in the
     * current Group It uses the getNextSibling method of the
     * DefaultMutableTreeNode.
     *
     * @return The SortableDefaultMutableTreeNode that represents the next
     * picture. If no picture can be found it returns null.
     */
    public SortableDefaultMutableTreeNode getNextGroupPicture() {
        synchronized (this.getRoot()) {
            DefaultMutableTreeNode nextNode = getNextSibling();
            while ((nextNode != null) && (!(nextNode.getUserObject() instanceof PictureInfo))) {
                nextNode = nextNode.getNextNode();
            }
            return (SortableDefaultMutableTreeNode) nextNode;
        }
    }

    /**
     * Returns the first child node under the current node which holds a
     * PictureInfo object.
     *
     * @return The first child node holding a picture or null if none can be
     * found.
     */
    public SortableDefaultMutableTreeNode findFirstPicture() {
        SortableDefaultMutableTreeNode testNode;
        Enumeration e = children();
        while (e.hasMoreElements()) {
            testNode = (SortableDefaultMutableTreeNode) e.nextElement();
            if (testNode.getUserObject() instanceof PictureInfo) {
                return testNode;
            } else if (testNode.getUserObject() instanceof GroupInfo) {
                testNode = testNode.findFirstPicture();
                if (testNode != null) {
                    return testNode;
                }
            }
        }
        return null;
    }

    /**
     * This method collects all pictures under the current node and returns them
     * as an Array List..
     *
     * @param recursive Pass true if the method is supposed to recursively
     *                  search the subgroups, false if not
     * @return A List of child nodes that hold a picture
     */
    public List<SortableDefaultMutableTreeNode> getChildPictureNodes(
            boolean recursive) {
        List<SortableDefaultMutableTreeNode> pictureNodes = new ArrayList<>();
        Enumeration kids = this.children();
        SortableDefaultMutableTreeNode node;

        while (kids.hasMoreElements()) {
            node = (SortableDefaultMutableTreeNode) kids.nextElement();
            if (recursive && node.getUserObject() instanceof GroupInfo) {
                pictureNodes.addAll(node.getChildPictureNodes(true));
            } else if (node.getUserObject() instanceof PictureInfo) {
                pictureNodes.add(node);
            }
        }
        return pictureNodes;
    }

    /**
     * A convenience method to tell if the current node has at least one picture
     * node in the tree of children. (Could be one)
     *
     * @return true if at least one picture is found, false if not
     */
    public boolean hasChildPictureNodes() {
        //Enumeration kids = this.children();
        Enumeration kids = this.breadthFirstEnumeration();
        SortableDefaultMutableTreeNode node;
        while (kids.hasMoreElements()) {
            node = (SortableDefaultMutableTreeNode) kids.nextElement();
            if (node.getUserObject() instanceof PictureInfo) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is being overridden to allow us to capture editing events on
     * the JTree that is rendering this node. The TreeCellEditor will send the
     * changed label as a String type object to the setUserObject method of this
     * class. My overriding this we can intercept this and update the
     * PictureInfo or GroupInfo accordingly.
     *
     * @param userObject The object to attach to the node
     */
    @Override
    public void setUserObject(Object userObject) {
        if (userObject instanceof String) {
            LOGGER.severe("Why is ever being called?");
            Object obj = getUserObject();
            if (obj instanceof GroupInfo) {
                ((GroupInfo) obj).setGroupName((String) userObject);
            } else if (obj instanceof PictureInfo) {
                ((PictureInfo) obj).setDescription((String) userObject);
            }
        } else if (userObject instanceof PictureInfo) {
            PictureInfo pictureInfo = (PictureInfo) userObject;
            Object oldUserObject = getUserObject();
            if (oldUserObject instanceof PictureInfo) {
                PictureInfo oldPi = (PictureInfo) oldUserObject;
                oldPi.removePictureInfoChangeListener(this);
            }
            pictureInfo.addPictureInfoChangeListener(this);
            super.setUserObject(userObject);
        } else {
            // fall back on the default behaviour
            super.setUserObject(userObject);
        }
        if (getPictureCollection() != null && getPictureCollection().getSendModelUpdates()) {
            getPictureCollection().sendNodeChanged(this);
        }
    }

    /**
     * Checks if the DropTargetDropEvent is suitable. Presently we can only deal
     * with local transfers of JpoTransferables
     *
     * @param event The drop event
     * @return true if acceptable, false if not
     */
    public static boolean isExecuteDropOk(DropTargetDropEvent event) {
        if (!event.isLocalTransfer()) {
            LOGGER.info("The drop is not a local Transfer. These are not supported. Aborting drop.");
            event.rejectDrop();
            event.dropComplete(false);
            return false;
        } else {
            LOGGER.info("The drop is a local Transfer.");
        }

        if (!event.isDataFlavorSupported(JpoTransferable.jpoNodeFlavor)) {
            LOGGER.info("The drop doesn't have a JpoTransferable.jpoNodeFlavor. Drop rejected.");
            event.rejectDrop();
            event.dropComplete(false);
            return false;
        } else {
            LOGGER.info("The drop is for a JpoTransferable.jpoNodeFlavor");
        }

        int actionType = event.getDropAction();
        if ((actionType == DnDConstants.ACTION_MOVE) || (actionType == DnDConstants.ACTION_COPY)) {
            event.acceptDrop(actionType);   // crucial Step!
        } else {
            LOGGER.info("The event has an odd Action Type. Drop rejected.");
            event.rejectDrop();
            event.dropComplete(false);
            return false;
        }
        return true;
    }

    /**
     * Extract the transferable nodes from the drop event
     *
     * @param event the drop event
     * @return a list of the transferable nodes
     * @throws UnsupportedFlavorException when a bad transferable is received
     * @throws IOException when an IO error occurs
     */
    @NotNull
    public static List<SortableDefaultMutableTreeNode> extractTransferableNodes(DropTargetDropEvent event)
            throws UnsupportedFlavorException, IOException, ClassCastException {
        Transferable t = event.getTransferable();
        Object o = t.getTransferData(JpoTransferable.jpoNodeFlavor);
        List<?> l = (List) o;
        return l.stream()
                .filter(element->element instanceof SortableDefaultMutableTreeNode)
                .map(element->(SortableDefaultMutableTreeNode)element)
                .collect(Collectors.toList());
    }

    /**
     * This method memorizes the group associated with the supplied node in the
     * Settings object. If the supplied node is not a group it's parent which
     * must be a group is memorised.
     *
     * @param node the node to memorize
     */
    public static void memorizeGroupOfDropLocation(SortableDefaultMutableTreeNode node) {
        if (node.getUserObject() instanceof GroupInfo) {
            Settings.memorizeGroupOfDropLocation(node);
        } else {
            SortableDefaultMutableTreeNode parent = node.getParent();
            if ((parent != null) && (parent.getUserObject() instanceof GroupInfo)) {
                Settings.memorizeGroupOfDropLocation(parent);
            } else {
                LOGGER.info("Failed to find the group of the drop location. Not memorizing in settings.");
            }
        }
    }

    /**
     * This method is called by the drop method of the DragTarget to do the
     * move. It deals with the intricacies of the drop event and handles all the
     * moving, cloning and positioning that is required.
     *
     * @param event The event the listening object received.
     */
    public void executeDrop(DropTargetDropEvent event) {
        LOGGER.info("Data Flavours: [" + event.getCurrentDataFlavorsAsList()
                .stream()
                .map(n -> n.getClass().toString())
                .collect(Collectors.joining(", ")) + "]");
        if (!isExecuteDropOk(event)) {
            LOGGER.info("Can't accept drop, rejecting drop event");
            event.rejectDrop();
            event.dropComplete(false);
            return;
        }

        List<SortableDefaultMutableTreeNode> transferableNodes;
        try {
            transferableNodes = extractTransferableNodes(event);
        } catch (UnsupportedFlavorException | IOException | ClassCastException ex) {
            LOGGER.info("Error while collecting the transferables. Exception: " + ex.getMessage());
            event.dropComplete(false);
            return;
        }

        for (SortableDefaultMutableTreeNode sourceNode : transferableNodes) {
            if (this.isNodeAncestor(sourceNode)) {
                LOGGER.info("One of the transferring nodes is an ancestor of "
                        + "the current node which would orphan the tree.\n"
                        + "Ancestor node: " + sourceNode.toString()
                        + "\nCurrent node: " + this.toString());
                JOptionPane.showMessageDialog(Settings.anchorFrame,
                        Settings.jpoResources.getString("moveNodeError"),
                        Settings.jpoResources.getString("genericError"),
                        JOptionPane.ERROR_MESSAGE);
                event.dropComplete(false);
                return;
            }
        }

        memorizeGroupOfDropLocation(this);

        boolean dropcomplete = false;
        for (SortableDefaultMutableTreeNode sourceNode : transferableNodes) {
            if ((sourceNode.getUserObject() instanceof PictureInfo) && (this.getUserObject() instanceof GroupInfo)) {
                // a picture is being dropped onto a group; add it at the end
                if (event.getDropAction() == DnDConstants.ACTION_MOVE) {
                    LOGGER.info("Moving Picture node " + sourceNode.toString() + " onto last position in Group node " + this.toString());
                    sourceNode.moveToLastChild(this);
                } else {
                    LOGGER.info("Cloning Picture node " + sourceNode.toString() + " onto last position in Group node " + this.toString());
                    SortableDefaultMutableTreeNode newNode = new SortableDefaultMutableTreeNode(((PictureInfo) sourceNode.getUserObject()).getClone());
                    add(newNode);
                }
                dropcomplete = true;
            } else if ((sourceNode.getUserObject() instanceof PictureInfo) && (this.getUserObject() instanceof PictureInfo)) {
                // a picture is being dropped onto a picture and should be inserted before the target node
                SortableDefaultMutableTreeNode parentNode = this.getParent();
                if (event.getDropAction() == DnDConstants.ACTION_MOVE) {
                    LOGGER.info("Moving Picture node " + sourceNode.toString() + " before Picture node " + this.toString());
                    sourceNode.removeFromParent();
                    int indexPosition = parentNode.getIndex(this);
                    parentNode.insert(sourceNode, indexPosition);
                } else {
                    LOGGER.info("Copying Picture node " + sourceNode.toString() + " before Picture node " + this.toString());
                    SortableDefaultMutableTreeNode newNode = new SortableDefaultMutableTreeNode(((PictureInfo) sourceNode.getUserObject()).getClone());
                    int indexPosition = parentNode.getIndex(this);
                    parentNode.insert(newNode, indexPosition);
                }
                dropcomplete = true;
            } else {
                LOGGER.info("Dropping Group node " + sourceNode.toString() + " onto Group node " + this.toString());
                if (!this.isRoot()) {
                    GroupDropPopupMenu groupDropPopupMenu = new GroupDropPopupMenu(event, sourceNode, this);
                    groupDropPopupMenu.show(event.getDropTargetContext().getDropTarget().getComponent(), event.getLocation().x, event.getLocation().y);
                } else {
                    // Group was dropped on the root node --> add at first place.
                    sourceNode.removeFromParent();
                    this.insert(sourceNode, 0);
                    dropcomplete = true;
                    getPictureCollection().setUnsavedUpdates();
                }
            }
        }
        event.dropComplete(dropcomplete);
    }

    /**
     * This is where the Nodes in the tree find out about changes in the
     * PictureInfo object
     *
     * @param e The event
     */
    @Override
    public void pictureInfoChangeEvent(PictureInfoChangeEvent e) {
        LOGGER.fine(String.format("The SDMTN %s received a PictureInfoChangeEvent %s", this.toString(), e.toString()));
        getPictureCollection().sendNodeChanged(this);
    }

    /**
     * This inner class creates a popup menu for group drop events to find out
     * whether to drop into before or after the drop node.
     */
    class GroupDropPopupMenu
            extends JPopupMenu {

        /**
         * This inner class creates a popup menu for group drop events to find
         * out whether to drop into before or after the drop node.
         *
         * @param event      The event
         * @param sourceNode the source node
         * @param targetNode the target node
         */
        private GroupDropPopupMenu(final DropTargetDropEvent event,
                                   final SortableDefaultMutableTreeNode sourceNode,
                                   final SortableDefaultMutableTreeNode targetNode) {

            // menu item that allows the user to edit the group description
            JMenuItem dropBefore = new JMenuItem(Settings.jpoResources.getString("GDPMdropBefore"));
            dropBefore.addActionListener((ActionEvent e) -> {
                SortableDefaultMutableTreeNode parentNode = targetNode.getParent();
                sourceNode.removeFromParent();
                int currentIndex = parentNode.getIndex(targetNode);
                parentNode.insert(sourceNode, currentIndex);
                event.dropComplete(true);
                getPictureCollection().setUnsavedUpdates();
            });
            add(dropBefore);

            // menu item that allows the user to edit the group description
            JMenuItem dropAfter = new JMenuItem(Settings.jpoResources.getString("GDPMdropAfter"));
            dropAfter.addActionListener((ActionEvent e) -> {
                SortableDefaultMutableTreeNode parentNode = targetNode.getParent();
                sourceNode.removeFromParent();
                int currentIndex = parentNode.getIndex(targetNode);
                parentNode.insert(sourceNode, currentIndex + 1);
                event.dropComplete(true);
                getPictureCollection().setUnsavedUpdates();
            });
            add(dropAfter);

            // menu item that allows the user to edit the group description
            JMenuItem dropIntoFirst = new JMenuItem(Settings.jpoResources.getString("GDPMdropIntoFirst"));
            dropIntoFirst.addActionListener((ActionEvent e) -> {
                synchronized (targetNode.getRoot()) {
                    sourceNode.removeFromParent();
                    targetNode.insert(sourceNode, 0);
                }
                event.dropComplete(true);
                getPictureCollection().setUnsavedUpdates();
            });
            add(dropIntoFirst);

            // menu item that allows the user to edit the group description
            JMenuItem dropIntoLast = new JMenuItem(Settings.jpoResources.getString("GDPMdropIntoLast"));
            dropIntoLast.addActionListener((ActionEvent e) -> {
                synchronized (targetNode.getRoot()) {
                    sourceNode.removeFromParent();
                    int childCount = targetNode.getChildCount();
                    targetNode.insert(sourceNode, childCount);
                }
                event.dropComplete(true);
                getPictureCollection().setUnsavedUpdates();
            });
            add(dropIntoLast);

            // menu item that allows the user to edit the group description
            JMenuItem dropCancel = new JMenuItem(Settings.jpoResources.getString("GDPMdropCancel"));
            dropCancel.addActionListener((ActionEvent e) -> {
                LOGGER.info("cancel drop");
                event.dropComplete(false);
            });
            add(dropCancel);
        }
    }

    /**
     * This method removes the designated SortableDefaultMutableTreeNode from
     * the tree. The parent node is made the currently selected node.
     */
    public void deleteNode() {
        LOGGER.fine(String.format("Delete requested for node: %s", toString()));
        if (this.isRoot()) {
            LOGGER.info("Delete attempted on Root node. Can't do this! Aborted.");
            JOptionPane.showMessageDialog(Settings.anchorFrame,
                    Settings.jpoResources.getString("deleteRootNodeError"),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        getPictureCollection().setUnsavedUpdates();
        synchronized (this.getRoot()) {

            SortableDefaultMutableTreeNode parentNode = this.getParent();

            int[] childIndices = {parentNode.getIndex(this)};
            Object[] removedChildren = {this};

            super.removeFromParent();

            if (getPictureCollection().getSendModelUpdates()) {
                LOGGER.fine(String.format("Sending delete message. Model: %s, Parent: %s, ChildIndex %d, removedChild: %s ", getPictureCollection().getTreeModel(), parentNode, childIndices[0], removedChildren[0].toString()));
                getPictureCollection().sendNodesWereRemoved(parentNode, childIndices, removedChildren);
            }
        }

        Enumeration e = this.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            Settings.recentDropNodes.remove(e.nextElement());
        }

    }

    /**
     * Removes the node from the parent and sends a model update.
     */
    @Override
    public void removeFromParent() {
        synchronized (this.getRoot()) {

            SortableDefaultMutableTreeNode oldParentNode = this.getParent();
            if (oldParentNode == null) {
                LOGGER.info(String.format("Why would you try to remove node %s from it's parent when it has none?", toString()));
                return;
            }
            int oldParentIndex = oldParentNode.getIndex(this);
            super.removeFromParent();

            if (getPictureCollection().getSendModelUpdates()) {
                getPictureCollection().sendNodesWereRemoved(oldParentNode,
                        new int[]{oldParentIndex},
                        new Object[]{this});
            }
        }
    }

    /**
     * Returns a new SortableDefaultTreeMode which has the same content as the
     * source node
     *
     * @return a new node which is a clone of the old one
     */
    public SortableDefaultMutableTreeNode getClone() {
        SortableDefaultMutableTreeNode newNode = new SortableDefaultMutableTreeNode();
        if (this.getUserObject() instanceof PictureInfo) {
            newNode.setUserObject(((PictureInfo) this.getUserObject()).getClone());
        } else if (this.getUserObject() instanceof GroupInfo) {
            newNode.setUserObject(((GroupInfo) this.getUserObject()).getClone());
            Enumeration e = children();
            while (e.hasMoreElements()) {
                //logger.info( String.format( "Next Element: %s", e.nextElement().toString() ) );
                newNode.add(((SortableDefaultMutableTreeNode) e.nextElement()).getClone());
            }
        }
        return newNode;
    }

    /**
     * This method adds a new node to the data model of the tree. It is the
     * overridden add method which will first do the default behavior and then
     * send a notification to the Tree Model if model updates are being
     * requested. Likewise the unsaved changes of the collection are only being
     * updated when model updates are not being reported. This allows the
     * loading of collections (which of course massively change the collection
     * in memory) to report nothing changed.
     *
     * @param newNode the new node
     */
    public void add(SortableDefaultMutableTreeNode newNode) {
        synchronized (this.getRoot()) {
            super.add(newNode);
        }
        if (getPictureCollection().getSendModelUpdates()) {
            int index = this.getIndex(newNode);
            LOGGER.fine(String.format("The new node %s has index %d", newNode, index));
            getPictureCollection().sendNodesWereInserted(this, new int[]{index});
            getPictureCollection().setUnsavedUpdates();
        }
    }

    /**
     * Adds a new Group to the current node with the indicated description.
     *
     * @param description Description for the group
     * @return The new node is returned for convenience.
     */
    public SortableDefaultMutableTreeNode addGroupNode(String description) {
        synchronized (this.getRoot()) {
            SortableDefaultMutableTreeNode newNode
                    = new SortableDefaultMutableTreeNode(
                    new GroupInfo(description));
            add(newNode);
            return newNode;
        }
    }

    /**
     * Inserts the node and notifies the tree model of changes if we are sending
     * Model updates
     *
     * @param node  The node
     * @param index the index position
     */
    public void insert(SortableDefaultMutableTreeNode node, int index) {
        LOGGER.log(Level.FINE, "insert was called for node: {0}", node.toString());
        synchronized (this.getRoot()) {
            super.insert(node, index);
        }
        getPictureCollection().setUnsavedUpdates();
        if (getPictureCollection().getSendModelUpdates()) {
            getPictureCollection().sendNodesWereInserted(this, new int[]{index});
        }
    }

    /**
     * Validates the target of the picture copy instruction and tries to find
     * the appropriate thing to do. It does the following steps:<br>
     * 1: If the target is a directory the filename of the original is used.<br>
     * 2: If the target is an existing file the copy is aborted<br>
     * 3: If the target directory doesn't exist then the directories are
     * created.<br>
     * 4: The file extension is made to be that of the original if it isn't
     * already that.<br>
     * When all preconditions are met the image is copied
     *
     * @param targetFile The target location for the new Picture.
     * @return true if successful, false if not
     */
    public boolean validateAndCopyPicture(@NonNull File targetFile) {
        Objects.requireNonNull(targetFile, "targetFile must not be null");
        if (!(this.getUserObject() instanceof PictureInfo)) {
            LOGGER.severe("Only PictureInfo nodes can be copied! Copy for this picture aborted.");
            return false;
        }

        PictureInfo pictureInfo = (PictureInfo) this.getUserObject();
        File originalFile = pictureInfo.getImageFile();

        if (targetFile.exists()) {
            if (!targetFile.isDirectory()) {
                targetFile = Tools.inventPicFilename(targetFile.getParentFile(), originalFile.getName());
            }
        } else // it doesn't exist
            if (!targetFile.mkdirs()) {
                JOptionPane.showMessageDialog(Settings.anchorFrame,
                        Settings.jpoResources.getString("CopyImageDirError") + targetFile.toString(),
                        Settings.jpoResources.getString("genericError"),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

        if (targetFile.isDirectory()) {
            if (!targetFile.canWrite()) {
                JOptionPane.showMessageDialog(Settings.anchorFrame,
                        Settings.jpoResources.getString("htmlDistCanWriteError"),
                        Settings.jpoResources.getString("genericError"),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            targetFile = Tools.inventPicFilename(targetFile, originalFile.getName());
        }

        targetFile = Tools.correctFilenameExtension(FilenameUtils.getExtension(String.valueOf(originalFile)), targetFile);

        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        try {
            FileUtils.copyFile(pictureInfo.getImageFile(), targetFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(Settings.anchorFrame,
                    "IOException: " + e.getMessage(),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Settings.memorizeCopyLocation(targetFile.getParent());
        JpoEventBus.getInstance().post(new CopyLocationsChangedEvent());
        return true;
    }

    /**
     * When this method is invoked on a node it is moved to the first child
     * position of it's parent node.
     */
    public void moveNodeToTop() {
        if (this.isRoot()) {
            return;  // don't do anything with a root node.
        }
        synchronized (this.getRoot()) {
            SortableDefaultMutableTreeNode parentNode = this.getParent();
            // abort if this action was attempted on the top node
            if (parentNode.getIndex(this) < 1) {
                return;
            }
            this.removeFromParent();
            parentNode.insert(this, 0);
        }
        getPictureCollection().setUnsavedUpdates();
    }

    /**
     * When this method is invoked on a node it moves itself one position up
     * towards the first child position of it's parent node.
     */
    public void moveNodeUp() {
        if (this.isRoot()) {
            return;  // don't do anything with a root node.
        }
        synchronized (this.getRoot()) {
            SortableDefaultMutableTreeNode parentNode = this.getParent();
            int currentIndex = parentNode.getIndex(this);
            // abort if this action was attempted on the top node or not a child
            if (currentIndex < 1) {
                return;
            }
            this.removeFromParent();
            parentNode.insert(this, currentIndex - 1);
        }
        getPictureCollection().setUnsavedUpdates();
    }

    /**
     * Method that moves a node down one position
     */
    public void moveNodeDown() {
        if (this.isRoot()) {
            return;  // don't do anything with a root node.
        }
        synchronized (this.getRoot()) {
            SortableDefaultMutableTreeNode parentNode = this.getParent();
            int childCount = parentNode.getChildCount();
            int currentIndex = parentNode.getIndex(this);
            // abort if this action was attempted on the bottom node
            if ((currentIndex == -1)
                    || (currentIndex == childCount - 1)) {
                return;
            }
            this.removeFromParent();
            parentNode.insert(this, currentIndex + 1);
        }
        getPictureCollection().setUnsavedUpdates();
    }

    /**
     * Method that moves a node to the bottom of the current branch
     */
    public void moveNodeToBottom() {
        if (this.isRoot()) {
            return;  // don't do anything with a root node.
        }

        synchronized (this.getRoot()) {
            SortableDefaultMutableTreeNode parentNode = this.getParent();
            int childCount = parentNode.getChildCount();
            // abort if this action was attempted on the bottom node
            if ((parentNode.getIndex(this) == -1)
                    || (parentNode.getIndex(this) == childCount - 1)) {
                return;
            }
            this.removeFromParent();
            parentNode.insert(this, childCount - 1);
        }
        getPictureCollection().setUnsavedUpdates();
    }

    /**
     * When this method is invoked on a node it becomes a sub-node of it's
     * preceding group.
     */
    public void indentNode() {
        if (this.isRoot()) {
            return;  // don't do anything with a root node.
        }

        synchronized (this.getRoot()) {
            SortableDefaultMutableTreeNode parentNode = this.getParent();
            SortableDefaultMutableTreeNode childBefore = this;
            do {
                childBefore = (SortableDefaultMutableTreeNode) parentNode.getChildBefore(childBefore);
            } while ((childBefore != null) && (!(childBefore.getUserObject() instanceof GroupInfo)));

            if (childBefore == null) {
                SortableDefaultMutableTreeNode newGroup
                        = new SortableDefaultMutableTreeNode(
                        new GroupInfo(Settings.jpoResources.getString("newGroup")));
                parentNode.insert(newGroup, 0);
                this.removeFromParent();
                newGroup.add(this);
            } else {
                this.removeFromParent();
                childBefore.add(this);
            }
        }
        getPictureCollection().setUnsavedUpdates();
    }

    /**
     * Method that outdents a node. This means the node will be placed just
     * after it's parent's node as a child of it's grandparent.
     */
    public void outdentNode() {
        if (this.isRoot()) {
            return;  // don't do anything with a root node.
        }
        SortableDefaultMutableTreeNode parentNode = this.getParent();
        if (parentNode.isRoot()) {
            return;  // don't do anything with a root parent node.
        }

        synchronized (this.getRoot()) {
            SortableDefaultMutableTreeNode grandParentNode = parentNode.getParent();
            int index = grandParentNode.getIndex(parentNode);

            this.removeFromParent();
            grandParentNode.insert(this, index + 1);
        }
        getPictureCollection().setUnsavedUpdates();
    }

    /**
     * Method that moves a node to bottom of the specified target group node. It
     * sets the collection's unsaved updates to true.
     *
     * @param targetNode The target node you wish to attach the node to
     * @return true if the move was successful, false if not
     */
    public boolean moveToLastChild(SortableDefaultMutableTreeNode targetNode) {
        if (this.isRoot()) {
            LOGGER.fine("You can't move the root node to be a child of another node! Aborting move.");
            return false;
        }
        if (!targetNode.getAllowsChildren()) {
            LOGGER.fine("You can't move a node onto a node that doesn't allow child nodes.");
            return false;
        }

        synchronized (targetNode.getRoot()) {
            this.removeFromParent();
            targetNode.add(this);
        }

        getPictureCollection().setUnsavedUpdates();
        return true;
    }

    /**
     * Method that moves the node to the spot before the indicated node
     *
     * @param targetNode The before which you wish to insert the node to
     * @return true if the move was successful, false if not
     */
    public boolean moveBefore(SortableDefaultMutableTreeNode targetNode) {
        if (isNodeDescendant(targetNode)) {
            LOGGER.fine("Can't move to a descendant node. Aborting move.");
            return false;
        }

        if (targetNode.isRoot()) {
            LOGGER.fine("You can't move anything in front of the the root node! Aborting move.");
            return false;
        }

        synchronized (targetNode.getRoot()) {
            SortableDefaultMutableTreeNode targetParentNode = targetNode.getParent();
            int targetIndex = targetParentNode.getIndex(targetNode);
            return moveToIndex(targetParentNode, targetIndex);
        }
    }

    /**
     * Method that moves the node to the specified index
     *
     * @param parentNode The parent node that will get the child
     * @param index      the position at which to insert
     * @return true if the move was successful, false if not
     */
    public boolean moveToIndex(SortableDefaultMutableTreeNode parentNode,
                               int index) {
        if (isNodeDescendant(parentNode)) {
            LOGGER.fine("Can't move to a descendant node. Aborting move.");
            return false;
        }

        int offset = 0;
        if (this.getParent() != null) {
            if (this.getParent().equals(parentNode) && (this.getParent().getIndex(this) < index)) {
                // correct the index because the poll will take away one slot
                offset = -1;
            }
            this.removeFromParent();
        }
        parentNode.insert(this, index + offset);
        getPictureCollection().setUnsavedUpdates();
        return true;
    }

    /**
     * Informs whether this node allows children. If the node holds a
     * PictureInfo it does not allow child nodes, if it holds a GroupInfo, it
     * does.
     *
     * @return true if child nodes are allowed, false if not
     */
    @Override
    public boolean getAllowsChildren() {
        if (userObject != null) {
            if (userObject instanceof PictureInfo) {
                return false;
            } else if (userObject instanceof GroupInfo) {
                return true;
            }
        }
        return super.getAllowsChildren();
    }

    /*
     * Copies the pictures from the source tree to the target directory and adds
     * them to the collection creating a progress GUI.
     *
     * @param sourceDir          The source directory for the pictures
     * @param targetDir          The target directory for the pictures
     * @param groupName          the new name for the group
     * @param newOnly            If true only pictures not yet in the collection will be
     *                           added.
     * @param retainDirectories  indicates that the directory structure should be
     *                           preserved.
     * @param selectedCategories the categories to be applied to the newly
     *                           loaded pictures.
     * @return the new group
     *
    public SortableDefaultMutableTreeNode copyAddPictures(File sourceDir,
                                                          File targetDir, String groupName, boolean newOnly,
                                                          boolean retainDirectories, HashSet<Object> selectedCategories) {
        File[] files = sourceDir.listFiles();
        ProgressGui progGui = new ProgressGui(Tools.countfiles(files),
                Settings.jpoResources.getString("PictureAdderProgressDialogTitle"),
                Settings.jpoResources.getString("picturesAdded"));

        SortableDefaultMutableTreeNode newGroup
                = new SortableDefaultMutableTreeNode(
                new GroupInfo(groupName));

        getPictureCollection().setSendModelUpdates(false);
        boolean picturesAdded = copyAddPictures1(Objects.requireNonNull(files), targetDir, newGroup, progGui, newOnly, retainDirectories, selectedCategories);
        progGui.switchToDoneMode();
        getPictureCollection().setSendModelUpdates(true);
        if (picturesAdded) {
            add(newGroup);
        } else {
            newGroup = null;
        }
        return newGroup;
    }*/

    /**
     * Copies the pictures from the source tree to the target directory and adds
     * them to the collection. This method does the actual loop.
     *
     * @param files              Files to add
     * @param targetDir          Target Directory
     * @param receivingNode      Receiving Node
     * @param progGui            Progress GUI
     * @param newOnly            whether to add only new pictures
     * @param retainDirectories  Whether to retain the directory structure
     * @param selectedCategories Categories to add
     * @return true if pictures were added, false if not.
     */
    protected static boolean copyAddPictures1(File[] files,
                                              File targetDir,
                                              SortableDefaultMutableTreeNode receivingNode,
                                              ProgressGui progGui,
                                              boolean newOnly,
                                              boolean retainDirectories,
                                              HashSet<Object> selectedCategories) {

        LOGGER.info(String.format("Copying %d files from directory %s to node %s", files.length + 1, targetDir.toString(), receivingNode.toString()));
        boolean picturesAdded = false;
        // add all the files from the array as nodes to the start node.
        for (int i = 0;
             (i < files.length) && (!progGui.getInterruptSemaphore().getShouldInterrupt()); i++) {
            File addFile = files[i];
            if (!addFile.isDirectory()) {
                File targetFile = Tools.inventPicFilename(targetDir, addFile.getName());
                long crc = copyPicture(addFile, targetFile);
                if (newOnly && Settings.getPictureCollection().isInCollection(crc)) {
                    targetFile.delete();
                    progGui.decrementTotal();
                } else {
                    receivingNode.addPicture(targetFile, selectedCategories);
                    progGui.progressIncrement();
                    picturesAdded = true;
                }
            } else if (Tools.hasPictures(addFile)) {
                SortableDefaultMutableTreeNode subNode;
                if (retainDirectories) {
                    subNode = receivingNode.addGroupNode(addFile.getName());
                } else {
                    subNode = receivingNode;
                }
                boolean a = copyAddPictures1(Objects.requireNonNull(addFile.listFiles()), targetDir, subNode, progGui, newOnly, retainDirectories, selectedCategories);
                picturesAdded = a || picturesAdded;
            } else {
                LOGGER.log(Level.INFO, "No pictures in directory {0}", addFile.toString());
            }
        }
        return picturesAdded;
    }

    /**
     * Copy any file from sourceFile source File to sourceFile target File
     * location.
     *
     * @param sourceFile the source file location
     * @param targetFile the target file location
     * @return The crc of the copied picture.
     */
    public static long copyPicture(File sourceFile, File targetFile) {
        LOGGER.fine(String.format("Copying file %s to file %s", sourceFile.toString(), targetFile.toString()));
        try (
                InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(targetFile)) {

            BufferedInputStream bin = new BufferedInputStream(in);
            BufferedOutputStream bout = new BufferedOutputStream(out);

            return copyBufferedStream(bin, bout);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    Settings.anchorFrame,
                    Settings.jpoResources.getString("copyPictureError1")
                            + sourceFile.toString()
                            + Settings.jpoResources.getString("copyPictureError2")
                            + targetFile.toString()
                            + Settings.jpoResources.getString("copyPictureError3")
                            + e.getMessage(),
                    Settings.jpoResources.getString("genericError"),
                    JOptionPane.ERROR_MESSAGE);
            return Long.MIN_VALUE;
        }
    }

    /*
     * Copies the pictures from the source tree to the target directory and adds
     * them to the collection only if they have not been seen by the camera
     * before.
     *
     * @param sourceDir          source directory
     * @param targetDir          target directory
     * @param cam                The camera object with knows the checksums of the pictures
     *                           seen before.
     * @param groupName          name of the new group
     * @param retainDirectories  whether to retain the directory structure
     * @param selectedCategories categories to apply
     * @return the new group
     *
    public SortableDefaultMutableTreeNode copyAddPictures(File sourceDir,
                                                          File targetDir, String groupName, Camera cam,
                                                          boolean retainDirectories, HashSet<Object> selectedCategories) {
        File[] files = sourceDir.listFiles();
        ProgressGui progGui = new ProgressGui(Tools.countfiles(files),
                Settings.jpoResources.getString("PictureAdderProgressDialogTitle"),
                Settings.jpoResources.getString("picturesAdded"));
        SortableDefaultMutableTreeNode newGroup
                = new SortableDefaultMutableTreeNode(
                new GroupInfo(groupName));

        getPictureCollection().setSendModelUpdates(false);

        cam.zapNewImage();
        boolean picturesAdded = copyAddPictures1(Objects.requireNonNull(files), targetDir, newGroup, progGui, cam, retainDirectories, selectedCategories);

        cam.storeNewImage();
        Settings.writeCameraSettings();

        getPictureCollection().setSendModelUpdates(true);
        progGui.switchToDoneMode();
        if (picturesAdded) {
            add(newGroup);
        } else {
            newGroup = null;
        }
        return newGroup;
    }*/

    /**
     * Copies the pictures from the source File collection into the target node
     *
     * @param newPictures A Collection framework of the new picture Files
     * @param targetDir   The target directory for the copy operation
     * @param copyMode    Set to true if you want to copy, false if you want to
     *                    move the pictures.
     * @param progressBar The optional progressBar that should be incremented.
     */
    public void copyAddPictures(Collection<File> newPictures, File targetDir,
                                boolean copyMode, final JProgressBar progressBar) {
        LOGGER.fine(String.format("Copy/Moving %d pictures to target directory %s", newPictures.size(), targetDir.toString()));
        getPictureCollection().setSendModelUpdates(false);
        for (File file : newPictures) {
            LOGGER.fine(String.format("Processing file %s", file.toString()));
            if (progressBar != null) {
                SwingUtilities.invokeLater(
                        () -> progressBar.setValue(progressBar.getValue() + 1)
                );
            }
            File targetFile = Tools.inventPicFilename(targetDir, file.getName());
            LOGGER.fine(String.format("Target file name chosen as: %s", targetFile.toString()));
            copyPicture(file, targetFile);

            if (!copyMode) {
                file.delete();
            }
            addPicture(targetFile, null);
        }
        getPictureCollection().setSendModelUpdates(true);
    }

    /**
     * Copies the pictures from the source tree to the target directory and adds
     * them to the collection. This method does the actual loop.
     *
     * @param files              The files to copy
     * @param targetDir          The target directory
     * @param receivingNode      The node to which to add them
     * @param progGui            A Progress GUI
     * @param cam                A camera
     * @param retainDirectories  Whether to retain directories
     * @param selectedCategories Selected categories
     * @return true if OK, false if not
     */
    protected static boolean copyAddPictures1(File[] files,
                                              File targetDir,
                                              SortableDefaultMutableTreeNode receivingNode,
                                              ProgressGui progGui,
                                              Camera cam,
                                              boolean retainDirectories,
                                              HashSet<Object> selectedCategories) {

        boolean picturesAdded = false;
        // add all the files from the array as nodes to the start node.
        for (int i = 0;
             (i < files.length) && (!progGui.getInterruptSemaphore().getShouldInterrupt()); i++) {
            File addFile = files[i];
            if (!addFile.isDirectory()) {
                if (cam.getUseFilename() && cam.inOldImage(addFile)) {
                    // ignore image if the filename is known
                    cam.copyToNewImage(addFile); // put it in the known pictures Hash
                    progGui.decrementTotal();
                } else {
                    File targetFile = Tools.inventPicFilename(targetDir, addFile.getName());
                    long crc = copyPicture(addFile, targetFile);
                    cam.storePictureNewImage(addFile, crc); // remember it next time
                    if (cam.inOldImage(crc)) {
                        targetFile.delete();
                        progGui.decrementTotal();
                    } else {
                        receivingNode.addPicture(targetFile, selectedCategories);
                        progGui.progressIncrement();
                        picturesAdded = true;
                    }
                }
            } else if (Tools.hasPictures(addFile)) {
                SortableDefaultMutableTreeNode subNode;
                if (retainDirectories) {
                    subNode = receivingNode.addGroupNode(addFile.getName());
                } else {
                    subNode = receivingNode;
                }
                boolean a = copyAddPictures1(Objects.requireNonNull(addFile.listFiles()), targetDir, subNode, progGui, cam, retainDirectories, selectedCategories);
                picturesAdded = a || picturesAdded;
            } else {
                LOGGER.log(Level.INFO, "No pictures in directory {0}", addFile.toString());
            }
        }
        return picturesAdded;
    }

    /**
     * Creates and add a new picture node to the current node from an image
     * file.
     *
     * @param addFile            the file of the picture that should be added
     * @param newOnly            flag whether to check if the picture is in the collection
     *                           already; if true will only add the picture if its not yet included
     * @param selectedCategories selected categories
     * @return true if the node was added, false if not.
     */
    public boolean addSinglePicture(File addFile, boolean newOnly,
                                    HashSet<Object> selectedCategories) {
        LOGGER.fine(String.format("Adding File: %s, NewOnly: %b to node %s", addFile.toString(), newOnly, toString()));
        if (newOnly && getPictureCollection().isInCollection(addFile)) {
            return false; // only add pics not in the collection already
        } else {
            return addPicture(addFile, selectedCategories);
        }
    }

    /**
     * this method adds a new Picture to the current node if the JVM has a
     * reader for it.
     *
     * @param addFile            the file that should be added
     * @param categoryAssignment Can be null
     * @return true if the picture was valid, false if not.
     */
    public boolean addPicture(File addFile, HashSet<Object> categoryAssignment) {
        LOGGER.fine(String.format("Adding file %s to the node %s", addFile.toString(), toString()));
        PictureInfo newPictureInfo = new PictureInfo();

        if (!SourcePicture.jvmHasReader(addFile)) {
            LOGGER.info(String.format("Not adding file %s because the Java Virtual Machine has not got a reader for the file.", addFile.toString()));
            return false; // don't add if there is no reader.
        }
        newPictureInfo.setImageLocation(addFile);
        newPictureInfo.setDescription(FilenameUtils.getBaseName(addFile.getName()));
        newPictureInfo.setChecksum(Tools.calculateChecksum(addFile));
        if (categoryAssignment != null) {
            newPictureInfo.setCategoryAssignment(categoryAssignment);
        }
        SortableDefaultMutableTreeNode newNode = new SortableDefaultMutableTreeNode(newPictureInfo);

        this.add(newNode);
        getPictureCollection().setUnsavedUpdates();

        ExifInfo exifInfo = new ExifInfo(newPictureInfo.getImageFile());
        exifInfo.decodeExifTags();
        newPictureInfo.setCreationTime(exifInfo.getCreateDateTime());
        newPictureInfo.setLatLng(exifInfo.latLng);
        newPictureInfo.setRotation(exifInfo.rotation);

        return true;
    }

    /**
     * This method returns whether the supplied node is a descendant of the
     * deletions that have been detected in the TreeModelListener delivered
     * TreeModelEvent.
     *
     * @param affectedNode The node to check whether it is or is a descendant of
     *                     the deleted node.
     * @param e            the TreeModelEvent that was detected
     * @return true if successful, false if not
     */
    public static boolean wasNodeDeleted(
            SortableDefaultMutableTreeNode affectedNode, TreeModelEvent e) {
        //logger.info( "SDMTN.wasNodeDeleted invoked for: " + affectedNode.toString() + " / " + e.toString() );
        TreePath removedChild;
        TreePath currentNodeTreePath = new TreePath(affectedNode.getPath());
        Object[] children = e.getChildren();
        for (Object child : children) {
            removedChild = new TreePath(child);
            if (removedChild.isDescendant(currentNodeTreePath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SortableDefaultMutableTreeNode getParent() {
        return (SortableDefaultMutableTreeNode) super.getParent();
    }

    @Override
    public SortableDefaultMutableTreeNode getRoot() {
        return (SortableDefaultMutableTreeNode) super.getRoot();
    }

}
