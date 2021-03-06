package export;

import org.jpo.export.WebsiteGenerator;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

/*
 Copyright (C) 2018 - 2018  Richard Eigenmann, Zurich, Switzerland
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
 *
 * @author Richard Eigenmann
 */
public class WebsiteGeneratorTest {

    /**
     * Test of cleanupFilename method, of class WebsiteGenerator.
     */
    @Test
    public void testCleanupFilename() {
        String filename = "directory\\file.xml";  // actually contains directory\file.xml
        String wanted = "directory_file.xml";  // actually contains directory\file.xml
        String got = WebsiteGenerator.cleanupFilename( filename );
        assertEquals( "A backslash could be made into an underscore", wanted, got );
    }

    /**
     * Test of writeCss method, of class WebsiteGenerator.
     */
    @Test
    public void testWriteCss() {
        try {
            Path path = Files.createTempDirectory( "UnitTestsTempDir" );
            WebsiteGenerator.writeCss( path.toFile() );
            File cssFile = new File( path.toFile(), "jpo.css" );
            assertTrue( "org.jpo.css was supposed to exists in directory " + path.toString(), cssFile.exists() );
            assertTrue( "The org.jpo.css file could not be deleted", cssFile.delete() );
            assertTrue( "The temporary directory could not be deleted", path.toFile().delete() );
        } catch ( IOException ex ) {
            fail( "Was not supposed to fail with the following Exception: " + ex.getMessage() );
        }
    }

    /**
     * Test of writeRobotsTxt method, of class WebsiteGenerator.
     */
    @Test
    public void testWriteRobotsTxt() {
        try {
            Path path = Files.createTempDirectory( "UnitTestsTempDir" );
            WebsiteGenerator.writeRobotsTxt( path.toFile() );
            File cssFile = new File( path.toFile(), "robots.txt" );
            assertTrue( "robots.txt was supposed to exists in directory " + path.toString(), cssFile.exists() );
            assertTrue( "The robots.txt file could not be deleted", cssFile.delete() );
            assertTrue( "The temporary directory could not be deleted", path.toFile().delete() );
        } catch ( IOException ex ) {
            fail( "Was not supposed to fail with the following Exception: " + ex.getMessage() );
        }
    }

    /**
     * Test of writeJpoJs method, of class WebsiteGenerator.
     */
    @Test
    public void testWriteJpoJs() {
        try {
            Path path = Files.createTempDirectory( "UnitTestsTempDir" );
            WebsiteGenerator.writeJpoJs( path.toFile() );
            File cssFile = new File( path.toFile(), "jpo.js" );
            assertTrue( "org.jpo.js was supposed to exists in directory " + path.toString(), cssFile.exists() );
            assertTrue( "The org.jpo.js file could not be deleted", cssFile.delete() );
            assertTrue( "The temporary directory could not be deleted", path.toFile().delete() );
        } catch ( IOException ex ) {
            fail( "Was not supposed to fail with the following Exception: " + ex.getMessage() );
        }
    }

}
