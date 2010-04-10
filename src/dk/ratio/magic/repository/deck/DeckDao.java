package dk.ratio.magic.repository.deck;

import java.util.List;

import dk.ratio.magic.domain.db.deck.Comment;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.util.repository.Page;

public interface DeckDao
{
    public Deck get(int id);
    public Deck create(Deck deck, User author);
    public Deck copy(User author, int oldDeckId, String newTitle);
    public void update(Deck deck);
    public void delete(Integer deckId);

    public Deck updateCharts(Deck deck);

    public Page<Deck> getPublicDeckPage(Integer pageNumber);
    public Page<Deck> getHiddenUserDeckPage(Integer pageNumber, Integer userId);
    public Page<Deck> getPublicUserDeckPage(Integer pageNumber, Integer userId);

    public List<Comment> getComments(int deckId);
    public Comment addComment(Comment comment, int deckId, User author);
}
