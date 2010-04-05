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
