package uk.ac.ebi.pride.data.core;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * Reference is added by PRIDE XML 2.0., also a generic reference for mzIdentML References.
 * <p/>
 * This object represents the information for MzIdentMl and PRIDE Reference Objects.
 * <p/>
 * User: rwang
 * Date: 27-Jan-2010
 * Time: 09:45:42
 */
public class Reference extends IdentifiableParamGroup {

    /**
     * The names of the authors of the reference.
     */
    private String authors = null;

    /**
     * The DOI of the referenced publication.
     */
    private String doi = null;

    /**
     * The editor(s) of the reference.
     */
    private String editor = null;

    /**
     * the full reference line used by PRIDE XML Objects
     */
    private String fullReference = null;

    /**
     * The issue name or number.
     */
    private String issue = null;

    /**
     * The page numbers.
     */
    private String pages = null;

    /**
     * The name of the journal, book etc.
     */
    private String publication = null;

    /**
     * The publisher of the publication.
     */
    private String publisher = null;

    /**
     * The title of the BibliographicReference.
     */
    private String title = null;

    /**
     * The volume name or number.
     */
    private String volume = null;

    /**
     * The year of publication.
     */
    private String year = null;

    /**
     * Constructors for PRIDE XML Reference Object
     *
     * @param params
     * @param fullReference
     */
    public Reference(ParamGroup params, String fullReference) {
        this(params, null, null, null, null, null, null, null, null, null, null, null, null, fullReference);
    }

    /**
     * Constructors for PRIDE XML Reference Object
     *
     * @param cvParams
     * @param userParams
     * @param id
     * @param name
     * @param fullReference
     */
    public Reference(List<CvParam> cvParams, List<UserParam> userParams, String id, String name, String fullReference) {
        this(new ParamGroup(cvParams, userParams), id, name, null, null, null, null, null, null, null, null, null,
             null, fullReference);
    }

    public Reference(String id, String name, String doi, String title, String pages, String issue, String volume,
                     String year, String editor, String publisher, String publication, String authors,
                     String fullReference) {
        this(null, id, name, doi, title, pages, issue, volume, year, editor, publisher, publication, authors,
             fullReference);
    }

    public Reference(ParamGroup params, String id, String name, String doi, String title, String pages, String issue,
                     String volume, String year, String editor, String publisher, String publication, String authors,
                     String fullReference) {
        super(params, id, name);
        this.doi           = doi;
        this.title         = title;
        this.pages         = pages;
        this.issue         = issue;
        this.volume        = volume;
        this.year          = year;
        this.editor        = editor;
        this.publisher     = publisher;
        this.publication   = publication;
        this.authors       = authors;
        this.fullReference = fullReference;
    }

    public Reference(List<CvParam> cvParams, List<UserParam> userParams, String id, String name, String doi,
                     String title, String pages, String issue, String volume, String year, String editor,
                     String publisher, String publication, String authors, String fullReference) {
        this(new ParamGroup(cvParams, userParams), id, name, doi, title, pages, issue, volume, year, editor, publisher,
             publication, authors, fullReference);
    }

    public String getFullReference() {
        return fullReference;
    }

    public void setFullReference(String fullReference) {
        this.fullReference = fullReference;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Reference reference = (Reference) o;

        if (authors != null ? !authors.equals(reference.authors) : reference.authors != null) return false;
        if (doi != null ? !doi.equals(reference.doi) : reference.doi != null) return false;
        if (editor != null ? !editor.equals(reference.editor) : reference.editor != null) return false;
        if (fullReference != null ? !fullReference.equals(reference.fullReference) : reference.fullReference != null)
            return false;
        if (issue != null ? !issue.equals(reference.issue) : reference.issue != null) return false;
        if (pages != null ? !pages.equals(reference.pages) : reference.pages != null) return false;
        if (publication != null ? !publication.equals(reference.publication) : reference.publication != null)
            return false;
        if (publisher != null ? !publisher.equals(reference.publisher) : reference.publisher != null) return false;
        if (title != null ? !title.equals(reference.title) : reference.title != null) return false;
        if (volume != null ? !volume.equals(reference.volume) : reference.volume != null) return false;
        if (year != null ? !year.equals(reference.year) : reference.year != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + (doi != null ? doi.hashCode() : 0);
        result = 31 * result + (editor != null ? editor.hashCode() : 0);
        result = 31 * result + (fullReference != null ? fullReference.hashCode() : 0);
        result = 31 * result + (issue != null ? issue.hashCode() : 0);
        result = 31 * result + (pages != null ? pages.hashCode() : 0);
        result = 31 * result + (publication != null ? publication.hashCode() : 0);
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (volume != null ? volume.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        return result;
    }
}



