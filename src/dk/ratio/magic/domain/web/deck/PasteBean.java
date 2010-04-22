package dk.ratio.magic.domain.web.deck;

public class PasteBean
{
    String cards;

    public String getCards()
    {
        return cards;
    }

    public void setCards(String cards)
    {
        this.cards = cards;
    }

    @Override
    public String toString()
    {
        return "PasteBean{" +
                "cards='" + cards + '\'' +
                '}';
    }
}
