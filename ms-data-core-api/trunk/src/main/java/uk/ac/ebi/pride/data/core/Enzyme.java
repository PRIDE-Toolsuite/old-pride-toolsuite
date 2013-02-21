package uk.ac.ebi.pride.data.core;

/**
 * todo: add documentation
 * Created by IntelliJ IDEA.
 * User: yperez
 * Date: 05/08/11
 * Time: 16:16
 */
public class Enzyme {
    private String     cTermGain       = null;
    private ParamGroup enzymeName      = null;
    private String     id              = null;
    private int        minDistance     = -1;
    private int        missedCleavages = -1;
    private String     nTermGain       = null;
    private String     name            = null;
    private boolean    semiSpecific    = false;
    private String     siteRegExp      = null;

    public Enzyme(String id, String name, boolean semiSpecific, int missedCleavages, int minDistance,
                  ParamGroup enzymeName, String siteRegExp) {
        this.id              = id;
        this.name            = name;
        this.semiSpecific    = semiSpecific;
        this.missedCleavages = missedCleavages;
        this.minDistance     = minDistance;
        this.enzymeName      = enzymeName;
        this.siteRegExp      = siteRegExp;
    }

    public String getSiteRegExp() {
        return siteRegExp;
    }

    public void setSiteRegExp(String siteRegExp) {
        this.siteRegExp = siteRegExp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSemiSpecific() {
        return semiSpecific;
    }

    public void setSemiSpecific(boolean semiSpecific) {
        this.semiSpecific = semiSpecific;
    }

    public int getMissedCleavages() {
        return missedCleavages;
    }

    public void setMissedCleavages(int missedCleavages) {
        this.missedCleavages = missedCleavages;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public ParamGroup getEnzymeName() {
        return enzymeName;
    }

    public void setEnzymeName(ParamGroup enzymeName) {
        this.enzymeName = enzymeName;
    }

    public String getnTermGain() {
        return nTermGain;
    }

    public void setnTermGain(String nTermGain) {
        this.nTermGain = nTermGain;
    }

    public String getcTermGain() {
        return cTermGain;
    }

    public void setcTermGain(String cTermGain) {
        this.cTermGain = cTermGain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Enzyme enzyme = (Enzyme) o;

        if (minDistance != enzyme.minDistance) return false;
        if (missedCleavages != enzyme.missedCleavages) return false;
        if (semiSpecific != enzyme.semiSpecific) return false;
        if (cTermGain != null ? !cTermGain.equals(enzyme.cTermGain) : enzyme.cTermGain != null) return false;
        if (enzymeName != null ? !enzymeName.equals(enzyme.enzymeName) : enzyme.enzymeName != null) return false;
        if (id != null ? !id.equals(enzyme.id) : enzyme.id != null) return false;
        if (nTermGain != null ? !nTermGain.equals(enzyme.nTermGain) : enzyme.nTermGain != null) return false;
        if (name != null ? !name.equals(enzyme.name) : enzyme.name != null) return false;
        if (siteRegExp != null ? !siteRegExp.equals(enzyme.siteRegExp) : enzyme.siteRegExp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cTermGain != null ? cTermGain.hashCode() : 0;
        result = 31 * result + (enzymeName != null ? enzymeName.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + minDistance;
        result = 31 * result + missedCleavages;
        result = 31 * result + (nTermGain != null ? nTermGain.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (semiSpecific ? 1 : 0);
        result = 31 * result + (siteRegExp != null ? siteRegExp.hashCode() : 0);
        return result;
    }
}



