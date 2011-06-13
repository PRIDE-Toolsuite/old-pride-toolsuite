package uk.ac.ebi.pride.gui.component.protein;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.DataAccessException;
import uk.ac.ebi.pride.gui.GUIUtilities;
import uk.ac.ebi.pride.gui.component.DataAccessControllerPane;
import uk.ac.ebi.pride.gui.component.EventBusSubscribable;
import uk.ac.ebi.pride.gui.component.exception.ThrowableEntry;
import uk.ac.ebi.pride.gui.component.message.MessageType;
import uk.ac.ebi.pride.gui.component.mzgraph.SpectrumViewPane;
import uk.ac.ebi.pride.gui.component.sequence.ProteinSequencePane;

import javax.swing.*;
import java.awt.*;

/**
 * This tab pane shows both the spectrum browser and protein sequence panel
 * <p/>
 * User: rwang
 * Date: 09/06/11
 * Time: 11:37
 */
public class ProteinVizPane extends DataAccessControllerPane implements EventBusSubscribable {
    private static Logger logger = LoggerFactory.getLogger(ProteinVizPane.class);
    /**
     * the default background color
     */
    private static final Color BACKGROUND_COLOUR = Color.white;

    private SpectrumViewPane spectrumViewPane;
    private ProteinSequencePane proteinSequencePane;

    public ProteinVizPane(DataAccessController controller) {
        super(controller);
    }

    @Override
    protected void setupMainPane() {
        // set layout
        this.setLayout(new BorderLayout());
        this.setBackground(BACKGROUND_COLOUR);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    protected void addComponents() {
        // tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BACKGROUND_COLOUR);

        // tab index
        int tabIndex = 0;

        try {
            if (controller.hasSpectrum()) {
                // Spectrum view pane
                spectrumViewPane = new SpectrumViewPane(controller);
                Icon mzViewIcon = GUIUtilities.loadIcon(appContext.getProperty("spectrum.tab.icon.small"));
                tabbedPane.insertTab(appContext.getProperty("spectrum.tab.title"), mzViewIcon,
                        spectrumViewPane, appContext.getProperty("spectrum.tab.tooltip"), tabIndex);
                tabIndex++;
            }
        } catch (DataAccessException e) {
            String msg = "Failed to check the availability of spectrum";
            logger.error(msg, e);
            appContext.addThrowableEntry(new ThrowableEntry(MessageType.ERROR, msg, e));
        }

        // protein sequence pane
        proteinSequencePane = new ProteinSequencePane(controller);
        JScrollPane scrollPane = new JScrollPane(proteinSequencePane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(BACKGROUND_COLOUR);
        Icon protSeqIcon = GUIUtilities.loadIcon(appContext.getProperty("protein.sequence.tab.icon.small"));
        tabbedPane.insertTab(appContext.getProperty("protein.sequence.tab.title"), protSeqIcon,
                scrollPane, appContext.getProperty("protein.sequence.tab.tooltip"), tabIndex);

        this.add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void subscribeToEventBus() {
        if (spectrumViewPane != null) {
            spectrumViewPane.subscribeToEventBus();
        }
        proteinSequencePane.subscribeToEventBus();
    }

}