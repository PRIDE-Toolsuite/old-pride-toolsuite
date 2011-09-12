package uk.ac.ebi.pride.gui.event;

import org.bushe.swing.event.AbstractEventServiceEvent;
import uk.ac.ebi.pride.data.coreIdent.Quantitation;

/**
 * Event to notify that a quantitation has been selected
 *
 * User: rwang
 * Date: 19/08/2011
 * Time: 16:59
 */
public class QuantSelectionEvent extends AbstractEventServiceEvent {
    public enum Type {PROTIEN, PEPTIDE}

    private Comparable id;
    private int referenceSampleIndex;
    private Type type;
    private boolean selected;

    public QuantSelectionEvent(Object source,
                               Comparable id, int refSampleIndex,
                               Type type, boolean selected) {
        super(source);
        this.id= id;
        this.referenceSampleIndex = refSampleIndex;
        this.type = type;
        this.selected = selected;
    }

    public Comparable getId() {
        return id;
    }

    public void setId(Comparable id) {
        this.id = id;
    }

    public int getReferenceSampleIndex() {
        return referenceSampleIndex;
    }

    public void setReferenceSampleIndex(int referenceSampleIndex) {
        this.referenceSampleIndex = referenceSampleIndex;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
