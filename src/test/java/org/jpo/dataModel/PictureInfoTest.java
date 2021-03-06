package org.jpo.dataModel;

import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.*;

/*
 Copyright (C) 2017 - 2019 Richard Eigenmann.
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
        PictureInfo pi = new PictureInfo(new File("c:\\picture.jpg"), "My Sample Picture");
        assertEquals("Should return the description", "My Sample Picture", pi.toString());
    }

    /**
     * Test of getDescription method, of class PictureInfo.
     */
    @Test
    public void testGetDescription() {
        PictureInfo pi = new PictureInfo(new File("c:\\picture.jpg"), "My Sample Picture");
        assertEquals("Should return the description", "My Sample Picture", pi.getDescription());
    }

    private int changeEvents;

    /**
     * Test of setDescription method, of class PictureInfo.
     */
    @Test
    public void testSetDescription() {
        PictureInfo pi = new PictureInfo();
        changeEvents = 0;
        PictureInfoChangeListener picl = (PictureInfoChangeEvent arg0) -> changeEvents += 1;
        pi.addPictureInfoChangeListener(picl);
        pi.setDescription("A description");
        assertEquals("Expecting what went in to come out", "A description", pi.getDescription());
        assertEquals("Expecting 1 change event", 1, changeEvents);
        pi.setDescription("A different description");
        assertEquals("Expecting what went in to come out", "A different description", pi.getDescription());
        assertEquals("Expecting a second change event", 2, changeEvents);
    }

    private int countEvents;

    /**
     * Test Description change event
     */
    @Test
    public void testSetDescriptionSame() {
        PictureInfo pi = new PictureInfo();
        countEvents = 0;
        PictureInfoChangeListener picl = (PictureInfoChangeEvent arg0) -> countEvents += 1;
        pi.addPictureInfoChangeListener(picl);
        pi.setDescription("A picture description");
        assertEquals("Expecting what went in to come out", "A picture description", pi.getDescription());
        assertEquals("Expecting 1 change event", 1, countEvents);
        pi.setDescription("A picture description");
        assertEquals("Expecting what went in to come out", "A picture description", pi.getDescription());
        assertEquals("Expecting no new change event because it was the same that went in", 1, countEvents);
    }

    /**
     * Test of appendToDescription method, of class PictureInfo.
     */
    @Test
    public void testAppendToDescription() {
        PictureInfo pi = new PictureInfo();
        pi.setDescription("A picture description");
        pi.appendToDescription(" concatenated from two texts");
        assertEquals("Expecting that the description concatenated", "A picture description concatenated from two texts", pi.getDescription());
    }

    /**
     * Test of descriptionContains method, of class PictureInfo.
     */
    @Test
    public void testDescriptionContains() {
        PictureInfo pi = new PictureInfo();
        pi.setDescription("A picture of a big town at sunset");
        assertTrue("Expecting to find a substring", pi.descriptionContains("town"));
    }

    /**
     * Test of getImageLocation method, of class PictureInfo.
     */
    @Test
    public void testGetImageLocation() {
        PictureInfo pi = new PictureInfo( new File("/dir/picture.jpg"), "My Sample Picture");
        String highresLocation = pi.getImageLocation();
        assertEquals("file:/dir/picture.jpg", highresLocation);
    }

    /**
     * Test of getImageFile method, of class PictureInfo.
     */
    @Test
    public void testGetImageFile() {
        PictureInfo pi = new PictureInfo(new File("/dir/picture.jpg"), "My Sample Picture");
        File f = pi.getImageFile();
        assertEquals("Checking getHighresFile", new File("/dir/picture.jpg"), f);
    }

    /**
     * Test of getImageURIOrNull method, of class PictureInfo.
     */
    @Test
    @Ignore ("Was Deprecated")
    public void testGetImageURIOrNull() {
        PictureInfo pi = new PictureInfo();
        String goodLocation = "/image.jpg";
        pi.setImageLocation(new File(goodLocation));
        URI pulledUri = pi.getImageURIOrNull();
        assertEquals("file:" + goodLocation, pulledUri.toString());

        PictureInfo pi2 = new PictureInfo();
        URI nullUri = pi2.getImageURIOrNull();
        assertEquals("", nullUri.toString());

        PictureInfo pi3 = new PictureInfo();
        String badLocation = "?äöü&~`";
        pi3.setImageLocation(new File(badLocation));
        URI nullUri2 = pi3.getImageURIOrNull();
        assertNull(nullUri2);

    }

    @Test
    public void testSetImageLocationString() {
        PictureInfo pi = new PictureInfo();
        pi.setImageLocation(new File ("/dir/picture.jpg"));
        File f = pi.getImageFile();
        assertEquals("Testing what went in comes out", f.toString(), "/dir/picture.jpg");
    }

    @Test
    public void testSetImageLocationWithSpace() {
        PictureInfo pi = new PictureInfo();
        pi.setImageLocation(new File("/dir/picture file.jpg"));
        File f = pi.getImageFile();
        assertEquals("Testing what went in comes out", f.toString(), "/dir/picture file.jpg");
    }

    /**
     * Test of setImageLocation method, of class PictureInfo.
     */
    @Test
    public void testSetImageLocationUrl() {
        try {
            PictureInfo pi = new PictureInfo();
            pi.setImageLocation(new File(new URL("file:///dir/picture.jpg").toURI()));
            File f = pi.getImageFile();
            assertEquals("Testing that the Highres Location was memorised correctly", f.toString(), "/dir/picture.jpg");
        } catch (MalformedURLException | URISyntaxException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test of appendToImageLocation method, of class PictureInfo.
     */
    @Test
    public void testAppendToImageLocation() {
        PictureInfo pi = new PictureInfo();
        pi.appendToImageLocation("file:///dir/picture");
        pi.appendToImageLocation(".jpg");
        File f = pi.getImageFile();
        assertEquals("Testing that the Highres Location was memorised correctly", f.toString(), "/dir/picture.jpg");
    }

    /**
     * Test of getImageFilename method, of class PictureInfo.
     */
    @Test
    public void testGetHighresFilename() {
        PictureInfo pi = new PictureInfo();
        pi.setImageLocation(new File ("/dir/picture.jpg"));
        String filename = pi.getImageFile().getName();
        assertEquals("Testing that the filename can be derived from the Highres Location correctly", filename, "picture.jpg");
    }

    /**
     * A dumb PictureInfoChangeListener that only counts the events received
     */
    private final PictureInfoChangeListener pictureInfoChangeListener = new PictureInfoChangeListener() {

        @Override
        public void pictureInfoChangeEvent(PictureInfoChangeEvent pictureInfoChangeEvent) {
            eventsReceived++;
        }
    };

    private int eventsReceived;

    /**
     * Test the change listener
     */
    @Test
    public void testPictureInfoChangeListener() {
        eventsReceived = 0;
        PictureInfo pi = new PictureInfo();
        assertEquals("To start off there should be no events", 0, eventsReceived);
        pi.setDescription("Step 1");
        assertEquals("There is no listener attached so there is no event", 0, eventsReceived);
        pi.addPictureInfoChangeListener(pictureInfoChangeListener);
        pi.setDescription("Step 2");
        assertEquals("The listener should have fired and we should have 1 event", 1, eventsReceived);
        pi.removePictureInfoChangeListener(pictureInfoChangeListener);
        pi.setDescription("Step 3");
        assertEquals("The detached listener should not have fired", 1, eventsReceived);
    }

    /**
     * Test dumpToXml
     */
    @Test
    public void testDumpToXml() {
        URL u = Objects.requireNonNull(PictureInfoTest.class.getClassLoader().getResource("exif-test-canon-eos-350d.jpg"));
        final PictureInfo pi = new PictureInfo(new File(u.getFile()), "First <Picture> & difficult xml chars ' \"");
        pi.setComment("Comment <<&>'\">");
        pi.setFilmReference("Reference <<&>'\">");
        pi.setRotation(45.1);
        pi.setPhotographer("Richard Eigenmann <<&>'\">");
        pi.setLatLng("22.67x33.89");
        pi.setCopyrightHolder("Sandra Keller <<&>'\">");
        pi.addCategoryAssignment("1");
        pi.setChecksum(1234);

        StringWriter sw = new StringWriter();
        try (BufferedWriter bw = new BufferedWriter(sw)) {
            pi.dumpToXml(bw);
        } catch (IOException ex) {
            Logger.getLogger(PictureInfoTest.class.getName()).log(Level.SEVERE, "The dumpToXml should really not throw an IOException", ex);
            fail("Unexpected IOException");
        }

        URL jpgResource = PictureInfoTest.class.getClassLoader().getResource("exif-test-canon-eos-350d.jpg");

        String expected = "<picture>\n"
                + "\t<description><![CDATA[First <Picture> & difficult xml chars ' \"]]></description>\n"
                + "\t<file_URL>"
                + Objects.requireNonNull(jpgResource).toString()
                + "</file_URL>\n"
                + "\t<checksum>1234</checksum>\n"
                + "\t<COMMENT>Comment &lt;&lt;&amp;&gt;&apos;&quot;&gt;</COMMENT>\n"
                + "\t<PHOTOGRAPHER>Richard Eigenmann &lt;&lt;&amp;&gt;&apos;&quot;&gt;</PHOTOGRAPHER>\n"
                + "\t<film_reference>Reference &lt;&lt;&amp;&gt;&apos;&quot;&gt;</film_reference>\n"
                + "\t<COPYRIGHT_HOLDER>Sandra Keller &lt;&lt;&amp;&gt;&apos;&quot;&gt;</COPYRIGHT_HOLDER>\n"
                + "\t<ROTATION>45.100000</ROTATION>\n"
                + "\t<LATLNG>22.670000x33.890000</LATLNG>\n"
                + "\t<categoryAssignment index=\"1\"/>\n"
                + "</picture>\n";

        String result = sw.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testChecksum() {
        PictureInfo pi = new PictureInfo();
        pi.setChecksum(123456789);
        assertEquals(123456789, pi.getChecksum());
        assertEquals("123456789", pi.getChecksumAsString());

        pi.setChecksum(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, pi.getChecksum());
        assertEquals("N/A", pi.getChecksumAsString());
    }

    @Test
    public void testCalculateChecksum() {
        URL image = Objects.requireNonNull(PictureInfoTest.class.getClassLoader().getResource("exif-test-canon-eos-350d.jpg"));
        PictureInfo pi = null;
        try {
            pi = new PictureInfo(new File(image.toURI()), "Sample Picture");
        } catch (URISyntaxException e) {
            fail(e.getMessage());
        }
        assertEquals("N/A", pi.getChecksumAsString());
        pi.calculateChecksum();
        assertEquals("778423829", pi.getChecksumAsString());
    }

}
