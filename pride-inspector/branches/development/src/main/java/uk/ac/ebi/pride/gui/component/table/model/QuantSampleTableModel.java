package uk.ac.ebi.pride.gui.component.table.model;

import uk.ac.ebi.pride.data.core.QuantitativeSample;

import java.util.ArrayList;
import java.util.List;

/**
 * Quantitative sample table model, which contains all the date related quantitative sub samples
 * <p/>
 * User: rwang
 * Date: 11/08/2011
 * Time: 10:13
 */
public class QuantSampleTableModel extends ListTableModel<QuantitativeSample> {

    /**
     * table column title
     */
    public enum TableHeader {
        ROW_NUMBER_COLUMN("#", "Row Number"),
        SAMPLE_REAGENT("Reagent", "Isotope Labelling Reagent"),
        SAMPLE_SPECIES("Species", "Species"),
        SAMPLE_TISSUE("Tissue", "Tissue"),
        SAMPLE_CELL_LINE("Cell", "Cell"),
        SAMPLE_GO_TERM("GO Term", "GO Term"),
        SAMPLE_DISEASE("Disease", "Disease"),
        SAMPLE_DESCRIPTION("Description", "Description");

        private final String header;
        private final String toolTip;

        private TableHeader(String header, String tooltip) {
            this.header = header;
            this.toolTip = tooltip;
        }

        public String getHeader() {
            return header;
        }

        public String getToolTip() {
            return toolTip;
        }
    }

    public QuantSampleTableModel(QuantitativeSample sample) {

        // add header
        addHeaders(sample);

        // add contacts
        addData(sample);
    }

    @Override
    public void initializeTableModel() {
    }

    private void addHeaders(QuantitativeSample sample) {
        columnNames.put(TableHeader.ROW_NUMBER_COLUMN.getHeader(), TableHeader.ROW_NUMBER_COLUMN.getToolTip());

        if (sample.hasReagent()) {
            columnNames.put(TableHeader.SAMPLE_REAGENT.getHeader(), TableHeader.SAMPLE_REAGENT.getToolTip());
        }

        if (sample.hasSpecies()) {
            columnNames.put(TableHeader.SAMPLE_SPECIES.getHeader(), TableHeader.SAMPLE_SPECIES.getToolTip());
        }

        if (sample.hasTissue()) {
            columnNames.put(TableHeader.SAMPLE_TISSUE.getHeader(), TableHeader.SAMPLE_TISSUE.getToolTip());
        }

        if (sample.hasCellLine()) {
            columnNames.put(TableHeader.SAMPLE_CELL_LINE.getHeader(), TableHeader.SAMPLE_CELL_LINE.getToolTip());
        }

        if (sample.hasGOTerm()) {
            columnNames.put(TableHeader.SAMPLE_GO_TERM.getHeader(), TableHeader.SAMPLE_GO_TERM.getToolTip());
        }

        if (sample.hasDisease()) {
            columnNames.put(TableHeader.SAMPLE_DISEASE.getHeader(), TableHeader.SAMPLE_DISEASE.getToolTip());
        }

        if (sample.hasDescription()) {
            columnNames.put(TableHeader.SAMPLE_DESCRIPTION.getHeader(), TableHeader.SAMPLE_DESCRIPTION.getToolTip());
        }
    }

    @Override
    public void addData(QuantitativeSample sample) {


        for (int i = 1; i < QuantitativeSample.MAX_SUB_SAMPLE_SIZE; i++) {
            if (sample.hasSubSample(i)) {
                List<Object> content = new ArrayList<Object>();

                int rowCnt = getRowCount();
                content.add(rowCnt + 1);

                if (sample.hasReagent()) {
                    content.add(sample.getReagent(i));
                }

                if (sample.hasSpecies()) {
                    content.add(sample.getSpecies(i));
                }

                if (sample.hasTissue()) {
                    content.add(sample.getTissue(i));
                }

                if (sample.hasCellLine()) {
                    content.add(sample.getCellLine(i));
                }

                if (sample.hasGOTerm()) {
                    content.add(sample.getGOTerm(i));
                }

                if (sample.hasDisease()) {
                    content.add(sample.getDisease(i));
                }

                if (sample.hasDescription()) {
                    content.add(sample.getDescription(i));
                }

                this.addRow(content);
                fireTableRowsInserted(rowCnt, rowCnt);
            }
        }
    }
}
