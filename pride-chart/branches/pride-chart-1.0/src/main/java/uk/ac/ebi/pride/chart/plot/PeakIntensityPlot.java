package uk.ac.ebi.pride.chart.plot;

import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import uk.ac.ebi.pride.chart.PrideChartType;
import uk.ac.ebi.pride.chart.dataset.PrideDataType;
import uk.ac.ebi.pride.chart.plot.label.CategoryPercentageLabel;

import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

/**
* User: Qingwei
* Date: 14/06/13
*/
public class PeakIntensityPlot extends PrideCategoryPlot {
    private CategoryDataset dataset;

    public PeakIntensityPlot(CategoryDataset dataset, PrideDataType dataType) {
        this(dataset, dataType, true);
    }

    public PeakIntensityPlot(CategoryDataset dataset, PrideDataType dataType, boolean smallPlot) {
        super(PrideChartType.PEAK_INTENSITY, dataset, smallPlot);

        BarRenderer renderer = (BarRenderer) getRenderer();
        for (int i = 0; i < dataset.getColumnCount(); i++) {
            renderer.setSeriesItemLabelGenerator(i, new CategoryPercentageLabel());
            renderer.setSeriesItemLabelsVisible(i, true);
        }

        this.dataset = dataset;

        // only display current data type series.
        for (PrideDataType type : dataType.getChildren()) {
            setVisible(false, type);
        }
        setVisible(true, dataType);
    }

    public void setVisible(boolean visible, PrideDataType dataType) {
        BarRenderer renderer = (BarRenderer) getRenderer();

        for (int i = 0; i < dataset.getRowCount(); i++) {
            if (dataset.getRowKey(i).equals(PrideDataType.ALL_SPECTRA)) {
                renderer.setSeriesPaint(i, Color.BLUE);
            } else if (dataset.getRowKey(i).equals(PrideDataType.IDENTIFIED_SPECTRA)) {
                renderer.setSeriesPaint(i, Color.RED);
            } else if (dataset.getRowKey(i).equals(PrideDataType.UNIDENTIFIED_SPECTRA)) {
                renderer.setSeriesPaint(i, Color.GREEN);
            }

            if (dataset.getRowKey(i).equals(dataType.getTitle())) {
                renderer.setSeriesVisible(i, visible);
            }
        }
    }

    @Override
    public Map<PrideDataType, Boolean> getOptionList() {
        Map<PrideDataType, Boolean> optionList = new TreeMap<PrideDataType, Boolean>();

        optionList.put(PrideDataType.IDENTIFIED_SPECTRA, false);
        optionList.put(PrideDataType.UNIDENTIFIED_SPECTRA, false);
        optionList.put(PrideDataType.ALL_SPECTRA, false);

        PrideDataType dataType;
        for (Object key : dataset.getRowKeys()) {
            dataType = PrideDataType.findBy((String) key);
            optionList.put(dataType, true);
        }

        return optionList;
    }

    @Override
    public boolean isMultiOptional() {
        return true;
    }
}
