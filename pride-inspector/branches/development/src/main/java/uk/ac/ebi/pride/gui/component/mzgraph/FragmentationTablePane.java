package uk.ac.ebi.pride.gui.component.mzgraph;

import org.bushe.swing.event.ContainerEventServiceFinder;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.DataAccessException;
import uk.ac.ebi.pride.data.core.BinaryDataArray;
import uk.ac.ebi.pride.data.core.CvParam;
import uk.ac.ebi.pride.data.core.Modification;
import uk.ac.ebi.pride.data.core.Peptide;
import uk.ac.ebi.pride.gui.GUIUtilities;
import uk.ac.ebi.pride.gui.action.PrideAction;
import uk.ac.ebi.pride.gui.action.impl.OpenHelpAction;
import uk.ac.ebi.pride.gui.component.DataAccessControllerPane;
import uk.ac.ebi.pride.gui.component.EventBusSubscribable;
import uk.ac.ebi.pride.gui.component.report.RemovalReportMessage;
import uk.ac.ebi.pride.gui.component.report.SummaryReportMessage;
import uk.ac.ebi.pride.gui.event.SummaryReportEvent;
import uk.ac.ebi.pride.gui.event.container.PeptideEvent;
import uk.ac.ebi.pride.gui.event.subscriber.PeptideEventSubscriber;
import uk.ac.ebi.pride.gui.task.TaskEvent;
import uk.ac.ebi.pride.gui.utils.AnnotationUtils;
import uk.ac.ebi.pride.gui.utils.PeptideTranslate;
import uk.ac.ebi.pride.iongen.model.PrecursorIon;
import uk.ac.ebi.pride.iongen.model.impl.DefaultPrecursorIon;
import uk.ac.ebi.pride.mol.MoleculeUtilities;
import uk.ac.ebi.pride.mol.ProductIonPair;
import uk.ac.ebi.pride.mzgraph.chart.data.annotation.IonAnnotation;
import uk.ac.ebi.pride.mzgraph.chart.graph.MzTablePanel;
import uk.ac.ebi.pride.mzgraph.gui.ExperimentalFragmentedIonsTable;
import uk.ac.ebi.pride.term.CvTermReference;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Creator: Qingwei-XU
 * Date: 19/11/12
 */

public class FragmentationTablePane extends DataAccessControllerPane<Peptide, Void> implements EventBusSubscribable {
    private static final Logger logger = LoggerFactory.getLogger(FragmentationTablePane.class);

    private MzTablePanel mzTablePanel;

    private DataAccessController controller;

    /**
     * this is a threshold based on delta m/z value. if the spectrum delta m/z value
     * great than this, means maybe there exists some errors in the experimental,
     * system not show any annotations (including auto and manual) in table panel.
     */
    private final double DELTA_MZ_THRESHOLD = 1;

    /**
     * Subscribe to peptide event
     */
    private PeptideEventSubscriber peptideSubscriber;

    public FragmentationTablePane(DataAccessController controller) {
        super(controller);

        this.controller = controller;
    }

    @Override
    protected void setupMainPane() {
        // setup the main pane
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    protected void addComponents() {
        mzTablePanel = new MzTablePanel();
        mzTablePanel.setPreferredSize(new Dimension(200, 200));
        this.add(mzTablePanel, BorderLayout.CENTER);
    }

    @Override
    public void subscribeToEventBus(EventService eventBus) {
        // get local event bus
        if (eventBus == null) {
            eventBus = ContainerEventServiceFinder.getEventService(this);
        }

        // subscriber
        peptideSubscriber = new PeptideEventSubscriber(controller, this);

        // subscribeToEventBus
        eventBus.subscribe(PeptideEvent.class, peptideSubscriber);
    }

    public MzTablePanel getMzTablePanel() {
        return mzTablePanel;
    }

    /**
     * this is a threshold based on delta m/z value. if the spectrum delta m/z value
     * great than this, means maybe there exists some errors in the experimental,
     * system not show any annotations (including auto and manual) in table panel.
     */
    private boolean isOverflow(Peptide peptide) {
        boolean overflow = false;
        try {
            String sequence = peptide.getSequence();
            Comparable specId = peptide.getSpectrum().getId();

            int charge = getCharge(peptide);
            if (charge <= 0) {
                return true;
            }

            double mz = controller.getPrecursorMz(specId);

            java.util.List<Modification> mods = peptide.getModifications();

            java.util.List<Double> ptmMasses = new ArrayList<Double>();
            for (Modification mod : mods) {
                java.util.List<Double> monoMasses = mod.getMonoMassDeltas();
                if (monoMasses != null && !monoMasses.isEmpty()) {
                    ptmMasses.add(monoMasses.get(0));
                }
            }

            Double deltaMass = MoleculeUtilities.calculateDeltaMz(sequence, mz, charge, ptmMasses);
            if (Double.compare(Math.abs(deltaMass.doubleValue()), DELTA_MZ_THRESHOLD) >= 0) {
                overflow = true;
            }
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
        }

        return overflow;
    }

    private String getSelectedIonCvParamValue(java.util.List<CvParam> cvParams, CvTermReference... refs) {
        String value = null;

        for (CvParam param : cvParams) {
            if (cvParams != null && !cvParams.isEmpty()) {
                for (CvTermReference ref : refs) {
                    if (param.getAccession().equals(ref.getAccession())) {
                        value = param.getValue();
                        break;
                    }
                }
            }
        }

        return value;
    }

    private int getCharge(Peptide peptide) {
        String value = getSelectedIonCvParamValue(
                peptide.getCvParams(),
                CvTermReference.PSI_ION_SELECTION_CHARGE_STATE,
                CvTermReference.ION_SELECTION_CHARGE_STATE
        );

        if (value == null) {
            value = getSelectedIonCvParamValue(
                    peptide.getSpectrum().getPrecursors().get(0).getSelectedIons().get(0).getCvParams(),
                    CvTermReference.PSI_ION_SELECTION_CHARGE_STATE,
                    CvTermReference.ION_SELECTION_CHARGE_STATE
            );
        }

        return value == null ? -1 : Integer.parseInt(value);
    }

    @Override
    public void succeed(TaskEvent<Peptide> peptideTaskEvent) {
        Peptide peptide = peptideTaskEvent.getValue();

        BinaryDataArray mzBinary = peptide == null ? null : peptide.getSpectrum().getMzBinaryDataArray();
        BinaryDataArray intentBinary = peptide == null ? null : peptide.getSpectrum().getIntensityBinaryDataArray();

        if (mzBinary != null && intentBinary != null) {
            int charge = getCharge(peptide);

            uk.ac.ebi.pride.mol.Peptide newPeptide = new PeptideTranslate(peptide).translate();
            // fragmentation table only show charge<=2 fragmentation ions.
            charge = charge > 2 ? 2 : charge;
            PrecursorIon precursorIon = new DefaultPrecursorIon(newPeptide, charge);
            ExperimentalFragmentedIonsTable table = new ExperimentalFragmentedIonsTable(
                    precursorIon,
                    ProductIonPair.B_Y,
                    mzBinary.getDoubleArray(),
                    intentBinary.getDoubleArray()
            );
            mzTablePanel.setTable(table);

            if (charge <= 0) {
                mzTablePanel.setCalculate(false);
            } else {
                mzTablePanel.setCalculate(! isOverflow(peptide));

                java.util.List<IonAnnotation> ions = AnnotationUtils.convertToIonAnnotations(peptide.getFragmentIons());
                if (ions.size() > 0) {
                    // manual annotations
                    mzTablePanel.addAllManualAnnotations(ions);
                    table.setShowAuto(false);
                } else {
                    table.setShowAuto(true);
                }

                JTabbedPane tabbedPane = (JTabbedPane) getParent();
                // set fragmentation table tab panel enable or disable.
                if (mzTablePanel.isCalculate()) {
                    tabbedPane.setEnabledAt(1, true);
                } else {
                    tabbedPane.setEnabledAt(1, false);
                    tabbedPane.setSelectedIndex(0);
                }

                int tabPaneWidth = getParent().getWidth();
                int tabPaneHeight = getParent().getHeight();
                mzTablePanel.getTablePanel().setPreferredSize(new Dimension((int)(tabPaneWidth * 0.85), tabPaneHeight - 80));
                mzTablePanel.getChartPanel().setPreferredSize(new Dimension((int)(tabPaneWidth * 0.15), tabPaneHeight - 80));

                // Summary Report Message
                EventBus.publish(new SummaryReportEvent(this, controller, new RemovalReportMessage(Pattern.compile(".*Annotation.*"))));
                if (mzTablePanel.isShowAuto()) {
                    EventBus.publish(new SummaryReportEvent(
                            this,
                            controller,
                            new SummaryReportMessage(
                                    SummaryReportMessage.Type.WARNING,
                                    "Auto MS/MS Annotations",
                                    "Automatic MS/MS spectrum annotations."
                            )
                    ));
                } else {
                    EventBus.publish(new SummaryReportEvent(
                            this,
                            controller,
                            new SummaryReportMessage(
                                    SummaryReportMessage.Type.SUCCESS,
                                    "Imported MS/MS Annotations",
                                    "MS/MS annotations imported from the search engine."
                            )
                    ));
                }

                // help button
                Icon helpIcon = GUIUtilities.loadIcon(appContext.getProperty("help.icon.small"));
                String helpTooltip = appContext.getProperty("help.tooltip");
                PrideAction helpAction = new OpenHelpAction(null, helpIcon, "help.mzgraph.fragmentation");
                helpAction.putValue(Action.SHORT_DESCRIPTION, helpTooltip);
                JToggleButton helpButton = mzTablePanel.getHelpButton();
                helpButton.setVisible(true);
                helpButton.setAction(helpAction);
            }
        }
    }
}
