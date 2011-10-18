package uk.ac.ebi.pride.data.core;

/**
 * Description of the default peak processing method.
 * Variable methods should be described in the appropriate acquisition section.
 * if no acquisition-specific details are found, then this information serves as
 * the default.
 * <p/>
 * In mzML 1.1.0.1, cvParams has the following semantic requirements:
 * 1. May have one or more "data processing parameter" (low intensity threshold,
 * high intensity threshold, completion time, inclusive low intensity threshold,
 * inclusive high intensity threshold)
 * <p/>
 * 2. Must have one or more "data transformation" (deisotoping, charge deconvolution,
 * Conversion to mzML, Conversion to mxXML, Conversion to mzData, baseline reduction,
 * low intensity data point removal, conversion to dta, retention time alignment,
 * high intensity data point removal and et al.
 * <p/>
 * <p/>
 * User: rwang
 * Date: 04-Feb-2010
 * Time: 16:58:59
 */
public class ProcessingMethod extends ParamGroup {

    /**
     * order of steps
     */
    private int order = -1;

    /**
     * software type
     */
    private Software software = null;

    /**
     * Constructor
     *
     * @param order    required.
     * @param software required.
     * @param params   optional.
     */
    public ProcessingMethod(int order, Software software, ParamGroup params) {
        super(params);
        this.order    = order;
        this.software = software;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }
}



