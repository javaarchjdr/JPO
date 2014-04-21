package jpo.dataModel;

import static junit.framework.Assert.assertNotNull;
import junit.framework.TestCase;
import org.xml.sax.InputSource;

/**
 * Tests for the XmlReader class
 * @author Richard Eigenmann
 */
public class XmlReaderTest extends TestCase {
    
    /**
     * Constructor for the XmlReader tests
     * @param testName 
     */
    public XmlReaderTest(String testName) {
        super(testName);
    }

    /**
     * Jpo uses the dtd file in the classpath. As this can go missing if the build is 
     * poor this unit test checks whether it is there
     */
    public void testGetDtd() {
        // not sure how I want to create the object as it needs input streams and stuff so I'll just do the code that
        // the method resolveEntity does.
        InputSource s =  new InputSource( Settings.CLASS_LOADER.getResourceAsStream( "jpo/collection.dtd" ) );
        assertNotNull( "No collection.dtd found", s );
    }

        
}
