package dk.ratio.magic.domain.db.card;

import java.util.Date;

public class QueueItem
{
    private int cardId;
    private int sellerId;
    private Date dateAdded;
    private Double price;
    private String cardName;

    public int getCardId()
    {
        return cardId;
    }

    public void setCardId(int cardId)
    {
        this.cardId = cardId;
    }

    public int getSellerId()
    {
        return sellerId;
    }

    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }

    public Date getDateAdded()
    {
        return dateAdded == null ? null : (Date) dateAdded.clone();
    }

    public void setDateAdded(Date dateAdded)
    {
        this.dateAdded = dateAdded == null ? null : (Date) dateAdded.clone();
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public String getCardName()
    {
        return cardName;
    }

    public void setCardName(String cardName)
    {
        this.cardName = cardName;
    }

    @Override
    public String toString()
    {
        return "QueueItem{" +
                "cardId=" + cardId +
                ", sellerId=" + sellerId +
                ", dateAdded=" + dateAdded +
                ", price=" + price +
                ", cardName='" + cardName + '\'' +
                '}';
    }
}
