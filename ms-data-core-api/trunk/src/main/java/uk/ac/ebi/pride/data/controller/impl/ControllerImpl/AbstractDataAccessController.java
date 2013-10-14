package uk.ac.ebi.pride.data.controller.impl.ControllerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.org.mozilla.javascript.IdScriptableObject;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.DataAccessUtilities;
import uk.ac.ebi.pride.data.core.*;
import uk.ac.ebi.pride.data.utils.CollectionUtils;
import uk.ac.ebi.pride.data.utils.QuantCvTermReference;
import uk.ac.ebi.pride.engine.SearchEngineType;
import uk.ac.ebi.pride.term.CvTermReference;

import java.util.*;

/**
 * AbstractDataAccessController provides an abstract implementation of DataAccessController.
 * This is solely based on getting the data directly from data source.
 * <p/>
 * User: rwang, yperez
 * Date: 03-Feb-2010
 * Time: 12:22:24
 */
public abstract class AbstractDataAccessController implements DataAccessController {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDataAccessController.class);

    private static final int NUMBER_OF_PROTEIN_TO_CHECK = 10;
    private static final int NUMBER_OF_PEPTIDE_TO_CHECK = 20;

    /**
     * Unique id to identify the data access controller
     */
    private String uid;
    /**
     * The name of the data source for displaying purpose
     */
    private String name;
    /**
     * The I/O type of the data source
     */
    private Type type;
    /**
     * The type of contents can be present in the data source
     */
    private final Set<ContentCategory> categories;
    /**
     * Data source, such as: File
     */
    private Object source;


    protected AbstractDataAccessController() {
        this(null);
    }

    protected AbstractDataAccessController(Object source) {
        setSource(source);
        categories = new HashSet<ContentCategory>();
    }

    @Override
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Collection<ContentCategory> getContentCategories() {
        return categories;
    }

    public void setContentCategories(ContentCategory... categories) {
        CollectionUtils.replaceValuesInCollection(Arrays.asList(categories), this.categories);
    }

    @Override
    public Object getSource() {
        return source;
    }

    public void setSource(Object src) {
        this.source = src;
    }

    @Override
    public void close() {
    }

    @Override
    public ExperimentMetaData getExperimentMetaData() {
        return null;
    }

    @Override
    public IdentificationMetaData getIdentificationMetaData() {
        return null;
    }

    @Override
    public MzGraphMetaData getMzGraphMetaData() {
        return null;
    }

    public Collection<Sample> getSamples() {
        return Collections.emptyList();
    }


    public ParamGroup getAdditional() {
        return null;
    }

    @Override
    public boolean hasSpectrum() {
        return getNumberOfSpectra() > 0;
    }

    @Override
    public boolean hasProteinSequence() {
        return false;
    }

    @Override
    public int getNumberOfSpectra() {
        return getSpectrumIds().size();
    }

    public int getNumberOfIdentifiedSpectra() {
        return 0;
    }

    @Override
    public int getSpectrumIndex(Comparable id) {
        int index = -1;
        Collection<Comparable> ids = getSpectrumIds();
        if (ids != null) {
            index = CollectionUtils.getIndex(ids, id);
        }
        return index;
    }

    @Override
    public Collection<Spectrum> getSpectraByIndex(int index, int offset) {
        List<Spectrum> spectra = new ArrayList<Spectrum>();
        Collection<Comparable> specIds = getSpectrumIds();
        if (specIds != null && index < specIds.size()) {
            int stopIndex = index + offset;
            int idSize = specIds.size();
            stopIndex = stopIndex >= idSize ? idSize : stopIndex;
            for (int i = index; i < stopIndex; i++) {
                Comparable specId = CollectionUtils.getElement(specIds, i);
                spectra.add(getSpectrumById(specId));
            }
        }

        return spectra;
    }

    @Override
    public boolean isIdentifiedSpectrum(Comparable specId) {
        return false;
    }

    @Override
    public int getNumberOfSpectrumPeaks(Comparable specId) {
        int numOfPeaks = 0;
        Spectrum spectrum = getSpectrumById(specId);
        if (spectrum != null) {
            numOfPeaks = DataAccessUtilities.getNumberOfPeaks(spectrum);
        }
        return numOfPeaks;
    }

    @Override
    public int getSpectrumMsLevel(Comparable specId) {
        int msLevel = -1;
        Spectrum spectrum = getSpectrumById(specId);
        if (spectrum != null) {
            msLevel = DataAccessUtilities.getMsLevel(spectrum);
        }
        return msLevel;
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
        Integer charge = null;
        Spectrum spectrum = getSpectrumById(specId);
        if (spectrum != null) {
            charge = DataAccessUtilities.getPrecursorChargeParamGroup(spectrum);
        }
        return charge;
    }

    @Override
    public double getSpectrumPrecursorMz(Comparable specId) {
        double mz = -1;
        Spectrum spectrum = getSpectrumById(specId);
        if (spectrum != null) {
            mz = DataAccessUtilities.getPrecursorMz(spectrum);
        }
        return mz;
    }

    /**
     * Get precursor charge on peptide level
     * Note: sometimes, precursor charge at the peptide level is different from the precursor charge at the spectrum level
     * As the peptide-level precursor charge is often assigned by search engine rather than ms instrument
     *
     * @param identId   identification id
     * @param peptideId peptid eid, can be the index of the peptide as well.
     * @return precursor charge, 0 should be returned if not available
     * @throws uk.ac.ebi.pride.data.controller.DataAccessException
     *          data access exception
     */
    @Override
    public Integer getPeptidePrecursorCharge(Comparable identId, Comparable peptideId) {
        Integer charge = null;

        Protein ident = getProteinById(identId);
        if (ident != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(ident, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                charge = peptide.getPrecursorCharge();
            }
        }

        return charge;
    }

    @Override
    public double getPeptidePrecursorMz(Comparable proteinId, Comparable peptideId) {
        double mz = -1;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                mz = peptide.getPrecursorMz();
            }
        }
        return mz;
    }

    @Override
    public double getSpectrumPrecursorIntensity(Comparable specId) {
        double intent = -1;
        Spectrum spectrum = getSpectrumById(specId);
        if (spectrum != null) {
            intent = DataAccessUtilities.getPrecursorIntensity(spectrum);
        }
        return intent;
    }

    @Override
    public double getSumOfIntensity(Comparable specId) {
        double sum = 0;
        Spectrum spectrum = getSpectrumById(specId);
        if (spectrum != null) {
            sum = DataAccessUtilities.getSumOfIntensity(spectrum);
        }
        return sum;
    }

    @Override
    public boolean hasChromatogram() {
        return getNumberOfChromatograms() > 0;
    }

    @Override
    public int getNumberOfChromatograms() {
        return getChromatogramIds().size();
    }

    @Override
    public int getChromatogramIndex(Comparable chromaId) {
        int index = -1;
        Collection<Comparable> ids = getChromatogramIds();
        if (ids != null) {
            index = CollectionUtils.getIndex(ids, chromaId);
        }
        return index;
    }

    @Override
    public Collection<Chromatogram> getChromatogramByIndex(int index, int offset) {
        List<Chromatogram> chromas = new ArrayList<Chromatogram>();
        Collection<Comparable> chromatogramIds = getChromatogramIds();
        if (chromatogramIds != null && index < chromatogramIds.size()) {
            int stopIndex = index + offset;
            int idSize = chromatogramIds.size();
            stopIndex = stopIndex >= idSize ? idSize : stopIndex;
            for (int i = index; i < stopIndex; i++) {
                Comparable chromaId = CollectionUtils.getElement(chromatogramIds, i);
                chromas.add(getChromatogramById(chromaId));
            }
        }

        return chromas;
    }

    @Override
    public boolean hasProtein() {
        return getNumberOfProteins() > 0;
    }

    @Override
    public boolean hasProteinAmbiguityGroup() {
        return getNumberOfProteinAmbiguityGroups() > 0;
    }

    @Override
    public boolean hasPeptide() {
        return getNumberOfPeptides() > 0;
    }

    @Override
    public int getNumberOfProteins() {
        return getProteinIds().size();
    }

    @Override
    public int getNumberOfProteinAmbiguityGroups() {
        return getProteinAmbiguityGroupIds().size();
    }

    @Override
    public Collection<Comparable> getProteinAmbiguityGroupIds() {
        return Collections.emptyList();
    }

    @Override
    public ProteinGroup getProteinAmbiguityGroupById(Comparable proteinGroupId) {
        return null;
    }

    @Override
    public int indexOfProtein(Comparable proteinId) {
        int index = -1;
        Collection<Comparable> ids = getProteinIds();
        if (ids != null) {
            index = CollectionUtils.getIndex(ids, proteinId);
        }
        return index;
    }

    public Collection<Protein> getProteinByIndex(int index, int offset) {
        List<Protein> proteins = new ArrayList<Protein>();
        Collection<Comparable> proteinIds = getProteinIds();
        if (proteinIds != null && index < proteinIds.size()) {
            int stopIndex = index + offset;
            int idSize = proteinIds.size();
            stopIndex = stopIndex >= idSize ? idSize : stopIndex;
            for (int i = index; i < stopIndex; i++) {
                Comparable intentId = CollectionUtils.getElement(proteinIds, i);
                proteins.add(getProteinById(intentId));
            }
        }

        return proteins;
    }

    @Override
    public String getProteinAccession(Comparable proteinId) {
        String acc = null;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            acc = protein.getDbSequence().getAccession();
        }
        return acc;
    }

    @Override
    public String getProteinAccessionVersion(Comparable proteinId) {
        String accVersion = null;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            accVersion = protein.getDbSequence().getAccessionVersion();
        }
        return accVersion;
    }

    @Override
    public double getProteinScore(Comparable proteinId) {
        double score = -1;
        Protein protein = getProteinById(proteinId);
        if ((protein != null) && (protein.getScore() != null)) {
            score = protein.getScore().getDefaultScore();
        }
        return score;
    }

    @Override
    public DBSequence getProteinSequence(Comparable proteinId) {
        DBSequence dbSequence = null;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            dbSequence = protein.getDbSequence();
        }
        return dbSequence;
    }

    @Override
    public double getProteinThreshold(Comparable proteinId) {
        double threshold = -1;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            threshold = protein.getThreshold();
        }
        return threshold;
    }

    @Override
    public SearchDataBase getSearchDatabase(Comparable proteinId) {
        SearchDataBase database = null;
        Protein protein = getProteinById(proteinId);
        if (protein != null && (protein.getDbSequence() != null)) {
            database = protein.getDbSequence().getSearchDataBase();
        }
        return database;
    }

    @Override
    public String getSearchDatabaseVersion(Comparable proteinId) {
        String version = null;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            version = protein.getDbSequence().getSearchDataBase().getVersion();
        }
        return version;
    }

    @Override
    public List<SearchEngineType> getSearchEngineTypes() {
        List<SearchEngineType> searchEngineTypes = new ArrayList<SearchEngineType>();
        Collection<Comparable> proteinIds = this.getProteinIds();
        if (!proteinIds.isEmpty()) {
            Protein protein = getProteinById(CollectionUtils.getElement(proteinIds, 0));
            if (protein != null) {
                if (protein.getScore() != null) {
                    searchEngineTypes.addAll(protein.getScore().getSearchEngineTypes());
                }
                // check the search engine types from the data source
                List<Peptide> peptides = protein.getPeptides();
                if (!peptides.isEmpty()) {
                    Peptide peptide = peptides.get(0);
                    if (peptide.getScore() != null) {
                        searchEngineTypes.addAll(peptide.getScore().getSearchEngineTypes());
                    }
                }
            }
        }

        return searchEngineTypes;
    }

    @Override
    public List<CvTermReference> getAvailableProteinLevelScores() {
        Collection<Comparable> proteinIds = this.getProteinIds();
        List<CvTermReference> cvTermReferences = Collections.emptyList();
        if (!proteinIds.isEmpty()) {
            Protein protein = getProteinById(CollectionUtils.getElement(proteinIds, 0));
            if (protein != null) {
                Score score = protein.getScore();
                if (score != null) {
                    cvTermReferences = score.getCvTermReferenceWithValues();
                }
            }
        }
        return cvTermReferences;

    }

    @Override
    public List<CvTermReference> getAvailablePeptideLevelScores() {
        Collection<Comparable> proteinIds = this.getProteinIds();
        List<CvTermReference> cvTermReferences = Collections.emptyList();
        if (!proteinIds.isEmpty()) {
            Protein protein = getProteinById(CollectionUtils.getElement(proteinIds, 0));
            if (protein != null && !protein.getPeptides().isEmpty()) {
                List<Peptide> peptides = protein.getPeptides();
                Peptide peptide = peptides.get(0);
                Score score = peptide.getScore();
                if (score != null) {
                    cvTermReferences = score.getCvTermReferenceWithValues();
                }
            }
        }
        return cvTermReferences;
    }

    @Override
    public Score getProteinScores(Comparable proteinId) {
        Protein protein = getProteinById(proteinId);
        Score score = null;
        if (protein != null) {
            score = DataAccessUtilities.getScore(protein);
            protein.setScore(score);
        }
        return score;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Comparable> getPeptideIds(Comparable proteinId) {
        Collection<Comparable> ids = new ArrayList<Comparable>();
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            List<Peptide> peptides = protein.getPeptides();
            if (!peptides.isEmpty()) {
                for (int index = 0; index < peptides.size(); index++) {
                    ids.add(index);
                }
            }
        }
        return ids;
    }

    @Override
    public Peptide getPeptideByIndex(Comparable proteinId, Comparable index) {
        Peptide peptide = null;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(index.toString()));
        }
        return peptide;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getPeptideSequences(Comparable proteinId) {
        List<String> sequences = new ArrayList<String>();
        // read from data source
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            List<Peptide> peptides = protein.getPeptides();
            if (!peptides.isEmpty()) {
                for (Peptide peptide : peptides) {
                    String seq = peptide.getPeptideSequence().getSequence();
                    sequences.add(seq);
                }
            }
        }

        return sequences;
    }

    @Override
    public Collection<PeptideEvidence> getPeptideEvidences(Comparable proteinId, Comparable peptideId) {
        Peptide peptide = getPeptideByIndex(proteinId, peptideId);
        return peptide.getPeptideEvidenceList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getNumberOfPeptides(Comparable proteinId) {
        int cnt = 0;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            cnt = DataAccessUtilities.getNumberOfPeptides(protein);
        }
        return cnt;
    }

    @Override
    public int getNumberOfPeptides() {
        int cnt = 0;

        Collection<Comparable> ids = getProteinIds();
        if (ids != null) {
            for (Comparable id : ids) {
                cnt += getNumberOfPeptides(id);
            }
        }

        return cnt;
    }

    @Override
    public int getNumberOfPeptidesByRank(int rank) {
        int cnt = 0;

        Collection<Comparable> ids = getProteinIds();
        if (ids != null) {
            for (Comparable id : ids) {
                Collection<Comparable> peptideIds = getPeptideIds(id);
                for (Comparable peptideId : peptideIds) {
                    cnt += (getPeptideRank(id, peptideId) == rank) ? 1 : 0;
                }
            }
        }
        return cnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getNumberOfUniquePeptides(Comparable proteinId) {
        int cnt = 0;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            cnt = DataAccessUtilities.getNumberOfUniquePeptides(protein);
        }
        return cnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getNumberOfPTMs(Comparable proteinId) {
        int cnt = 0;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            cnt = DataAccessUtilities.getNumberOfPTMs(protein);
        }
        return cnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getNumberOfPTMs(Comparable proteinId, Comparable peptideId) {
        int cnt = 0;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            List<Peptide> peptides = protein.getPeptides();
            if (!peptides.isEmpty()) {
                Peptide peptide = peptides.get(Integer.parseInt(peptideId.toString()));
                if (peptide != null) {
                    cnt = DataAccessUtilities.getNumberOfPTMs(peptide);
                }
            }
        }
        return cnt;
    }

    @Override
    public String getPeptideSequence(Comparable proteinId, Comparable peptideId) {
        String seq = null;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                seq = peptide.getPeptideSequence().getSequence();
            }
        }
        return seq;
    }

    @Override
    public int getPeptideRank(Comparable proteinId, Comparable peptideId) {
        return -1;
    }

    @Override
    public int getPeptideSequenceStart(Comparable proteinId, Comparable peptideId) {
        int start = -1;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                start = (peptide.getPeptideEvidenceList().get(0).getStartPosition() != null) ? peptide.getPeptideEvidenceList().get(0).getStartPosition() : start;
            }
        }
        return start;
    }

    @Override
    public int getPeptideSequenceEnd(Comparable proteinId, Comparable peptideId) {
        int stop = -1;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                stop = (peptide.getPeptideEvidenceList().get(0).getEndPosition() != null) ? peptide.getPeptideEvidenceList().get(0).getEndPosition() : stop;
            }
        }
        return stop;
    }

    @Override
    public Comparable getPeptideSpectrumId(Comparable proteinId, Comparable peptideId) {
        Comparable specId = null;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                Spectrum spectrum = peptide.getSpectrum();
                if (spectrum != null) {
                    specId = spectrum.getId();
                }
            }
        }
        return specId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Modification> getPTMs(Comparable proteinId, Comparable peptideId) {
        List<Modification> mods = new ArrayList<Modification>();
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                List<Modification> rawMods = peptide.getPeptideSequence().getModifications();
                mods.addAll(rawMods);
            }
        }
        return mods;
    }

    @Override
    public Collection<SubstitutionModification> getSubstitutionPTMs(Comparable proteinId, Comparable peptideId) {
        List<SubstitutionModification> mods = new ArrayList<SubstitutionModification>();
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                List<SubstitutionModification> rawMods = peptide.getPeptideSequence().getSubstitutionModifications();
                mods.addAll(rawMods);
            }
        }
        return mods;
    }

    @Override
    public int getNumberOfSubstitutionPTMs(Comparable proteinId) {
        int cnt = 0;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            cnt = DataAccessUtilities.getNumberOfSubstitutionPTMs(protein);
        }
        return cnt;
    }

    @Override
    public int getNumberOfSubstitutionPTMs(Comparable proteinId, Comparable peptideId) {
        int cnt = 0;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            List<Peptide> peptides = protein.getPeptides();
            if (!peptides.isEmpty()) {
                Peptide peptide = peptides.get(Integer.parseInt(peptideId.toString()));
                if (peptide != null) {
                    cnt = DataAccessUtilities.getNumberOfSubstitutionPTMs(peptide);
                }
            }
        }
        return cnt;
    }

    @Override
    public int getNumberOfFragmentIons(Comparable proteinId, Comparable peptideId) {
        int num = 0;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                List<FragmentIon> ions = peptide.getFragmentation();
                if (ions != null) {
                    num = ions.size();
                }
            }
        }
        return num;
    }

    @Override
    public Collection<FragmentIon> getFragmentIons(Comparable proteinId, Comparable peptideId) {
        List<FragmentIon> frags = new ArrayList<FragmentIon>();
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                List<FragmentIon> rawFrags = peptide.getFragmentation();
                frags.addAll(rawFrags);
            }
        }
        return frags;
    }

    @Override
    public Score getPeptideScore(Comparable proteinId, Comparable peptideId) {
        Score score = null;
        Protein protein = getProteinById(proteinId);
        if (protein != null) {
            Peptide peptide = DataAccessUtilities.getPeptide(protein, Integer.parseInt(peptideId.toString()));
            if (peptide != null) {
                if (peptide.getSpectrumIdentification().getScore() == null) {
                    score = DataAccessUtilities.getScore(peptide.getSpectrumIdentification());
                    peptide.getSpectrumIdentification().setScore(score);
                } else score = peptide.getSpectrumIdentification().getScore();
            }
        }
        return score;
    }

    @Override
    public boolean hasQuantData() {
        Collection<QuantCvTermReference> methods = getQuantMethods();
        return methods.size() > 0;
    }

    @Override
    public boolean hasProteinQuantData() {
        Collection<QuantCvTermReference> methods = getQuantMethods();

        for (QuantCvTermReference method : methods) {
            if (QuantCvTermReference.containsProteinQuantification(method)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasPeptideQuantData() {
        Collection<QuantCvTermReference> methods = getQuantMethods();

        for (QuantCvTermReference method : methods) {
            if (QuantCvTermReference.containsPeptideQuantification(method)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasProteinTotalIntensities() {
        return getProteinQuantUnit() == null;
    }

    @Override
    public boolean hasPeptideTotalIntensities() {
        return getPeptideQuantUnit() == null;
    }

    @Override
    public boolean hasLabelFreeQuantMethods() {
        // get the samples
        ParamGroup additionals = getAdditional();

        if (additionals != null) {
            // iterate over each sample
            List<CvParam> cvParams = additionals.getCvParams();
            for (CvParam cvParam : cvParams) {
                if (QuantCvTermReference.isLabelFreeMethod(cvParam)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasIsotopeLabellingQuantMethods() {
        // get the samples
        ParamGroup additionals = getAdditional();

        if (additionals != null) {
            // iterate over each sample
            List<CvParam> cvParams = additionals.getCvParams();
            if (!cvParams.isEmpty()) {
                for (CvParam cvParam : cvParams) {
                    if (QuantCvTermReference.isIsotopeLabellingMethodParam(cvParam)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public Collection<QuantCvTermReference> getQuantMethods() {
        Set<QuantCvTermReference> methods = new LinkedHashSet<QuantCvTermReference>();

        // get the samples
        ParamGroup additionals = getAdditional();

        if (additionals != null) {
            // iterate over each sample
            List<CvParam> cvParams = additionals.getCvParams();
            if (!cvParams.isEmpty()) {
                for (CvParam cvParam : cvParams) {
                    if (QuantCvTermReference.isQuantitativeMethodParam(cvParam)) {
                        methods.add(QuantCvTermReference.getQuantitativeMethodParam(cvParam));
                    }
                }
            }
        }
        return methods;
    }

    @Override
    public Collection<QuantCvTermReference> getLabelFreeQuantMethods() {
        Set<QuantCvTermReference> methods = new LinkedHashSet<QuantCvTermReference>();

        // get the samples
        ParamGroup additionals = getAdditional();

        if (additionals != null) {
            // iterate over each sample
            List<CvParam> cvParams = additionals.getCvParams();
            if (!cvParams.isEmpty()) {
                for (CvParam cvParam : cvParams) {
                    if (QuantCvTermReference.isLabelFreeMethod(cvParam)) {
                        methods.add(QuantCvTermReference.getQuantitativeMethodParam(cvParam));
                    }
                }
            }

        }

        return methods;
    }

    @Override
    public Collection<QuantCvTermReference> getProteinLabelFreeQuantMethods() {
        Collection<QuantCvTermReference> methods = getLabelFreeQuantMethods();
        Collection<QuantCvTermReference> protMethods = new ArrayList<QuantCvTermReference>();

        for (QuantCvTermReference method : methods) {
            if (QuantCvTermReference.containsProteinQuantification(method)) {
                protMethods.add(method);
            }
        }

        return protMethods;
    }

    @Override
    public Collection<QuantCvTermReference> getPeptideLabelFreeQuantMethods() {
        Collection<QuantCvTermReference> methods = getLabelFreeQuantMethods();
        Collection<QuantCvTermReference> peptideMethods = new ArrayList<QuantCvTermReference>();

        for (QuantCvTermReference method : methods) {
            if (QuantCvTermReference.containsPeptideQuantification(method)) {
                peptideMethods.add(method);
            }
        }

        return peptideMethods;
    }

    @Override
    public Collection<QuantCvTermReference> getIsotopeLabellingQuantMethods() {
        Set<QuantCvTermReference> methods = new LinkedHashSet<QuantCvTermReference>();

        // get the samples
        ParamGroup additionals = getAdditional();

        if (additionals != null) {
            // iterate over each sample
            List<CvParam> cvParams = additionals.getCvParams();
            if (!cvParams.isEmpty()) {
                for (CvParam cvParam : cvParams) {
                    if (QuantCvTermReference.isIsotopeLabellingMethodParam(cvParam)) {
                        methods.add(QuantCvTermReference.getQuantitativeMethodParam(cvParam));
                    }
                }
            }
        }

        return methods;
    }

    @Override
    public Collection<QuantCvTermReference> getProteinIsotopeLabellingQuantMethods() {
        Collection<QuantCvTermReference> methods = getIsotopeLabellingQuantMethods();
        Collection<QuantCvTermReference> protMethods = new ArrayList<QuantCvTermReference>();

        for (QuantCvTermReference method : methods) {
            if (QuantCvTermReference.containsProteinQuantification(method)) {
                protMethods.add(method);
            }
        }

        return protMethods;
    }

    @Override
    public Collection<QuantCvTermReference> getPeptideIsotopeLabellingQuantMethods() {
        Collection<QuantCvTermReference> methods = getIsotopeLabellingQuantMethods();
        Collection<QuantCvTermReference> peptideMethods = new ArrayList<QuantCvTermReference>();

        for (QuantCvTermReference method : methods) {
            if (QuantCvTermReference.containsPeptideQuantification(method)) {
                peptideMethods.add(method);
            }
        }

        return peptideMethods;
    }

    @Override
    public int getNumberOfReagents() {
        int num = 0;

        if (hasIsotopeLabellingQuantMethods()) {
            // get samples
            Collection<Sample> samples = getSamples();

            if (samples != null) {
                for (Sample sample : samples) {
                    List<CvParam> cvParams = sample.getCvParams();
                    for (CvParam cvParam : cvParams) {
                        if (QuantCvTermReference.isReagent(cvParam)) {
                            num++;
                        }
                    }
                }
            }
        }

        return num;
    }

    @Override
    public int getReferenceSubSampleIndex() {
        int index = -1;

        if (hasIsotopeLabellingQuantMethods()) {
            int cnt = NUMBER_OF_PROTEIN_TO_CHECK;
            if (hasProteinQuantData()) {
                Collection<Comparable> proteinIds = getProteinIds();

                for (Comparable proteinId : proteinIds) {
                    Quantification quant = getProteinQuantData(proteinId);
                    if (quant.hasTotalIntensities()) {
                        return index;
                    } else {
                        index = quant.getReferenceSubSampleIndex();
                        if (index > 0) {
                            break;
                        }
                    }
                    cnt--;
                    if (cnt == 0) {
                        break;
                    }
                }
            } else if (hasPeptideQuantData()) {
                Collection<Comparable> proteinIds = getProteinIds();

                for (Comparable proteinId : proteinIds) {
                    Collection<Comparable> peptideIds = getPeptideIds(proteinId);
                    for (Comparable peptideId : peptideIds) {
                        Quantification quant = getPeptideQuantData(proteinId, peptideId);
                        if (quant.hasTotalIntensities()) {
                            return index;
                        } else {
                            index = quant.getReferenceSubSampleIndex();
                            if (index > 0) {
                                break;
                            }
                        }
                    }
                    cnt--;
                    if (cnt == 0) {
                        break;
                    }
                }
            }
        }

        return index;
    }

    @Override
    public QuantitativeSample getQuantSample() {
        QuantitativeSample sampleDesc = new QuantitativeSample();

        Collection<Sample> samples = getSamples();
        if (samples != null && !samples.isEmpty()) {
            Sample sample = CollectionUtils.getElement(samples, 0);
            List<CvParam> cvParams = sample.getCvParams();
            // scan for all the species
            if (!cvParams.isEmpty()) {
                for (CvParam cvParam : cvParams) {
                    String cvLabel = cvParam.getCvLookupID().toLowerCase();
                    if ("newt".equals(cvLabel)) {
                        sampleDesc.setSpecies(cvParam);
                    } else if ("bto".equals(cvLabel)) {
                        sampleDesc.setTissue(cvParam);
                    } else if ("cl".equals(cvLabel)) {
                        sampleDesc.setCellLine(cvParam);
                    } else if ("go".equals(cvLabel)) {
                        sampleDesc.setGOTerm(cvParam);
                    } else if ("doid".equals(cvLabel)) {
                        sampleDesc.setDisease(cvParam);
                    } else if (QuantCvTermReference.isSubSampleDescription(cvParam)) {
                        sampleDesc.setDescription(cvParam);
                    } else if (QuantCvTermReference.isReagent(cvParam)) {
                        sampleDesc.setReagent(cvParam);
                    }
                }
            }
        }

        return sampleDesc;
    }

    @Override
    public QuantCvTermReference getProteinQuantUnit() {
        Collection<Comparable> proteinIds = getProteinIds();

        int cnt = NUMBER_OF_PROTEIN_TO_CHECK;

        for (Comparable proteinId : proteinIds) {
            Quantification quant = getProteinQuantData(proteinId);
            QuantCvTermReference unit = quant.getUnit();
            cnt--;
            if (unit != null) {
                return unit;
            }

            if (cnt == 0) {
                break;
            }
        }
        return null;
    }

    @Override
    public QuantCvTermReference getPeptideQuantUnit() {
        Collection<Comparable> proteinIds = getProteinIds();

        int cnt = NUMBER_OF_PEPTIDE_TO_CHECK;

        for (Comparable proteinId : proteinIds) {
            Collection<Comparable> peptideIds = getPeptideIds(proteinId);
            for (Comparable peptideId : peptideIds) {
                Quantification quant = getPeptideQuantData(proteinId, peptideId);
                QuantCvTermReference unit = quant.getUnit();
                if (unit != null) {
                    return unit;
                }
            }
            cnt--;

            if (cnt == 0) {
                break;
            }
        }
        return null;
    }

    @Override
    public Quantification getProteinQuantData(Comparable proteinId) {
        Protein protein = getProteinById(proteinId);
        return new Quantification(Quantification.Type.PROTEIN, protein.getCvParams());
    }

    @Override
    public Quantification getPeptideQuantData(Comparable proteinId, Comparable peptideId) {
        Peptide peptide = getPeptideByIndex(proteinId, peptideId);
        return new Quantification(Quantification.Type.PEPTIDE, peptide.getSpectrumIdentification().getCvParams());
    }

    @Override
    public boolean hasMetaDataInformation() {
        return true;
    }
}
