package uk.ac.ebi.pride.data.core;

import java.util.List;

/**
 * The class Peptide Manage the information for peptide Identifications
 * User: yperez
 * Date: 08/08/11
 * Time: 12:39
 */
public class Peptide {

    PeptideEvidence peptideEvidence = null;

    SpectrumIdentification spectrumIdentification = null;

    public Peptide(PeptideEvidence peptideEvidence, SpectrumIdentification spectrumIdentification){
        this.peptideEvidence = peptideEvidence;
        this.spectrumIdentification = spectrumIdentification;
    }

    public PeptideEvidence getPeptideEvidence() {
        return peptideEvidence;
    }

    public void setPeptideEvidence(PeptideEvidence peptideEvidence) {
        this.peptideEvidence = peptideEvidence;
    }

    public SpectrumIdentification getSpectrumIdentification() {
        return spectrumIdentification;
    }

    public void setSpectrumIdentification(SpectrumIdentification spectrumIdentification) {
        this.spectrumIdentification = spectrumIdentification;
    }

    public PeptideSequence getPeptideSequence() {
        return this.getPeptideEvidence().getPeptideSequence();
    }

    public Spectrum getSpectrum() {
        return this.getSpectrumIdentification().getSpectrum();
    }

    public void setSpectrum(Spectrum spectrum){
        this.getSpectrumIdentification().setSpectrum(spectrum);
    }

    public List<PeptideEvidence> getPeptideEvidenceList() {
        return this.getSpectrumIdentification().getPeptideEvidenceList();
    }

    public List<FragmentIon> getFragmentation() {
        return this.getSpectrumIdentification().getFragmentation();
    }

    public int getSequenceLength() {
        return this.getPeptideSequence().getSequence().length();
    }

    public List<Modification> getModifications() {
        return this.getPeptideEvidence().getPeptideSequence().getModificationList();
    }

    public String getSequence() {
        return this.getPeptideSequence().getSequence();
    }

    public boolean hasModification() {
        if(this.getPeptideSequence().getModificationList() == null) return false;
        if(this.getPeptideSequence().getModificationList().size() == 0) return false;
        return true;
    }

    public int getPrecursorCharge() {
        return this.getSpectrumIdentification().getChargeState();
    }

    public double getPrecursorMz() {
        return this.getSpectrumIdentification().getExperimentalMassToCharge();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Peptide peptide = (Peptide) o;

        if (peptideEvidence != null ? !peptideEvidence.equals(peptide.peptideEvidence) : peptide.peptideEvidence != null)
            return false;
        if (spectrumIdentification != null ? !spectrumIdentification.equals(peptide.spectrumIdentification) : peptide.spectrumIdentification != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = peptideEvidence != null ? peptideEvidence.hashCode() : 0;
        result = 31 * result + (spectrumIdentification != null ? spectrumIdentification.hashCode() : 0);
        return result;
    }
}



