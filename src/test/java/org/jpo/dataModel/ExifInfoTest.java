package org.jpo.dataModel;

import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/*
 ExifInfoTest.java: This class interacts with Drew Noake's library and extracts the Exif information

 Copyright (C) 2013-2019  Richard Eigenmann.
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
 * JUnit tests for the ExifInfo class that calls Drew Noake's exif extraction
 * library
 *
 * @author Richard Eigenmann
 */
public class ExifInfoTest {


    /**
     * A handy reference to 0 in the form of a double
     */
    private static final double ZERO = 0;

    /**
     * That the Exif reader gracefully handles a null URL
     */
    @Test
    public void testExifInfoNull() {
        ExifInfo exifInfo = new ExifInfo( null );
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "", exifInfo.aperture );
        assertEquals( "Camera parsing verification", "", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "", exifInfo.getCreateDateTime() );
        assertEquals( "ExifHeight parsing verification", "", exifInfo.exifHeight );
        assertEquals( "ExifWidth parsing verification", "", exifInfo.exifWidth );
        assertEquals( "FocalLength parsing verification", "", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
        assertEquals( "ShutterSpeed parsing verification", "", exifInfo.shutterSpeed );
        assertEquals( "Rotation parsing verification", 0, exifInfo.rotation );
    }

    /**
     * Test that we get the correct data off a Nikon D100 image
     */
    @Test
    public void testExifInfoD100() {
        final String NIKON_D100_IMAGE = "exif-test-nikon-d100-1.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( NIKON_D100_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + NIKON_D100_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI()) );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + NIKON_D100_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "f/11.0", exifInfo.aperture );
        assertEquals( "Camera parsing verification", "NIKON D100", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2008:11:07 16:23:25", exifInfo.getCreateDateTime() );
        assertEquals( "ExifHeight parsing verification", "233 pixels", exifInfo.exifHeight );
        assertEquals( "ExifWidth parsing verification", "350 pixels", exifInfo.exifWidth );
        assertEquals( "FocalLength parsing verification", "82 mm", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "ISO 640", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "24-120mm f/3.5-5.6", exifInfo.lens );
        assertEquals( "ShutterSpeed parsing verification", "1/750 sec", exifInfo.shutterSpeed );
        assertEquals( "Rotation parsing verification", 0, exifInfo.rotation );
    }

    /**
     * Test that we get the correct data off a Samsung Galaxy S4 image which
     * doesn't have location info
     */
    @Test
    public void testExifInfoS4() {
        final String SAMSUNG_S4_IMAGE = "exif-test-samsung-s4.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( SAMSUNG_S4_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + SAMSUNG_S4_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI()) );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + SAMSUNG_S4_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "f/2.2", exifInfo.aperture );
        assertEquals( "Camera parsing verification", "GT-I9505", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2013:08:27 23:37:56", exifInfo.getCreateDateTime() );
        assertEquals( "ExifHeight parsing verification", "1836 pixels", exifInfo.exifHeight );
        assertEquals( "ExifWidth parsing verification", "3264 pixels", exifInfo.exifWidth );
        assertEquals( "FocalLength parsing verification", "4.2 mm", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "50", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
        assertEquals( "ShutterSpeed parsing verification", "1/1883 sec", exifInfo.shutterSpeed );
        assertEquals( "Rotation parsing verification", 0, exifInfo.rotation );
    }

    /**
     * Test that we get the correct data off a Samsung Galaxy S4 image which
     * doesn't have location info
     */
    @Test
    public void testExifInfoS4Loc() {
        final String SAMSUNG_S4_LOC_IMAGE = "exif-test-samsung-s4-loc.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( SAMSUNG_S4_LOC_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + SAMSUNG_S4_LOC_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI()) );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + SAMSUNG_S4_LOC_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "f/2.2", exifInfo.aperture );
        assertEquals( "Camera parsing verification", "GT-I9505", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2014:01:15 12:11:46", exifInfo.getCreateDateTime() );
        assertEquals( "ExifHeight parsing verification", "1836 pixels", exifInfo.exifHeight );
        assertEquals( "ExifWidth parsing verification", "3264 pixels", exifInfo.exifWidth );
        assertEquals( "FocalLength parsing verification", "4.2 mm", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "50", exifInfo.iso );
        assertEquals( "Longitude parsing verification", 46.80724713888888, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", 9.812583916666668, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
        assertEquals( "ShutterSpeed parsing verification", "1/1879 sec", exifInfo.shutterSpeed );
        assertEquals( "Rotation parsing verification", 0, exifInfo.rotation );
    }

    /**
     * Test that we get the correct rotation data off a Samsung Galaxy S4 image
     * which was not rotated
     */
    @Test
    public void testExifInfoS4Rot0() {
        final String SAMSUNG_S4_ROT0_IMAGE = "exif-test-samsung-s4-rotation-0.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( SAMSUNG_S4_ROT0_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + SAMSUNG_S4_ROT0_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI()) );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + SAMSUNG_S4_ROT0_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "", exifInfo.aperture );
        assertEquals( "Camera parsing verification", "GT-I9505", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2014:04:21 09:30:13", exifInfo.getCreateDateTime() );
        assertEquals( "ExifWidth parsing verification", "2048 pixels", exifInfo.exifWidth );
        assertEquals( "ExifHeight parsing verification", "1152 pixels", exifInfo.exifHeight );
        assertEquals( "FocalLength parsing verification", "", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
        assertEquals( "ShutterSpeed parsing verification", "", exifInfo.shutterSpeed );
        assertEquals( "Rotation parsing verification", 0, exifInfo.rotation );
    }

    /**
     * Test that we get the correct rotation data off a Samsung Galaxy S4 image
     * which was rotated left
     */
    @Test
    public void testExifInfoS4RotLeft() {
        final String SAMSUNG_S4_ROT_LEFT_IMAGE = "exif-test-samsung-s4-rotation-left.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( SAMSUNG_S4_ROT_LEFT_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + SAMSUNG_S4_ROT_LEFT_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI()) );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + SAMSUNG_S4_ROT_LEFT_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "", exifInfo.aperture );
        assertEquals( "Camera parsing verification", "GT-I9505", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2014:04:21 09:30:22", exifInfo.getCreateDateTime() );
        assertEquals( "ExifWidth parsing verification", "2048 pixels", exifInfo.exifWidth );
        assertEquals( "ExifHeight parsing verification", "1152 pixels", exifInfo.exifHeight );
        assertEquals( "FocalLength parsing verification", "", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
        assertEquals( "ShutterSpeed parsing verification", "", exifInfo.shutterSpeed );
        assertEquals( "Rotation parsing verification", 90, exifInfo.rotation );
    }

    /**
     * Test that we get the correct rotation data off a Samsung Galaxy S4 image
     * which was rotated left
     */
    @Test
    public void testExifInfoS4RotRight() {
        final String SAMSUNG_S4_ROT_RIGHT_IMAGE = "exif-test-samsung-s4-rotation-right.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( SAMSUNG_S4_ROT_RIGHT_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + SAMSUNG_S4_ROT_RIGHT_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI()) );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + SAMSUNG_S4_ROT_RIGHT_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "", exifInfo.aperture );
        assertEquals( "Camera parsing verification", "GT-I9505", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2014:04:21 09:30:39", exifInfo.getCreateDateTime() );
        assertEquals( "ExifWidth parsing verification", "2048 pixels", exifInfo.exifWidth );
        assertEquals( "ExifHeight parsing verification", "1152 pixels", exifInfo.exifHeight );
        assertEquals( "FocalLength parsing verification", "", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
        assertEquals( "ShutterSpeed parsing verification", "", exifInfo.shutterSpeed );
        assertEquals( "Rotation parsing verification", 270, exifInfo.rotation );
    }

    /**
     * Test that we get the correct rotation data off a Samsung Galaxy S4 image
     * which was rotated upside down
     */
    @Test
    public void testExifInfoS4RotUpsideDown() {
        final String SAMSUNG_S4_ROT_UPSIDEDOWN_IMAGE = "exif-test-samsung-s4-upside-down.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( SAMSUNG_S4_ROT_UPSIDEDOWN_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + SAMSUNG_S4_ROT_UPSIDEDOWN_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI()) );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + SAMSUNG_S4_ROT_UPSIDEDOWN_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "", exifInfo.aperture );
        assertEquals( "Camera parsing verification", "GT-I9505", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2014:04:21 09:30:29", exifInfo.getCreateDateTime() );
        assertEquals( "ExifWidth parsing verification", "2048 pixels", exifInfo.exifWidth );
        assertEquals( "ExifHeight parsing verification", "1152 pixels", exifInfo.exifHeight );
        assertEquals( "FocalLength parsing verification", "", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
        assertEquals( "ShutterSpeed parsing verification", "", exifInfo.shutterSpeed );
        assertEquals( "Rotation parsing verification", 180, exifInfo.rotation );
    }

    /**
     * Tests for images from an Canon Eos 350d
     */
    @Test
    public void testExifInfoEos350d() {
        final String CANON_EOS350D_IMAGE = "exif-test-canon-eos-350d.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( CANON_EOS350D_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + CANON_EOS350D_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI()) );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + CANON_EOS350D_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "f/20.0", exifInfo.aperture );
        assertEquals( "ShutterSpeed parsing verification", "1/200 sec", exifInfo.shutterSpeed );
        assertEquals( "Camera parsing verification", "Canon EOS 350D DIGITAL", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2006:10:06 15:13:54", exifInfo.getCreateDateTime() );
        assertEquals( "ExifHeight parsing verification", "1664 pixels", exifInfo.exifHeight );
        assertEquals( "ExifWidth parsing verification", "2496 pixels", exifInfo.exifWidth );
        assertEquals( "FocalLength parsing verification", "18 mm", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "200", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        //TODO exiftool finds the lens but ExifInfo doesn't
        //assertEquals("Lens parsing verification", "18.0 - 55.0 mm (35 mm equivalent: 29.2 - 89.2 mm)", exifInfo.lens);
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
    }

    /**
     * Tests for images from a Canon Eos 60D
     */
    @Test
    public void testExifInfoCanonEos60D() {
        final String CANON_EOS60D_IMAGE = "exif-test-canon-eos-60d.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( CANON_EOS60D_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + CANON_EOS60D_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI()) );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + CANON_EOS60D_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "f/11.3", exifInfo.aperture );
        assertEquals( "ShutterSpeed parsing verification", "1/511 sec", exifInfo.shutterSpeed );
        assertEquals( "Camera parsing verification", "Canon EOS 60D", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2013:05:09 13:55:16", exifInfo.getCreateDateTime() );
        assertEquals( "ExifHeight parsing verification", "3456 pixels", exifInfo.exifHeight );
        assertEquals( "ExifWidth parsing verification", "5184 pixels", exifInfo.exifWidth );
        assertEquals( "FocalLength parsing verification", "85 mm", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "400", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "EF-S17-85mm f/4-5.6 IS USM", exifInfo.lens );

        assertEquals( "Rotation parsing verification", 0, exifInfo.rotation );
    }

    /**
     * Tests for images from an Canon Cybershot
     */
    @Test
    public void testExifInfoCybershot1() {
        final String CANON_CYBERSHOT1_IMAGE = "exif-test-sony-cybershot-1.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( CANON_CYBERSHOT1_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + CANON_CYBERSHOT1_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI() ));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + CANON_CYBERSHOT1_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "f/4.0", exifInfo.aperture );
        assertEquals( "ShutterSpeed parsing verification", "1/480 sec", exifInfo.shutterSpeed );
        assertEquals( "Camera parsing verification", "CYBERSHOT", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2002:02:20 16:17:37", exifInfo.getCreateDateTime() );
        assertEquals( "ExifHeight parsing verification", "768 pixels", exifInfo.exifHeight );
        assertEquals( "ExifWidth parsing verification", "1024 pixels", exifInfo.exifWidth );
        assertEquals( "FocalLength parsing verification", "6.1 mm", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "80", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
        assertEquals( "Rotation parsing verification", 0, exifInfo.rotation );
    }

    /**
     * Tests for a Sony D700
     */
    @Test
    public void testExifInfoSonyD700() {
        final String SONY_D700_IMAGE = "exif-test-sony-d700.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( SONY_D700_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + SONY_D700_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File(imageUrl.toURI() ));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + SONY_D700_IMAGE);
        }
        exifInfo.decodeExifTags();
        assertEquals( "Aperture parsing verification", "f/2.4", exifInfo.aperture );
        assertEquals( "ShutterSpeed parsing verification", "1/32 sec", exifInfo.shutterSpeed );
        assertEquals( "Camera parsing verification", "DSC-D700", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "1998:12:01 14:22:36", exifInfo.getCreateDateTime() );
        assertEquals( "ExifHeight parsing verification", "512 pixels", exifInfo.exifHeight );
        assertEquals( "ExifWidth parsing verification", "672 pixels", exifInfo.exifWidth );
        assertEquals( "FocalLength parsing verification", "", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "200", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
        assertEquals( "Rotation parsing verification", 0, exifInfo.rotation );
    }

    /**
     * Tests for a Sony P200
     */
    @Test
    public void testExifInfoSonyP200() {
        final String SONY_P200_IMAGE = "exif-test-sony-P200.jpg";
        URL imageUrl = ExifInfoTest.class.getClassLoader().getResource( SONY_P200_IMAGE );
        if ( imageUrl == null ) {
            fail("Unable to locate the test image: " + SONY_P200_IMAGE);
        }
        ExifInfo exifInfo = null;
        try {
            exifInfo = new ExifInfo( new File( imageUrl.toURI()) );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable to locate the test image: " + SONY_P200_IMAGE);
        }
        exifInfo.decodeExifTags();
        //System.out.println( exifInfo.getAllTags() );
        assertEquals( "Aperture parsing verification", "f/5.6", exifInfo.aperture );
        assertEquals( "ShutterSpeed parsing verification", "1/400 sec", exifInfo.shutterSpeed );
        assertEquals( "Camera parsing verification", "DSC-P200", exifInfo.camera );
        assertEquals( "CreateDateTime parsing verification", "2013:07:04 13:44:46", exifInfo.getCreateDateTime() );
        assertEquals( "ExifHeight parsing verification", "2304 pixels", exifInfo.exifHeight );
        assertEquals( "ExifWidth parsing verification", "3072 pixels", exifInfo.exifWidth );
        assertEquals( "FocalLength parsing verification", "7.9 mm", exifInfo.focalLength );
        assertEquals( "ISO parsing verification", "100", exifInfo.iso );
        assertEquals( "Longitude parsing verification", ZERO, exifInfo.latLng.getX() );
        assertEquals( "Latitude parsing verification", ZERO, exifInfo.latLng.getY() );
        assertEquals( "Lens parsing verification", "", exifInfo.lens );
        assertEquals( "Rotation parsing verification", 0, exifInfo.rotation );
    }

}
