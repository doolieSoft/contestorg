<?php

/**
 * @name Signature
 * @version 22/09/2011 (dd/mm/yyyy)
 * @author WebProjectHelper (http://www.elfangels.fr/webprojecthelper/)
 */
abstract class SignatureBase
{
	/** @var PDO  */
	protected $pdo;
	
	/** @var array tableau pour le chargement faineant */
	protected static $lazyload;
	
	/** @var int  */
	protected $idSignature;
	
	/** @var string prénom de la personne qui a signé le livre d'or */
	protected $prenom;
	
	/** @var int date du message */
	protected $date;
	
	/** @var string email de la personne qui a signé le livre d'or */
	protected $email;
	
	/** @var string message laisser par la personne qui a signé le livre d'or */
	protected $message;
	
	/**
	 * Construire un(e) signature
	 * @param $pdo PDO 
	 * @param $idSignature int 
	 * @param $prenom string Prénom de la personne qui a signé le livre d'or
	 * @param $date int Date du message
	 * @param $email string Email de la personne qui a signé le livre d'or
	 * @param $message string Message laisser par la personne qui a signé le livre d'or
	 * @param $lazyload bool Activer le chargement faineant ?
	 */
	protected function __construct(PDO $pdo,$idSignature,$prenom,$date,$email,$message,$lazyload=false)
	{
		// Sauvegarder pdo
		$this->pdo = $pdo;
		
		// Sauvegarder les attributs
		$this->idSignature = $idSignature;
		$this->prenom = $prenom;
		$this->date = $date;
		$this->email = $email;
		$this->message = $message;
		
		// Sauvegarder pour le chargement faineant
		if ($lazyload) {
			self::$lazyload[$idSignature] = $this;
		}
	}
	
	/**
	 * Créer un(e) signature
	 * @param $pdo PDO 
	 * @param $prenom string Prénom de la personne qui a signé le livre d'or
	 * @param $date int Date du message
	 * @param $email string Email de la personne qui a signé le livre d'or
	 * @param $message string Message laisser par la personne qui a signé le livre d'or
	 * @param $lazyload bool Activer le chargement faineant ?
	 * @return Signature Signature sur le livre d'or
	 */
	public static function create(PDO $pdo,$prenom,$date,$email,$message,$lazyload=true)
	{
		// Ajouter le/la signature dans la base de données
		$pdoStatement = $pdo->prepare('INSERT INTO '.Signature::TABLENAME.' ('.Signature::FIELDNAME_PRENOM.','.Signature::FIELDNAME_DATE.','.Signature::FIELDNAME_EMAIL.','.Signature::FIELDNAME_MESSAGE.') VALUES (?,?,?,?)');
		if (!$pdoStatement->execute(array($prenom,date('Y-m-d H:i:s',$date),$email,$message))) {
			throw new Exception('Erreur durant l\'insertion d\'un(e) signature dans la base de données');
		}
		
		// Construire le/la signature
		return new Signature($pdo,$pdo->lastInsertId(),$prenom,$date,$email,$message,$lazyload);
	}
	
	/**
	 * Requête de séléction
	 * @param $pdo PDO 
	 * @param $where string|array 
	 * @param $orderby string|array 
	 * @param $limit string|array 
	 * @param $from string|array 
	 * @return PDOStatement 
	 */
	protected static function _select(PDO $pdo,$where=null,$orderby=null,$limit=null,$from=null)
	{
		return $pdo->prepare('SELECT '.Signature::TABLENAME.'.'.Signature::FIELDNAME_IDSIGNATURE.', '.Signature::TABLENAME.'.'.Signature::FIELDNAME_PRENOM.', '.Signature::TABLENAME.'.'.Signature::FIELDNAME_DATE.', '.Signature::TABLENAME.'.'.Signature::FIELDNAME_EMAIL.', '.Signature::TABLENAME.'.'.Signature::FIELDNAME_MESSAGE.' '.
		                     'FROM '.Signature::TABLENAME.($from != null ? ', '.(is_array($from) ? implode(', ',$from) : $from) : '').
		                     ($where != null ? ' WHERE '.(is_array($where) ? implode(' AND ',$where) : $where) : '').
		                     ($orderby != null ? ' ORDER BY '.(is_array($orderby) ? implode(', ',$orderby) : $orderby) : '').
		                     ($limit != null ? ' LIMIT '.(is_array($limit) ? implode(', ', $limit) : $limit) : ''));
	}
	
	/**
	 * Charger un(e) signature
	 * @param $pdo PDO 
	 * @param $idSignature int 
	 * @param $lazyload bool Activer le chargement faineant ?
	 * @return Signature Signature sur le livre d'or
	 */
	public static function load(PDO $pdo,$idSignature,$lazyload=true)
	{
		// Déjà chargé(e) ?
		if (isset(self::$lazyload[$idSignature])) {
			return self::$lazyload[$idSignature];
		}
		
		// Charger le/la signature
		$pdoStatement = self::_select($pdo,Signature::FIELDNAME_IDSIGNATURE.' = ?');
		if (!$pdoStatement->execute(array($idSignature))) {
			throw new Exception('Erreur lors du chargement d\'un(e) signature depuis la base de données');
		}
		
		// Récupérer le/la signature depuis le jeu de résultats
		return self::fetch($pdo,$pdoStatement,$lazyload);
	}
	
	/**
	 * Charger tous/toutes les signatures
	 * @param $pdo PDO 
	 * @param $lazyload bool Activer le chargement faineant ?
	 * @return Signature[] Tableau de signatures
	 */
	public static function loadAll(PDO $pdo,$lazyload=false)
	{
		// Sélectionner tous/toutes les signatures
		$pdoStatement = self::selectAll($pdo);
		
		// Mettre chaque signature dans un tableau
		$signatures = array();
		while ($signature = self::fetch($pdo,$pdoStatement,$lazyload)) {
			$signatures[] = $signature;
		}
		
		// Retourner le tableau
		return $signatures;
	}
	
	/**
	 * Sélectionner tous/toutes les signatures
	 * @param $pdo PDO 
	 * @return PDOStatement 
	 */
	public static function selectAll(PDO $pdo)
	{
		$pdoStatement = self::_select($pdo);
		if (!$pdoStatement->execute()) {
			throw new Exception('Erreur lors du chargement de tous/toutes les signatures depuis la base de données');
		}
		return $pdoStatement;
	}
	
	/**
	 * Récupère le/la signature suivant(e) d'un jeu de résultats
	 * @param $pdo PDO 
	 * @param $pdoStatement PDOStatement 
	 * @param $lazyload bool Activer le chargement faineant ?
	 * @return Signature Signature sur le livre d'or
	 */
	public static function fetch(PDO $pdo,PDOStatement $pdoStatement,$lazyload=false)
	{
		// Extraire les valeurs
		$values = $pdoStatement->fetch();
		if (!$values) { return null; }
		list($idSignature,$prenom,$date,$email,$message) = $values;
		
		// Construire le/la signature
		return isset(self::$lazyload[$idSignature]) ? self::$lazyload[$idSignature] :
		       new Signature($pdo,$idSignature,$prenom,strtotime($date),$email,$message,$lazyload);
	}
	
	/**
	 * Compter les signatures
	 * @param $pdo PDO 
	 * @return int Nombre de signatures
	 */
	public static function count(PDO $pdo)
	{
		if (!($pdoStatement = $pdo->query('SELECT COUNT('.Signature::FIELDNAME_IDSIGNATURE.') FROM '.Signature::TABLENAME))) {
			throw new Exception('Erreur lors du comptage des signatures dans la base de données');
		}
		return $pdoStatement->fetchColumn();
	}
	
	/**
	 * Supprimer le/la signature
	 * @return bool Opération réussie ?
	 */
	public function delete()
	{
		// Supprimer le/la signature
		$pdoStatement = $this->pdo->prepare('DELETE FROM '.Signature::TABLENAME.' WHERE '.Signature::FIELDNAME_IDSIGNATURE.' = ?');
		if (!$pdoStatement->execute(array($this->getIdSignature()))) {
			throw new Exception('Erreur lors de la supression d\'un(e) signature dans la base de données');
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
		$pdoStatement = $this->pdo->prepare('UPDATE '.Signature::TABLENAME.' SET '.implode(', ', $updates).' WHERE '.Signature::FIELDNAME_IDSIGNATURE.' = ?');
		if (!$pdoStatement->execute(array_merge($values,array($this->getIdSignature())))) {
			throw new Exception('Erreur lors de la mise à jour d\'un champ d\'un(e) signature dans la base de données');
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
		return $this->_set(array(Signature::FIELDNAME_PRENOM,Signature::FIELDNAME_DATE,Signature::FIELDNAME_EMAIL,Signature::FIELDNAME_MESSAGE),array($this->prenom,date('Y-m-d H:i:s',$this->date),$this->email,$this->message));
	}
	
	/**
	 * Récupérer le/la idSignature
	 * @return int 
	 */
	public function getIdSignature()
	{
		return $this->idSignature;
	}
	
	/**
	 * Récupérer le/la prenom
	 * @return string Prénom de la personne qui a signé le livre d'or
	 */
	public function getPrenom()
	{
		return $this->prenom;
	}
	
	/**
	 * Définir le/la prenom
	 * @param $prenom string Prénom de la personne qui a signé le livre d'or
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setPrenom($prenom,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->prenom = $prenom;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Signature::FIELDNAME_PRENOM),array($prenom)) : true;
	}
	
	/**
	 * Récupérer le/la date
	 * @return int Date du message
	 */
	public function getDate()
	{
		return $this->date;
	}
	
	/**
	 * Définir le/la date
	 * @param $date int Date du message
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setDate($date,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->date = $date;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Signature::FIELDNAME_DATE),array(date('Y-m-d H:i:s',$date))) : true;
	}
	
	/**
	 * Récupérer le/la email
	 * @return string Email de la personne qui a signé le livre d'or
	 */
	public function getEmail()
	{
		return $this->email;
	}
	
	/**
	 * Définir le/la email
	 * @param $email string Email de la personne qui a signé le livre d'or
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setEmail($email,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->email = $email;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Signature::FIELDNAME_EMAIL),array($email)) : true;
	}
	
	/**
	 * Récupérer le/la message
	 * @return string Message laisser par la personne qui a signé le livre d'or
	 */
	public function getMessage()
	{
		return $this->message;
	}
	
	/**
	 * Définir le/la message
	 * @param $message string Message laisser par la personne qui a signé le livre d'or
	 * @param $execute bool Exécuter la requête update ?
	 * @return bool Opération réussie ?
	 */
	public function setMessage($message,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->message = $message;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array(Signature::FIELDNAME_MESSAGE),array($message)) : true;
	}
}

