package uk.ac.ebi.pride.data.controller.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.data.controller.impl.ControllerImpl.MzXmlControllerImpl;
import uk.ac.ebi.pride.data.core.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test Class for MzXML Controller
 *
 * User: yperez
 * Date: 3/13/12
 * Time: 9:58 PM
 */
public class MzXmlControllerImplTest {

    private MzXmlControllerImpl mzXmlController = null;

    @Before
    public void setUp() throws Exception {
        URL url = MzXmlControllerImplTest.class.getClassLoader().getResource("testfile.mzXML");
        if (url == null) {
            throw new IllegalStateException("no file for input found!");
        }
        File inputFile = new File(url.toURI());
        mzXmlController = new MzXmlControllerImpl(inputFile);
    }

    @After
    public void tearDown() throws Exception {
        mzXmlController.close();
    }

    @Test
    public void testGetSpectrumIds() throws Exception {
        List<Comparable> ids = new ArrayList<Comparable>(mzXmlController.getSpectrumIds());
        assertTrue("There should be four Spectras", ids.size() == 9181);
        assertTrue("The id of the first spectra should be", ids.contains("1"));
        assertTrue("The id of the Four spectra should be", ids.contains("4"));
    }

    @Test
    public void testGetSpectrumById() throws Exception {
        Spectrum spectrum = mzXmlController.getSpectrumById("2");
        assertTrue("The id of the spectra should be", spectrum.getId().equals("2"));
    }
}
