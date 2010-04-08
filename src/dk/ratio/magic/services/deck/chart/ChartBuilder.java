package dk.ratio.magic.services.deck.chart;

import dk.ratio.magic.domain.db.deck.Deck;
import org.jfree.chart.JFreeChart;

import java.awt.image.BufferedImage;

public class ChartBuilder {

    private int height;
    private int width;

    public ChartBuilder(int height, int width) {
        this.height = height;
        this.width = width;
    }

    private BufferedImage imageFromChart(JFreeChart chart) {
        return chart.createBufferedImage(width, height);
    }

    public BufferedImage createCoalescedManaCurveChart(Deck deck) {
        ManaCurveChart manaCurveChart = new ManaCurveChart(deck);
        return imageFromChart(manaCurveChart.getChart());
    }

    public BufferedImage createCreatureManaCurveChart(Deck deck) {
        ManaCurveChart manaCurveChart = new ManaCurveChart(deck);
        return imageFromChart(manaCurveChart.getCreatureCurveChart());
    }

    public BufferedImage createSpellManaCurveChart(Deck deck) {
        ManaCurveChart manaCurveChart = new ManaCurveChart(deck);
        return imageFromChart(manaCurveChart.getSpellCurveChart());
    }
}
