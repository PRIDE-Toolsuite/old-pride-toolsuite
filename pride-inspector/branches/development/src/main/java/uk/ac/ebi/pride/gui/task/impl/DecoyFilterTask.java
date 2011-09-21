package uk.ac.ebi.pride.gui.task.impl;

import org.bushe.swing.event.EventBus;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.gui.PrideInspectorContext;
import uk.ac.ebi.pride.gui.component.report.RemovalReportMessage;
import uk.ac.ebi.pride.gui.component.startup.ControllerContentPane;
import uk.ac.ebi.pride.gui.component.table.filter.DecoyAccessionFilter;
import uk.ac.ebi.pride.gui.component.table.model.PeptideTableModel;
import uk.ac.ebi.pride.gui.component.table.model.ProteinTableModel;
import uk.ac.ebi.pride.gui.component.table.model.QuantProteinTableModel;
import uk.ac.ebi.pride.gui.component.table.sorter.NumberTableRowSorter;
import uk.ac.ebi.pride.gui.desktop.Desktop;
import uk.ac.ebi.pride.gui.event.SummaryReportEvent;
import uk.ac.ebi.pride.gui.task.TaskAdapter;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.regex.Pattern;

/**
 * Task to set decoy filter
 *
 * User: rwang
 * Date: 16/09/2011
 * Time: 10:27
 */
public class DecoyFilterTask extends TaskAdapter<Void, Void>{
    private static final String TASK_NAME = "Calculating decoy ratio";
    private static final String TASK_DESCRIPTION = "Calculating decoy ratio for both protein and peptide";

    private DataAccessController controller;
    private DecoyAccessionFilter.Type type;
    private String criteria;

    public DecoyFilterTask(DataAccessController controller, DecoyAccessionFilter.Type type, String criteria) {
        this.controller = controller;
        this.type = type;
        this.criteria = criteria.toLowerCase();

        this.setName(TASK_NAME);
        this.setDescription(TASK_DESCRIPTION);
    }

    @Override
    protected Void doInBackground() throws Exception {
        PrideInspectorContext appContext = (PrideInspectorContext) Desktop.getInstance().getDesktopContext();
        // remove previous decoy ratio
        EventBus.publish(new SummaryReportEvent(this, controller, new RemovalReportMessage(Pattern.compile(".*decoy.*"))));
        // get content pane
        ControllerContentPane contentPane = (ControllerContentPane) appContext.getDataContentPane(controller);
        // protein tab
        JTable table = contentPane.getProteinTabPane().getIdentificationPane().getIdentificationTable();
        String protAccColName = ProteinTableModel.TableHeader.PROTEIN_ACCESSION_COLUMN.getHeader();
        int index = getAccessionColumnIndex(table.getModel(), protAccColName);
        setRowFilter(table, new DecoyAccessionFilter(type, criteria, index, false));
        // protein decoy ratio

        // peptide tab
        table = contentPane.getPeptideTabPane().getPeptidePane().getPeptideTable();
        protAccColName = PeptideTableModel.TableHeader.PROTEIN_ACCESSION_COLUMN.getHeader();
        index = getAccessionColumnIndex(table.getModel(), protAccColName);
        setRowFilter(table, new DecoyAccessionFilter(type, criteria, index, false));
        // quant tab
        if (contentPane.isQuantTabEnabled()) {
            table = contentPane.getQuantTabPane().getQuantProteinSelectionPane().getQuantProteinTable();
            protAccColName = QuantProteinTableModel.TableHeader.PROTEIN_ACCESSION_COLUMN.getHeader();
            index = getAccessionColumnIndex(table.getModel(), protAccColName);
            setRowFilter(table, new DecoyAccessionFilter(type, criteria, index, false));
        }

        return null;
    }

    private int getAccessionColumnIndex(TableModel tableModel, String protAccColName) {
        int colCnt = tableModel.getColumnCount();
        for (int i = 0; i < colCnt; i++) {
            if (tableModel.getColumnName(i).equals(protAccColName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Set the row filter
     *
     * @param rowFilter a given row filter
     */
    private void setRowFilter(JTable table, RowFilter rowFilter) {
        // get table model
        TableModel tableModel = table.getModel();
        RowSorter rowSorter = table.getRowSorter();
        if (rowSorter == null || !(rowSorter instanceof TableRowSorter)) {
            rowSorter = new NumberTableRowSorter(tableModel);
            table.setRowSorter(rowSorter);
        }
        ((TableRowSorter) rowSorter).setRowFilter(rowFilter);
    }
}
