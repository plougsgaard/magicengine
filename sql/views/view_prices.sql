/*
 * `magicengine`.`view_prices`
 */

CREATE VIEW `magicengine`.`view_prices` AS

SELECT id, card_id, seller_id, date_added, price
FROM prices;
