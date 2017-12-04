package jpo.dataModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import org.junit.Test;

/**
 * Tests for the PictureInfo class
 *
 * @author Richard Eigenmann
 */
public class PictureInfoTest {

    /**
     * Test of toString method, of class PictureInfo.
     */
    @Test
    public void testToString() {
        PictureInfo pi = new PictureInfo( "c:\\picture.jpg", "My Sample Picture" );
        assertEquals( "Should return the description", "My Sample Picture", pi.toString() );
    }

    /**
     * Test of getDescription method, of class PictureInfo.
     */
    @Test
    public void testGetDescription() {
        PictureInfo pi = new PictureInfo( "c:\\picture.jpg", "My Sample Picture" );
        assertEquals( "Should return the description", "My Sample Picture", pi.getDescription() );
    }

    int changeEvents;

    /**
     * Test of setDescription method, of class PictureInfo.
     */
    @Test
    public void testSetDescription() {
        PictureInfo pi = new PictureInfo();
        changeEvents = 0;
        PictureInfoChangeListener picl = ( PictureInfoChangeEvent arg0 ) -> {
            changeEvents += 1;
        };
        pi.addPictureInfoChangeListener( picl );
        pi.setDescription( "Rubbish" );
        assertEquals( "Expecting what went in to come out", "Rubbish", pi.getDescription() );
        assertEquals( "Expecting 1 change event", 1, changeEvents );
        pi.setDescription( "More Rubbish" );
        assertEquals( "Expecting what went in to come out", "More Rubbish", pi.getDescription() );
        assertEquals( "Expecting a second change event", 2, changeEvents );
    }

    int countEvents;

    /**
     * Test Description change event
     */
    @Test
    public void testSetDescriptionSame() {
        PictureInfo pi = new PictureInfo();
        countEvents = 0;
        PictureInfoChangeListener picl = ( PictureInfoChangeEvent arg0 ) -> {
            countEvents += 1;
        };
        pi.addPictureInfoChangeListener( picl );
        pi.setDescription( "Rubbish" );
        assertEquals( "Expecting what went in to come out", "Rubbish", pi.getDescription() );
        assertEquals( "Expecting 1 change event", 1, countEvents );
        pi.setDescription( "Rubbish" );
        assertEquals( "Expecting what went in to come out", "Rubbish", pi.getDescription() );
        assertEquals( "Expecting no new change event because it was the same that went in", 1, countEvents );
    }

    /**
     * Test of appendToDescription method, of class PictureInfo.
     */
    @Test
    public void testAppendToDescription() {
        PictureInfo pi = new PictureInfo();
        pi.setDescription( "Rubbish" );
        pi.appendToDescription( "Bin" );
        assertEquals( "Expecting that the description concatenated", "RubbishBin", pi.getDescription() );
    }

    /**
     * Test of descriptionContains method, of class PictureInfo.
     */
    @Test
    public void testDescriptionContains() {
        PictureInfo pi = new PictureInfo();
        pi.setDescription( "RubbishBinTrash" );
        assertEquals( "Expecting to find a substring", true, pi.descriptionContains( "Bin" ) );
    }

    /**
     * Test of getImageLocation method, of class PictureInfo.
     */
    @Test
    public void testGetHighresLocation() {
        PictureInfo pi = new PictureInfo( "file:///dir/picture.jpg", "My Sample Picture" );
        String highresLocation = pi.getImageLocation();
        assertEquals( "Checking getHighresLocation", "file:///dir/picture.jpg", highresLocation );
    }

    /**
     * Test of getImageFile method, of class PictureInfo.
     */
    @Test
    public void testGetHighresFile() {
        PictureInfo pi = new PictureInfo( "file:///dir/picture.jpg", "My Sample Picture" );
        File highresFile = pi.getImageFile();
        assertEquals( "Checking getHighresFile", new File( "/dir/picture.jpg" ), highresFile );
    }

    /**
     * Test of getImageURL method, of class PictureInfo.
     *
     * @throws Exception
     */
    @Test
    public void testGetHighresURL() throws Exception {
        PictureInfo pi = new PictureInfo( "file://dir/picture.jpg", "My Sample Picture" );
        URL highresURL = pi.getImageURL();
        assertEquals( "Checking getHighresURL", new URL( "file://dir/picture.jpg" ), highresURL );
    }

    /**
     * Test of getImageURLOrNull method, of class PictureInfo.
     */
    @Test
    public void testGetImageURLOrNull() {
        PictureInfo pi1 = new PictureInfo( "file://dir/picture.jpg", "My Sample Picture" );
        URL highresURL1 = pi1.getImageURLOrNull();
        try {
            assertEquals( "Checking getImageURLOrNull", new URL( "file://dir/picture.jpg" ), highresURL1 );
        } catch ( MalformedURLException ex ) {
            fail( "Test should not have thrown an exception: " + ex.getMessage() );
        }

        PictureInfo pi2 = new PictureInfo( "noProtocol://dir/picture.jpg", "My Sample Picture" );
        URL highresURL2 = pi2.getImageURLOrNull();
        assertNull( "Checking getHighresURLOrNull", highresURL2 );
    }

    /**
     * Test of getImageURIOrNull method, of class PictureInfo.
     */
    @Test
    public void testGetImageURIOrNull() {
        PictureInfo pi = new PictureInfo();
        String goodLocation = "file://image.jpg";
        pi.setImageLocation( goodLocation );
        URI pulledUri = pi.getImageURIOrNull();
        assertEquals( goodLocation, pulledUri.toString() );

        PictureInfo pi2 = new PictureInfo();
        URI nullUri = pi2.getImageURIOrNull();
        assertEquals( "", nullUri.toString() );

        PictureInfo pi3 = new PictureInfo();
        String badLocation = "?äöü&~`";
        pi3.setImageLocation( badLocation );
        URI nullUri2 = pi3.getImageURIOrNull();
        assertNull( nullUri2 );

    }

    /**
     * Test of setImageLocation method, of class PictureInfo.
     */
    @Test
    public void testSetHighresLocation_String() {
        PictureInfo pi = new PictureInfo();
        pi.setImageLocation( "file:///dir/picture.jpg" );
        File f = pi.getImageFile();
        assertEquals( "Testing that the Highres Location was memorised correctly", f.toString(), "/dir/picture.jpg" );
    }

    /**
     * Test of setImageLocation method, of class PictureInfo.
     *
     * @throws MalformedURLException
     */
    @Test
    public void testSetHighresLocation_URL() throws MalformedURLException {
        PictureInfo pi = new PictureInfo();
        pi.setImageLocation( new URL( "file:///dir/picture.jpg" ) );
        File f = pi.getImageFile();
        assertEquals( "Testing that the Highres Location was memorised correctly", f.toString(), "/dir/picture.jpg" );
    }

    /**
     * Test of appendToImageLocation method, of class PictureInfo.
     */
    @Test
    public void testAppendToHighresLocation() {
        PictureInfo pi = new PictureInfo();
        pi.setImageLocation( "file:///dir/picture" );
        pi.appendToImageLocation( ".jpg" );
        File f = pi.getImageFile();
        assertEquals( "Testing that the Highres Location was memorised correctly", f.toString(), "/dir/picture.jpg" );
    }

    /**
     * Test of getImageFilename method, of class PictureInfo.
     */
    @Test
    public void testGetHighresFilename() {
        PictureInfo pi = new PictureInfo();
        pi.setImageLocation( "file:///dir/picture.jpg" );
        String filename = pi.getImageFilename();
        assertEquals( "Testing that the filename can be derived from the Highres Location correctly", filename, "picture.jpg" );
    }

    /**
     * A dumb PictureInfoChangeListener that only counts the events received
     */
    PictureInfoChangeListener pictureInfoChangeListener = new PictureInfoChangeListener() {

        @Override
        public void pictureInfoChangeEvent( PictureInfoChangeEvent pice ) {
            eventsReceived++;
        }
    };

    int eventsReceived;

    /**
     * Test the change listener
     */
    @Test
    public void testPictureInfoChangeListener() {
        eventsReceived = 0;
        PictureInfo pi = new PictureInfo();
        assertEquals( "To start off there should be no events", 0, eventsReceived );
        pi.setDescription( "Step 1" );
        assertEquals( "There is no listener attached so there is no event", 0, eventsReceived );
        pi.addPictureInfoChangeListener( pictureInfoChangeListener );
        pi.setDescription( "Step 2" );
        assertEquals( "The listener should have fired and we should have 1 event", 1, eventsReceived );
        pi.removePictureInfoChangeListener( pictureInfoChangeListener );
        pi.setDescription( "Step 3" );
        assertEquals( "The detached listener should not have fired", 1, eventsReceived );
    }

    /**
     * Test dumpToXml
     */
    @Test
    public void testDumpToXml() {
        final PictureInfo pi = new PictureInfo( Settings.CLASS_LOADER.getResource( "exif-test-canon-eos-350d.jpg" ), "First Picture" );
        pi.setComment( "Comment" );
        pi.setFilmReference( "Reference" );
        pi.setRotation( 45.1 );
        pi.setPhotographer( "Richard Eigenmann" );
        pi.setLatLng( "22.67x33.89" );
        pi.setCopyrightHolder( "Sandra Keller" );
        pi.addCategoryAssignment( "1" );
        pi.setChecksum( 1234 );

        StringWriter sw = new StringWriter();
        try (
                //FileWriter sw = new FileWriter( "/tmp/output.xml" );
                BufferedWriter bw = new BufferedWriter( sw ); ) {
            pi.dumpToXml( bw );
        } catch ( IOException ex ) {
            Logger.getLogger( PictureInfoTest.class.getName() ).log( Level.SEVERE, "The dumpToXml should really not throw an IOException", ex );
            fail( "Unexpected IOException" );
        }

        String expected = "<picture>\n"
                + "\t<description><![CDATA[First Picture]]></description>\n"
                + "\t<file_URL>file:" + System.getProperty("user.dir") + File.separator
                + "build/resources/test/exif-test-canon-eos-350d.jpg</file_URL>\n"
                + "\t<checksum>1234</checksum>\n"
                + "\t<COMMENT>Comment</COMMENT>\n"
                + "\t<PHOTOGRAPHER>Richard Eigenmann</PHOTOGRAPHER>\n"
                + "\t<film_reference>Reference</film_reference>\n"
                + "\t<COPYRIGHT_HOLDER>Sandra Keller</COPYRIGHT_HOLDER>\n"
                + "\t<ROTATION>45.100000</ROTATION>\n"
                + "\t<LATLNG>22.670000x33.890000</LATLNG>\n"
                + "\t<categoryAssignment index=\"1\"/>\n"
                + "</picture>\n";

        String result = sw.toString();
        assertEquals( expected, result );
    }

    @Test
    public void testChecksum() {
        PictureInfo pi = new PictureInfo();
        pi.setChecksum( 123456789 );
        assertEquals( 123456789, pi.getChecksum() );
        assertEquals( "123456789", pi.getChecksumAsString() );

        pi.setChecksum( Long.MIN_VALUE );
        assertEquals( Long.MIN_VALUE, pi.getChecksum() );
        assertEquals( "N/A", pi.getChecksumAsString() );
    }

    @Test
    public void testCalculateChecksum() {
        URL image = Settings.CLASS_LOADER.getResource( "exif-test-canon-eos-350d.jpg" );
        PictureInfo pi = new PictureInfo( image, "Sample Picture" );
        assertEquals( "N/A", pi.getChecksumAsString() );
        pi.calculateChecksum();
        assertEquals( "778423829", pi.getChecksumAsString() );
    }

}
