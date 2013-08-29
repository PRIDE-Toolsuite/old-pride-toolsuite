package uk.ac.ebi.pride.gui.component.chart;

import uk.ac.ebi.pride.chart.PrideChartType;
import uk.ac.ebi.pride.chart.io.PrideDataReader;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.impl.ControllerImpl.PrideDBAccessControllerImpl;
import uk.ac.ebi.pride.gui.GUIUtilities;
import uk.ac.ebi.pride.gui.PrideInspector;
import uk.ac.ebi.pride.gui.PrideInspectorContext;
import uk.ac.ebi.pride.gui.component.DataAccessControllerPane;
import uk.ac.ebi.pride.gui.component.PrideInspectorLoadingPanel;
import uk.ac.ebi.pride.gui.component.startup.ControllerContentPane;
import uk.ac.ebi.pride.gui.component.utils.Iconable;
import uk.ac.ebi.pride.gui.task.TaskAdapter;
import uk.ac.ebi.pride.gui.task.TaskEvent;
import uk.ac.ebi.pride.gui.task.impl.LoadChartDataTask;
import uk.ac.ebi.pride.gui.utils.DefaultGUIBlocker;
import uk.ac.ebi.pride.gui.utils.GUIBlocker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>Class to create a TabPane for integrating the PRIDE-Chart into PRIDE-Inspector.</p>
 *
 * @author Antonio Fabregat
 *         Date: 10-ago-2010
 *         Time: 14:10:41
 */
public class ChartTabPane extends DataAccessControllerPane<PrideDataReader, Void>
        implements Iconable {
    /**
     * The tab title
     */
    private static final String PANE_TITLE = "Summary Charts";

    /**
     * The number of columns
     */
    private static final int COLS = 3;

    /**
     * Reference to inspector context
     */
    private PrideInspectorContext viewerContext;

    /**
     * The list of charts to be managed in the tab
     */
    private PrideDataReader reader;

    /**
     * Constructor
     *
     * @param controller data access controller
     * @param parentComp parent container
     */
    public ChartTabPane(DataAccessController controller, JComponent parentComp) {
        super(controller, parentComp);
    }

    /**
     * Setup the main pane
     */
    @Override
    protected void setupMainPane() {
        viewerContext = (PrideInspectorContext) uk.ac.ebi.pride.gui.desktop.Desktop.getInstance().getDesktopContext();
        setTitle(PANE_TITLE);

        // set the final icon
        PrideInspectorContext context = (PrideInspectorContext) PrideInspector.getInstance().getDesktopContext();
//        this.setIcon(GUIUtilities.loadIcon(context.getProperty("chart.icon.small")));

        // set the loading icon
        this.setLoadingIcon(GUIUtilities.loadIcon(context.getProperty("chart_loading.icon.small")));
    }

    /**
     * Set properties for ChartTabPane
     *
     * @param charts the number of charts to be displayed
     */
    private void setupInitialMainPane(int charts) {
        int border = 3;
        int rows = (int) Math.ceil(charts / (double) COLS);
        setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        setLayout(new GridLayout(rows, COLS, border, border));
    }

    private void createPrideCharts() {
        TaskAdapter<PrideDataReader, Void> lcd;

        if (controller.getType().equals(DataAccessController.Type.DATABASE)) {
            lcd = new LoadChartDataTask(controller, (String) ((PrideDBAccessControllerImpl) controller).getExperimentAcc());
        } else {
            lcd = new LoadChartDataTask(controller);
        }

        // add a task listener
        lcd.addTaskListener(this);

        // start running the task
        lcd.setGUIBlocker(new DefaultGUIBlocker(lcd, GUIBlocker.Scope.NONE, null));
        viewerContext.addTask(lcd);
    }

    public void showBigView(PrideChartType chartType) {
        removeAll();
        setLayout(new BorderLayout());
        add(new PrideChartBigPane(this, reader, chartType), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showThumbnailView(PrideDataReader reader) {
        removeAll();

        PrideChartType[] chartTypeList = PrideChartType.values();
        setupInitialMainPane(chartTypeList.length);

        for (PrideChartType chartType : chartTypeList) {
            PrideChartThumbnailPane ct = new PrideChartThumbnailPane(this, reader, chartType);
            add(ct);
        }
        revalidate();
        repaint();
    }

    private void showWarningMessage(String msg, boolean launchButton) {
        this.setLayout(new BorderLayout());

        JPanel msgPanel = new JPanel();
        msgPanel.setPreferredSize(new Dimension(500, 40));
        msgPanel.setBackground(Color.white);
        msgPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.LINE_AXIS));

        msgPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        // warning message label
        JLabel msgLabel = new JLabel(GUIUtilities.loadIcon(viewerContext.getProperty("chart.warning.icon.small")));
        msgLabel.setText(msg);
        msgPanel.add(msgLabel);

        // add a glue to fill the empty space
        msgPanel.add(Box.createHorizontalGlue());

        if (launchButton) {
            // button to start calculate charts
            JButton computeButton = new JButton("Start");
            computeButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ChartTabPane.this.removeAll();
                    createPrideCharts();
                }
            });
            msgPanel.add(computeButton);
        }

        msgPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        this.add(msgPanel, BorderLayout.NORTH);

        if (!launchButton) {
            JPanel loadingPanel = new PrideInspectorLoadingPanel();
            this.add(loadingPanel, BorderLayout.CENTER);
        }

    }

    /**
     * Add the rest of components
     */
    @Override
    public void populate() {
        removeAll();
        //get spectra threshold
        if (DataAccessController.Type.DATABASE.equals(controller.getType())) {
            createPrideCharts();
        } else {
            String msg = viewerContext.getProperty("chart.warning.message");
            showWarningMessage(msg, true);
        }
    }

    @Override
    public void succeed(TaskEvent<PrideDataReader> listTaskEvent) {
        this.reader = listTaskEvent.getValue();
        int chartSize = reader.getHistogramDataSourceMap().size() + reader.getXYDataSourceMap().size() + reader.getErrorMap().size();

        showThumbnailView(reader);

        String title = PANE_TITLE + " (" + chartSize + ")";
        setTitle(title);

        ControllerContentPane dataContentPane = (ControllerContentPane) viewerContext.getDataContentPane(controller);
        if (dataContentPane != null) {
            dataContentPane.setTabTitle(dataContentPane.getChartTabIndex(), title);
        }
    }

    @Override
    public void started(TaskEvent event) {
        showIcon(getLoadingIcon());
    }

    @Override
    public void finished(TaskEvent<Void> event) {
        showIcon(getIcon());
    }

    /**
     * Show a different icon if the parent component is not null and an instance of DataContentDisplayPane
     *
     * @param icon icon to show
     */
    private void showIcon(Icon icon) {
        if (parentComponent != null && parentComponent instanceof ControllerContentPane) {
            ControllerContentPane contentPane = (ControllerContentPane) parentComponent;
            contentPane.setTabIcon(contentPane.getChartTabIndex(), icon);
        }
    }
}
