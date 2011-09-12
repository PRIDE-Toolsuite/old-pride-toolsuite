package uk.ac.ebi.pride.gui.task.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.DataAccessException;
import uk.ac.ebi.pride.data.coreIdent.Peptide;
import uk.ac.ebi.pride.data.coreIdent.Spectrum;
import uk.ac.ebi.pride.gui.component.exception.ThrowableEntry;
import uk.ac.ebi.pride.gui.component.message.MessageType;

/**
 * Created by IntelliJ IDEA.
 * User: rwang
 * Date: 10/06/11
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public class RetrieveSpectrumTask extends AbstractDataAccessTask<Spectrum, Void> {
    private static final Logger logger = LoggerFactory.getLogger(RetrievePeptideTask.class);

    private Comparable identId;
    private Comparable peptideId;
    private Comparable spectrumId;

    public RetrieveSpectrumTask(DataAccessController controller, Comparable spectrumId) {
        super(controller);
        this.spectrumId = spectrumId;
    }

    public RetrieveSpectrumTask(DataAccessController controller, Comparable identId, Comparable peptideId) {
        super(controller);
        this.identId = identId;
        this.peptideId = peptideId;
        this.setName("Loading Spectrum");
        this.setDescription("Loading Spectrum [ID: " + peptideId + "]");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Spectrum retrieve() throws Exception {
        if (spectrumId != null) {
            return getSpectrum(spectrumId);
        } else {
            return getSpectrum(identId, peptideId);
        }
    }

    /**
     * Get spectrum by spectrum id
     *
     * @param spectrumId    spectrum id
     * @return  Spectrum    spectrum
     */
    private Spectrum getSpectrum(Comparable spectrumId) {
        Spectrum result = null;

        try {
            result = controller.getSpectrumById(spectrumId);
        } catch (DataAccessException dex) {
            String msg = "Failed to retrieve data entry from data source";
            logger.error(msg, dex);
            appContext.addThrowableEntry(new ThrowableEntry(MessageType.ERROR, msg, dex));
        }

        return result;
    }

    /**
     * Get spectrum by identification id and peptide id
     *
     * @param identId   protein identification id
     * @param peptideId peptide id
     * @return  Spectrum    spectrum
     */
    private Spectrum getSpectrum(Comparable identId, Comparable peptideId) {
        Peptide peptide = null;
        try {
            peptide = controller.getPeptideById(identId, peptideId);
        } catch (DataAccessException dex) {
            String msg = "Failed to retrieve peptide";
            logger.error(msg, dex);
            appContext.addThrowableEntry(new ThrowableEntry(MessageType.ERROR, msg, dex));
        }

        Spectrum spectrum = null;
        if (peptide != null) {
            // get spectrum
            spectrum = peptide.getSpectrum();
            if (spectrum != null) {
                // reassign peptide, this is for fragmented ions and modifications
                spectrum.setPeptide(peptide);
            }
        }
        return spectrum;
    }
}
