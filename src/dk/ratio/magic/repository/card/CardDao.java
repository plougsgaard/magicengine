package dk.ratio.magic.repository.card;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import dk.ratio.magic.util.repository.Page;

import java.util.List;

public interface CardDao
{
    public Card getCard(String cardName);
    public Card getCard(int cardId);
    public Card getPrices(Card card);

    public List<Card> getCards();
    public List<Card> getCards(List<Card> cards);
    
    public byte[] getImage(int cardId);
    public byte[] getCutout(int cardId);
    public void setCutout(int cardId, byte[] data);
    public byte[] getThumbnail(int cardId);
    public void setThumbnail(int cardId, byte[] data);

    public List<Card> getSuggestions(String likeness);

    public Page<Card> getCardPage(Integer pageNumber);

    public Card addCard(Card card, byte[] image);
    public Card addPrices(Card card, List<Price> prices);

    public Card updatePrice(Card card);
}
