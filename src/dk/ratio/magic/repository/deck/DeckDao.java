package dk.ratio.magic.repository.deck;

import java.util.List;

import dk.ratio.magic.domain.db.deck.Comment;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.util.repository.Page;

public interface DeckDao
{
    public Deck getDeck(int id);
    public List<Comment> getComments(int deckId);

    public int getPublicDecksCount();
    public List<Deck> getPublicDecks();
    public List<Deck> getPublicDecksInRange(int begin, int end);
    public List<Deck> getUserDecks(int userId);

    public Page<Deck> getPublicDeckPage(Integer pageNumber);
    public Page<Deck> getUserDeckPage(Integer pageNumber, Integer userId);
    public Page<Deck> getPublicUserDeckPage(Integer pageNumber, Integer userId);

    public Deck addDeck(Deck deck, User author);
    public Deck duplicateDeck(User author, int oldDeckId, String newTitle);
    public void update(Deck deck);
    public void deleteDeck(Integer deckId);

    public Comment addComment(Comment comment, int deckId, User author);
}
