package dk.ratio.magic.repository.card;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Image;
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
    
    public Image getCardImage(int cardId);
    public List<Card> getSuggestions(String likeness);

    public int getCardCount();

    public Page<Card> getCardPage(Integer pageNumber);

    public Card addCard(Card card, Image image);
    public Card addCard(Card card, Image image, List<Price> prices);

    public Card addPrices(Card card, List<Price> prices);
    public Card updatePrice(Card card);
}
