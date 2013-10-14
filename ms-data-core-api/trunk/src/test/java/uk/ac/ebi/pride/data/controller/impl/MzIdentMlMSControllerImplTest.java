package uk.ac.ebi.pride.data.controller.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.data.controller.impl.ControllerImpl.MzIdentMLControllerImpl;
import uk.ac.ebi.pride.data.core.Spectrum;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: yperez
 * Date: 8/14/12
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class MzIdentMlMSControllerImplTest {

    private MzIdentMLControllerImpl mzIdentMlController = null;


    @Before
    public void setUp() throws Exception {
        URL url = MzIdentMlControllerImplTest.class.getClassLoader().getResource("small.mzid");
        if (url == null) {
            throw new IllegalStateException("no file for input found!");
        }
        File inputFile = new File(url.toURI());
        mzIdentMlController = new MzIdentMLControllerImpl(inputFile);
    }

    @After
    public void tearDown() throws Exception {
        mzIdentMlController.close();
    }


    @Test
    public void addMSController() throws Exception {
        URL url = MzIdentMlControllerImplTest.class.getClassLoader().getResource("small.mgf");
        File filems = new File(url.getFile());
        List<File> fileList = new ArrayList<File>();
        fileList.add(filems);
        mzIdentMlController.addMSController(fileList);
        Spectrum spectrum = mzIdentMlController.getSpectrumById("730!SD_1");
        assertTrue("There should be 60 peaks", spectrum.getIntensityBinaryDataArray().getDoubleArray().length == 60);
        int i = mzIdentMlController.getNumberOfIdentifiedSpectra();
        int j = mzIdentMlController.getNumberOfSpectra();
    }


}
