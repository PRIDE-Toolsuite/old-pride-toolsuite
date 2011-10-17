package uk.ac.ebi.pride.data.core;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * <p/>
 * Description of source file, including identification file, location and type.
 * the attributes fileFormat is used to manage the cvterm for file format in MzIdentMl
 * and the externalFormatDocumentURI is used to store the information of the external shcema.
 * <p/>
 * In mzML 1.1.0.1, the following cv terms must be added:
 * <p/>
 * 1. Must include only one a child term of "native spectrum identifier format"
 * (thermo nativeID format, waters nativeID format, WIFF nativeID format and et al)
 * <p/>
 * 2. Must include one or more child terms of "data file check-sum type" (MD5, SHA-1)
 * <p/>
 * 3. Must include only one child term of "source file type" (waters raw file,
 * ABI WIFF file, Thermo RAW file and et al)
 * <p/>
 * User: rwang, yperez
 * Date: 04-Feb-2010
 * Time: 15:53:36
 */
public class SourceFile extends IdentifiableParamGroup {

    /**
     * A URI to access documentation and tools to interpret the external format
     * of the ExternalData instance. For example, XML Schema or static libraries
     * (APIs) to access binary formats.
     */
    private String externalFormatDocumentationURI = null;

    /**
     * The format of the ExternalData file, for example "tiff" for image files.
     */
    private CvParam fileFormat = null;

    /**
     * location of the source file
     */
    private String path = null;

    /**
     * Constructor for special cases were you don't have and Id
     *
     * @param name Name
     * @param path location
     */
    public SourceFile(String name, String path) {

        // there should be a single source file per spectrum
        super("", name);
        this.path = path;
    }

    /**
     * Constructor to the MzMl File Source
     *
     * @param params ParamGroup (CvTerms and User Params)
     * @param id  ID
     * @param name Name
     * @param path location of the source File
     */
    public SourceFile(ParamGroup params, String id, String name, String path) {
        super(params, id, name);
        this.path = path;
    }

    /**
     * Constructor of MzIdentMl source File
     *
     * @param params ParamGroup (CvTerms and User Params)
     * @param id  ID
     * @param name Name
     * @param path location of the source File
     * @param fileFormat CvTerm to define the FileFormat
     * @param externalFormatDocumentationURI the external uri of the Format Documentation
     */
    public SourceFile(ParamGroup params, String id, String name, String path, CvParam fileFormat,
                      String externalFormatDocumentationURI) {
        super(params, id, name);
        this.path                           = path;
        this.fileFormat                     = fileFormat;
        this.externalFormatDocumentationURI = externalFormatDocumentationURI;
    }

    /**
     * Get the location of the File
     *
     * @return location
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the location
     *
     * @param path location of the file
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Get CvParam to define the File Format
     *
     * @return CvParam
     */
    public CvParam getFileFormat() {
        return fileFormat;
    }

    /**
     * Set CvParam to define the File Format
     *
     * @param fileFormat CvParam
     */
    public void setFileFormat(CvParam fileFormat) {
        this.fileFormat = fileFormat;
    }

    /**
     * Get the external uri of the File Format Documentation
     *
     * @return uri of the File Format Documentation
     */
    public String getExternalFormatDocumentationURI() {
        return externalFormatDocumentationURI;
    }

    /**
     * Get the external uri of the File Format Documentation
     *
     * @param externalFormatDocumentationURI uri of the File Format Documentation
     */
    public void setExternalFormatDocumentationURI(String externalFormatDocumentationURI) {
        this.externalFormatDocumentationURI = externalFormatDocumentationURI;
    }
}


