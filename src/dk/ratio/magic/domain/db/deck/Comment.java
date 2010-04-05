package dk.ratio.magic.domain.db.deck;

import dk.ratio.magic.domain.db.user.User;

import java.util.Date;

public class Comment
{
    private int id;
    private User author;
    private String text;
    private Date dateAdded;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public User getAuthor()
    {
        return author;
    }

    public void setAuthor(User author)
    {
        this.author = author;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Date getDateAdded()
    {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded)
    {
        this.dateAdded = dateAdded;
    }
}
