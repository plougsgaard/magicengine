DROP TABLE IF EXISTS `magicengine`.`prices`;
CREATE TABLE  `magicengine`.`prices` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `card_id` int(10) unsigned NOT NULL,
  `price` double NOT NULL,
  `seller_id` int(10) unsigned NOT NULL,
  `date_added` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

