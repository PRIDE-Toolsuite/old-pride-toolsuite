package uk.ac.ebi.pride.chart;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: qingwei
 * Date: 05/07/13
 */
public class PrideChartTypeTest {
    @Test
    public void testNavigate() throws Exception {
        PrideChartType previous = PrideChartType.DELTA_MASS;
        for (PrideChartType chartType : PrideChartType.values()) {
            assertEquals(chartType.previous(), previous);
            previous = chartType;
        }

        PrideChartType next = PrideChartType.PEAK_INTENSITY;
        PrideChartType[] types = PrideChartType.values();
        for (int i = types.length - 1; i >= 0; i--) {
            assertEquals(types[i].next(), next);
            next = types[i];
        }
    }
}
