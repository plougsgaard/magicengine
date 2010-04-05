DROP TABLE IF EXISTS `magicengine`.`deckcards`;
CREATE TABLE  `magicengine`.`deckcards` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `deck_id` int(10) unsigned NOT NULL,
  `card_id` int(10) unsigned NOT NULL,
  `count` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


