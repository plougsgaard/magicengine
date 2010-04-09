package dk.ratio.magic.services.deck.chart;

import dk.ratio.magic.domain.db.deck.Deck;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;

import java.awt.*;
import java.util.ArrayList;

/**
 * A chart representing the mana curve of a deck. It presents two categories; the mana curve for creatures, and the
 * mana curve for other spells.
 */
public class ManaCurveChart implements Chart {
    private final Log logger = LogFactory.getLog(getClass());

    JFreeChart creatureCurveChart;
    JFreeChart spellCurveChart;
    JFreeChart coalescedCurveChart;
    DeckStatistics stats;
    Deck deck;

    public ManaCurveChart(Deck deck) {
        logger.info("[ManaCurveChart] Instantiating!");
        this.deck = deck;

        logger.info("[ManaCurveChart] Creating statistics.");
        stats = new DeckStatistics(deck);

        logger.info("[ManaCurveChart] Creating charts...");
        creatureCurveChart = makeCreatureChart();
        spellCurveChart = makeSpellsChart();
        coalescedCurveChart = makeCoalescedChart();
    }

    private JFreeChart makeCreatureChart() {
        JFreeChart chart = ChartFactory.createXYBarChart(
                null,
                "Converted Mana Cost",
                false,
                "Cards",
                makeDataSet(stats.getCreatureCMCs()),
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        setStyle(chart);

        return chart;
    }

    private JFreeChart makeSpellsChart() {
        JFreeChart chart = ChartFactory.createXYBarChart(
                null,
                "Converted Mana Cost",
                false,
                "Cards",
                makeDataSet(stats.getSpellCMCs()),
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        setStyle(chart);

        return chart;
    }

    private JFreeChart makeCoalescedChart() {
        JFreeChart chart = ChartFactory.createXYBarChart(
                null,
                "Converted Mana Cost",
                false,
                "Cards",
                makeDataSet(stats.getCoalescedCMCs()),
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        setStyle(chart);

        return chart;
    }

    private XYIntervalSeriesCollection makeDataSet(ArrayList<Pair<Integer, Integer>> list) {
        XYIntervalSeriesCollection dataSet = new XYIntervalSeriesCollection();

        for (Pair<Integer, Integer> pair : list) {
            XYIntervalSeries series = new XYIntervalSeries(pair.getFirst());
            addToSeriesWithXInterval(series, 0.25d, pair.getFirst(), pair.getSecond());
            dataSet.addSeries(series);
        }

        return dataSet;
    }

    private void setStyle(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();

        chart.setAntiAlias(true);

        chart.getPlot().setBackgroundPaint(new Color(238, 238, 238));

        // domain axis = x axis
        chart.getXYPlot().getDomainAxis().setAxisLineVisible(false);
        chart.getXYPlot().getDomainAxis().setLabelPaint(new Color(139, 138, 138));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Georgia", Font.PLAIN, 22));
        chart.getXYPlot().getDomainAxis().setTickMarkPaint(new Color(160, 160, 160));
        chart.getXYPlot().getDomainAxis().setTickLabelPaint(new Color(77, 76, 76));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(new Font("Georgia", Font.BOLD, 16));

        // range axis = y axis
        chart.getXYPlot().getRangeAxis().setAxisLineVisible(false);
        chart.getXYPlot().getRangeAxis().setLabelPaint(new Color(139, 138, 138));
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Georgia", Font.PLAIN, 22));
        chart.getXYPlot().getRangeAxis().setTickMarkPaint(new Color(160, 160, 160));
        chart.getXYPlot().getRangeAxis().setTickLabelPaint(new Color(77, 76, 76));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(new Font("Georgia", Font.BOLD, 16));

        chart.getXYPlot().setDomainGridlinePaint(new Color(160, 160, 160)); // y-grid
        chart.getXYPlot().setDomainCrosshairVisible(false);
        chart.getXYPlot().setDomainMinorGridlinesVisible(false);
        chart.getXYPlot().setOutlineVisible(true);
        chart.getXYPlot().setOutlinePaint(new Color(160, 160, 160));

        chart.getXYPlot().setRangeZeroBaselineVisible(false);
        chart.getXYPlot().setRangeMinorGridlinesVisible(true);
        chart.getXYPlot().setRangeMinorGridlinePaint(new Color(200,  200,  200)); // minor x-grid
        chart.getXYPlot().setRangeGridlinePaint(new Color(217,  91,  67)); // x-grid
        chart.getXYPlot().setRangeGridlinesVisible(true);
        chart.getXYPlot().setRangeCrosshairVisible(false);

        // Set y-axis to display integral values only:
        NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // Set x-axis to display integral values only:
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // Set bar colours
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(246, 230, 179));
        renderer.setSeriesPaint(1, new Color(236, 208, 120));
        renderer.setSeriesPaint(2, new Color(217,  91,  67));
        renderer.setSeriesPaint(3, new Color(192,  41,  66));
        renderer.setSeriesPaint(4, new Color( 84,  36,  55));
        renderer.setSeriesPaint(5, new Color( 83, 119, 122));
        renderer.setSeriesPaint(6, new Color(  0, 133, 191));
        renderer.setSeriesPaint(7, new Color(  0, 110, 183));

        // Remove ugly 3D-like shadow effect:
        XYBarRenderer xyRenderer = (XYBarRenderer) renderer;
        xyRenderer.setShadowVisible(false);
        xyRenderer.setBarPainter(new StandardXYBarPainter());
    }

    private void addToSeriesWithXInterval(XYIntervalSeries series, double interval, int xVal, int yVal) {
        series.add(xVal, xVal - interval, xVal + interval, yVal, yVal, yVal);
    }

    /**
     * Get a chart showing the mana curve only for creatures.
     *
     * @return the mana curve for creatures as a chart.
     */
    public JFreeChart getCreatureCurveChart() {
        return creatureCurveChart;
    }

    /**
     * Get a chart showing the mana curve only for non-creature spells.
     *
     * @return the mana curve for non-creature spells as a chart.
     */
    public JFreeChart getSpellCurveChart() {
        return spellCurveChart;
    }

    /**
     * Get a chart showing the mana curve for all spells.
     *
     * @return the mana curve for all spells.
     */
    public JFreeChart getCoalescedCurveChart() {
        return coalescedCurveChart;
    }

    /**
     * Default returns the coalesced mana curve chart.
     *
     * @return the coalesced mana curve chart.
     */
    public JFreeChart getChart() {
        return getCoalescedCurveChart();
    }
}
