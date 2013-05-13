<?php

/**
 * @name Erreur
 * @version 13/05/2013 (dd/mm/yyyy)
 * @author WebProjectHelper (http://www.elfangels.fr/webprojecthelper/)
 */
abstract class ErreurBase
{
	// Nom de la table
	const TABLENAME = 'contestorg_erreur';
	
	// Nom des champs
	const FIELDNAME_IDERREUR = 'iderreur';
	const FIELDNAME_DATE = 'date';
	const FIELDNAME_DESCRIPTION = 'description';
	const FIELDNAME_CLIENTVERSION = 'clientversion';
	const FIELDNAME_CLIENTLOG = 'clientlog';
	const FIELDNAME_CLIENTXML = 'clientxml';
	const FIELDNAME_ENVIRONNEMENTOS = 'environnementos';
	const FIELDNAME_ENVIRONNEMENTJAVA = 'environnementjava';
	
	/** @var PDO  */
	protected $pdo;
	
	/** @var array tableau pour le chargement fainéant */
	protected static $lazyload;
	
	/** @var int  */
	protected $idErreur;
	
	/** @var int date */
	protected $date;
	
	/** @var string description de l'erreur */
	protected $description;
	
	/** @var string version de l'application */
	protected $clientVersion;
	
	/** @var string fichier de log */
	protected $clientLOG;
	
	/** @var string fichier de tournoi */
	protected $clientXML;
	
	/** @var string nom et version de l'os */
	protected $environnementOS;
	
	/** @var string version de java */
	protected $environnementJAVA;
	
	/**
	 * Construire un(e) erreur
	 * @param $pdo PDO 
	 * @param $idErreur int 
	 * @param $date int Date
	 * @param $description string Description de l'erreur
	 * @param $clientLOG string Fichier de LOG
	 * @param $clientVersion string Version de l'application
	 * @param $clientXML string Fichier de tournoi
	 * @param $environnementOS string Nom et version de l'OS
	 * @param $environnementJAVA string Version de JAVA
	 * @param $lazyload bool Activer le chargement fainéant ?
	 */
	protected function __construct(PDO $pdo,$idErreur,$date,$description,$clientLOG,$clientVersion=null,$clientXML=null,$environnementOS=null,$environnementJAVA=null,$lazyload=false)
	{
		// Sauvegarder pdo
		$this->pdo = $pdo;
		
		// Sauvegarder les attributs
		$this->idErreur = $idErreur;
		$this->date = $date;
		$this->description = $description;
		$this->clientLOG = $clientLOG;
		$this->clientVersion = $clientVersion;
		$this->clientXML = $clientXML;
		$this->environnementOS = $environnementOS;
		$this->environnementJAVA = $environnementJAVA;
		
		// Sauvegarder pour le chargement fainéant
		if ($lazyload) {
			self::$lazyload[$idErreur] = $this;
		}
	}
	
	/**
	 * Créer un(e) erreur
	 * @param $pdo PDO 
	 * @param $date int Date
	 * @param $description string Description de l'erreur
	 * @param $clientLOG string Fichier de LOG
	 * @param $clientVersion string Version de l'application
	 * @param $clientXML string Fichier de tournoi
	 * @param $environnementOS string Nom et version de l'OS
	 * @param $environnementJAVA string Version de JAVA
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return Erreur Erreur dans l'application
	 */
	public static function create(PDO $pdo,$date,$description,$clientLOG,$clientVersion=null,$clientXML=null,$environnementOS=null,$environnementJAVA=null,$lazyload=true)
	{
		// Ajouter le/la erreur dans la base de données
		$pdoStatement = $pdo->prepare('INSERT INTO '.Erreur::TABLENAME.' ('.Erreur::FIELDNAME_DATE.','.Erreur::FIELDNAME_DESCRIPTION.','.Erreur::FIELDNAME_CLIENTLOG.','.Erreur::FIELDNAME_CLIENTVERSION.','.Erreur::FIELDNAME_CLIENTXML.','.Erreur::FIELDNAME_ENVIRONNEMENTOS.','.Erreur::FIELDNAME_ENVIRONNEMENTJAVA.') VALUES (?,?,?,?,?,?,?)');
		if (!$pdoStatement->execute(array(date('Y-m-d H:i:s',$date),$description,$clientLOG,$clientVersion,$clientXML,$environnementOS,$environnementJAVA))) {
			throw new Exception('Erreur durant l\'insertion d\'un(e) erreur dans la base de données');
		}
		
		// Construire le/la erreur
		return new Erreur($pdo,$pdo->lastInsertId(),$date,$description,$clientLOG,$clientVersion,$clientXML,$environnementOS,$environnementJAVA,$lazyload);
	}
	
	/**
	 * Requête de sélection
	 * @param $pdo PDO 
	 * @param $where string|array 
	 * @param $orderby string|array 
	 * @param $limit string|array 
	 * @param $from string|array 
	 * @return PDOStatement 
	 */
	protected static function _select(PDO $pdo,$where=null,$orderby=null,$limit=null,$from=null)
	{
		return $pdo->prepare('SELECT DISTINCT '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_IDERREUR.', '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_DATE.', '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_DESCRIPTION.', '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_CLIENTLOG.', '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_CLIENTVERSION.', '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_CLIENTXML.', '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_ENVIRONNEMENTOS.', '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_ENVIRONNEMENTJAVA.' '.
		                     'FROM '.Erreur::TABLENAME.($from != null ? ', '.(is_array($from) ? implode(', ',$from) : $from) : '').
		                     ($where != null ? ' WHERE '.(is_array($where) ? implode(' AND ',$where) : $where) : '').
		                     ($orderby != null ? ' ORDER BY '.(is_array($orderby) ? implode(', ',$orderby) : $orderby) : '').
		                     ($limit != null ? ' LIMIT '.(is_array($limit) ? implode(', ', $limit) : $limit) : ''));
	}
	
	/**
	 * Charger un(e) erreur
	 * @param $pdo PDO 
	 * @param $idErreur int 
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return Erreur Erreur dans l'application
	 */
	public static function load(PDO $pdo,$idErreur,$lazyload=true)
	{
		// Déjà chargé(e) ?
		if (isset(self::$lazyload[$idErreur])) {
			return self::$lazyload[$idErreur];
		}
		
		// Charger le/la erreur
		$pdoStatement = self::_select($pdo,Erreur::FIELDNAME_IDERREUR.' = ?');
		if (!$pdoStatement->execute(array($idErreur))) {
			throw new Exception('Erreur lors du chargement d\'un(e) erreur depuis la base de données');
		}
		
		// Récupérer le/la erreur depuis le jeu de résultats
		return self::fetch($pdo,$pdoStatement,$lazyload);
	}
	
	/**
	 * Charger tous/toutes les erreurs
	 * @param $pdo PDO 
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return Erreur[] Tableau de erreurs
	 */
	public static function loadAll(PDO $pdo,$lazyload=false)
	{
		// Sélectionner tous/toutes les erreurs
		$pdoStatement = self::selectAll($pdo);
		
		// Récupèrer tous/toutes les erreurs
		$erreurs = self::fetchAll($pdo,$pdoStatement,$lazyload);
		
		// Retourner le tableau
		return $erreurs;
	}
	
	/**
	 * Sélectionner tous/toutes les erreurs
	 * @param $pdo PDO 
	 * @return PDOStatement 
	 */
	public static function selectAll(PDO $pdo)
	{
		$pdoStatement = self::_select($pdo);
		if (!$pdoStatement->execute()) {
			throw new Exception('Erreur lors du chargement de tous/toutes les erreurs depuis la base de données');
		}
		return $pdoStatement;
	}
	
	/**
	 * Récupèrer le/la erreur suivant(e) d'un jeu de résultats
	 * @param $pdo PDO 
	 * @param $pdoStatement PDOStatement 
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return Erreur Erreur dans l'application
	 */
	public static function fetch(PDO $pdo,PDOStatement $pdoStatement,$lazyload=false)
	{
		// Extraire les valeurs
		$values = $pdoStatement->fetch();
		if (!$values) { return null; }
		list($idErreur,$date,$description,$clientLOG,$clientVersion,$clientXML,$environnementOS,$environnementJAVA) = $values;
		
		// Construire le/la erreur
		return isset(self::$lazyload[$idErreur]) ? self::$lazyload[$idErreur] :
		       new Erreur($pdo,$idErreur,strtotime($date),$description,$clientLOG,$clientVersion,$clientXML,$environnementOS,$environnementJAVA,$lazyload);
	}
	
	/**
	 * Récupèrer tous/toutes les erreurs d'un jeu de résultats
	 * @param $pdo PDO 
	 * @param $pdoStatement PDOStatement 
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return Erreur[] Tableau de erreurs
	 */
	public static function fetchAll(PDO $pdo,PDOStatement $pdoStatement,$lazyload=false)
	{
		$erreurs = array();
		while ($erreur = self::fetch($pdo,$pdoStatement,$lazyload)) {
			$erreurs[] = $erreur;
		}
		return $erreurs;
	}
	
	/**
	 * Supprimer le/la erreur
	 * @return bool Opération réussie ?
	 */
	public function delete()
	{
		// Supprimer les erreurExceptions associé(e)s
		$select = $this->selectErreurExceptions();
		while ($erreurException = ErreurException::fetch($this->pdo,$select)) {
			if (!$erreurException->delete()) { return false; }
		}
		
		// Supprimer le/la erreur
		$pdoStatement = $this->pdo->prepare('DELETE FROM '.Erreur::TABLENAME.' WHERE '.Erreur::FIELDNAME_IDERREUR.' = ?');
		if (!$pdoStatement->execute(array($this->getIdErreur()))) {
			throw new Exception('Erreur lors de la suppression d\'un(e) erreur dans la base de données');
		}
		
		// Opération réussie ?
		return $pdoStatement->rowCount() == 1;
	}
	
	/**
	 * Mettre à jour un champ dans la base de données
	 * @param $fields array 
	 * @param $values array 
	 * @return bool Opération réussie ?
	 */
	protected function _set($fields,$values)
	{
		// Préparer la mise à jour
		$updates = array();
		foreach ($fields as $field) {
			$updates[] = $field.' = ?';
		}
		
		// Mettre à jour le champ
		$pdoStatement = $this->pdo->prepare('UPDATE '.Erreur::TABLENAME.' SET '.implode(', ', $updates).' WHERE '.Erreur::FIELDNAME_IDERREUR.' = ?');
		if (!$pdoStatement->execute(array_merge($values,array($this->getIdErreur())))) {
			throw new Exception('Erreur lors de la mise à jour d\'un champ d\'un(e) erreur dans la base de données');
		}
		
		// Opération réussie ?
		return $pdoStatement->rowCount() == 1;
	}
	
	/**
	 * Mettre à jour tous les champs dans la base de données
	 * @return bool Opération réussie ?
	 */
	public function update()
	{
		return $this->_set(array(Erreur::FIELDNAME_DATE,Erreur::FIELDNAME_DESCRIPTION,Erreur::FIELDNAME_CLIENTVERSION,Erreur::FIELDNAME_CLIENTLOG,Erreur::FIELDNAME_CLIENTXML,Erreur::FIELDNAME_ENVIRONNEMENTOS,Erreur::FIELDNAME_ENVIRONNEMENTJAVA),array(date('Y-m-d H:i:s',$this->date),$this->description,$this->clientVersion,$this->clientLOG,$this->clientXML,$this->environnementOS,$this->environnementJAVA));
	}
	
	/**
	 * Récupérer le/la idErreur
	 * @return int 
	 */
	public function getIdErreur()
	{
		return $this->idErreur;
	}
	
	/**
	 * Récupérer le/la date
	 * @return int Date
	 */
	public function getDate()
	{
		return $this->date;
	}
	
	/**
	 * Définir le/la date
	 * @param $date int Date
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setDate($date,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->date = $date;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Erreur::FIELDNAME_DATE),array(date('Y-m-d H:i:s',$date))) : true;
	}
	
	/**
	 * Récupérer le/la description
	 * @return string Description de l'erreur
	 */
	public function getDescription()
	{
		return $this->description;
	}
	
	/**
	 * Définir le/la description
	 * @param $description string Description de l'erreur
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setDescription($description,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->description = $description;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Erreur::FIELDNAME_DESCRIPTION),array($description)) : true;
	}
	
	/**
	 * Récupérer le/la clientVersion
	 * @return string Version de l'application
	 */
	public function getClientVersion()
	{
		return $this->clientVersion;
	}
	
	/**
	 * Définir le/la clientVersion
	 * @param $clientVersion string Version de l'application
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setClientVersion($clientVersion=null,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->clientVersion = $clientVersion;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Erreur::FIELDNAME_CLIENTVERSION),array($clientVersion)) : true;
	}
	
	/**
	 * Récupérer le/la clientLOG
	 * @return string Fichier de LOG
	 */
	public function getClientLOG()
	{
		return $this->clientLOG;
	}
	
	/**
	 * Définir le/la clientLOG
	 * @param $clientLOG string Fichier de LOG
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setClientLOG($clientLOG,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->clientLOG = $clientLOG;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Erreur::FIELDNAME_CLIENTLOG),array($clientLOG)) : true;
	}
	
	/**
	 * Récupérer le/la clientXML
	 * @return string Fichier de tournoi
	 */
	public function getClientXML()
	{
		return $this->clientXML;
	}
	
	/**
	 * Définir le/la clientXML
	 * @param $clientXML string Fichier de tournoi
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setClientXML($clientXML=null,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->clientXML = $clientXML;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Erreur::FIELDNAME_CLIENTXML),array($clientXML)) : true;
	}
	
	/**
	 * Récupérer le/la environnementOS
	 * @return string Nom et version de l'OS
	 */
	public function getEnvironnementOS()
	{
		return $this->environnementOS;
	}
	
	/**
	 * Définir le/la environnementOS
	 * @param $environnementOS string Nom et version de l'OS
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setEnvironnementOS($environnementOS=null,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->environnementOS = $environnementOS;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Erreur::FIELDNAME_ENVIRONNEMENTOS),array($environnementOS)) : true;
	}
	
	/**
	 * Récupérer le/la environnementJAVA
	 * @return string Version de JAVA
	 */
	public function getEnvironnementJAVA()
	{
		return $this->environnementJAVA;
	}
	
	/**
	 * Définir le/la environnementJAVA
	 * @param $environnementJAVA string Version de JAVA
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setEnvironnementJAVA($environnementJAVA=null,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->environnementJAVA = $environnementJAVA;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Erreur::FIELDNAME_ENVIRONNEMENTJAVA),array($environnementJAVA)) : true;
	}
	
	/**
	 * Sélectionner les erreurExceptions
	 * @return PDOStatement 
	 */
	public function selectErreurExceptions()
	{
		return ErreurException::selectByErreur($this->pdo,$this);
	}
}

