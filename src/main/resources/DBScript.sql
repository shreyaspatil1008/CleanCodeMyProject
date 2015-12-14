CREATE TABLE `user` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`userEmail` VARCHAR(50) NOT NULL,
	`emailPassword` VARCHAR(50) NOT NULL,
	`userCreatedTime` DATETIME NOT NULL,
	`userLastUpdatedTime` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=4;


CREATE TABLE `note` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`noteTitle` VARCHAR(50) NOT NULL,
	`noteString` VARCHAR(1000) NULL DEFAULT NULL,
	`noteCreatedTime` DATETIME NOT NULL,
	`noteLastUpdatedTime` DATETIME NULL DEFAULT NULL,
	`userId` INT(11) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `FK_USERID_ID` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;

insert into user values
(1,'shreyas.patil@gmail.com','test@1234','2015-11-08 08:40:10',null),
(1,'kavya.keerthi@gmail.com','test@1234','2015-11-08 08:41:10',null),
(1,'shan.patil@gmail.com','test@1234','2015-11-08 08:42:10',null);