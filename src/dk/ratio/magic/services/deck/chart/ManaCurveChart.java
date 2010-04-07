package dk.ratio.magic.services.deck.chart;

import dk.ratio.magic.domain.db.deck.Deck;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * A chart representing the mana curve of a deck. It presents two categories; the mana curve for creatures, and the
 * mana curve for other spells.
 */
public class ManaCurveChart implements Chart {
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

        // Add column with other spells mana curve to data set:
        for (Pair<Integer, Integer> pair : stats.getSpellCMCs()) {
            dataSet.addValue(pair.getSecond(), "CMC " + pair.getFirst(), "");
        }

        // Add column with creature mana curve to data set:
        for (Pair<Integer, Integer> pair : stats.getCreatureCMCs()) {
            dataSet.addValue(pair.getSecond(), "CMC " + pair.getFirst(), "");
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

    public JFreeChart getCreatureCurveChart() {
        return creatureCurveChart;
    }

    public JFreeChart getSpellCurveChart() {
        return spellCurveChart;
    }

    public JFreeChart getCoalescedCurveChart() {
        return coalescedCurveChart;
    }

    /**
     * Default returns the coalesced mana curve chart.
     *
     * @return the coalesced mana curve chart.
     */
    public JFreeChart getChart() {
        return coalescedCurveChart;
    }
}
