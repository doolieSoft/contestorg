<?php

/**
 * @name ErreurException
 * @version 13/05/2013 (dd/mm/yyyy)
 * @author WebProjectHelper (http://www.elfangels.fr/webprojecthelper/)
 */
abstract class ErreurExceptionBase
{
	// Nom de la table
	const TABLENAME = 'contestorg_erreurexception';
	
	// Nom des champs
	const FIELDNAME_IDERREUREXCEPTION = 'iderreurexception';
	const FIELDNAME_ERREUR_IDERREUR = 'fk_iderreur';
	const FIELDNAME_MESSAGE = 'message';
	const FIELDNAME_STACKTRACE = 'stacktrace';
	
	/** @var PDO  */
	protected $pdo;
	
	/** @var array tableau pour le chargement fainéant */
	protected static $lazyload;
	
	/** @var int  */
	protected $idErreurException;
	
	/** @var int id de erreur */
	protected $erreur;
	
	/** @var string message */
	protected $message;
	
	/** @var string stacktrace */
	protected $stacktrace;
	
	/**
	 * Construire un(e) erreurException
	 * @param $pdo PDO 
	 * @param $idErreurException int 
	 * @param $erreur int Id de erreur
	 * @param $message string Message
	 * @param $stacktrace string Stacktrace
	 * @param $lazyload bool Activer le chargement fainéant ?
	 */
	protected function __construct(PDO $pdo,$idErreurException,$erreur,$message,$stacktrace,$lazyload=false)
	{
		// Sauvegarder pdo
		$this->pdo = $pdo;
		
		// Sauvegarder les attributs
		$this->idErreurException = $idErreurException;
		$this->erreur = $erreur;
		$this->message = $message;
		$this->stacktrace = $stacktrace;
		
		// Sauvegarder pour le chargement fainéant
		if ($lazyload) {
			self::$lazyload[$idErreurException] = $this;
		}
	}
	
	/**
	 * Créer un(e) erreurException
	 * @param $pdo PDO 
	 * @param $erreur Erreur Erreur dans l'application
	 * @param $message string Message
	 * @param $stacktrace string Stacktrace
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return ErreurException Exception dans l'application
	 */
	public static function create(PDO $pdo,Erreur $erreur,$message,$stacktrace,$lazyload=true)
	{
		// Ajouter le/la erreurException dans la base de données
		$pdoStatement = $pdo->prepare('INSERT INTO '.ErreurException::TABLENAME.' ('.ErreurException::FIELDNAME_ERREUR_IDERREUR.','.ErreurException::FIELDNAME_MESSAGE.','.ErreurException::FIELDNAME_STACKTRACE.') VALUES (?,?,?)');
		if (!$pdoStatement->execute(array($erreur->getIdErreur(),$message,$stacktrace))) {
			throw new Exception('Erreur durant l\'insertion d\'un(e) erreurException dans la base de données');
		}
		
		// Construire le/la erreurException
		return new ErreurException($pdo,$pdo->lastInsertId(),$erreur->getIdErreur(),$message,$stacktrace,$lazyload);
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
		return $pdo->prepare('SELECT DISTINCT '.ErreurException::TABLENAME.'.'.ErreurException::FIELDNAME_IDERREUREXCEPTION.', '.ErreurException::TABLENAME.'.'.ErreurException::FIELDNAME_ERREUR_IDERREUR.', '.ErreurException::TABLENAME.'.'.ErreurException::FIELDNAME_MESSAGE.', '.ErreurException::TABLENAME.'.'.ErreurException::FIELDNAME_STACKTRACE.' '.
		                     'FROM '.ErreurException::TABLENAME.($from != null ? ', '.(is_array($from) ? implode(', ',$from) : $from) : '').
		                     ($where != null ? ' WHERE '.(is_array($where) ? implode(' AND ',$where) : $where) : '').
		                     ($orderby != null ? ' ORDER BY '.(is_array($orderby) ? implode(', ',$orderby) : $orderby) : '').
		                     ($limit != null ? ' LIMIT '.(is_array($limit) ? implode(', ', $limit) : $limit) : ''));
	}
	
	/**
	 * Charger un(e) erreurException
	 * @param $pdo PDO 
	 * @param $idErreurException int 
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return ErreurException Exception dans l'application
	 */
	public static function load(PDO $pdo,$idErreurException,$lazyload=true)
	{
		// Déjà chargé(e) ?
		if (isset(self::$lazyload[$idErreurException])) {
			return self::$lazyload[$idErreurException];
		}
		
		// Charger le/la erreurException
		$pdoStatement = self::_select($pdo,ErreurException::FIELDNAME_IDERREUREXCEPTION.' = ?');
		if (!$pdoStatement->execute(array($idErreurException))) {
			throw new Exception('Erreur lors du chargement d\'un(e) erreurException depuis la base de données');
		}
		
		// Récupérer le/la erreurException depuis le jeu de résultats
		return self::fetch($pdo,$pdoStatement,$lazyload);
	}
	
	/**
	 * Charger tous/toutes les erreurExceptions
	 * @param $pdo PDO 
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return ErreurException[] Tableau de erreurExceptions
	 */
	public static function loadAll(PDO $pdo,$lazyload=false)
	{
		// Sélectionner tous/toutes les erreurExceptions
		$pdoStatement = self::selectAll($pdo);
		
		// Récupèrer tous/toutes les erreurExceptions
		$erreurExceptions = self::fetchAll($pdo,$pdoStatement,$lazyload);
		
		// Retourner le tableau
		return $erreurExceptions;
	}
	
	/**
	 * Sélectionner tous/toutes les erreurExceptions
	 * @param $pdo PDO 
	 * @return PDOStatement 
	 */
	public static function selectAll(PDO $pdo)
	{
		$pdoStatement = self::_select($pdo);
		if (!$pdoStatement->execute()) {
			throw new Exception('Erreur lors du chargement de tous/toutes les erreurExceptions depuis la base de données');
		}
		return $pdoStatement;
	}
	
	/**
	 * Récupèrer le/la erreurException suivant(e) d'un jeu de résultats
	 * @param $pdo PDO 
	 * @param $pdoStatement PDOStatement 
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return ErreurException Exception dans l'application
	 */
	public static function fetch(PDO $pdo,PDOStatement $pdoStatement,$lazyload=false)
	{
		// Extraire les valeurs
		$values = $pdoStatement->fetch();
		if (!$values) { return null; }
		list($idErreurException,$erreur,$message,$stacktrace) = $values;
		
		// Construire le/la erreurException
		return isset(self::$lazyload[$idErreurException]) ? self::$lazyload[$idErreurException] :
		       new ErreurException($pdo,$idErreurException,$erreur,$message,$stacktrace,$lazyload);
	}
	
	/**
	 * Récupèrer tous/toutes les erreurExceptions d'un jeu de résultats
	 * @param $pdo PDO 
	 * @param $pdoStatement PDOStatement 
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return ErreurException[] Tableau de erreurExceptions
	 */
	public static function fetchAll(PDO $pdo,PDOStatement $pdoStatement,$lazyload=false)
	{
		$erreurExceptions = array();
		while ($erreurException = self::fetch($pdo,$pdoStatement,$lazyload)) {
			$erreurExceptions[] = $erreurException;
		}
		return $erreurExceptions;
	}
	
	/**
	 * Supprimer le/la erreurException
	 * @return bool Opération réussie ?
	 */
	public function delete()
	{
		// Supprimer le/la erreurException
		$pdoStatement = $this->pdo->prepare('DELETE FROM '.ErreurException::TABLENAME.' WHERE '.ErreurException::FIELDNAME_IDERREUREXCEPTION.' = ?');
		if (!$pdoStatement->execute(array($this->getIdErreurException()))) {
			throw new Exception('Erreur lors de la suppression d\'un(e) erreurException dans la base de données');
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
		$pdoStatement = $this->pdo->prepare('UPDATE '.ErreurException::TABLENAME.' SET '.implode(', ', $updates).' WHERE '.ErreurException::FIELDNAME_IDERREUREXCEPTION.' = ?');
		if (!$pdoStatement->execute(array_merge($values,array($this->getIdErreurException())))) {
			throw new Exception('Erreur lors de la mise à jour d\'un champ d\'un(e) erreurException dans la base de données');
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
		return $this->_set(array(ErreurException::FIELDNAME_ERREUR_IDERREUR,ErreurException::FIELDNAME_MESSAGE,ErreurException::FIELDNAME_STACKTRACE),array($this->erreur,$this->message,$this->stacktrace));
	}
	
	/**
	 * Récupérer le/la idErreurException
	 * @return int 
	 */
	public function getIdErreurException()
	{
		return $this->idErreurException;
	}
	
	/**
	 * Récupérer le/la erreur
	 * @return Erreur Erreur dans l'application
	 */
	public function getErreur()
	{
		return Erreur::load($this->pdo,$this->erreur);
	}
	
	/**
	 * Définir le/la erreur
	 * @param $erreur Erreur Erreur dans l'application
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setErreur(Erreur $erreur,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->erreur = $erreur->getIdErreur();
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(ErreurException::FIELDNAME_ERREUR_IDERREUR),array($erreur->getIdErreur())) : true;
	}
	
	/**
	 * Sélectionner les erreurExceptions par erreur
	 * @param $pdo PDO 
	 * @param $erreur Erreur Erreur dans l'application
	 * @return PDOStatement 
	 */
	public static function selectByErreur(PDO $pdo,Erreur $erreur)
	{
		$pdoStatement = self::_select($pdo,ErreurException::FIELDNAME_ERREUR_IDERREUR.' = ?');
		if (!$pdoStatement->execute(array($erreur->getIdErreur()))) {
			throw new Exception('Erreur lors du chargement de tous/toutes les erreurExceptions d\'un(e) erreur depuis la base de données');
		}
		return $pdoStatement;
	}
	
	/**
	 * Récupérer le/la message
	 * @return string Message
	 */
	public function getMessage()
	{
		return $this->message;
	}
	
	/**
	 * Définir le/la message
	 * @param $message string Message
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setMessage($message,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->message = $message;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(ErreurException::FIELDNAME_MESSAGE),array($message)) : true;
	}
	
	/**
	 * Récupérer le/la stacktrace
	 * @return string Stacktrace
	 */
	public function getStacktrace()
	{
		return $this->stacktrace;
	}
	
	/**
	 * Définir le/la stacktrace
	 * @param $stacktrace string Stacktrace
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setStacktrace($stacktrace,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->stacktrace = $stacktrace;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(ErreurException::FIELDNAME_STACKTRACE),array($stacktrace)) : true;
	}
}

