/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpo.dataModel;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.*;

/**
 *
 * @author Richard Eigenmann
 */
public class NodeStatisticsTest{


    private SortableDefaultMutableTreeNode rootNode;

    private final GroupInfo gr1 = new GroupInfo("Group1");

    private SortableDefaultMutableTreeNode group1;

    private final GroupInfo gr2 = new GroupInfo("Group2");

    private SortableDefaultMutableTreeNode group2;

    private final GroupInfo gr3 = new GroupInfo("Group3");

    private SortableDefaultMutableTreeNode group3;

    private final PictureInfo pi1 = new PictureInfo();

    private SortableDefaultMutableTreeNode picture1;

    private final PictureInfo pi2 = new PictureInfo();

    private SortableDefaultMutableTreeNode picture2;

    private final PictureInfo pi3 = new PictureInfo(new File("images/image3.jpg"), "Third Picture");

    private SortableDefaultMutableTreeNode picture3;

    private final PictureInfo pi4 = new PictureInfo(new File("images/image4.jpg"), "Fourth Picture");

    private SortableDefaultMutableTreeNode picture4;

    private final PictureInfo pi5 = new PictureInfo(new File("images/image5.jpg"), "Fifth Picture");

    private SortableDefaultMutableTreeNode picture5;

    /**
     * Set up tests
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        rootNode = new SortableDefaultMutableTreeNode();
        group1 = new SortableDefaultMutableTreeNode(gr1);
        group2 = new SortableDefaultMutableTreeNode(gr2);
        pi1.setImageLocation(new File(NodeStatisticsTest.class.getClassLoader().getResource( "exif-test-canon-eos-350d.jpg" ).toURI()));
        pi1.setDescription( "First Picture");
        pi2.setImageLocation(new File(NodeStatisticsTest.class.getClassLoader().getResource( "exif-test-canon-eos-60d.jpg" ).toURI()));
        pi2.setDescription("Second Picture");
        picture1 = new SortableDefaultMutableTreeNode(pi1);
        picture2 = new SortableDefaultMutableTreeNode(pi2);
        group3 = new SortableDefaultMutableTreeNode(gr3);
        picture3 = new SortableDefaultMutableTreeNode(pi3);
        picture4 = new SortableDefaultMutableTreeNode(pi4);
        picture5 = new SortableDefaultMutableTreeNode(pi5);
        rootNode.add(group1);
        rootNode.add(group2);
        group1.add(picture1);
        group1.add(picture2);
        group1.add(group3);
        group3.add(picture3);
        group2.add(picture4);
        group2.add(picture5);
    }

    /**
     * Test of getNode method, of class NodeStatistics.
     */
    @Test
    public void testSetGetNode() {
        NodeStatistics ns = new NodeStatistics(null);
        assertNull("A null node should return null", ns.getNode());

        NodeStatistics ns1 = new NodeStatistics(rootNode);
        assertEquals("When we set a node it should be the one coming back", rootNode, ns1.getNode());

    }

    /**
     * Test of getNumberOfNodes method, of class NodeStatistics.
     */
    @Test
    public void testGetNumberOfNodes() {
        NodeStatistics ns = new NodeStatistics(null);
        assertEquals("Counting nodes on a null node should return 0 nodes", 0, ns.getNumberOfNodes());

        NodeStatistics ns1 = new NodeStatistics(rootNode);
        assertEquals("Counting number of nodes", 9, ns1.getNumberOfNodes());

    }

    /**
     * Test of getNumberOfGroups method, of class NodeStatistics.
     */
    @Test
    public void testGetNumberOfGroups() {
        NodeStatistics ns = new NodeStatistics(rootNode);
        assertEquals("Counting number of groups", 3, ns.getNumberOfGroups());
    }

    /**
     * Test of getNumberOfPictures method, of class NodeStatistics.
     */
    @Test
    public void testGetNumberOfPictures() {
        NodeStatistics ns = new NodeStatistics(rootNode);
        assertEquals("Counting number of pictures", 5, ns.getNumberOfPictures());
    }

    /**
     * Test of countPictures method, of class NodeStatistics.
     */
    @Test
    public void testCountPictures() {
        assertEquals("Recursive picture count", 3, NodeStatistics.countPictures(group1, true));
        assertEquals("Non Recursive picture count", 2, NodeStatistics.countPictures(group1, false));
    }

    
    /**
     * Test of countPictures with null parameter
     */
    @Test
    public void testCountPicturesNull() {
        assertEquals(0, NodeStatistics.countPictures(null, true));
        assertEquals(0, NodeStatistics.countPictures(null, false));
    }

    
    /**
     * Test sizeOfPictures returns 0 when a null node is sent in
     */
    @Test
    public void testSizeOfPicturesNull() {
        NodeStatistics ns = new NodeStatistics(null);
        try {
            ns.getSizeOfPictures();
        } catch (NullPointerException ex) {
            return;
        }
        fail("Should have thown a NPE!");
    }
    
    /**
     * Test sizeOfPictures on a single PictureInfo Node
     */
    @Test
    public void testSizeOfPicturesPictureInfo() {
        NodeStatistics ns = new NodeStatistics(picture1);
        assertEquals( 3148102, ns.getSizeOfPictures() );
    }

    
        /**
     * Test sizeOfPictures on a GroupInfo Node tree
     */
    @Test
    public void testSizeOfPicturesGroupInfo() {
        NodeStatistics ns = new NodeStatistics(rootNode);
        assertEquals( 9061284, ns.getSizeOfPictures() );
    }

}
