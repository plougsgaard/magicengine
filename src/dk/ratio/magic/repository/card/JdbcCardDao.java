package dk.ratio.magic.repository.card;

import dk.ratio.magic.domain.db.card.Seller;
import dk.ratio.magic.util.repository.Page;
import dk.ratio.magic.util.repository.Pagination;
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
import dk.ratio.magic.domain.db.card.Image;
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
        String selectClause = "SELECT " +
                              "card.id, " +
                              "card.card_name, " +
                              "card.mana_cost, " +
                              "card.converted_mana_cost, " +
                              "card.types, " +
                              "card.card_text, " +
                              "card.expansion, " +
                              "card.rarity, " +
                              "card.card_number, " +
                              "card.artist, " +
                              "card.price ";
        String fromWhereClause = "FROM cards card " +
                                 "WHERE card.id=:id " +
                                 "LIMIT 1";
        String query = selectClause + fromWhereClause;
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
        String selectClause = "SELECT " +
                              "card.id, " +
                              "card.card_name, " +
                              "card.mana_cost, " +
                              "card.converted_mana_cost, " +
                              "card.types, " +
                              "card.card_text, " +
                              "card.expansion, " +
                              "card.rarity, " +
                              "card.card_number, " +
                              "card.artist, " +
                              "card.price ";
        String fromWhereClause = "FROM cards card";
        String query = selectClause + fromWhereClause;
        return simpleJdbcTemplate.query(
                query,
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
    public Image getCardImage(int cardId)
    {
        String query = "SELECT " +
                       "card.id, " +
                       "card.image " +
                       "FROM cards card " +
                       "WHERE card.id=:id ";
        List<Image> results = simpleJdbcTemplate.query(
                query,
                new ImageMapper(),
                new MapSqlParameterSource()
                    .addValue("id", cardId)
        );
        if (results.size() != 1)
        {
            logger.warn("An image was requested, but was not found. " +
                        "[cardId: " + cardId + "]");
            return null;
        }
        return results.get(0);
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
                "SELECT " +
                "card.id, " +
                "card.card_name, " +
                "card.mana_cost, " +
                "card.converted_mana_cost, " +
                "card.types, " +
                "card.card_text, " +
                "card.expansion, " +
                "card.rarity, " +
                "card.card_number, " +
                "card.artist, " +
                "card.price " +
                "FROM cards card " +
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

    /**
     * Get the number of cards in the database. Useful stuff!
     *
     * @return number of cards in database
     */
    public int getCardCount()
    {
        return simpleJdbcTemplate.queryForInt(
                "SELECT COUNT(*) FROM cards",
                new MapSqlParameterSource()
        );
    }

    public Page<Card> getCardPage(Integer pageNumber)
    {
        String selectClause = "SELECT " +
                              "card.id, " +
                              "card.card_name, " +
                              "card.mana_cost, " +
                              "card.converted_mana_cost, " +
                              "card.types, " +
                              "card.card_text, " +
                              "card.expansion, " +
                              "card.rarity, " +
                              "card.card_number, " +
                              "card.artist, " +
                              "card.price ";
        String fromWhereClause = "FROM cards card " +
                                 "ORDER BY card.card_name ASC";
        return new Pagination<Card>().fetchPage(
                pageNumber, simpleJdbcTemplate, selectClause, fromWhereClause,
                new MapSqlParameterSource(),
                new CardMapper()
        );
    }

    public Card addCard(Card card, Image image)
    {
        // Insert the card (with image) into the `cards` table
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertClause = "INSERT INTO cards " +
                              "(card_name, mana_cost, converted_mana_cost, types, card_text, " +
                              " expansion, rarity, card_number, artist, image) ";
        String valuesClause = "VALUES " +
                              "(:card_name, :mana_cost, :converted_mana_cost, :types, :card_text, " +
                              " :expansion, :rarity, :card_number, :artist, :image)";
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
                .addValue("rarity", card.getRarity())
                .addValue("card_number", card.getCardNumber())
                .addValue("artist", card.getArtist())
                .addValue("image", image.getData()),
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

    /**
     * Add a card to the database.
     *
     * @param card card to add
     * @param image image of card
     * @param prices list of prices (possibly empty)
     * @return the card just added - with the id added to it,
     *         and null if something went terribly wrong
     */
    public Card addCard(Card card, Image image, List<Price> prices)
    {
        // Insert the card (with image) into the `cards` table
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertClause = "INSERT INTO cards " +
                              "(card_name, mana_cost, converted_mana_cost, types, card_text, " +
                              " expansion, rarity, card_number, artist, image) ";
        String valuesClause = "VALUES " +
                              "(:card_name, :mana_cost, :converted_mana_cost, :types, :card_text, " +
                              " :expansion, :rarity, :card_number, :artist, :image)";
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
                .addValue("rarity", card.getRarity())
                .addValue("card_number", card.getCardNumber())
                .addValue("artist", card.getArtist())
                .addValue("image", image.getData()),
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

        return addPrices(card, prices);
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
        logger.info("Adding prices for card. " +
                    "[card.getCardName(): " + card.getCardName() + "] " +
                    "[prices.size(): " + prices.size() + "] " +
                    "");
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

    public Card updatePrice(Card card)
    {
        logger.info("Updating price for card. " +
                    "[card.getCardName(): " + card.getCardName() + "] " +
                    "");

        card = getPrices(card);

        Double current = 0d;
        for (Price price : card.getPrices()) {
            if (current == 0d || price.getPrice() < current) {
                current = price.getPrice();
            }
        }
        card.setPrice(current);

        logger.info("Computed the price to update the card with. " +
                    "[card.getPrice(): " + card.getPrice() + "] " +
                    "");

        simpleJdbcTemplate.update(
                "UPDATE cards SET price = :price WHERE id = :id",
                new MapSqlParameterSource()
                        .addValue("id", card.getId())
                        .addValue("price", card.getPrice()));

        return card;
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
            card.setRarity(rs.getString("card.rarity"));
            card.setCardNumber(rs.getString("card.card_number"));
            card.setArtist(rs.getString("card.artist"));
            card.setPrice(rs.getDouble("card.price"));
            return card;
        }
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

    private static class ImageMapper implements RowMapper<Image>
    {
        private final Log logger = LogFactory.getLog(getClass());
        public Image mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            Image image = new Image();
            image.setData(rs.getBytes("card.image"));
            return image;
        }
    }
}
