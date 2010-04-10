DROP TABLE IF EXISTS `magicengine`.`decks`;
CREATE TABLE  `magicengine`.`decks` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(145) NOT NULL DEFAULT 'Untitled',
  `format` varchar(45) NOT NULL DEFAULT 'Extended',
  `status` varchar(45) NOT NULL DEFAULT 'Hidden',
  `colours` varchar(45) NOT NULL DEFAULT '',
  `description` mediumtext,
  `author_id` int(10) unsigned NOT NULL DEFAULT '1',
  `date_added` datetime NOT NULL,
  `date_modified` datetime NOT NULL,
  `feature_card_id` int(10) unsigned NOT NULL DEFAULT '1',
  `chart_curve_all` mediumblob,
  `chart_curve_creatures` mediumblob,
  `chart_curve_spells` mediumblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

