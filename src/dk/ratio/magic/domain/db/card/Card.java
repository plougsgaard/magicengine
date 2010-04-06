package dk.ratio.magic.domain.db.card;

import java.util.List;

public class Card implements Comparable<Card>
{
    private int id;

    private String cardName;
    private String manaCost = "";
    private String convertedManaCost = "";
    private String types = "";
    private String cardText = "";
    private String expansion = "";
    private String rarity = "";
    private String cardNumber = "";
    private String artist = "";

    private Double price = 0d;
    private List<Price> prices;

    private int count;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCardName()
    {
        return cardName;
    }

    public void setCardName(String cardName)
    {
        this.cardName = cardName;
    }

    public String getManaCost()
    {
        return manaCost;
    }

    public void setManaCost(String manaCost)
    {
        this.manaCost = manaCost;
    }

    public String getConvertedManaCost()
    {
        return convertedManaCost;
    }

    public void setConvertedManaCost(String convertedManaCost)
    {
        this.convertedManaCost = convertedManaCost;
    }

    public String getTypes()
    {
        return types;
    }

    public void setTypes(String types)
    {
        this.types = types;
    }

    public String getCardText()
    {
        return cardText;
    }

    public void setCardText(String cardText)
    {
        this.cardText = cardText;
    }

    public String getExpansion()
    {
        return expansion;
    }

    public void setExpansion(String expansion)
    {
        this.expansion = expansion;
    }

    public String getRarity()
    {
        return rarity;
    }

    public void setRarity(String rarity)
    {
        this.rarity = rarity;
    }

    public String getCardNumber()
    {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public List<Price> getPrices()
    {
        return prices;
    }

    public void setPrices(List<Price> prices)
    {
        this.prices = prices;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public String toString()
    {
        return "Card{" +
               "id=" + id +
               ", cardName='" + cardName + '\'' +
               ", manaCost='" + manaCost + '\'' +
               ", convertedManaCost='" + convertedManaCost + '\'' +
               ", types='" + types + '\'' +
               ", cardText='" + cardText + '\'' +
               ", expansion='" + expansion + '\'' +
               ", rarity='" + rarity + '\'' +
               ", cardNumber='" + cardNumber + '\'' +
               ", artist='" + artist + '\'' +
               ", price=" + price +
               ", prices=" + prices +
               ", count=" + count +
               '}';
    }

    /**
     * Order:
     *
     * 1) Lands
     * 2) Creatures
     * 3) Other
     *
     * @param other other card
     * @return -1, 0 and 1 depending on the order
     */
    public int compareTo(Card other)
    {
        if (types.contains("Land")) {
            if (other.getTypes().contains("Land")) {
                return 0;
            } else {
                return -1;
            }
        }
        else if (types.contains("Creature")) {
            if (other.getTypes().contains("Land")) {
                return 1;
            } else if (other.getTypes().contains("Creature")) {
                return 0;
            } else {
                return -1;
            }
        }
        else if (other.getTypes().contains("Land") || other.getTypes().contains("Creature")) {
            return 1;
        }
        return 0;
    }
}
