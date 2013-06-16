package uk.ac.ebi.pride.gui.component.table.model;

import uk.ac.ebi.pride.data.Tuple;

import java.util.List;

/**
 * ChromatogramTableModel stores all the details related to chromatogram table.
 *
 * User: rwang
 * Date: 12-Apr-2010
 * Time: 16:05:12
 */
public class ChromatogramTableModel extends ProgressiveListTableModel<Void, Tuple<TableContentType, List<Object>>> {

    /**
     * table column title
     */
    public enum TableHeader {
        CHROMATOGRAM_ID_COLUMN("Chromatogram ID", "Chromatogram ID");

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

    @Override
    public void initializeTableModel() {
        TableHeader[] headers = TableHeader.values();
        for (TableHeader header : headers) {
            columnNames.put(header.getHeader(), header.getToolTip());
        }
    }

    @Override
    public void addData(Tuple<TableContentType, List<Object>> newData) {
        TableContentType type = newData.getKey();
        int rowCnt = this.getRowCount();
        if (TableContentType.CHROMATOGRAM.equals(type)) {
            this.addRow(newData.getValue());
            fireTableRowsInserted(rowCnt, rowCnt);
        }
    }
}
