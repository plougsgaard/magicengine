package dk.ratio.magic.domain.db.card;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class Card implements Comparable<Card>
{
    private final Log logger = LogFactory.getLog(getClass());

    private int id;

    private String cardName;
    private String manaCost = "";
    private String convertedManaCost = "";
    private String types = "";
    private String cardText = "";
    private String expansion = "";
    private String setCode = "";
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
        if (StringUtils.isBlank(convertedManaCost)) {
            return "0";
        }
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

    public String getSetCode()
    {
        return setCode;
    }

    public void setSetCode(String setCode)
    {
        this.setCode = setCode;
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

    @Override
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
                ", setCode='" + setCode + '\'' +
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
     * Internal order is subject to CMC where less is first.
     *
     * @param other other card
     * @return -1, 0 and 1 depending on the order
     */
    public int compareTo(Card other)
    {
        int cmc = 0, cmcOther = 0;
        try {
            cmc = Integer.parseInt(convertedManaCost);
        } catch (NumberFormatException ignored) {}
        try {
            cmcOther = Integer.parseInt(other.getConvertedManaCost());
        } catch (NumberFormatException ignored) {}

        if (types.contains("Land")) {
            if (other.getTypes().contains("Basic Land")) {
                return 1;
            }
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
                return cmc <= cmcOther ? -1 : 1;
            } else {
                return -1;
            }
        }
        else if (other.getTypes().contains("Land") || other.getTypes().contains("Creature")) {
            return 1;
        }
        return cmc <= cmcOther ? -1 : 1;
    }
}
