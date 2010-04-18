/*
 * `magicengine`.`view_pricequeue`
 */

CREATE VIEW `view_pricequeue` AS
SELECT
cardssellers.seller_id as seller_id,
latestprices.id AS price_id,
cardssellers.id as card_id,
latestprices.date_added,
latestprices.price,
cardssellers.card_name
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
