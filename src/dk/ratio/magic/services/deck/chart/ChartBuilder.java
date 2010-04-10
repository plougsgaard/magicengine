package dk.ratio.magic.services.deck.chart;

import dk.ratio.magic.domain.db.deck.Deck;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ChartBuilder {

    private Log logger = LogFactory.getLog(getClass());

    private int height;
    private int width;

    public ChartBuilder(int height, int width) {
        this.height = height;
        this.width = width;
    }

    private BufferedImage imageFromChart(JFreeChart chart) {
        return chart.createBufferedImage(width, height);
    }

    /**
     * Create an image representing the mana curve over all spells in a deck.
     *
     * @param deck the deck to create the mana curve for.
     * @return an image of the mana curve chart.
     */
    public BufferedImage createCoalescedManaCurveChart(Deck deck) {
        ManaCurveChart manaCurveChart = new ManaCurveChart(deck);
        return imageFromChart(manaCurveChart.getChart());
    }

    /**
     * Create an image representing the mana curve over the creature spells in a deck.
     *
     * @param deck the deck to create the mana curve for.
     * @return an image of the mana curve chart.
     */
    public BufferedImage createCreatureManaCurveChart(Deck deck) {
        ManaCurveChart manaCurveChart = new ManaCurveChart(deck);
        return imageFromChart(manaCurveChart.getCreatureCurveChart());
    }

    /**
     * Create an image representing the mana curve over the non-creature spells in a deck.
     *
     * @param deck the deck to create the mana curve for.
     * @return an image of the mana curve chart.
     */
    public BufferedImage createSpellManaCurveChart(Deck deck) {
        ManaCurveChart manaCurveChart = new ManaCurveChart(deck);
        return imageFromChart(manaCurveChart.getSpellCurveChart());
    }

    public Deck buildCharts(Deck deck) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        boolean written = ImageIO.write(
                createCreatureManaCurveChart(deck), "png", outputStream);
        if (!written) {
            logger.warn("Failed to write creature chart!");
        }
        outputStream.flush();
        deck.setChartCurveCreatures(outputStream.toByteArray());
        outputStream.reset();

        written = ImageIO.write(
                createSpellManaCurveChart(deck), "png", outputStream);
        if (!written) {
            logger.warn("Failed to write spell chart!");
        }
        outputStream.flush();
        deck.setChartCurveSpells(outputStream.toByteArray());
        outputStream.reset();

        written = ImageIO.write(
                createCoalescedManaCurveChart(deck), "png", outputStream);
        if (!written) {
            logger.warn("Failed to write creature chart!");
        }
        outputStream.flush();
        deck.setChartCurveAll(outputStream.toByteArray());

        outputStream.close();

        return deck;
    }

}
