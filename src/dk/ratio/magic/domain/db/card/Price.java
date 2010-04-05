package dk.ratio.magic.domain.db.card;

import java.util.Date;

public class Price implements Comparable<Price>
{
    private int id;
    private Card card;
    private Seller seller;
    private Double price;
    private Date dateAdded;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Card getCard()
    {
        return card;
    }

    public void setCard(Card card)
    {
        this.card = card;
    }

    public Seller getSeller()
    {
        return seller;
    }

    public void setSeller(Seller seller)
    {
        this.seller = seller;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public Date getDateAdded()
    {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded)
    {
        this.dateAdded = dateAdded;
    }

    public int compareTo(Price other)
    {
        if (price < other.getPrice()) {
            return -1;
        } else if (price > other.getPrice()) {
            return 1;
        }
        return 0;
    }
}
