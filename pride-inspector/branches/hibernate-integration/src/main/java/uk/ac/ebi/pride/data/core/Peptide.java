package uk.ac.ebi.pride.data.core;

import java.util.List;

/**
 * Description of a peptide.
 *
 * User: rwang
 * Date: 08-Feb-2010
 * Time: 10:47:53
 */
public class Peptide extends ParamGroup {
    /** peptide sequence */
    private String sequence = null;
    /** the start position of the petitide in the protein */
    private int start = -1;
    /** the end position of the petitide in the protein */
    private int end = -1;
    /** a list of modifications */
    private List<Modification> modifications = null;
    /** a list of fragmentIons */
    private List<FragmentIon> fragmentIons = null;
    /** reference to the spectrum */
    private Spectrum spectrum = null;

    /**
     * Constructor
     * @param params    optional.
     * @param sequence  required.
     * @param start required.
     * @param end   required.
     * @param modifications optional.
     * @param fragmentIons  optional.
     * @param spectrum  optional.
     */
    public Peptide(ParamGroup params,
                   String sequence,
                   int start,
                   int end,
                   List<Modification> modifications,
                   List<FragmentIon> fragmentIons,
                   Spectrum spectrum) {
        super(params);
        setSequence(sequence);
        setStart(start);
        setEnd(end);
        setModifications(modifications);
        setFragmentIons(fragmentIons);
        setSpectrum(spectrum);
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        if (sequence == null) {
            throw new IllegalArgumentException("Peptide sequence can not be NULL");
        } else {
            this.sequence = sequence;
        }
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<Modification> getModifications() {
        return modifications;
    }

    public void setModifications(List<Modification> modifications) {
        this.modifications = modifications;
    }

    public List<FragmentIon> getFragmentIons() {
        return fragmentIons;
    }

    public void setFragmentIons(List<FragmentIon> fragmentIons) {
        this.fragmentIons = fragmentIons;
    }

    public Spectrum getSpectrum() {
        return spectrum;
    }

    public void setSpectrum(Spectrum spectrum) {
        this.spectrum = spectrum;
    }

    public int getSequenceLength() {
        if (sequence != null) {
            return sequence.length();
        } else {
            return 0;
        }
    }
}