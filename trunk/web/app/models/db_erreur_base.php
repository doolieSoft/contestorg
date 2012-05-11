<?php

/**
 * @name Erreur
 * @version 10/05/2012 (dd/mm/yyyy)
 * @author WebProjectHelper (http://www.elfangels.fr/webprojecthelper/)
 */
abstract class ErreurBase
{
	/** @var PDO  */
	protected $pdo;
	
	/** @var array tableau pour le chargement fainéant */
	protected static $lazyload;
	
	/** @var int  */
	protected $idErreur;
	
	/** @var string description de l'erreur */
	protected $description;
	
	/** @var string contenu du fichier de log */
	protected $log;
	
	/**
	 * Construire un(e) erreur
	 * @param $pdo PDO 
	 * @param $idErreur int 
	 * @param $description string Description de l'erreur
	 * @param $log string Contenu du fichier de log
	 * @param $lazyload bool Activer le chargement fainéant ?
	 */
	protected function __construct(PDO $pdo,$idErreur,$description,$log,$lazyload=false)
	{
		// Sauvegarder pdo
		$this->pdo = $pdo;
		
		// Sauvegarder les attributs
		$this->idErreur = $idErreur;
		$this->description = $description;
		$this->log = $log;
		
		// Sauvegarder pour le chargement fainéant
		if ($lazyload) {
			self::$lazyload[$idErreur] = $this;
		}
	}
	
	/**
	 * Créer un(e) erreur
	 * @param $pdo PDO 
	 * @param $description string Description de l'erreur
	 * @param $log string Contenu du fichier de log
	 * @param $lazyload bool Activer le chargement fainéant ?
	 * @return Erreur Erreur survenue dans ContestOrg
	 */
	public static function create(PDO $pdo,$description,$log,$lazyload=true)
	{
		// Ajouter le/la erreur dans la base de données
		$pdoStatement = $pdo->prepare('INSERT INTO '.Erreur::TABLENAME.' ('.Erreur::FIELDNAME_DESCRIPTION.','.Erreur::FIELDNAME_LOG.') VALUES (?,?)');
		if (!$pdoStatement->execute(array($description,$log))) {
			throw new Exception('Erreur durant l\'insertion d\'un(e) erreur dans la base de données');
		}
		
		// Construire le/la erreur
		return new Erreur($pdo,$pdo->lastInsertId(),$description,$log,$lazyload);
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
		return $pdo->prepare('SELECT DISTINCT '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_IDERREUR.', '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_DESCRIPTION.', '.Erreur::TABLENAME.'.'.Erreur::FIELDNAME_LOG.' '.
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
	 * @return Erreur Erreur survenue dans ContestOrg
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
	 * @return Erreur Erreur survenue dans ContestOrg
	 */
	public static function fetch(PDO $pdo,PDOStatement $pdoStatement,$lazyload=false)
	{
		// Extraire les valeurs
		$values = $pdoStatement->fetch();
		if (!$values) { return null; }
		list($idErreur,$description,$log) = $values;
		
		// Construire le/la erreur
		return isset(self::$lazyload[$idErreur]) ? self::$lazyload[$idErreur] :
		       new Erreur($pdo,$idErreur,$description,$log,$lazyload);
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
		return $this->_set(array(Erreur::FIELDNAME_DESCRIPTION,Erreur::FIELDNAME_LOG),array($this->description,$this->log));
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
	 * Récupérer le/la log
	 * @return string Contenu du fichier de log
	 */
	public function getLog()
	{
		return $this->log;
	}
	
	/**
	 * Définir le/la log
	 * @param $log string Contenu du fichier de log
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setLog($log,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->log = $log;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Erreur::FIELDNAME_LOG),array($log)) : true;
	}
}

