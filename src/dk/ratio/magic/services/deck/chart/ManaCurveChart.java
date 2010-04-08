package dk.ratio.magic.services.deck.chart;

import dk.ratio.magic.domain.db.deck.Deck;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

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
        this.deck = deck;
        stats = new DeckStatistics(deck);
        creatureCurveChart = makeCreatureChart();
        spellCurveChart = makeSpellsChart();
        coalescedCurveChart = makeCoalescedChart();
    }

    private JFreeChart makeCreatureChart() {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        // Add column with creature spell mana curve:
        for (Pair<Integer, Integer> pair : stats.getCreatureCMCs()) {
            dataSet.addValue(pair.getSecond(), "CMC " + pair.getFirst(), "");
        }

        return ChartFactory.createBarChart(
                "Mana curve (creatures)", // Chart title
                "CMC", // x-axis description
                "Number of cards", // y-axis description
                dataSet,
                PlotOrientation.VERTICAL,
                true, // Chart contains a legend
                false, // No tooltips
                false // No urls
        );
    }

    private JFreeChart makeSpellsChart() {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        // Add column with other spells mana curve:
        for (Pair<Integer, Integer> pair : stats.getSpellCMCs()) {
            dataSet.addValue(pair.getSecond(), "CMC " + pair.getFirst(), "");
        }

        return ChartFactory.createBarChart(
                "Mana curve (other spells)", // Chart title
                "CMC", // x-axis description
                "Number of cards", // y-axis description
                dataSet,
                PlotOrientation.VERTICAL,
                true, // Chart contains a legend
                false, // No tooltips
                false // No urls
        );
    }

    private JFreeChart makeCoalescedChart() {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        // Add column with creature mana curve to data set:
        for (Pair<Integer, Integer> pair : stats.getCreatureCMCs()) {
            if (pair.getSecond() != 0) {
                //logger.info("CMC, #Creatures: " + pair.getFirst() + ", " + pair.getSecond());
                dataSet.addValue(pair.getSecond(), pair.getFirst(), "Spells");
            }
        }

        // Add column with other spells mana curve to data set:
        for (Pair<Integer, Integer> pair : stats.getSpellCMCs()) {
            if (pair.getSecond() != 0) {
                //logger.info("CMC, #Spells: " + pair.getFirst() + ", " + pair.getSecond());
                dataSet.addValue(pair.getSecond(), pair.getFirst(), "Creatures");
            }
        }
        return ChartFactory.createBarChart(
                "Mana curve (coalesced)", // Chart title
                "CMC", // x-axis description
                "Number of cards", // y-axis description
                dataSet,
                PlotOrientation.VERTICAL,
                true, // Chart contains a legend
                false, // No tooltips
                false // No urls
        );
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
