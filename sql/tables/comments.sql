DROP TABLE IF EXISTS `magicengine`.`comments`;
CREATE TABLE  `magicengine`.`comments` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `text` mediumtext,
  `deck_id` int(10) unsigned NOT NULL,
  `author_id` int(10) unsigned NOT NULL,
  `date_added` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
