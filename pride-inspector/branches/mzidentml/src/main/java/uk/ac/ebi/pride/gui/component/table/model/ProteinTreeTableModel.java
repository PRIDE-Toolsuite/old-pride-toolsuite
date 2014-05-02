package uk.ac.ebi.pride.gui.component.table.model;

import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import uk.ac.ebi.pride.data.Tuple;
import uk.ac.ebi.pride.gui.component.sequence.AnnotatedProtein;
import uk.ac.ebi.pride.gui.task.TaskEvent;
import uk.ac.ebi.pride.gui.task.TaskListener;
import uk.ac.ebi.pride.gui.utils.ProteinAccession;
import uk.ac.ebi.pride.term.CvTermReference;
import uk.ac.ebi.pride.tools.protein_details_fetcher.model.Protein;

import javax.swing.tree.TreePath;
import java.util.*;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class ProteinTreeTableModel extends AbstractTreeTableModel implements TaskListener<Void, Tuple<TableContentType, Object>> {

    private final Map<String, String> columnNames;
    private final Collection<CvTermReference> proteinScores;
    private final Map<Comparable, ProteinTableRow> proteinGroupIdToProteinTableRow;
    private final Random random;

    public ProteinTreeTableModel(Collection<CvTermReference> proteinScores) {
        super(new ProteinTableRow());
        this.columnNames = new LinkedHashMap<String, String>();
        this.proteinGroupIdToProteinTableRow = Collections.synchronizedMap(new LinkedHashMap<Comparable, ProteinTableRow>());
        this.random = new Random();
        this.proteinScores = proteinScores;
        addAdditionalColumns(proteinScores);
    }

    private void addAdditionalColumns(Collection<CvTermReference> proteinScores) {
        columnNames.clear();

        // add columns for search engine scores
        ProteinTableHeader[] headers = ProteinTableHeader.values();
        for (ProteinTableHeader header : headers) {
            columnNames.put(header.getHeader(), header.getToolTip());
            if (proteinScores != null && ProteinTableHeader.PROTEIN_ID.getHeader().equals(header.getHeader())) {
                for (uk.ac.ebi.pride.term.CvTermReference scoreCvTerm : proteinScores) {
                    String name = scoreCvTerm.getName();
                    columnNames.put(name, name);
                }
            }
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    public int getColumnIndex(String header) {
        int index = -1;

        List<Map.Entry<String, String>> entries = new LinkedList<Map.Entry<String, String>>(columnNames.entrySet());

        for (Map.Entry<String, String> entry : entries) {
            if (entry.getKey().equals(header)) {
                index = entries.indexOf(entry);
            }
        }

        return index;
    }

    public String getColumnName(int index) {
        String columnName = null;

        List<Map.Entry<String, String>> entries = new LinkedList<Map.Entry<String, String>>(columnNames.entrySet());
        Map.Entry<String, String> entry = entries.get(index);
        if (entry != null) {
            columnName = entry.getKey();
        }

        return columnName;
    }

    public String getColumnTooltip(int index) {
        String tooltip = null;

        List<Map.Entry<String, String>> entries = new LinkedList<Map.Entry<String, String>>(columnNames.entrySet());
        Map.Entry<String, String> entry = entries.get(index);
        if (entry != null) {
            tooltip = entry.getValue();
        }

        return tooltip;
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (getRoot().equals(node) || node == null || !(node instanceof ProteinTableRow)) {
            return null;
        }

        ProteinTableRow proteinTableRow = (ProteinTableRow) node;
        String columnName = getColumnName(column);

        if (ProteinTableHeader.COMPARE.getHeader().equals(columnName)) {
            return proteinTableRow.getComparisonState();
        } else if (ProteinTableHeader.PROTEIN_ACCESSION.getHeader().equals(columnName)) {
            return proteinTableRow.getProteinAccession();
        } else if (ProteinTableHeader.PROTEIN_NAME.getHeader().equals(columnName)) {
            return proteinTableRow.getProteinName();
        } else if (ProteinTableHeader.PROTEIN_STATUS.getHeader().equals(columnName)) {
            return proteinTableRow.getProteinAccessionStatus();
        } else if (ProteinTableHeader.PROTEIN_SEQUENCE_COVERAGE.getHeader().equals(columnName)) {
            return proteinTableRow.getSequenceCoverage();
        } else if (ProteinTableHeader.THEORETICAL_ISOELECTRIC_POINT.getHeader().equals(columnName)) {
            return proteinTableRow.getIsoelectricPoint();
        } else if (ProteinTableHeader.IDENTIFICATION_THRESHOLD.getHeader().equals(columnName)) {
            return proteinTableRow.getThreshold();
        } else if (ProteinTableHeader.NUMBER_OF_PEPTIDES.getHeader().equals(columnName)) {
            return proteinTableRow.getNumberOfPeptides();
        } else if (ProteinTableHeader.NUMBER_OF_UNIQUE_PEPTIDES.getHeader().equals(columnName)) {
            return proteinTableRow.getNumberOfUniquePeptides();
        } else if (ProteinTableHeader.NUMBER_OF_PTMS.getHeader().equals(columnName)) {
            return proteinTableRow.getNumberOfPTMs();
        } else if (ProteinTableHeader.PROTEIN_ID.getHeader().equals(columnName)) {
            return proteinTableRow.getProteinId();
        } else if (ProteinTableHeader.PROTEIN_GROUP_ID.getHeader().equals(columnName)) {
            return proteinTableRow.getProteinGroupId();
        } else if (ProteinTableHeader.ADDITIONAL.getHeader().equals(columnName)) {
            return proteinTableRow.getProteinId();
        } else {
            return getProteinScore(proteinTableRow, columnName);
        }
    }

    private Double getProteinScore(ProteinTableRow proteinTableRow, String columnName) {
        List<Double> scores = proteinTableRow.getScores();

        int scoreIndex = 0;

        for (CvTermReference scoreTermReference : proteinScores) {
            if (scoreTermReference.getName().equals(columnName)) {
                return scores.get(scoreIndex);
            }
            scoreIndex++;
        }

        return null;
    }

    @Override
    public Object getChild(Object parent, int index) {
        ProteinTableRow parentProteinRow = (ProteinTableRow) parent;
        return parentProteinRow.getChildProteinTableRows().get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        ProteinTableRow parentProteinRow = (ProteinTableRow) parent;
        return parentProteinRow.getChildProteinTableRows().size();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        ProteinTableRow parentProteinRow = (ProteinTableRow) parent;
        return parentProteinRow.getChildProteinTableRows().indexOf(child);
    }

    public TreeModelSupport getTreeModelSupport() {
        return modelSupport;
    }

    public void addData(Tuple<TableContentType, Object> newData) {
        TableContentType type = newData.getKey();

        if (TableContentType.PROTEIN.equals(type)) {
            addProteinTableRow((ProteinTableRow) newData.getValue());
        } else if (TableContentType.PROTEIN_DETAILS.equals(type)) {
            addProteinDetailData(newData.getValue());
        } else if (TableContentType.PROTEIN_SEQUENCE_COVERAGE.equals(type)) {
            addSequenceCoverageData(newData.getValue());
        }
    }

    private void addProteinTableRow(ProteinTableRow proteinTableRow) {
        Comparable proteinGroupId = proteinTableRow.getProteinGroupId();

            if (proteinGroupId == null) {
                // generate an random id
            proteinGroupId = proteinTableRow.getProteinId().toString() + random.nextInt();
            }

            ProteinTableRow parentProteinTableRow = proteinGroupIdToProteinTableRow.get(proteinGroupId);

            if (parentProteinTableRow == null) {

                // add as a parent node
            proteinGroupIdToProteinTableRow.put(proteinGroupId, proteinTableRow);

            ((ProteinTableRow) getRoot()).addChildProteinTableRow(proteinTableRow);
            int childIndex = getIndexOfChild(getRoot(), proteinTableRow);
            modelSupport.fireChildAdded(new TreePath(getRoot()), childIndex, proteinTableRow);

            } else {
            parentProteinTableRow.addChildProteinTableRow(proteinTableRow);
            int childIndex = getIndexOfChild(parentProteinTableRow, proteinTableRow);
            modelSupport.fireChildAdded(new TreePath(parentProteinTableRow), childIndex, proteinTableRow);
            }
    }

    private void addProteinDetailData(Object value) {
        // get a map of protein accession to protein details
        Map<String, Protein> proteins = (Map<String, Protein>) value;

        // iterate over each row, set the protein name
        Collection<ProteinTableRow> parentProteinTableRows = proteinGroupIdToProteinTableRow.values();
        for (ProteinTableRow parentProteinTableRow : parentProteinTableRows) {
            addProteinDetailsForProteinTableRow(proteins, (ProteinTableRow) getRoot(), parentProteinTableRow);

            for (ProteinTableRow childProteinTableRow : parentProteinTableRow.getChildProteinTableRows()) {
                addProteinDetailsForProteinTableRow(proteins, parentProteinTableRow, childProteinTableRow);
            }
        }
    }

    private void addProteinDetailsForProteinTableRow(Map<String, Protein> proteins, ProteinTableRow parentProteinTableRow, ProteinTableRow childProteinTableRow) {
        Object proteinAccession = childProteinTableRow.getProteinAccession();

        if (proteinAccession != null) {

            String mappedAccession = ((ProteinAccession) proteinAccession).getMappedAccession();

            if (mappedAccession != null) {

                Protein protein = proteins.get(mappedAccession);

                if (protein != null) {
                    AnnotatedProtein annotatedProtein = new AnnotatedProtein(protein);

                    // set protein name
                    childProteinTableRow.setProteinName(annotatedProtein.getName());

                    // set protein status
                    childProteinTableRow.setProteinAccessionStatus(annotatedProtein.getStatus().name());

                    // notify a row change
                    int indexOfChild = getIndexOfChild(parentProteinTableRow, childProteinTableRow);
                    modelSupport.fireChildChanged(new TreePath(parentProteinTableRow), indexOfChild, childProteinTableRow);
                }
            }
        }
    }

    /**
     * Add protein sequence coverages
     *
     * @param newData sequence coverage map
     */
    private void addSequenceCoverageData(Object newData) {
        // map contains sequence coverage
        Map<Comparable, Double> coverageMap = (Map<Comparable, Double>) newData;

        // iterate over each row, set the protein name
        Collection<ProteinTableRow> proteinTableRows = proteinGroupIdToProteinTableRow.values();
        for (ProteinTableRow parentProteinTableRow : proteinTableRows) {
            updateSequenceCoverageData(coverageMap, (ProteinTableRow) getRoot(), parentProteinTableRow);

            for (ProteinTableRow childProteinTableRow : parentProteinTableRow.getChildProteinTableRows()) {
                updateSequenceCoverageData(coverageMap, parentProteinTableRow, childProteinTableRow);
            }
        }
    }

    private void updateSequenceCoverageData(Map<Comparable, Double> coverageMap,
                                            ProteinTableRow parentProteinTableRow,
                                            ProteinTableRow childProteinTableRow) {
        Object proteinId = childProteinTableRow.getProteinId();
        Double coverage = coverageMap.get(proteinId);

        if (coverage != null) {
            // set protein name
            childProteinTableRow.setSequenceCoverage(coverage);
            // notify a row change
            modelSupport.fireChildChanged(new TreePath(parentProteinTableRow),
                    getIndexOfChild(parentProteinTableRow, childProteinTableRow), childProteinTableRow);
        }
    }

    @Override
    public void process(TaskEvent<List<Tuple<TableContentType, Object>>> event) {
        List<Tuple<TableContentType, Object>> newDataList = event.getValue();
        for (Tuple<TableContentType, Object> newData : newDataList) {
            addData(newData);
        }
    }

    @Override
    public void started(TaskEvent<Void> event) {
    }

    @Override
    public void finished(TaskEvent<Void> event) {
    }

    @Override
    public void failed(TaskEvent<Throwable> event) {
    }

    @Override
    public void succeed(TaskEvent<Void> event) {
    }

    @Override
    public void cancelled(TaskEvent<Void> event) {
    }

    @Override
    public void interrupted(TaskEvent<InterruptedException> iex) {
    }

    @Override
    public void progress(TaskEvent<Integer> progress) {
    }
}
