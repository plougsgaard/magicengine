DROP TABLE IF EXISTS `magicengine`.`cards`;
CREATE TABLE  `magicengine`.`cards` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `card_name` varchar(255) NOT NULL,
  `mana_cost` varchar(255) NOT NULL,
  `converted_mana_cost` varchar(255) NOT NULL,
  `types` varchar(255) NOT NULL,
  `card_text` mediumtext NOT NULL,
  `expansion` varchar(255) NOT NULL,
  `set_code` varchar(5) DEFAULT NULL,
  `card_number` varchar(255) NOT NULL,
  `rarity` varchar(255) NOT NULL,
  `artist` varchar(255) NOT NULL,
  `image` mediumblob NOT NULL,
  `price` double NOT NULL DEFAULT '0',
  `cutout` mediumblob,
  `thumbnail` mediumblob,
  PRIMARY KEY (`id`),
  UNIQUE KEY `card_name` (`card_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;