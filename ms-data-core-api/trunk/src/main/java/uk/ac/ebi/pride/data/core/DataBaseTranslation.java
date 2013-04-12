package uk.ac.ebi.pride.data.core;

import uk.ac.ebi.pride.data.utils.CollectionUtils;

import java.util.List;

/**
 * DataBaseTranslation control and storage the information of Nucleotide Databases.
 * <p/>
 * User: yperez
 * Date: 05/08/11
 * Time: 17:36
 */
public class DataBaseTranslation {

    private final List<Integer> allowedFrames;

    private final List<IdentifiableParamGroup> translationTables;

    public DataBaseTranslation(List<Integer> allowedFrames, List<IdentifiableParamGroup> translationTables) {
        this.allowedFrames = CollectionUtils.createListFromList(allowedFrames);
        this.translationTables = CollectionUtils.createListFromList(translationTables);
    }

    public List<Integer> getAllowedFrames() {
        return allowedFrames;
    }

    public void setAllowedFrames(List<Integer> allowedFrames) {
        CollectionUtils.replaceValuesInCollection(allowedFrames, this.allowedFrames);
    }

    public List<IdentifiableParamGroup> getTranslationTables() {
        return translationTables;
    }

    public void setTranslationTables(List<IdentifiableParamGroup> translationTables) {
        CollectionUtils.replaceValuesInCollection(translationTables, this.translationTables);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataBaseTranslation)) return false;

        DataBaseTranslation that = (DataBaseTranslation) o;

        if (!allowedFrames.equals(that.allowedFrames)) return false;
        if (!translationTables.equals(that.translationTables)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = allowedFrames.hashCode();
        result = 31 * result + translationTables.hashCode();
        return result;
    }
}



