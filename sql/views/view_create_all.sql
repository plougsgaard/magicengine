/*
 * `magicengine`.`view_prices`
 */

CREATE VIEW `magicengine`.`view_prices` AS

SELECT id, card_id, seller_id, date_added, price
FROM prices;

/*
 * `magicengine`.`view_maxprices`
 */

CREATE VIEW `magicengine`.`view_maxprices` AS

SELECT card_id, max(date_added) AS most_recent
FROM prices
GROUP BY card_id, seller_id;

/*
 * `magicengine`.`view_latestprices`
 */

CREATE VIEW `magicengine`.`view_latestprices` AS

SELECT
price.id, price.card_id, price.seller_id, price.date_added, price.price
FROM view_prices price
JOIN view_maxprices AS most_recent_price
ON
price.date_added = most_recent_price.most_recent
AND
price.card_id = most_recent_price.card_id
GROUP BY price.id;

/*
 * `magicengine`.`view_cardscount`
 */

CREATE VIEW `magicengine`.`view_cardscount` AS

SELECT

card.id,
deckcard.deck_id,
deckcard.count

FROM cards card
LEFT JOIN deckcards deckcard
ON card.id = deckcard.card_id;


/*
 * `magicengine`.`view_comments`
 */

CREATE VIEW `magicengine`.`view_comments` AS

SELECT
comment.id,
comment.author_id,
comment.deck_id,
comment.text,
comment.date_added,
author.name,
author.email

FROM comments comment
LEFT JOIN
users author
ON author.id = comment.author_id;
