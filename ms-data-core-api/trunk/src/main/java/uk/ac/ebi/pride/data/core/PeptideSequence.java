package uk.ac.ebi.pride.data.core;

import uk.ac.ebi.pride.data.utils.CollectionUtils;
import uk.ac.ebi.pride.mol.AminoAcid;

import java.util.ArrayList;
import java.util.List;

/**
 * One (poly)peptide (a sequence with modifications). The combination of Peptide sequence
 * and modifications must be unique in the file.
 * User: yperez
 * Date: 04/08/11
 * <p/>
 */
public class PeptideSequence extends IdentifiableParamGroup implements Comparable {

    /**
     * A molecule modification specification. If n modifications have been found on a peptide,
     * there should be n instances of Modification.
     */
    private final List<Modification> modifications;

    /**
     * The amino acid sequence of the (poly)peptide. If a substitution modification has been found,
     * the original sequence should be reported.
     */
    private String sequence;

    /**
     * A modification where one residue is substituted by another (amino acid change).
     * This attribute is used by the MzIdentMl Peptide Object.
     */
    private final List<SubstitutionModification> substitutionModifications;

    public PeptideSequence(String id, String name, String sequence, List<Modification> modifications) {
        this(null, id, name, sequence, modifications, null);
    }

    public PeptideSequence(ParamGroup params, String id, String name, String sequence,
                           List<Modification> modifications) {
        this(params, id, name, sequence, modifications, null);
    }

    public PeptideSequence(String id, String name, String sequence, List<Modification> modifications,
                           List<SubstitutionModification> substitutionModifications) {
        this(null, id, name, sequence, modifications, substitutionModifications);
    }

    public PeptideSequence(List<CvParam> cvParams, List<UserParam> userParams, String id, String name, String sequence,
                           List<Modification> modifications) {
        this(new ParamGroup(cvParams, userParams), id, name, sequence, modifications, null);
    }

    public PeptideSequence(List<CvParam> cvParams, List<UserParam> userParams, String id, String name, String sequence,
                           List<Modification> modifications,
                           List<SubstitutionModification> substitutionModifications) {
        this(new ParamGroup(cvParams, userParams), id, name, sequence, modifications, substitutionModifications);
    }

    public PeptideSequence(ParamGroup params, String id, String name, String sequence,
                           List<Modification> modifications,
                           List<SubstitutionModification> substitutionModifications) {
        super(params, id, name);
        this.sequence = sequence;
        this.modifications = CollectionUtils.createListFromList(modifications);
        this.substitutionModifications = CollectionUtils.createListFromList(substitutionModifications);
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<Modification> getModifications() {
        return modifications;
    }

    public void setModifications(List<Modification> modifications) {
        CollectionUtils.replaceValuesInCollection(modifications, this.modifications);
    }

    public List<SubstitutionModification> getSubstitutionModifications() {
        return substitutionModifications;
    }

    public void setSubstitutionModifications(List<SubstitutionModification> substitutionModifications) {
        CollectionUtils.replaceValuesInCollection(substitutionModifications, this.substitutionModifications);
    }

    public List<AminoAcid> getAminoAcidList() {
        List<AminoAcid> sequenceList = new ArrayList<AminoAcid>();
        for (Character character : sequence.toCharArray()) {
            sequenceList.add(AminoAcid.getAminoAcid(character));
        }
        return sequenceList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeptideSequence)) return false;
        if (!super.equals(o)) return false;

        PeptideSequence that = (PeptideSequence) o;

        if (!modifications.equals(that.modifications)) return false;
        return !(sequence != null ? !sequence.equals(that.sequence) : that.sequence != null) && substitutionModifications.equals(that.substitutionModifications);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + modifications.hashCode();
        result = 31 * result + (sequence != null ? sequence.hashCode() : 0);
        result = 31 * result + substitutionModifications.hashCode();
        return result;
    }

    @Override
    public int compareTo(Object o) {
        return (((PeptideSequence) o).getSequence().compareToIgnoreCase(this.getSequence()));
    }

    @Override
    public String toString() {
        return sequence;
    }
}



