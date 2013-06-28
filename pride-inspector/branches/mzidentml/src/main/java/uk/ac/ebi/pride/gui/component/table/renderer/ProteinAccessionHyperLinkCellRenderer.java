package uk.ac.ebi.pride.gui.component.table.renderer;

import uk.ac.ebi.pride.gui.utils.ProteinAccession;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class ProteinAccessionHyperLinkCellRenderer extends JLabel implements TableCellRenderer {

    public ProteinAccessionHyperLinkCellRenderer() {
        this.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        java.util.List<String> accessions = new ArrayList<String>();
        if (value != null) {
            if (value instanceof ProteinAccession) {
                accessions.add(((ProteinAccession)value).getAccession());
            } else {
                Collection<ProteinAccession> proteinAccessions = (Collection<ProteinAccession>) value;
                for (ProteinAccession proteinAccession : proteinAccessions) {
                    accessions.add(proteinAccession.getAccession());
                }
            }
        }

        if (!accessions.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("<html>");
            for (String accession : accessions) {
                builder.append("<a href='").append(accession).append("'>");
                builder.append(accession);
                builder.append("</a> ");
            }
            builder.append("</html>");
            this.setText(builder.toString());
        } else {
            this.setText(null);
        }

        // set background
        if (isSelected) {
            this.setBackground(table.getSelectionBackground());
        } else {
            this.setBackground(table.getBackground());
        }
        // repaint the component
        this.revalidate();
        this.repaint();
        return this;
    }
}
