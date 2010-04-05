package dk.ratio.magic.domain.web.decks;

import java.util.Arrays;

public class DeckFilterBean
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
        return "DeckFilterBean{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", colours=" + (colours == null ? null : Arrays.asList(colours)) +
                '}';
    }
}
