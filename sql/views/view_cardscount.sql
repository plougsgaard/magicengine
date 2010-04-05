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
