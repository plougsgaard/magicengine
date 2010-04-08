package dk.ratio.magic.services.deck.chart;

import dk.ratio.magic.domain.db.deck.Deck;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
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
                "CMC",
                false,
                "Number of cards",
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
                "CMC",
                false,
                "Number of cards",
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
                "CMC",
                false,
                "Number of cards",
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

        // Set y-axis to display integral values only:
        NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // Set bar colours to blue:
        XYItemRenderer renderer = plot.getRenderer();
        for (int i = 0; i <= 15; i++) {
            renderer.setSeriesPaint(i, Color.BLUE);
        }
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
