package uk.ac.ebi.pride.gui.component.protein;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bushe.swing.event.EventSubscriber;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.DataAccessException;
import uk.ac.ebi.pride.gui.GUIUtilities;
import uk.ac.ebi.pride.gui.component.PrideInspectorTabPane;
import uk.ac.ebi.pride.gui.component.exception.ThrowableEntry;
import uk.ac.ebi.pride.gui.component.message.MessageType;
import uk.ac.ebi.pride.gui.component.mzdata.MzDataTabPane;
import uk.ac.ebi.pride.gui.component.startup.ControllerContentPane;
import uk.ac.ebi.pride.gui.event.container.ExpandPanelEvent;
import uk.ac.ebi.pride.gui.task.TaskEvent;

import javax.swing.*;
import java.awt.*;

/**
 * IdentTabPane displays protein identification and peptide related information.
 * It also visualize the spectrum peak list.
 * <p/>
 * <p/>
 * User: rwang
 * Date: 01-Mar-2010
 * Time: 16:23:02
 */
public class ProteinTabPane extends PrideInspectorTabPane {

    private static final Logger logger = Logger.getLogger(MzDataTabPane.class.getName());

    /**
     * title
     */
    private static final String IDENTIFICATION_TITLE = "Protein";
    /**
     * resize weight for inner split pane
     */
    private static final double INNER_SPLIT_PANE_RESIZE_WEIGHT = 0.6;
    /**
     * resize weight for outer split pane
     */
    private static final double OUTER_SPLIT_PANE_RESIZE_WEIGHT = 0.6;
    /**
     * the size of the divider for split pane
     */
    private static final int DIVIDER_SIZE = 2;
    /**
     * Inner split pane contains peptide panel and mzgraph view pane
     */
    private JSplitPane innerPane;
    /**
     * Outer split pane contains inner split pane and protein panel
     */
    private JSplitPane outerPane;
    /**
     * Identification selection pane
     */
    private ProteinSelectionPane identPane;
    /**
     * Peptide selection pane
     */
    private PeptideSelectionPane peptidePane;
    /**
     * visualize mzgraph
     */
    private ProteinVizPane vizTabPane;

    /**
     * Subscribe to expand protein panel event
     */
    private ExpandProteinPanelSubscriber expandProteinPanelSubscriber;

    /**
     * Constructor
     *
     * @param controller data access controller
     * @param parentComp DataContentDisplayPane
     */
    public ProteinTabPane(DataAccessController controller, JComponent parentComp) {
        super(controller, parentComp);
    }

    /**
     * Setup the main display pane and its title
     */
    @Override
    protected void setupMainPane() {
        // add event subscriber
        expandProteinPanelSubscriber = new ExpandProteinPanelSubscriber();
        getContainerEventService().subscribe(ExpandPanelEvent.class, expandProteinPanelSubscriber);

        // layout
        this.setLayout(new BorderLayout());

        // set the title for the panel
        try {
            int ids = controller.getNumberOfIdentifications();
            String numIdent = " (" + ids + ")";

            this.setTitle(IDENTIFICATION_TITLE + numIdent);
        } catch (DataAccessException dex) {
            String msg = String.format("%s failed on : %s", this, dex);
            logger.log(Level.ERROR, msg, dex);
            appContext.addThrowableEntry(new ThrowableEntry(MessageType.ERROR, msg, dex));
        }

        // set the final icon
        this.setIcon(GUIUtilities.loadIcon(appContext.getProperty("identification.tab.icon.small")));

        // set the loading icon
        this.setLoadingIcon(GUIUtilities.loadIcon(appContext.getProperty("identification.tab.loading.icon.small")));
    }

    /**
     * Add the rest components
     */
    @Override
    protected void addComponents() {
        // create the inner split pane
        innerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        innerPane.setBorder(BorderFactory.createEmptyBorder());
        innerPane.setOneTouchExpandable(false);
        innerPane.setDividerSize(DIVIDER_SIZE);
        innerPane.setResizeWeight(INNER_SPLIT_PANE_RESIZE_WEIGHT);

        // create the outer split pane
        outerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        outerPane.setBorder(BorderFactory.createEmptyBorder());
        outerPane.setOneTouchExpandable(false);
        outerPane.setDividerSize(DIVIDER_SIZE);
        outerPane.setResizeWeight(OUTER_SPLIT_PANE_RESIZE_WEIGHT);

        // protein identification selection pane
        identPane = new ProteinSelectionPane(controller);
        outerPane.setTopComponent(identPane);

        // peptide selection pane
        peptidePane = new PeptideSelectionPane(controller);
        innerPane.setTopComponent(peptidePane);

        // visualization tab pane
        vizTabPane = new ProteinVizPane(controller);
        vizTabPane.setMinimumSize(new Dimension(200, 200));
        innerPane.setBottomComponent(vizTabPane);
        outerPane.setBottomComponent(innerPane);

        this.add(outerPane, BorderLayout.CENTER);

        // subscribe to event bus
        peptidePane.subscribeToEventBus();
        vizTabPane.subscribeToEventBus();
    }

    /**
     * Return a reference to the identification pane
     *
     * @return IdentificationSelectionPane  identification selection pane
     */
    public ProteinSelectionPane getIdentificationPane() {
        return identPane;
    }

    /**
     * Return a reference to the peptide pane
     *
     * @return PeptideSelectionPane peptide pane
     */
    public PeptideSelectionPane getPeptidePane() {
        return peptidePane;
    }

    @Override
    public void started(TaskEvent event) {
        showIcon(getLoadingIcon());
    }

    @Override
    public void finished(TaskEvent event) {
        showIcon(getIcon());
    }

    /**
     * Show a different icon if the parent component is not null and an instance of DataContentDisplayPane
     *
     * @param icon icon to show
     */
    private void showIcon(Icon icon) {
        if (parentComponent != null && parentComponent instanceof ControllerContentPane && icon != null) {
            ControllerContentPane contentPane = (ControllerContentPane) parentComponent;
            contentPane.setTabIcon(contentPane.getProteinTabIndex(), icon);
        }
    }

    /**
     * Event handler for expanding protein panel
     */
    private class ExpandProteinPanelSubscriber implements EventSubscriber<ExpandPanelEvent> {

        @Override
        public void onEvent(ExpandPanelEvent event) {
            boolean visible = innerPane.isVisible();
            innerPane.setVisible(!visible);
            outerPane.setDividerSize(visible ? 0 : DIVIDER_SIZE);
            outerPane.resetToPreferredSizes();
        }
    }
}
