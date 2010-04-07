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

    public BufferedImage createManaCurveChart(Deck deck) {
        Chart manaCurveChart = new ManaCurveChart(deck);
        return imageFromChart(manaCurveChart.getChart());
    }
}
