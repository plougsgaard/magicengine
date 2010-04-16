/*
 * `magicengine`.`view_cardssellers`
 */

CREATE VIEW `view_cardssellers` AS
SELECT s.id AS seller_id, c.id, c.card_name FROM sellers s, cards c;
