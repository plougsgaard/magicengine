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
