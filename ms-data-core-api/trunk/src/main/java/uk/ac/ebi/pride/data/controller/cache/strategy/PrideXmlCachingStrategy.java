package uk.ac.ebi.pride.data.controller.cache.strategy;

import uk.ac.ebi.pride.data.controller.cache.CacheEntry;
import uk.ac.ebi.pride.data.controller.impl.ControllerImpl.PrideXmlControllerImpl;
import uk.ac.ebi.pride.jaxb.xml.PrideXmlReader;

import java.util.ArrayList;

/**
 * PrideXmlAccessCacheBuilder initialize the cache for pride xml reading.
 * <p/>
 * User: rwang
 * Date: 06-Sep-2010
 * Time: 17:08:20
 */
public class PrideXmlCachingStrategy extends AbstractCachingStrategy {

    /**
     * Spectrum ids and identification ids are cached.
     */
    @Override
    public void cache() {
        // get a reference to xml reader
        PrideXmlReader reader = ((PrideXmlControllerImpl) controller).getReader();

        // clear and add spectrum ids
        cache.clear(CacheEntry.SPECTRUM_ID);
        cache.storeInBatch(CacheEntry.SPECTRUM_ID, new ArrayList<Comparable>(reader.getSpectrumIds()));

        // clear and add peptide ids
        cache.clear(CacheEntry.PROTEIN_ID);
        cache.storeInBatch(CacheEntry.PROTEIN_ID, new ArrayList<Comparable>(reader.getIdentIds()));
    }
}



