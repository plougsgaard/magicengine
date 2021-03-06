package dk.ratio.magic.repository.card;

import dk.ratio.magic.domain.db.card.QueueItem;
import dk.ratio.magic.domain.db.card.Seller;
import dk.ratio.magic.util.repository.Page;
import dk.ratio.magic.util.repository.Pagination;
import org.apache.commons.dbcp.DelegatingResultSet;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcCardDao implements CardDao
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private SimpleJdbcTemplate simpleJdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Picks out a card with a given name if it exists.
     *
     * First queries for the id of the card, then delegates
     * the rest of the query to the other getCard method.
     *
     * @param cardName card name
     * @return card if it exists in the db, null ow.
     */
    public Card getCard(String cardName)
    {
        try {
            String query = "SELECT " +
                           "card.id " +
                           "FROM cards card " +
                           "WHERE card.card_name = :cardName";
            Integer cardId = simpleJdbcTemplate.queryForInt(
                    query,
                    new MapSqlParameterSource()
                        .addValue("cardName", cardName)
            );
            return getCard(cardId);
        }
        catch (EmptyResultDataAccessException e) {
            logger.warn("Requested card not in database. " +
                        "[cardName: " + cardName + "]");
            return null;
        }
    }

    /**
     * Picks out a card with a given id if it exists.
     *
     * @param cardId card id
     * @return card if it exists in the db, null ow.
     */
    public Card getCard(int cardId)
    {
        String fromWhereClause = CardMapper.FROM +
                                 "WHERE card.id=:id " +
                                 "LIMIT 1";
        String query = CardMapper.SELECT + fromWhereClause;
        List<Card> cards = simpleJdbcTemplate.query(
                query,
                new CardMapper(),
                new MapSqlParameterSource()
                    .addValue("id", cardId)
        );
        if (cards.size() != 1)
        {
            logger.warn("A card was requested, but was not found. " +
                        "[cardId: " + cardId + "]");
            return null;
        }

        return cards.get(0);
    }

    public List<Card> getCards()
    {
        return simpleJdbcTemplate.query(
                CardMapper.SELECT + CardMapper.FROM,
                new CardMapper(),
                new MapSqlParameterSource()
        );
    }

    public List<Card> getCards(List<Card> cards)
    {
        List<Card> result = new ArrayList<Card>(cards.size());
        for (Card shallowCard : cards) {
            Card card = getCard(shallowCard.getId());
            card.setCount(shallowCard.getCount());
            result.add(card);
        }
        return result;
    }

    public Page<QueueItem> getQueuePage(Integer pageNumber)
    {
        return new Pagination<QueueItem>().fetchPage(
                pageNumber, simpleJdbcTemplate, QueueItemMapper.SELECT, QueueItemMapper.FROM,
                new MapSqlParameterSource(),
                new QueueItemMapper(),
                QueueItemMapper.PAGE_SIZE
        );
    }

    public List<Price> getPrices()
    {
        String query = "SELECT " +
                       "price.id, " +
                       "price.price, " +
                       "price.seller_id, " +
                       "price.card_id," +
                       "price.date_added " +
                       "FROM prices price ";
        return simpleJdbcTemplate.query(query, new PriceMapper());
    }

    public Card getPrices(Card card)
    {
        String query = "SELECT " +
                       "price.id, " +
                       "price.price, " +
                       "price.seller_id, " +
                       "price.card_id," +
                       "price.date_added " +
                       "FROM view_latestprices price " +
                       "WHERE price.card_id = :cardId";
        card.setPrices(simpleJdbcTemplate.query(
                query,
                new PriceMapper(),
                new MapSqlParameterSource()
                    .addValue("cardId", card.getId())
        ));
        return card;
    }

    /**
     * Retrieves the image of a card.
     *
     * Mainly because it weighs heavily this isn't included when a
     * card is requested.
     *
     * @param cardId id of card
     * @return image of card if it exists, null ow.
     */
    public byte[] getImage(int cardId)
    {
        return simpleJdbcTemplate.queryForObject(
                "SELECT image FROM cards WHERE id = :cardId", byte[].class,
                new MapSqlParameterSource()
                .addValue("cardId", cardId)
        );
    }

    public byte[] getCutout(int cardId)
    {
        return simpleJdbcTemplate.queryForObject(
                "SELECT cutout FROM cards WHERE id = :cardId", byte[].class,
                new MapSqlParameterSource()
                .addValue("cardId", cardId)
        );
    }

    public void setCutout(int cardId, byte[] data)
    {
        simpleJdbcTemplate.update(
                "UPDATE cards SET cutout = :cutout WHERE id = :cardId",
                new MapSqlParameterSource()
                .addValue("cardId", cardId)
                .addValue("cutout", data)
        );
    }

    public byte[] getThumbnail(int cardId)
    {
        return simpleJdbcTemplate.queryForObject(
                "SELECT thumbnail FROM cards WHERE id = :cardId", byte[].class,
                new MapSqlParameterSource()
                .addValue("cardId", cardId)
        );
    }

    public void setThumbnail(int cardId, byte[] data)
    {
        simpleJdbcTemplate.update(
                "UPDATE cards SET thumbnail = :thumbnail WHERE id = :cardId",
                new MapSqlParameterSource()
                .addValue("cardId", cardId)
                .addValue("thumbnail", data)
        );
    }

    /**
     * Finds suggestions to which cards a text input might refer to.
     *
     * This particular implementation sets the limit to 8.
     *
     * @param inputValue the input to match when finding suggestions
     * @return a suggestion list of card names
     */
    public List<Card> getSuggestions(String inputValue)
    {
        return getSuggestions(inputValue, 8);
    }

    /**
     * Finds suggestions to which cards a text input might refer to.
     *
     * @param fragment the input to match when finding suggestions
     * @param limit the maximum number of card names to return
     * @return a suggestion list of card names
     */
    private List<Card> getSuggestions(String fragment, int limit)
    {
        String query =
                CardMapper.SELECT +
                CardMapper.FROM +
                "WHERE card.card_name LIKE :fragment " +
                "LIMIT :limit";
        return simpleJdbcTemplate.query(
                query,
                new CardMapper(),
                new MapSqlParameterSource()
                    .addValue("fragment", "%" + fragment + "%")
                    .addValue("limit", limit)
        );
    }

    public Page<Card> getCardPage(Integer pageNumber)
    {
        String fromWhereClause = CardMapper.FROM +
                                 "ORDER BY card.card_name ASC";
        return new Pagination<Card>().fetchPage(
                pageNumber, simpleJdbcTemplate, CardMapper.SELECT, fromWhereClause,
                new MapSqlParameterSource(),
                new CardMapper()
        );
    }

    public Card addCard(Card card, byte[] image)
    {
        // Insert the card (with image) into the `cards` table
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertClause = "INSERT INTO cards " +
                              "(card_name, mana_cost, converted_mana_cost, types, card_text, " +
                              " expansion, set_code, rarity, card_number, artist, image) ";
        String valuesClause = "VALUES " +
                              "(:card_name, :mana_cost, :converted_mana_cost, :types, :card_text, " +
                              " :expansion, :set_code, :rarity, :card_number, :artist, :image)";
        String query = insertClause + valuesClause;
        int cardInsertCount = namedParameterJdbcTemplate.update(
                query,
                new MapSqlParameterSource()
                .addValue("card_name", card.getCardName())
                .addValue("mana_cost", card.getManaCost())
                .addValue("converted_mana_cost", card.getConvertedManaCost())
                .addValue("types", card.getTypes())
                .addValue("card_text", card.getCardText())
                .addValue("expansion", card.getExpansion())
                .addValue("set_code", card.getSetCode())
                .addValue("rarity", card.getRarity())
                .addValue("card_number", card.getCardNumber())
                .addValue("artist", card.getArtist())
                .addValue("image", image),
                keyHolder
        );
        if (cardInsertCount != 1 || keyHolder.getKeyList().size() == 0) {
            logger.error("Error inserting card into database. " +
                        "[card.getCardName(): " + card.getCardName() + "]");
            return null;
        } else {
            logger.info("Card successfully inserted. " +
                        "[card.getCardName(): " + card.getCardName() + "] " +
                        "[cardInsertCount: " + cardInsertCount + "] ");
        }

        card.setId(keyHolder.getKey().intValue());

        return card;
    }

    public Price addPrice(Card card, Price price)
    {
        ArrayList<Price> prices = new ArrayList<Price>(1);
        prices.add(price);
        addPrices(card, prices);
        return price;
    }

    /**
     * Adds the new prices to the database.
     *
     * @param card card to add new prices for
     * @param prices the prices to be added
     * @return the card with updated prices
     */
    public Card addPrices(Card card, List<Price> prices)
    {
        for (Price price : prices) {
            String insertClause = "INSERT INTO prices " +
                                  "(card_id, price, seller_id, date_added) ";
            String valuesClause = "VALUES " +
                                  "(:card_id, :price, :seller_id, :date_added) ";
            String query = insertClause + valuesClause;
            int priceInsertCount = namedParameterJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                    .addValue("card_id", card.getId())
                    .addValue("price", price.getPrice())
                    .addValue("seller_id", price.getSeller().getId())
                    .addValue("date_added", new Timestamp(System.currentTimeMillis()))
            );
            if (priceInsertCount != 1) {
                logger.error("Error inserting price into database. " +
                        "[card.getCardName(): " + card.getCardName() + "] " +
                        "[price.getPrice(): " + price.getPrice() + "] ");
            } else {
                logger.info("Price successfully inserted. " +
                        "[card.getCardName(): " + card.getCardName() + "] " +
                        "[price.getPrice(): " + price.getPrice() + "] " +
                        "[priceInsertCount: " + priceInsertCount + "] ");
            }
        }

        return updatePrice(getPrices(card));
    }

    public Card updateCard(Card card, byte[] image)
    {
        String query =
                "UPDATE cards SET " +
                "card_name = :card_name, mana_cost = :mana_cost, converted_mana_cost = :converted_mana_cost, " +
                "types = :types, card_text = :card_text, expansion = :expansion, set_code = :set_code, " +
                "rarity = :rarity, card_number = :card_number, artist = :artist, price = :price, " +
                "image = :image, thumbnail = :thumbnail, cutout = :cutout " +
                "WHERE id = :id";

        int count = simpleJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("card_name", card.getCardName())
                            .addValue("mana_cost", card.getManaCost())
                            .addValue("converted_mana_cost", card.getConvertedManaCost())
                            .addValue("types", card.getTypes())
                            .addValue("card_text", card.getCardText())
                            .addValue("expansion", card.getExpansion())
                            .addValue("set_code", card.getSetCode())
                            .addValue("rarity", card.getRarity())
                            .addValue("card_number", card.getCardNumber())
                            .addValue("artist", card.getArtist())
                            .addValue("price", card.getPrice())
                            .addValue("image", image)
                            .addValue("thumbnail", null)
                            .addValue("cutout", null)
                            .addValue("id", card.getId())
        );

        logger.info("Card successfully updated. " +
                    "[card: " + card + "] " +
                    "[count: " + count + "] ");

        return card;
    }

    public Card updatePrice(Card card)
    {
        card = getPrices(card);

        // find the lowest price (not 0d) and set it
        Double current = 0d;
        for (Price price : card.getPrices()) {
            if (current == 0d || (price.getPrice() != 0d && price.getPrice() < current)) {
                current = price.getPrice();
            }
        }
        card.setPrice(current);

        simpleJdbcTemplate.update(
                "UPDATE cards SET price = :price WHERE id = :id",
                new MapSqlParameterSource()
                        .addValue("id", card.getId())
                        .addValue("price", card.getPrice()));

        return card;
    }

    public void updatePrices(List<Price> prices)
    {
        for (Price price : prices) {
            simpleJdbcTemplate.update("UPDATE prices SET price = :price WHERE id = :id",
                    new MapSqlParameterSource()
            .addValue("id", price.getId())
            .addValue("price", price.getPrice()));
        }
    }

    public List<QueueItem> getFirstInQueue()
    {
        List<QueueItem> result = new ArrayList<QueueItem>(3);

        for (int i = 1; i <= 3; ++i) {
            String query = QueueItemMapper.SELECT + QueueItemMapper.FROM
                    + " WHERE seller_id = :sellerId LIMIT 1";
            List<QueueItem> items = simpleJdbcTemplate.query(
                    query,
                    new QueueItemMapper(),
                    new MapSqlParameterSource().addValue("sellerId", i));
            result.add(items.get(0));
        }
        return result;
    }

    private static class CardMapper implements RowMapper<Card>
    {
        private final Log logger = LogFactory.getLog(getClass());
        public Card mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            Card card = new Card();
            card.setId(rs.getInt("card.id"));
            card.setCardName(rs.getString("card.card_name"));
            card.setManaCost(rs.getString("card.mana_cost"));
            card.setConvertedManaCost(rs.getString("card.converted_mana_cost"));
            card.setTypes(rs.getString("card.types"));
            card.setCardText(rs.getString("card.card_text"));
            card.setExpansion(rs.getString("card.expansion"));
            card.setSetCode(rs.getString("card.set_code"));
            card.setRarity(rs.getString("card.rarity"));
            card.setCardNumber(rs.getString("card.card_number"));
            card.setArtist(rs.getString("card.artist"));
            card.setPrice(rs.getDouble("card.price"));
            return card;
        }

        public static final String SELECT = 
                "SELECT " +
                "card.id, " +
                "card.card_name, " +
                "card.mana_cost, " +
                "card.converted_mana_cost, " +
                "card.types, " +
                "card.card_text, " +
                "card.expansion, " +
                "card.set_code, " +
                "card.rarity, " +
                "card.card_number, " +
                "card.artist, " +
                "card.price ";
        public static final String FROM =
                "FROM cards card ";
        public static final int PAGE_SIZE = 10;
    }

    private static class PriceMapper implements RowMapper<Price>
    {
        public Price mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            Price price = new Price();
            Card card = new Card();
            Seller seller = new Seller();

            price.setId(rs.getInt("price.id"));
            price.setPrice(rs.getDouble("price.price"));
            price.setDateAdded(rs.getTimestamp("price.date_added"));

            seller.setId(rs.getInt("price.seller_id"));
            card.setId(rs.getInt("price.card_id"));

            price.setCard(card);
            price.setSeller(seller);
            return price;
        }
    }

    private static class QueueItemMapper implements RowMapper<QueueItem>
    {
        private final Log logger = LogFactory.getLog(getClass());
        public QueueItem mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            QueueItem queueItem = new QueueItem();

            queueItem.setSellerId(rs.getInt("seller_id"));
            queueItem.setCardId(rs.getInt("card_id"));
            queueItem.setDateAdded(rs.getTimestamp("date_added"));
            queueItem.setPrice(rs.getDouble("price"));
            queueItem.setCardName(rs.getString("card_name"));

            return queueItem;
        }

        public static final String SELECT =
                "SELECT " +
                "seller_id, " +
                "card_id, " +
                "date_added, " +
                "price, " +
                "card_name ";
        public static final String FROM =
                "FROM view_pricequeue ";
        public static final int PAGE_SIZE = 20;
    }
}
