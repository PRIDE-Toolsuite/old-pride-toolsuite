package uk.ac.ebi.pride.chart;

import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import uk.ac.ebi.pride.chart.dataset.PrideDataType;
import uk.ac.ebi.pride.chart.dataset.PrideDatasetFactory;
import uk.ac.ebi.pride.chart.dataset.PrideHistogramDataSource;
import uk.ac.ebi.pride.chart.dataset.PrideXYDataSource;
import uk.ac.ebi.pride.chart.io.PrideDataReader;
import uk.ac.ebi.pride.chart.io.PrideSpectrumHistogramDataSource;
import uk.ac.ebi.pride.chart.plot.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 * User: Qingwei
 * Date: 12/06/13
 */
public class PrideChartFactory {
    private static ChartTheme currentTheme = new StandardChartTheme("JFree");

    public static PrideXYPlot getXYPlot(PrideXYDataSource dataSource, PrideChartType type) {
        if (dataSource == null) {
            return null;
        }

        PrideXYPlot plot;
        switch (type) {
            case DELTA_MASS:
                plot = new DeltaMZPlot(PrideDatasetFactory.getXYDataset(dataSource));
                break;
            case PEPTIDES_PROTEIN:
                plot = new PeptidesProteinPlot(PrideDatasetFactory.getXYDataset(dataSource));
                break;
            case MISSED_CLEAVAGES:
                plot = new MissedCleavagesPlot(PrideDatasetFactory.getXYDataset(dataSource));
                break;
            case PRECURSOR_CHARGE:
                plot = new PrecursorChargePlot(PrideDatasetFactory.getXYDataset(dataSource));
                break;
            case PRECURSOR_MASSES:
                plot = new PrecursorMassesPlot(PrideDatasetFactory.getXYDataset(dataSource), PrideDataType.ALL_SPECTRA);
                break;
            default:
                throw new IllegalArgumentException("Can not create XY style plot.");
        }

        return plot;
    }

    public static PrideXYPlot getAvgPlot(PrideHistogramDataSource dataSource) {
        return new AverageMSPlot(PrideDatasetFactory.getXYDataset((PrideSpectrumHistogramDataSource) dataSource), PrideDataType.ALL_SPECTRA);
    }

    public static PrideCategoryPlot getHistogramPlot(PrideHistogramDataSource dataSource, PrideChartType type) {
        if (dataSource == null) {
            return null;
        }

        PrideCategoryPlot plot;
        switch (type) {
            case PEAKS_MS:
                plot = new PeaksMSPlot(PrideDatasetFactory.getHistogramDataset(dataSource));
                break;
            case PEAK_INTENSITY:
                plot = new PeakIntensityPlot(PrideDatasetFactory.getHistogramDataset(dataSource), PrideDataType.ALL_SPECTRA);
                break;
            default:
                throw new IllegalArgumentException("Can not create Histogram style plot.");
        }

        return plot;
    }

    public static JFreeChart getChart(PrideCategoryPlot plot) {
        if (plot == null) {
            return null;
        }

        String title = plot.isSmallPlot() ? plot.getTitle() : plot.getFullTitle();

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, plot.isLegend());
        currentTheme.apply(chart);
        return chart;
    }

    public static JFreeChart getChart(PrideXYPlot plot) {
        if (plot == null) {
            return null;
        }

        String title = plot.isSmallPlot() ? plot.getTitle() : plot.getFullTitle();

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, plot.isLegend());
        currentTheme.apply(chart);
        return chart;
    }

    public static JFreeChart getChart(PrideDataReader reader, PrideChartType chartType) {
        if (chartType == null) {
            throw new NullPointerException("Chart Type can not set null!");
        }

        SortedMap<PrideChartType, PrideXYDataSource> xyDataSourceMap = reader.getXYDataSourceMap();
        SortedMap<PrideChartType, PrideHistogramDataSource> histogramDataSourceMap = reader.getHistogramDataSourceMap();

        PrideXYDataSource xyDataSource;
        PrideHistogramDataSource histogramDataSource;

        xyDataSource = xyDataSourceMap.get(chartType);
        if (xyDataSource != null) {
            return getChart(getXYPlot(xyDataSource, chartType));
        }

        histogramDataSource = histogramDataSourceMap.get(chartType);
        if (histogramDataSource != null) {
            if (chartType == PrideChartType.AVERAGE_MS) {
                return getChart(getAvgPlot(histogramDataSource));
            } else {
                return getChart(getHistogramPlot(histogramDataSource, chartType));
            }
        }

        return null;
    }

    public static List<JFreeChart> getChartList(PrideDataReader reader, PrideChartSummary summary) {
        List<JFreeChart> charts = new ArrayList<JFreeChart>();

        JFreeChart chart;
        for (PrideChartType chartType : summary.getAll()) {
            chart = getChart(reader, chartType);

            if (chart != null) {
                charts.add(chart);
            }
        }

        return charts;
    }


}
