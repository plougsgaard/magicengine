package dk.ratio.magic.domain.db.deck;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.user.User;

import java.util.List;

public class Deck
{
    private int id;
    private String title;
    private String format;
    private String status;
    private String colours;
    private String description;
    private List<Card> cards;
    private User author;

    private byte[] chartCurveAll;
    private byte[] chartCurveCreatures;
    private byte[] chartCurveSpells;

    private int featureCardId;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getColours()
    {
        return colours;
    }

    public void setColours(String colours)
    {
        this.colours = colours;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<Card> getCards()
    {
        return cards;
    }

    public void setCards(List<Card> cards)
    {
        this.cards = cards;
    }

    public User getAuthor()
    {
        return author;
    }

    public void setAuthor(User author)
    {
        this.author = author;
    }

    public byte[] getChartCurveAll()
    {
        return chartCurveAll;
    }

    public void setChartCurveAll(byte[] chartCurveAll)
    {
        this.chartCurveAll = chartCurveAll;
    }

    public byte[] getChartCurveCreatures()
    {
        return chartCurveCreatures;
    }

    public void setChartCurveCreatures(byte[] chartCurveCreatures)
    {
        this.chartCurveCreatures = chartCurveCreatures;
    }

    public byte[] getChartCurveSpells()
    {
        return chartCurveSpells;
    }

    public void setChartCurveSpells(byte[] chartCurveSpells)
    {
        this.chartCurveSpells = chartCurveSpells;
    }

    public int getFeatureCardId()
    {
        return featureCardId;
    }

    public void setFeatureCardId(int featureCardId)
    {
        this.featureCardId = featureCardId;
    }

    @Override
    public String toString()
    {
        return "Deck{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", format='" + format + '\'' +
                ", status='" + status + '\'' +
                ", colours='" + colours + '\'' +
                ", description='" + description + '\'' +
                ", cards=" + cards +
                ", author=" + author +
                ", chartCurveAll=" + ((chartCurveAll == null) ? "null" : "not null") +
                ", chartCurveCreatures=" + ((chartCurveCreatures == null) ? "null" : "not null") +
                ", chartCurveSpells=" + ((chartCurveSpells == null) ? "null" : "not null") +
                ", featureCardId=" + featureCardId +
                '}';
    }
}
