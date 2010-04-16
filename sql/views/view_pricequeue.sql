/*
 * `magicengine`.`view_pricequeue`
 */

CREATE VIEW `view_pricequeue` AS
SELECT v.id AS price_id, c.id as card_id, c.seller_id, v.date_added, v.price, c.card_name
FROM
view_cardssellers AS c
left join `view_latestprices` v
ON c.seller_id = v.seller_id AND c.id = v.card_id
ORDER BY
price IS NOT NULL ASC,
date_added ASC,
price DESC,
card_id DESC,
v.seller_id DESC;
