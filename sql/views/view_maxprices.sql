/*
 * `magicengine`.`view_maxprices`
 */

CREATE VIEW `magicengine`.`view_maxprices` AS

SELECT card_id, max(date_added) AS most_recent
FROM prices
GROUP BY card_id, seller_id;
