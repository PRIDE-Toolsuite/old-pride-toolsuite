package uk.ac.ebi.pride.data.core;

import uk.ac.ebi.pride.data.utils.CollectionUtils;

import java.util.List;

/**
 * A database for searching mass spectra. Examples include a set of amino
 * acid sequence entries, or annotated spectra libraries.
 * <p>The SearchDataBase Element contains contains the following members:
 * - version: Database Version
 * - Release Database
 * - name of the database: CvTerms and CvParm
 * - Description of the DataBase.
 * </p>
 * User: yperez
 * Date: 08/08/11
 * Time: 11:52
 */
public class SearchDataBase extends ExternalData {

    /**
     * Description of the database with CVTerms
     */
    private List<CvParam> description;

    /**
     * The database name may be given as a cvParam if it maps exactly to one
     * of the release databases listed in the CV, otherwise a userParam should be
     * used.
     */
    private ParamGroup nameDatabase;

    /**
     * The number of residues in the database.
     */
    private long numDatabaseResidue;

    /**
     * The total number of sequences in the database.
     */
    private int numDatabaseSequence;

    /**
     * The date and time the database was released to the public; omit this
     * attribute when the date and time are unknown or not applicable
     * (e.g. custom databases).
     */
    private String releaseDate;

    /**
     * The version of the database.
     */
    private String version;

    public SearchDataBase(String name, String databaseVersion) {
        this(null, name, null, null, null, databaseVersion, null, -1, -1, null, null);
    }

    public SearchDataBase(String id, String name, String location, CvParam fileFormat,
                          String externalFormatDocumentationURI, String version, String releaseDate,
                          int numDatabaseSequence, long numDatabaseResidue, ParamGroup nameDatabase,
                          List<CvParam> description) {
        super(id, name, location, fileFormat, externalFormatDocumentationURI);
        this.version             = version;
        this.releaseDate         = releaseDate;
        this.numDatabaseSequence = numDatabaseSequence;
        this.numDatabaseResidue  = numDatabaseResidue;
        this.nameDatabase        = nameDatabase;
        this.description         = CollectionUtils.createListFromList(description);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getNumDatabaseSequence() {
        return numDatabaseSequence;
    }

    public void setNumDatabaseSequence(int numDatabaseSequence) {
        this.numDatabaseSequence = numDatabaseSequence;
    }

    public long getNumDatabaseResidue() {
        return numDatabaseResidue;
    }

    public void setNumDatabaseResidue(long numDatabaseResidue) {
        this.numDatabaseResidue = numDatabaseResidue;
    }

    public ParamGroup getNameDatabase() {
        return nameDatabase;
    }

    public void setNameDatabase(ParamGroup nameDatabase) {
        this.nameDatabase = nameDatabase;
    }

    public List<CvParam> getDescription() {
        return description;
    }

    public void setDescription(List<CvParam> description) {
        CollectionUtils.replaceValuesInCollection(description, this.description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchDataBase)) return false;
        if (!super.equals(o)) return false;

        SearchDataBase that = (SearchDataBase) o;

        if (numDatabaseResidue != that.numDatabaseResidue) return false;
        if (numDatabaseSequence != that.numDatabaseSequence) return false;
        if (!description.equals(that.description)) return false;
        if (nameDatabase != null ? !nameDatabase.equals(that.nameDatabase) : that.nameDatabase != null) return false;
        if (releaseDate != null ? !releaseDate.equals(that.releaseDate) : that.releaseDate != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (nameDatabase != null ? nameDatabase.hashCode() : 0);
        result = 31 * result + (int) (numDatabaseResidue ^ (numDatabaseResidue >>> 32));
        result = 31 * result + numDatabaseSequence;
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}



