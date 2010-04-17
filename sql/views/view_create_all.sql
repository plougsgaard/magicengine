

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
FROM prices price
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

/*
 * `magicengine`.`view_cardssellers`
 */

CREATE VIEW `view_cardssellers` AS
SELECT s.id AS seller_id, c.id, c.card_name FROM sellers s, cards c;

/*
 * `magicengine`.`view_pricequeue`
 */

CREATE VIEW `view_pricequeue` AS
SELECT latestprices.id AS price_id, cardssellers.id as card_id,
cardssellers.seller_id as seller_id,
latestprices.date_added, latestprices.price, cardssellers.card_name
FROM
view_cardssellers AS cardssellers
left join `view_latestprices` latestprices
ON cardssellers.seller_id = latestprices.seller_id AND cardssellers.id = latestprices.card_id
ORDER BY
price IS NOT NULL ASC,
date_added ASC,
price DESC,
card_id DESC,
latestprices.seller_id DESC;
