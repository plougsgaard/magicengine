package dk.ratio.magic.domain.web.decks;

import java.util.Arrays;

public class DeckFilter
{
    String title;
    String author;
    String[] colours;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String[] getColours()
    {
        return colours;
    }

    public void setColours(String[] colours)
    {
        this.colours = colours;
    }

    @Override
    public String toString()
    {
        return "DeckFilter{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", colours=" + (colours == null ? null : Arrays.asList(colours)) +
                '}';
    }
}
