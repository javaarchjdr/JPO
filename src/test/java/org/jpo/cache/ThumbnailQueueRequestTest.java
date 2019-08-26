package org.jpo.cache;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.jpo.dataModel.SortableDefaultMutableTreeNode;
import org.jpo.gui.ThumbnailController;
import org.jpo.gui.swing.Thumbnail;
import static junit.framework.TestCase.fail;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Richard Eigenmann
 */
public class ThumbnailQueueRequestTest {

    @Test
    public void increasePriorityToTest() {
        // TravisCI runs headless so we can't execute the below test
        if ( GraphicsEnvironment.isHeadless() ) {
            return;
        }

        try {
            SwingUtilities.invokeAndWait( () -> {
                ThumbnailController controller = new ThumbnailController( new Thumbnail(), 350 );
                SortableDefaultMutableTreeNode node = new SortableDefaultMutableTreeNode();
                ThumbnailQueueRequest tqr = new ThumbnailQueueRequest( controller, node, ThumbnailQueueRequest.QUEUE_PRIORITY.LOWEST_PRIORITY, new Dimension( 350, 350 ) );
                Assert.assertEquals( "Priority should be LOWEST_PRIORITY", ThumbnailQueueRequest.QUEUE_PRIORITY.LOWEST_PRIORITY, tqr.priority );
                tqr.increasePriorityTo( ThumbnailQueueRequest.QUEUE_PRIORITY.LOW_PRIORITY );
                Assert.assertEquals( "Priority should have been increased to LOW_PRIORITY", ThumbnailQueueRequest.QUEUE_PRIORITY.LOW_PRIORITY, tqr.priority );
                tqr.increasePriorityTo( ThumbnailQueueRequest.QUEUE_PRIORITY.MEDIUM_PRIORITY );
                Assert.assertEquals( "Priority should have been increased to MEDIUM_PRIORITY", ThumbnailQueueRequest.QUEUE_PRIORITY.MEDIUM_PRIORITY, tqr.priority );
                tqr.increasePriorityTo( ThumbnailQueueRequest.QUEUE_PRIORITY.HIGH_PRIORITY );
                Assert.assertEquals( "Priority should have been increased to HIGH_PRIORITY", ThumbnailQueueRequest.QUEUE_PRIORITY.HIGH_PRIORITY, tqr.priority );
                tqr.increasePriorityTo( ThumbnailQueueRequest.QUEUE_PRIORITY.MEDIUM_PRIORITY );
                Assert.assertEquals( "Priority should stay at HIGH_PRIORITY when increasing to MEDIUM_PRIORITY", ThumbnailQueueRequest.QUEUE_PRIORITY.HIGH_PRIORITY, tqr.priority );
            } );
        } catch ( InterruptedException | InvocationTargetException ex ) {
            Logger.getLogger( ThumbnailQueueRequestTest.class.getName() ).log( Level.SEVERE, null, ex );
            fail( ex.getMessage() );
        }
    }
}