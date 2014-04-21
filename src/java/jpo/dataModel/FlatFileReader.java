package jpo.dataModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JOptionPane;
import jpo.EventBus.AddFlatFileRequest;
import jpo.EventBus.JpoEventBus;
import jpo.EventBus.ShowGroupRequest;

/**
 * Class to import a flat file of pictures into the supplied node
 *
 * @author Richard Eigenmann
 */
public class FlatFileReader {

    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( FlatFileReader.class.getName() );

    /**
     * Constructs a FlatFileReader and imports the pictures listed in the file
     * @param request The request
     */
    public FlatFileReader( AddFlatFileRequest request ) {
        SortableDefaultMutableTreeNode newNode = new SortableDefaultMutableTreeNode(
                new GroupInfo( request.getFile().getName() ) );

        try ( BufferedReader in = new BufferedReader( new FileReader( request.getFile() ) ) ) {
            while ( in.ready() ) {
                String line = in.readLine();
                File testFile;
                LOGGER.info( "Testing file: " + line );
                try {
                    testFile = new File( new URI( line ) );
                } catch ( URISyntaxException | IllegalArgumentException x ) {
                    LOGGER.info( x.getLocalizedMessage() );
                    // The filename might just be a plain filename without URI format try this:
                    testFile = new File (line );
                    line = testFile.toURI().toString();
                }

                if ( !testFile.canRead() ) {
                    LOGGER.info( "Can't read file: " + line );
                    continue;
                }

                try ( FileInputStream fis = new FileInputStream( testFile );
                        ImageInputStream iis = ImageIO.createImageInputStream( fis ); ) {
                    Iterator i = ImageIO.getImageReaders( iis );
                    if ( !i.hasNext() ) {
                        LOGGER.info( "No reader for file: " + line );
                        continue;
                    }
                    LOGGER.info( "I do have a reader for file: " + line );
                } catch ( IOException ex ) {
                    LOGGER.info( ex.getLocalizedMessage() );
                    continue;
                }

                LOGGER.info( "adding file to node: " + line );
                SortableDefaultMutableTreeNode newPictureNode = new SortableDefaultMutableTreeNode(
                        new PictureInfo( line, Tools.stripOutFilenameRoot( testFile ) ) );
                newNode.add( newPictureNode );
            }
            in.close();
            request.getNode().add( newNode );
            request.getNode().getPictureCollection().sendNodeStructureChanged( request.getNode() );
            request.getNode().getPictureCollection().setUnsavedUpdates( false );
            JpoEventBus.getInstance().post( new ShowGroupRequest( newNode ) );
        } catch ( IOException ex ) {
            LOGGER.severe( ex.getLocalizedMessage() );
            JOptionPane.showMessageDialog( Settings.anchorFrame,
                    ex.getLocalizedMessage(),
                    Settings.jpoResources.getString( "genericError" ),
                    JOptionPane.ERROR_MESSAGE );
        }
    }

}
