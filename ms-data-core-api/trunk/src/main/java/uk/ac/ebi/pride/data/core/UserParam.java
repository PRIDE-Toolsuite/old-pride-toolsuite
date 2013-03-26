package uk.ac.ebi.pride.data.core;

/**
 * Uncontrolled user parameters, allowing free text.
 * Should be created with caution.
 * <p/>
 * User: rwang, yperez
 * Date: 27-Jan-2010
 * Time: 09:34:30
 */
public class UserParam extends Parameter {

    /**
     * the data type of the parameter
     */
    private String type;

    /**
     * Constructor
     *
     * @param name           required.
     * @param type           optional.
     * @param value          optional.
     * @param unitAcc        optional.
     * @param unitName       optional.
     * @param unitCVLookupID optional.
     */
    public UserParam(String name, String type, String value, String unitAcc, String unitName, String unitCVLookupID) {
        super(name, value, unitAcc, unitName, unitCVLookupID);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserParam userParam = (UserParam) o;

        return !(type != null ? !type.equals(userParam.type) : userParam.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}



