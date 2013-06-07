package uk.ac.ebi.pride.data.controller.cache;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashMap;

/**
 * CacheCategory provides a list of cache categories
 * Each category defines the type of the data and the type of data structure to store the data.
 * <p/>
 * User: rwang
 * Date: 07-Sep-2010
 * Time: 10:52:54
 */
public enum CacheEntry {
    SPECTRUM(CachedMap.class, 10),                                   // Map<Spectrum id, Spectrum>
    SPECTRADATA_TO_SPECTRUMIDS(HashMap.class,null),               // Map<Comparable, List<Comparable>>
    PROTEIN_TO_PEPTIDE_EVIDENCES(HashMap.class, null),       //Map<db squence id,List<Spectrum identification item id>>>
    PROTEIN_TO_PROTEIN_GROUP_ID(HashMap.class,null),            // Map<Comparable, Comparable>
    PROTEIN_GROUP_ID(ArrayList.class, null),                         // List of Protein Groups
    CHROMATOGRAM(CachedMap.class, 10),                             // Map<Chromatogram id, Chromatogram>
    PROTEIN(CachedMap.class, 10),                               // Map<Identification id, Identification>
    PEPTIDE(CachedMap.class, 10),                                      // Map<Tuple<Comparable, Comparable>, Peptide>
    EXPERIMENT_ACC(ArrayList.class, null),                           // List<Experiement Accession>
    EXPERIMENT_METADATA(ArrayList.class, null),                     // List<Experiment Metadata>
    PROTEIN_METADATA(ArrayList.class, null),                  // ToDo: document
    MZGRAPH_METADATA(ArrayList.class, null),                       // ToDo: document
    SEARCH_ENGINE_TYPE(ArrayList.class, null),                       // List<SearchEngineTypes>
    PROTEIN_LEVEL_SCORES(ArrayList.class, null),                       // List<CvTermReference>
    PEPTIDE_LEVEL_SCORES(ArrayList.class, null),                       // List<CvTermReference>
    SPECTRUM_ID(ArrayList.class, null),                               // List<Spectrum id>
    CHROMATOGRAM_ID(ArrayList.class, null),                         // List<Chromatogram id>
    PROTEIN_ID(ArrayList.class, null),                           // List<Identification id>
    MS_LEVEL(HashMap.class, null),                                     // Map<Spectrum id, Ms level>
    SPECTRUM_LEVEL_PRECURSOR_CHARGE(HashMap.class, null),                          // Map<Spectrum id, Precursor charge>
    SPECTRUM_LEVEL_PRECURSOR_MZ(HashMap.class, null),                               // Map<Spectrum id, Precursor m/z>
    PRECURSOR_INTENSITY(HashMap.class, null),                        // Map<Spectrum id, Precursor intensity>
    PROTEIN_ACCESSION(HashMap.class, null),                           // Map<Identification id, Protein accession>
    PROTEIN_ACCESSION_VERSION(HashMap.class, null),                 // Map<Identification id, Protein accession version>
    PROTEIN_SEARCH_DATABASE(HashMap.class, null),                   // Map<Identification id, Protein search database>
    PROTEIN_SEARCH_DATABASE_VERSION(HashMap.class, null),           // Map<Identification id, Protein search database version>
    SCORE(HashMap.class, null),                                         // Map<Identification id, Score>
    THRESHOLD(HashMap.class, null),                                   // Map<Identification id, Threshold>
    PROTEIN_TO_PARAM(HashMap.class, null),                     // Map<Identification id, ParamGroup>
    PROTEIN_TO_PEPTIDE(HashMap.class, null),                    // Map<Identification id, List<Peptide id>>
    PEPTIDE_SEQUENCE(HashMap.class, null),                            // Map<Peptide Id, peptide sequence>
    PEPTIDE_START(HashMap.class, null),                               // Map<Peptide Id, peptide start location>
    PEPTIDE_END(HashMap.class, null),                                  // Map<Peptide Id, peptide end location>
    PEPTIDE_TO_SPECTRUM(HashMap.class, null),                         // Map<Peptide Id, spectrum id>  in mzidentml the spectrum have two
                                                                          // components the spectrum id and the file id, then is Ma<Peptide Id, String[]>
    PEPTIDE_TO_PARAM(HashMap.class, null),                            // Map<Peptide Id, ParamGroup>
    NUMBER_OF_FRAGMENT_IONS(HashMap.class, null),                  // Map<Peptide Id, number of fragment ions>
    PEPTIDE_TO_MODIFICATION(HashMap.class, null),                     // Map<Peptide Id, List<Tuple<Accession, location>>>
    MODIFICATION(HashMap.class, null),                                 // Map<Accession, Modification>, a light weight implementation
    SUM_OF_INTENSITY(HashMap.class, null),                             // Map<Spectrum id, sum of all intensity>
    NUMBER_OF_PEAKS(HashMap.class, null),                             // Map<Spectrum id, number of peaks>

    PROTEIN_QUANT_UNIT(ArrayList.class, null), //List<QuantCvTermReference>
    PEPTIDE_QUANT_UNIT(ArrayList.class, null), // List<QuantCvTermReference>
    FRAGMENTATION_TABLE (HashMap.class, null),  // Map<Fragmentation id, IdentifiableParamGroup>
    CV_LOOKUP (HashMap.class, null),  // Map<cv label, CVLookup>
    SPECTRA_DATA (HashMap.class, null); // Map<Spectra data id, SpectraData>

    private final Class   dataStructType;
    private final Integer size;

    private CacheEntry(Class dataStructType, Integer size) {
        this.dataStructType = dataStructType;
        this.size           = size;
    }

    public Class getDataStructType() {
        return dataStructType;
    }

    public Integer getSize() {
        return size;
    }


}



