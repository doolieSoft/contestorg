-- Issue #85 : Améliorer le rapport d'erreur

-- Erreur dans l'application
ALTER TABLE contestorg_erreur
	ADD date              DATETIME NOT NULL,
	ADD clientversion     VARCHAR(10),
	CHANGE log clientlog  TEXT NOT NULL,
	ADD clientxml         TEXT,
	ADD environnementos   VARCHAR(50),
	ADD environnementjava VARCHAR(50);

-- Exception dans l'application
CREATE TABLE contestorg_erreurexception (
	idexception  INT AUTO_INCREMENT NOT NULL,
	fk_iderreur  INT NOT NULL,
	message      VARCHAR(100) NOT NULL,
	stacktrace   TEXT NOT NULL,
	PRIMARY KEY (idexception),
	FOREIGN KEY (fk_iderreur) REFERENCES contestorg_erreur (iderreur))
ENGINE = MYISAM CHARACTER SET UTF8;