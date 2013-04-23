-- Signature sur le livre d'or
CREATE TABLE contestorg_signature (
	idsignature  INT AUTO_INCREMENT NOT NULL,
	prenom       VARCHAR(20) NOT NULL,
	date         DATETIME NOT NULL,
	email        VARCHAR(130) NOT NULL,
	message      TEXT NOT NULL,
	PRIMARY KEY (idsignature))
ENGINE = MYISAM CHARACTER SET UTF8;

-- Erreur dans l'application
CREATE TABLE contestorg_erreur (
	iderreur     INT AUTO_INCREMENT NOT NULL,
	description  VARCHAR(250) NOT NULL,
	log          TEXT NOT NULL,
	PRIMARY KEY (iderreur))
ENGINE = MYISAM CHARACTER SET UTF8;