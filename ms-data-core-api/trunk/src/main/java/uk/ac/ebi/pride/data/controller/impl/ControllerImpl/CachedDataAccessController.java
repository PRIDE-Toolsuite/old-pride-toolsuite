package uk.ac.ebi.pride.data.controller.impl.ControllerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.chart.graphics.implementation.PrideChart;
import uk.ac.ebi.pride.chart.graphics.implementation.PrideChartFactory;
import uk.ac.ebi.pride.chart.model.implementation.ExperimentSummaryData;
import uk.ac.ebi.pride.chart.model.implementation.SpectralDataPerExperimentException;
import uk.ac.ebi.pride.data.Tuple;
import uk.ac.ebi.pride.data.controller.DataAccessException;
import uk.ac.ebi.pride.data.controller.DataAccessMode;
import uk.ac.ebi.pride.data.controller.DataAccessUtilities;
import uk.ac.ebi.pride.data.controller.cache.Cache;
import uk.ac.ebi.pride.data.controller.cache.CacheAccessor;
import uk.ac.ebi.pride.data.controller.cache.CacheEntry;
import uk.ac.ebi.pride.data.controller.cache.CachingStrategy;
import uk.ac.ebi.pride.data.core.*;
import uk.ac.ebi.pride.data.utils.CollectionUtils;
import uk.ac.ebi.pride.data.utils.QuantCvTermReference;
import uk.ac.ebi.pride.engine.SearchEngineType;
import uk.ac.ebi.pride.term.CvTermReference;

import java.util.*;

/**
 * CachedDataAccessController is abstract class, which enables caching for DataAccessController.
 * Cache is used to store the information used to populate GUI's tables.
 * <p/>
 * It provides the option to choose different running mode, at the present, there are two modes:
 * <p/>
 * 1.CACHE_ONLY: only get the information from cache.
 * 2.CACHE_AND_SOURCE: it first checks the cache, if not exist, then read from data source.
 * <p/>
 * User: rwang
 * Date: 13-Sep-2010
 * Time: 14:26:03
 */
public abstract class CachedDataAccessController extends AbstractDataAccessController {

    private static final Logger logger = LoggerFactory.getLogger(CachedDataAccessController.class);
    /**
     * data access mode
     */
    private DataAccessMode mode;
    /**
     * Note: this cache is related to each experiment, must be reset when switching experiment.
     */
    private final Cache cache;
    /**
     * builder is responsible for initializing the Cache
     */
    private CachingStrategy cachingStrategy;

    public CachedDataAccessController() {
        this(null, DataAccessMode.CACHE_AND_SOURCE);
    }

    public CachedDataAccessController(DataAccessMode mode) {
        this(null, mode);
    }

    public CachedDataAccessController(Object source, DataAccessMode mode) {
        super(source);
        this.mode = mode;
        this.cache = new CacheAccessor();
    }

    public Cache getCache() {
        return cache;
    }

    public CachingStrategy getCachingStrategy() {
        return cachingStrategy;
    }

    public void setCachingStrategy(CachingStrategy builder) {
        this.cachingStrategy = builder;
        this.cachingStrategy.setDataAccessController(this);
        this.cachingStrategy.setCache(cache);
    }

    public void populateCache() {
        cache.clear();

        if (cachingStrategy != null) {
            try {
                cachingStrategy.cache();
            } catch (Exception e) {
                String msg = "Exception while trying to populate cache";
                logger.error(msg, e);
                throw new DataAccessException(msg, e);
            }
        }
    }

    /**
     * Get the runtime mode
     *
     * @return mode    runtime mode
     */
    public DataAccessMode getMode() {
        return mode;
    }

    /**
     * Set the runtime mode
     *
     * @param mode DataAccessMode
     */
    public void setMode(DataAccessMode mode) {
        this.mode = mode;
    }

    /**
     * Get spectrum ids from cache
     *
     * @return Collection<Comparable>  a collection of spectrum ids
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Comparable> getSpectrumIds() {
        Collection<Comparable> ids = (Collection<Comparable>) cache.get(CacheEntry.SPECTRUM_ID);
        return ids == null ? Collections.<Comparable>emptyList() : ids;
    }

    /**
     * Get chromatogram ids from cache
     *
     * @return Collection<Comparable>  a collection of chromatogram ids.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Comparable> getChromatogramIds() {
        Collection<Comparable> ids = (Collection<Comparable>) cache.get(CacheEntry.CHROMATOGRAM_ID);
        return ids == null ? Collections.<Comparable>emptyList() : ids;
    }

    /**
     * Get identification ids from cache
     *
     * @return Collection<Comparable>  a collection of identification ids.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Comparable> getProteinIds() {
        Collection<Comparable> ids = (Collection<Comparable>) cache.get(CacheEntry.PROTEIN_ID);
        return ids == null ? Collections.<Comparable>emptyList() : ids;
    }

    /**
     * Get spectrum object from cache
     * It uses cache by default
     *
     * @param id Spectrum id
     * @return Spectrum spectrum
     */
    @Override
    public Spectrum getSpectrumById(Comparable id) {
        return getSpectrumById(id, true);
    }

    /**
     * Get spectrum using a spectrum id, gives the option to choose whether to use cache.
     * This implementation provides a way of by passing the cache.
     *
     * @param id       spectrum id
     * @param useCache true means to use cache
     * @return Spectrum spectrum object
     */
    Spectrum getSpectrumById(Comparable id, boolean useCache) {
        return useCache ? (Spectrum) cache.get(CacheEntry.SPECTRUM, id) : null;
    }

    /**
     * Get chromatogram object form cache
     * It uses cache by default
     *
     * @param chromaId chromatogram string id
     * @return Chromatogram chromatogram object
     */
    @Override
    public Chromatogram getChromatogramById(Comparable chromaId) {
        return getChromatogramById(chromaId, true);
    }

    /**
     * Get chromatogram using a chromatogram id, gives the option to choose whether to use cache.
     * This implementation provides a way of by passing the cache.
     *
     * @param id       chromatogram id
     * @param useCache true means to use cache
     * @return Chromatogram chromatogram object
     */
    public Chromatogram getChromatogramById(Comparable id, boolean useCache) {
        return useCache ? (Chromatogram) cache.get(CacheEntry.CHROMATOGRAM, id) : null;
    }

    /**
     * Get identification object from cache
     * It uses the cache by default
     *
     * @param proteinId a string id of Identification
     * @return Identification  identification object
     */
    @Override
    public Protein getProteinById(Comparable proteinId) {
        return getProteinById(proteinId, true);
    }

    /**
     * Get identification using a identification id, gives the option to choose whether to use cache.
     * This implementation provides a way of by passing the cache.
     *
     * @param proteinId protein identification id
     * @param useCache  true means to use cache
     * @return Identification identification object
     */
    Protein getProteinById(Comparable proteinId, boolean useCache) {
        return useCache ? (Protein) cache.get(CacheEntry.PROTEIN, proteinId) : null;
    }


    /**
     * Get number of peaks using spectrum id.
     * This implementation will check cache first.
     *
     * @param specId spectrum id.
     * @return int number of peaks
     */
    @Override
    public int getNumberOfSpectrumPeaks(Comparable specId) {
        Integer numOfPeaks = (Integer) cache.get(CacheEntry.NUMBER_OF_PEAKS, specId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && numOfPeaks == null) {
            numOfPeaks = super.getNumberOfSpectrumPeaks(specId);
            cache.store(CacheEntry.NUMBER_OF_PEAKS, specId, numOfPeaks);
        }
        return numOfPeaks == null ? 0 : numOfPeaks;
    }

    /**
     * Get ms level using spectrum id.
     * This implementation will check cache first. if return -1, means ms level
     * doesn't exist.
     *
     * @param specId spectrum id.
     * @return int ms level
     */
    @Override
    public int getSpectrumMsLevel(Comparable specId) {
        Integer msLevel = (Integer) cache.get(CacheEntry.MS_LEVEL, specId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && msLevel == null) {
            msLevel = super.getSpectrumMsLevel(specId);
            cache.store(CacheEntry.MS_LEVEL, specId, msLevel);
        }
        return msLevel == null ? -1 : msLevel;
    }

    /**
     * Get precursor charge of a spectrum.
     * This implementation will check cache first.
     *
     * @param specId spectrum id.
     * @return int precursor charge
     */
    @Override
    public Integer getSpectrumPrecursorCharge(Comparable specId) {
        Integer charge = (Integer) cache.get(CacheEntry.SPECTRUM_LEVEL_PRECURSOR_CHARGE, specId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && charge == null) {
            charge = super.getSpectrumPrecursorCharge(specId);
            cache.store(CacheEntry.SPECTRUM_LEVEL_PRECURSOR_CHARGE, specId, charge);
        }
        return charge;
    }

    /**
     * Get precursor charge on peptide level
     * Note: sometimes, precursor charge at the peptide level is different from the precursor charge at the spectrum level
     * As the peptide-level precursor charge is often assigned by search engine rather than ms instrument
     *
     * @param proteinId identification id
     * @param peptideId peptid eid, can be the index of the peptide as well.
     * @return precursor charge, 0 should be returned if not available
     * @throws uk.ac.ebi.pride.data.controller.DataAccessException
     *          data access exception
     */
    @Override
    public Integer getPeptidePrecursorCharge(Comparable proteinId, Comparable peptideId) {
        Integer charge = null;
        // get peptide additional parameters
        ParamGroup paramGroup = (ParamGroup) cache.get(CacheEntry.PEPTIDE_TO_PARAM, peptideId);
        if (paramGroup != null) {
            // get peptide precursor charge
            charge = DataAccessUtilities.getPrecursorChargeParamGroup(paramGroup);
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            charge = super.getPeptidePrecursorCharge(proteinId, peptideId);
        }

        return charge;
    }

    /**
     * Get precursor m/z value using spectrum id.
     * This implementation will check cache first.
     *
     * @param specId spectrum id.
     * @return double m/z
     */
    @Override
    public double getSpectrumPrecursorMz(Comparable specId) {
        Double mz = (Double) cache.get(CacheEntry.SPECTRUM_LEVEL_PRECURSOR_MZ, specId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && mz == null) {
            mz = super.getSpectrumPrecursorMz(specId);
            cache.store(CacheEntry.SPECTRUM_LEVEL_PRECURSOR_MZ, specId, mz);
        }
        return mz == null ? -1 : mz;
    }

    /**
     * Get precursor m/z from the peptide level
     *
     * @param proteinId identification id
     * @param peptideId peptid eid, can be the index of the peptide as well.
     * @return precursor mass
     */
    @Override
    public double getPeptidePrecursorMz(Comparable proteinId, Comparable peptideId) {
        double mz = -1;
        // get peptide additional parameters
        ParamGroup paramGroup = (ParamGroup) cache.get(CacheEntry.PEPTIDE_TO_PARAM, peptideId);
        if (paramGroup != null) {
            // get peptide precursor charge
            mz = DataAccessUtilities.getPrecursorMz(paramGroup);
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            mz = super.getPeptidePrecursorMz(proteinId, peptideId);
        }

        return mz;
    }

    /**
     * Get precursor intensity value using spectrum id.
     * This implementation will check cache first.
     *
     * @param specId spectrum id.
     * @return double intensity
     */
    @Override
    public double getSpectrumPrecursorIntensity(Comparable specId) {
        Double intent = (Double) cache.get(CacheEntry.PRECURSOR_INTENSITY, specId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && intent == null) {
            intent = super.getSpectrumPrecursorIntensity(specId);
            cache.store(CacheEntry.PRECURSOR_INTENSITY, specId, intent);
        }
        return intent == null ? -1 : intent;
    }

    /**
     * Get sum of intensity value using spectrum id.
     * This implementation will check cache first.
     *
     * @param specId spectrum id.
     * @return double sum of intensity
     */
    @Override
    public double getSumOfIntensity(Comparable specId) {
        Double sum = (Double) cache.get(CacheEntry.SUM_OF_INTENSITY, specId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && sum == null) {
            sum = super.getSumOfIntensity(specId);
            cache.store(CacheEntry.SUM_OF_INTENSITY, specId, sum);
        }
        return sum;
    }

    /**
     * Get protein accession value using identification id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return String protein accession
     */
    @Override
    public String getProteinAccession(Comparable proteinId) {
        String acc = (String) cache.get(CacheEntry.PROTEIN_ACCESSION, proteinId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && acc == null) {
            acc = super.getProteinAccession(proteinId);
            cache.store(CacheEntry.PROTEIN_ACCESSION, proteinId, acc);
        }
        return acc;
    }

    /**
     * Get protein accession version using identification id.
     *
     * @param proteinId identification id.
     * @return String
     */
    @Override
    public String getProteinAccessionVersion(Comparable proteinId) {
        String accVersion = (String) cache.get(CacheEntry.PROTEIN_ACCESSION_VERSION, proteinId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && accVersion == null) {
            accVersion = super.getProteinAccessionVersion(proteinId);
            if (accVersion != null) {
                cache.store(CacheEntry.PROTEIN_ACCESSION_VERSION, proteinId, accVersion);
            }
        }
        return accVersion;
    }

    /**
     * Get identification score using identification id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return double identification score
     */
    @Override
    public double getProteinScore(Comparable proteinId) {
        Double score = (Double) cache.get(CacheEntry.SCORE, proteinId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && score == null) {
            score = super.getProteinScore(proteinId);
            cache.store(CacheEntry.SCORE, proteinId, score);
        }
        return score == null ? -1 : score;
    }

    /**
     * Get identification threshold using identification id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return double sum of intensity
     */
    @Override
    public double getProteinThreshold(Comparable proteinId) {
        Double threshold = (Double) cache.get(CacheEntry.THRESHOLD, proteinId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && threshold == null) {
            threshold = super.getProteinThreshold(proteinId);
            cache.store(CacheEntry.THRESHOLD, proteinId, threshold);
        }
        return threshold == null ? -1 : threshold;
    }

    /**
     * Get search database using identification id
     *
     * @param proteinId identification id.
     * @return String search database
     */
    @Override
    public SearchDataBase getSearchDatabase(Comparable proteinId) {
        SearchDataBase database = (SearchDataBase) cache.get(CacheEntry.PROTEIN_SEARCH_DATABASE, proteinId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && database == null) {
            database = super.getSearchDatabase(proteinId);
            if (database != null) {
                cache.store(CacheEntry.PROTEIN_SEARCH_DATABASE, proteinId, database);
            }
        }
        return database;
    }

    /**
     * Get search database version using identification id
     *
     * @param proteinId identification id.
     * @return String search database version
     */
    @Override
    public String getSearchDatabaseVersion(Comparable proteinId) {
        String version = (String) cache.get(CacheEntry.PROTEIN_SEARCH_DATABASE_VERSION, proteinId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && version == null) {
            version = super.getSearchDatabaseVersion(proteinId);
            if (version != null) {
                cache.store(CacheEntry.PROTEIN_SEARCH_DATABASE_VERSION, proteinId, version);
            }
        }
        return version;
    }

    /**
     * Get peptide ids using identification id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return Collection<Comparable>   peptide ids collection
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Comparable> getPeptideIds(Comparable proteinId) {
        Collection<Comparable> ids = (List<Comparable>) cache.get(CacheEntry.PROTEIN_TO_PEPTIDE, proteinId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && ids == null) {
            ids = super.getPeptideIds(proteinId);
            cache.store(CacheEntry.PROTEIN_TO_PEPTIDE, proteinId, ids);
        }
        return ids;
    }

    @Override
    public Peptide getPeptideByIndex(Comparable proteinId, Comparable index) {
        return getPeptideByIndex(proteinId, index, true);
    }

    public Peptide getPeptideByIndex(Comparable proteinId, Comparable index, boolean useCache) {
        Peptide pep = null;

        if (useCache) {
            // check whether the identification exist in the cache already
            Protein ident = (Protein) cache.get(CacheEntry.PROTEIN, proteinId);
            if (ident != null) {
                int indexInt = Integer.parseInt(index.toString());
                List<Peptide> peptides = ident.getPeptides();
                if (!peptides.isEmpty() && indexInt >= 0 && indexInt < peptides.size()) {
                    pep = peptides.get(indexInt);
                }
            } else {
                pep = (Peptide) cache.get(CacheEntry.PEPTIDE, new Tuple<Comparable, Comparable>(proteinId, index));
            }
        }

        return pep;
    }

    /**
     * Get peptide sequences using identification id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return Collection<Comparable>   peptide ids collection
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<String> getPeptideSequences(Comparable proteinId) {
        List<Comparable> ids = (List<Comparable>) cache.get(CacheEntry.PROTEIN_TO_PEPTIDE, proteinId);
        List<String> sequences = new ArrayList<String>();
        if (ids != null && cache.hasCacheEntry(CacheEntry.PEPTIDE_SEQUENCE)) {
            // get each peptide sequence one by one from cache
            Collection<String> seqs = (Collection<String>) cache.getInBatch(CacheEntry.PEPTIDE_SEQUENCE, ids);
            sequences.addAll(seqs);
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            // read from data source
            sequences = super.getPeptideSequences(proteinId);
            // note: peptide id is not supported by pride xml
            // this is why we didn't store them to cache
        }

        return sequences;
    }

    /**
     * Get number of peptides using identification id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return int   number of peptides
     */
    @Override
    @SuppressWarnings("unchecked")
    public int getNumberOfPeptides(Comparable proteinId) {
        int cnt = 0;
        List<Comparable> ids = (List<Comparable>) cache.get(CacheEntry.PROTEIN_TO_PEPTIDE, proteinId);
        if (ids != null) {
            cnt = ids.size();
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            cnt = super.getNumberOfPeptides(proteinId);
        }
        return cnt;
    }

    /**
     * Get number of unique peptides using identification id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return int   number of unique peptides
     */
    @Override
    @SuppressWarnings("unchecked")
    public int getNumberOfUniquePeptides(Comparable proteinId) {
        int cnt = 0;
        List<Comparable> ids = (List<Comparable>) cache.get(CacheEntry.PROTEIN_TO_PEPTIDE, proteinId);
        if (ids != null && cache.hasCacheEntry(CacheEntry.PEPTIDE_SEQUENCE)) {
            Collection<String> seqs = (Collection<String>) cache.getInBatch(CacheEntry.PEPTIDE_SEQUENCE, ids);
            cnt = (new HashSet<String>(seqs)).size();
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            cnt = super.getNumberOfUniquePeptides(proteinId);
        }
        return cnt;
    }

    /**
     * Get number of ptms using identification id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return int   number of ptms
     */
    @Override
    @SuppressWarnings("unchecked")
    public int getNumberOfPTMs(Comparable proteinId) {
        int cnt = 0;
        List<Comparable> ids = (List<Comparable>) cache.get(CacheEntry.PROTEIN_TO_PEPTIDE, proteinId);
        if (ids != null && cache.hasCacheEntry(CacheEntry.PEPTIDE_TO_MODIFICATION)) {
            // get all ptm locations
            Collection<List<Tuple<String, Integer>>> ptms = (Collection<List<Tuple<String, Integer>>>) cache.getInBatch(CacheEntry.PEPTIDE_TO_MODIFICATION, ids);
            for (List<Tuple<String, Integer>> ptm : ptms) {
                cnt += ptm.size();
            }
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            cnt = super.getNumberOfPTMs(proteinId);
        }

        return cnt;
    }

    /**
     * Get number of ptms using peptide id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return int   number of ptms
     */
    @Override
    @SuppressWarnings("unchecked")
    public int getNumberOfPTMs(Comparable proteinId, Comparable peptideId) {
        int cnt = 0;
        List<Tuple<String, Integer>> locations = (List<Tuple<String, Integer>>) cache.get(CacheEntry.PEPTIDE_TO_MODIFICATION, peptideId);
        if (locations != null) {
            cnt = locations.size();
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            cnt = super.getNumberOfPTMs(proteinId, peptideId);
        }

        return cnt;
    }

    /**
     * Get peptide sequence using identification id and peptide id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return int   number of unique peptides
     */
    @Override
    public String getPeptideSequence(Comparable proteinId, Comparable peptideId) {
        String seq = (String) cache.get(CacheEntry.PEPTIDE_SEQUENCE, peptideId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && seq == null) {
            seq = super.getPeptideSequence(proteinId, peptideId);
            // note: peptide id is not supported by pride xml
            // this is why we didn't store them to cache
        }
        return seq;
    }

    /**
     * Get peptide sequence start using identification id and peptide id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return int   peptide sequence start
     */
    @Override
    public int getPeptideSequenceStart(Comparable proteinId, Comparable peptideId) {
        Integer start = (Integer) cache.get(CacheEntry.PEPTIDE_START, peptideId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && start == null) {
            start = super.getPeptideSequenceStart(proteinId, peptideId);
            // note: peptide id is not supported by pride xml
            // this is why we didn't store them to cache
        }
        return start == null ? -1 : start;
    }

    /**
     * Get peptide sequence stop using identification id and peptide id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return int   peptide sequence stop
     */
    @Override
    public int getPeptideSequenceEnd(Comparable proteinId, Comparable peptideId) {
        Integer stop = (Integer) cache.get(CacheEntry.PEPTIDE_END, peptideId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && stop == null) {
            stop = super.getPeptideSequenceEnd(proteinId, peptideId);
            // note: peptide id is not supported by pride xml
            // this is why we didn't store them to cache
        }
        return stop == null ? -1 : stop;
    }

    /**
     * Get peptide spectrum id using identification id and peptide id.
     * This implementation will check cache first.
     *
     * @param proteinId identification id.
     * @return int   peptide sequence stop
     */
    @Override
    public Comparable getPeptideSpectrumId(Comparable proteinId, Comparable peptideId) {
        Comparable specId = (Comparable) cache.get(CacheEntry.PEPTIDE_TO_SPECTRUM, peptideId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && specId == null) {
            specId = super.getPeptideSpectrumId(proteinId, peptideId);
            // note: peptide id is not supported by pride xml
            // this is why we didn't store them to cache
        }
        return specId;
    }

    /**
     * Get ptms using identification id nad peptide id
     *
     * @param proteinId identification id
     * @param peptideId peptide id, can be the index of the peptide
     * @return List<Modification>   a list of modifications.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Modification> getPTMs(Comparable proteinId, Comparable peptideId) {
        List<Tuple<String, Integer>> ptms = (List<Tuple<String, Integer>>) cache.get(CacheEntry.PEPTIDE_TO_MODIFICATION, peptideId);

        List<Modification> mods = new ArrayList<Modification>();

        if (ptms != null && !ptms.isEmpty()) {
            // create modification from cache
            for (Tuple<String, Integer> ptm : ptms) {
                String modAcc = ptm.getKey();
                Integer location = ptm.getValue();
                Modification mod = (Modification) cache.get(CacheEntry.MODIFICATION, modAcc);
                //Modification newMod = new Modification(mod, modAcc, mod.getModDatabase(), mod.getModDatabaseVersion(), mod.getMonoisotopicMassDelta(), mod.getAvgMassDelta(), location);
                Modification newMod = new Modification(modAcc, mod.getName(), location, mod.getResidues(), mod.getAvgMassDelta(), mod.getMonoisotopicMassDelta(), mod.getModDatabase(), mod.getModDatabaseVersion());
                mods.add(newMod);
            }
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            mods = super.getPTMs(proteinId, peptideId);
        }
        return mods;
    }

    /**
     * Get the number of fragment ions in a given peptide
     *
     * @param proteinId identification id
     * @param peptideId peptide id, can be the index of the peptide as well.
     * @return int number of fragment ions
     */
    @Override
    public int getNumberOfFragmentIons(Comparable proteinId, Comparable peptideId) {
        Integer num = (Integer) cache.get(CacheEntry.NUMBER_OF_FRAGMENT_IONS, peptideId);
        if (!DataAccessMode.CACHE_ONLY.equals(mode) && num == null) {
            num = super.getNumberOfFragmentIons(proteinId, peptideId);
        }
        return num == null ? 0 : num;
    }

    /**
     * Get peptide score from search engine
     *
     * @param proteinId identification id
     * @param peptideId peptide id, can be the index of the peptide as well.
     * @return PeptideScore    peptide score from search engine
     */
    @Override
    public Score getPeptideScore(Comparable proteinId, Comparable peptideId) {
        Score score = null;
        // get peptide additional parameters
        ParamGroup paramGroup = (ParamGroup) cache.get(CacheEntry.PEPTIDE_TO_PARAM, peptideId);
        if (paramGroup != null) {
            // get peptide score
            score = DataAccessUtilities.getScore(paramGroup);
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            score = super.getPeptideScore(proteinId, peptideId);
        }

        return score;
    }

    /**
     * Get search engine type
     *
     * @return SearchEngine    search engine
     */
    @Override
    public List<SearchEngineType> getSearchEngineTypes() {
        Collection<SearchEngineType> searchEngineTypes = (Collection<SearchEngineType>) cache.get(CacheEntry.SEARCH_ENGINE_TYPE);

        if (searchEngineTypes != null && !searchEngineTypes.isEmpty()) {
            return new ArrayList<SearchEngineType>(searchEngineTypes);
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            return super.getSearchEngineTypes();
        }

        return null;
    }

    /**
     * todo: need to see wether to cache this properly
     *
     * @return
     */
    @Override
    public List<CvTermReference> getAvailableProteinLevelScores() {
        Collection<CvTermReference> proteinLevelScores = (Collection<CvTermReference>) cache.get(CacheEntry.PROTEIN_LEVEL_SCORES);

        if (proteinLevelScores != null && !proteinLevelScores.isEmpty()) {
            return new ArrayList<CvTermReference>(proteinLevelScores);
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            return super.getAvailableProteinLevelScores();
        }

        return null;

    }

    /**
     * todo: need to see wether to cache this properly
     *
     * @return
     */
    @Override
    public List<CvTermReference> getAvailablePeptideLevelScores() {
        Collection<CvTermReference> peptideLevelScores = (Collection<CvTermReference>) cache.get(CacheEntry.PEPTIDE_LEVEL_SCORES);

        if (peptideLevelScores != null && !peptideLevelScores.isEmpty()) {
            return new ArrayList<CvTermReference>(peptideLevelScores);
        } else if (!DataAccessMode.CACHE_ONLY.equals(mode)) {
            return super.getAvailablePeptideLevelScores();
        }

        return null;
    }

    /**
     * Get chart data for generating chart component
     *
     * @return List<PrideChartManager> a list of chart data
     */
    @Override
    public List<PrideChartManager> getChartData() {
        ExperimentSummaryData spectralSummaryData;
        try {
            spectralSummaryData = new PrideChartSummaryData(this);
        } catch (SpectralDataPerExperimentException e) {
            String msg = "PrideChartSummaryData object could not be created";
            logger.error(msg, e);
            return Collections.emptyList();
        }

        List<PrideChartManager> list = new ArrayList<PrideChartManager>();
        for (PrideChart prideChart : PrideChartFactory.getAllCharts(spectralSummaryData)) {
            list.add(new PrideChartManager(prideChart));
        }
        return list;
    }

    /**
     * Get protein quantification unit
     *
     * @return QuantCvTermReference    quantification unit
     */
    @Override
    public QuantCvTermReference getProteinQuantUnit() {
        Collection<QuantCvTermReference> units;
        units = (Collection<QuantCvTermReference>) cache.get(CacheEntry.PROTEIN_QUANT_UNIT);

        if (units != null && !units.isEmpty()) {
            return CollectionUtils.getElement(units, 0);
        } else {
            QuantCvTermReference unit = super.getProteinQuantUnit();
            cache.store(CacheEntry.PROTEIN_QUANT_UNIT, unit);
            return unit;
        }
    }

    /**
     * Get peptide quantification unit
     *
     * @return QuantCvTermReference    quantification unit
     */
    @Override
    public QuantCvTermReference getPeptideQuantUnit() {
        Collection<QuantCvTermReference> units;
        units = (Collection<QuantCvTermReference>) cache.get(CacheEntry.PEPTIDE_QUANT_UNIT);

        if (units != null && !units.isEmpty()) {
            return CollectionUtils.getElement(units, 0);
        } else {
            QuantCvTermReference unit = super.getPeptideQuantUnit();
            cache.store(CacheEntry.PEPTIDE_QUANT_UNIT, unit);
            return unit;
        }
    }

    /**
     * Get the Experiment Meta Data
     *
     * @return ExperimentMetaData
     */
    @Override
    public ExperimentMetaData getExperimentMetaData() {
        Collection<ExperimentMetaData> metaDatas = (Collection<ExperimentMetaData>) cache.get(CacheEntry.EXPERIMENT_METADATA);

        if (metaDatas != null && !metaDatas.isEmpty()) {
            return CollectionUtils.getElement(metaDatas, 0);
        }
        return null;
    }

    /**
     * Get Identification Meta Data
     *
     * @return IdentificationMetaData
     */
    @Override
    public IdentificationMetaData getIdentificationMetaData() {
        Collection<IdentificationMetaData> metaDatas = (Collection<IdentificationMetaData>) cache.get(CacheEntry.PROTEIN_METADATA);
        if (metaDatas != null && !metaDatas.isEmpty()) {
            return CollectionUtils.getElement(metaDatas, 0);
        }
        return null;
    }

    /**
     * Get MzGraph Meta Data. The Meta Data at the Spectras Level
     *
     * @return MzGraphMetaData
     */
    @Override
    public MzGraphMetaData getMzGraphMetaData() {
        Collection<MzGraphMetaData> metaDatas = (Collection<MzGraphMetaData>) cache.get(CacheEntry.MZGRAPH_METADATA);

        if (metaDatas != null && !metaDatas.isEmpty()) {
            return CollectionUtils.getElement(metaDatas, 0);
        }
        return null;
    }

    @Override
    public Collection<Comparable> getProteinAmbiguityGroupIds() {
        Collection<Comparable> groupIds = (Collection<Comparable>) cache.get(CacheEntry.PROTEIN_GROUP_ID);

        if (groupIds == null || groupIds.isEmpty()) {
            groupIds = Collections.emptyList();
        }
        return groupIds;
    }

    /**
     * Close data access controller by clearing the cache first
     */
    @Override
    public void close() {
        cache.clear();
        super.close();
    }
}