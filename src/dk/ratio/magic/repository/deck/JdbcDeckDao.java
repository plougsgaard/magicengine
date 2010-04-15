package dk.ratio.magic.repository.deck;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.deck.Comment;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.util.repository.Page;
import dk.ratio.magic.util.repository.Pagination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class JdbcDeckDao implements DeckDao
{    
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private SimpleJdbcTemplate simpleJdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private CardDao cardDao;

    /**
     * Fetches a deck (fully populated) from the database.
     *
     * @param deckId id of the deck to fetch
     * @return       deck if found, null ow.
     */
    public Deck get(int deckId)
    {
        List<Deck> results = simpleJdbcTemplate.query(
                DeckMapper.SELECT_FROM +
                        "WHERE deck.id = :deckId",
                new DeckMapper(),
                new MapSqlParameterSource()
                        .addValue("deckId", deckId));
        if (results.size() != 1) {
            logger.warn("A deck was requested, but was not found. " +
                    "[deckId: " + deckId + "]");
            return null;
        }
        Deck result = results.get(0);
        List<Card> shallowCards = simpleJdbcTemplate.query(
                "SELECT " +
                        "card.id, " +
                        "card.deck_id, " +
                        "card.count " +
                        "FROM view_cardscount card " +
                        "WHERE card.deck_id = :deckId",
                new DeckCardMapper(),
                new MapSqlParameterSource()
                        .addValue("deckId", deckId)
        );
        result.setCards(cardDao.getCards(shallowCards));

        return result;
    }

    /**
     * Adds a deck (with no id) to the database and
     * returns the same deck, now with an id.
     *
     * @param deck      deck to add (with no id)
     * @param author    the user who wants to add it
     * @return          deck with id added, null if it could not
     *                  be added for some reason
     */
    public Deck create(Deck deck, User author)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int count = namedParameterJdbcTemplate.update(
            "INSERT INTO decks (title, colours, description, author_id, date_added, date_modified, feature_card_id) " +
            "VALUES (:title, :colours, :description, :author_id, :date_added, :date_modified, :feature_card_id)",
            new MapSqlParameterSource()
                .addValue("title", deck.getTitle())
                .addValue("colours", "")
                .addValue("description", "")
                .addValue("author_id", author.getId())
                .addValue("date_added", new Timestamp(System.currentTimeMillis()))
                .addValue("date_modified", new Timestamp(System.currentTimeMillis()))
                .addValue("feature_card_id", 1),
                keyHolder
        );
        if (keyHolder.getKeyList().size() == 0) {
            logger.error("Did not get any id back from the database.");
            return null;
        } else {
            deck.setId(keyHolder.getKey().intValue());
            logger.info("Added deck to the database. " +
                        "[deck: " + deck + "] " +
                        "[count: " + count + "] " +
                        "");
        }
        return deck;
    }

    public Deck copy(User author, int oldDeckId, String newTitle)
    {
        Deck deck = get(oldDeckId);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int count = namedParameterJdbcTemplate.update(
                "INSERT INTO decks " +
                        "(title, format, status, colours, description, author_id, " +
                        "chart_curve_all, chart_curve_creatures, chart_curve_spells, " +
                        "feature_card_id, date_added, date_modified) " +
                "VALUES " +
                        "(:title, :format, :status, :colours, :description, :author_id, " +
                        ":chart_curve_all, :chart_curve_creatures, :chart_curve_spells, " +
                        ":feature_card_id, :date_added, :date_modified)",
                new MapSqlParameterSource()
                        .addValue("title", newTitle)
                        .addValue("format", deck.getFormat())
                        .addValue("status", "Hidden")
                        .addValue("colours", deck.getColours())
                        .addValue("description", deck.getDescription())
                        .addValue("chart_curve_all", deck.getChartCurveAll())
                        .addValue("chart_curve_creatures", deck.getChartCurveCreatures())
                        .addValue("chart_curve_spells", deck.getChartCurveSpells())
                        .addValue("feature_card_id", deck.getFeatureCardId())
                        .addValue("author_id", author.getId())
                        .addValue("date_added", new Timestamp(System.currentTimeMillis()))
                        .addValue("date_modified", new Timestamp(System.currentTimeMillis())),
                keyHolder
        );
        if (keyHolder.getKeyList().size() == 0) {
            logger.error("Did not get any id back from the database.");
            return null;
        } else {
            deck.setId(keyHolder.getKey().intValue());
            logger.info("Added deck to the database. " +
                        "[deck: " + deck + "] " +
                        "[count: " + count + "] " +
                        "");
        }
        for (Card card : deck.getCards()) {
            namedParameterJdbcTemplate.update(
                "INSERT INTO deckcards (deck_id, card_id, count) " +
                "VALUES (:deck_id, :card_id, :count)",
                new MapSqlParameterSource()
                    .addValue("deck_id", deck.getId())
                    .addValue("card_id", card.getId())
                    .addValue("count", card.getCount())
            );
        }
        logger.info("Inserted cards into database. " +
                        "[deck.getCards().size(): " + deck.getCards().size() + "] " +
                        "");
        return deck;
    }

    public Deck updateCharts(Deck deck)
    {
        simpleJdbcTemplate.update(
                "UPDATE decks SET " +
                        "chart_curve_all = :chart_curve_all, " +
                        "chart_curve_creatures = :chart_curve_creatures, " +
                        "chart_curve_spells = :chart_curve_spells " +
                        "WHERE id = :id",
                new MapSqlParameterSource()
                        .addValue("id", deck.getId())
                        .addValue("chart_curve_all", deck.getChartCurveAll())
                        .addValue("chart_curve_creatures", deck.getChartCurveCreatures())
                        .addValue("chart_curve_spells", deck.getChartCurveSpells()));
        return deck;
    }

    /**
     * Saves a deck to the database. It follows this seemingly
     * inefficient algorithm:
     *
     * 1) Delete all cards associated with the deck
     * 2) Perform n inserts of cards where n is the number of
     *    cards in the deck
     * 3) Update the deck itself (cards are stored elsewhere)
     *
     * @param deck  deck to save (update) - must include a card list.
     */
    public void update(Deck deck)
    {
        if (deck.getCards() == null) {
            throw new IllegalArgumentException("Deck to be updated must include card list.");
        }
        int deleteCount = namedParameterJdbcTemplate.update(
            "DELETE FROM deckcards WHERE deck_id = :id",
            new MapSqlParameterSource()
                .addValue("id", deck.getId())
        );
        for (Card card : deck.getCards()) {
            namedParameterJdbcTemplate.update(
                "INSERT INTO deckcards (deck_id, card_id, count) " +
                "VALUES (:deck_id, :card_id, :count)",
                new MapSqlParameterSource()
                    .addValue("deck_id", deck.getId())
                    .addValue("card_id", card.getId())
                    .addValue("count", card.getCount())
            );
        }
        int updateCount = namedParameterJdbcTemplate.update(
                    "UPDATE decks SET " +
                    "title = :title, " +
                    "format = :format, " +
                    "status = :status, " +
                    "colours = :colours, " +
                    "description = :description, " +
                    "chart_curve_all = :chart_curve_all, " +
                    "chart_curve_creatures = :chart_curve_creatures, " +
                    "chart_curve_spells = :chart_curve_spells, " +
                    "feature_card_id = :feature_card_id, " +
                    "date_modified = :date_modified " +
                    "WHERE id = :id",
            new MapSqlParameterSource()
                .addValue("id", deck.getId())
                .addValue("title", deck.getTitle())
                .addValue("format", deck.getFormat())
                .addValue("status", deck.getStatus())
                .addValue("colours", deck.getColours())
                .addValue("description", deck.getDescription())
                .addValue("chart_curve_all", deck.getChartCurveAll())
                .addValue("chart_curve_creatures", deck.getChartCurveCreatures())
                .addValue("chart_curve_spells", deck.getChartCurveSpells())
                .addValue("feature_card_id", deck.getFeatureCardId())
                .addValue("date_modified", new Timestamp(System.currentTimeMillis()))
        );
        logger.info("Deck successfully saved. " +
                        "[deck.getId(): " + deck.getId() + "] " +
                        "[deck.getTitle(): " + deck.getTitle() + "] " +
                        "[deck.getCards().size(): " + deck.getCards().size() + "] " +
                        "[deleteCount: " + deleteCount + "] " +
                        "[updateCount: " + updateCount + "] ");
    }

    /**
     * Deletes a deck (completely).
     *
     * @param deckId id of deck to be deleted
     */
    public void delete(Integer deckId)
    {
        int cardsCount = namedParameterJdbcTemplate.update(
            "DELETE FROM deckcards WHERE deck_id = :id",
            new MapSqlParameterSource()
                .addValue("id", deckId)
        );
        int deckCount = namedParameterJdbcTemplate.update(
            "DELETE FROM decks WHERE id = :id",
            new MapSqlParameterSource()
                .addValue("id", deckId)
        );
        logger.info("Deck successfully deleted. " +
                        "[deckId: " + deckId + "] " +
                        "[cardsCount: " + cardsCount + "] " +
                        "[deckCount: " + deckCount + "] ");
    }

    public Page<Deck> getPublicDeckPage(Integer pageNumber)
    {
        String fromWhereClause =
                DeckMapper.FROM +
                "WHERE LOWER(deck.status) = 'public' " +
                DeckMapper.ORDER_BY;
        return new Pagination<Deck>().fetchPage(
                pageNumber, simpleJdbcTemplate, DeckMapper.SELECT, fromWhereClause,
                new MapSqlParameterSource(),
                new DeckMapper()
        );
    }

    public Page<Deck> getHiddenUserDeckPage(Integer pageNumber, Integer userId)
    {
        String fromWhereClause =
                DeckMapper.FROM +
                "WHERE author.id = :user_id " +
                DeckMapper.ORDER_BY;
        return new Pagination<Deck>().fetchPage(
                pageNumber, simpleJdbcTemplate, DeckMapper.SELECT, fromWhereClause,
                new MapSqlParameterSource().addValue("user_id", userId),
                new DeckMapper()
        );
    }

    public Page<Deck> getPublicUserDeckPage(Integer pageNumber, Integer userId)
    {
        String fromWhereClause =
                DeckMapper.FROM +
                "WHERE author.id = :user_id " +
                "AND LOWER(deck.status) = 'public' " +
                DeckMapper.ORDER_BY;
        return new Pagination<Deck>().fetchPage(
                pageNumber, simpleJdbcTemplate, DeckMapper.SELECT, fromWhereClause,
                new MapSqlParameterSource().addValue("user_id", userId),
                new DeckMapper()
        );
    }

    public List<Comment> getComments(int deckId)
    {
        return simpleJdbcTemplate.query(
                        CommentMapper.SELECT_FROM +
                        "WHERE comment.deck_id = :deckId " +
                        CommentMapper.ORDER_BY,
                new CommentMapper(),
                new MapSqlParameterSource()
                        .addValue("deckId", deckId));
    }

    public Comment addComment(Comment comment, int deckId, User author)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int count = namedParameterJdbcTemplate.update(
                "INSERT INTO comments " +
                "(text, deck_id, author_id, date_added) " +
                "VALUES " +
                "(:text, :deck_id, :author_id, :date_added) ",
                new MapSqlParameterSource()
                .addValue("text", comment.getText())
                .addValue("deck_id", deckId)
                .addValue("author_id", author.getId())
                .addValue("date_added", new Timestamp(System.currentTimeMillis())),
                keyHolder
        );
        if (count != 1) {
            logger.error("Could not insert comment into the database.");
            return null;
        }
        comment.setId(keyHolder.getKey().intValue());
        logger.info("Inserted a comment into the database. " +
                    "[comment: " + comment + "]" +
                    "");
        return comment;
    }

    /**
     * Is a deck mapper that maps shallow decks, that is it does
     * not populate the cards member of a deck.
     */
    private static class DeckMapper implements RowMapper<Deck>
    {
        private final Log logger = LogFactory.getLog(getClass());
        public Deck mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            Deck deck = new Deck();

            deck.setId(rs.getInt("deck.id"));
            deck.setTitle(rs.getString("deck.title"));
            deck.setFormat(rs.getString("deck.format"));
            deck.setStatus(rs.getString("deck.status"));
            deck.setColours(rs.getString("deck.colours"));
            deck.setDescription(rs.getString("deck.description"));

            deck.setChartCurveAll(rs.getBytes("deck.chart_curve_all"));
            deck.setChartCurveCreatures(rs.getBytes("deck.chart_curve_creatures"));
            deck.setChartCurveSpells(rs.getBytes("deck.chart_curve_spells"));

            deck.setFeatureCardId(rs.getInt("deck.feature_card_id"));

            User author = new User();
            author.setId(rs.getInt("author.id"));
            author.setEmail(rs.getString("author.email"));
            author.setName(rs.getString("author.name"));

            deck.setAuthor(author);

            return deck;
        }

        public static final String SELECT =
                "SELECT " +
                "deck.id, " +
                "deck.title, " +
                "deck.format, " +
                "deck.status, " +
                "deck.colours, " +
                "deck.description, " +
                "deck.author_id, " +
                "deck.chart_curve_all, " +
                "deck.chart_curve_creatures, " +
                "deck.chart_curve_spells, " +
                "deck.feature_card_id, " +
                "author.id, " +
                "author.email, " +
                "author.name ";

        public static final String FROM =
                "FROM decks deck " +
                "LEFT JOIN users author " +
                "ON deck.author_id = author.id ";

        public static final String SELECT_FROM =
                SELECT + FROM;

        public static final String ORDER_BY =
                "ORDER BY " +
                "deck.date_added, " +
                "deck.date_modified, " +
                "author.id " +
                "DESC ";
    }

    private static class CommentMapper implements RowMapper<Comment>
    {
        private final Log logger = LogFactory.getLog(getClass());
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            Comment comment = new Comment();

            comment.setId(rs.getInt("comment.id"));
            comment.setText(rs.getString("comment.text"));
            comment.setDateAdded(rs.getTimestamp("comment.date_added"));

            User author = new User();
            author.setId(rs.getInt("comment.author_id"));
            author.setEmail(rs.getString("comment.email"));
            author.setName(rs.getString("comment.name"));

            comment.setAuthor(author);

            return comment;
        }

        public static final String SELECT =
                "SELECT " +
                "comment.id, " +
                "comment.deck_id, " +
                "comment.author_id, " +
                "comment.text, " +
                "comment.date_added, " +
                "comment.name, " +
                "comment.email ";

        public static final String FROM =
                "FROM view_comments AS comment ";

        public static final String SELECT_FROM =
                SELECT + FROM;

        public static final String ORDER_BY =
                "ORDER BY comment.date_added ASC ";
    }

    private static class DeckCardMapper implements RowMapper<Card>
    {
        private final Log logger = LogFactory.getLog(getClass());
        public Card mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            Card card = new Card();
            card.setId(rs.getInt("card.id"));
            card.setCount(rs.getInt("card.count"));
            return card;
        }
    }
}
