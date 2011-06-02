<?php

/**
 * Signature sur le livre d'or
 * @class SignatureBase
 * @date 11/05/2011 (dd/mm/yyyy)
 * @generator WebProjectHelper (http://www.elfangels.fr/webprojecthelper/)
 */
class SignatureBase
{
	/// @var PDO 
	protected $pdo;
	
	/// @var array tableau pour le chargement rapide
	protected static $easyload;
	
	/// @var int 
	protected $idsignature;
	
	/// @var string prenom de la personne qui a signé le livre d'or
	protected $prenom;
	
	/// @var int date du message
	protected $date;
	
	/// @var string email de la personne qui a signé le livre d'or
	protected $email;
	
	/// @var string message laisser par la personne qui a signé le livre d'or
	protected $message;
	
	/**
	 * Construire un(e) signature
	 * @param $pdo PDO 
	 * @param $idsignature int 
	 * @param $prenom string prenom de la personne qui a signé le livre d'or
	 * @param $date int date du message
	 * @param $email string email de la personne qui a signé le livre d'or
	 * @param $message string message laisser par la personne qui a signé le livre d'or
	 * @param $easyload bool activer le chargement rapide ?
	 * @return Signature signature sur le livre d'or
	 */
	protected function __construct(PDO $pdo,$idsignature,$prenom,$date,$email,$message,$easyload=false)
	{
		// Sauvegarder pdo
		$this->pdo = $pdo;
		
		// Sauvegarder les attributs
		$this->idsignature = $idsignature;
		$this->prenom = $prenom;
		$this->date = $date;
		$this->email = $email;
		$this->message = $message;
		
		// Sauvegarder pour le chargement rapide
		if ($easyload) {
			SignatureBase::$easyload[$idsignature] = $this;
		}
	}
	
	/**
	 * Créer un(e) signature
	 * @param $pdo PDO 
	 * @param $prenom string prenom de la personne qui a signé le livre d'or
	 * @param $date int date du message
	 * @param $email string email de la personne qui a signé le livre d'or
	 * @param $message string message laisser par la personne qui a signé le livre d'or
	 * @param $easyload bool activer le chargement rapide ?
	 * @return Signature signature sur le livre d'or
	 */
	public static function create(PDO $pdo,$prenom,$date,$email,$message,$easyload=true)
	{
		// Ajouter le/la signature dans la base de données
		$pdoStatement = $pdo->prepare('INSERT INTO contestorg_signature (prenom,date,email,message) VALUES (?,?,?,?)');
		if (!$pdoStatement->execute(array($prenom,date('Y-m-d H:i:s',$date),$email,$message))) {
			throw new Exception('Erreur durant l\'insertion d\'un(e) signature dans la base de données');
		}
		
		// Construire le/la signature
		return new Signature($pdo,$pdo->lastInsertId(),$prenom,$date,$email,$message,$easyload);
	}
	
	/**
	 * Requête de séléction
	 * @param $pdo PDO 
	 * @param $where string 
	 * @param $orderby string 
	 * @param $limit string 
	 * @return PDOStatement 
	 */
	protected static function _select(PDO $pdo,$where=null,$orderby=null,$limit=null)
	{
		return $pdo->prepare('SELECT s.idsignature, s.prenom, s.date, s.email, s.message FROM contestorg_signature s '.
		                     ($where != null ? ' WHERE '.$where : '').
		                     ($orderby != null ? ' ORDER BY '.$orderby : '').
		                     ($limit != null ? ' LIMIT '.$limit : ''));
	}
	
	/**
	 * Charger un(e) signature
	 * @param $pdo PDO 
	 * @param $idsignature int 
	 * @param $easyload bool activer le chargement rapide ?
	 * @return Signature signature sur le livre d'or
	 */
	public static function load(PDO $pdo,$idsignature,$easyload=true)
	{
		// Déjà chargé(e) ?
		if (isset(SignatureBase::$easyload[$idsignature])) {
			return SignatureBase::$easyload[$idsignature];
		}
		
		// Charger le/la signature
		$pdoStatement = SignatureBase::_select($pdo,'s.idsignature = ?');
		if (!$pdoStatement->execute(array($idsignature))) {
			throw new Exception('Erreur lors du chargement d\'un(e) signature depuis la base de données');
		}
		
		// Récupérer le/la signature depuis le jeu de résultats
		return SignatureBase::fetch($pdo,$pdoStatement,$easyload);
	}
	
	/**
	 * Charger tous/toutes les signatures
	 * @param $pdo PDO 
	 * @param $easyload bool activer le chargement rapide ?
	 * @return Signature[] tableau de signatures
	 */
	public static function loadAll(PDO $pdo,$easyload=false)
	{
		// Sélectionner tous/toutes les signatures
		$pdoStatement = SignatureBase::selectAll($pdo);
		
		// Mettre chaque signature dans un tableau
		$signatures = array();
		while ($signature = SignatureBase::fetch($pdo,$pdoStatement,$easyload)) {
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
		$pdoStatement = SignatureBase::_select($pdo);
		if (!$pdoStatement->execute()) {
			throw new Exception('Erreur lors du chargement de tous/toutes les signatures depuis la base de données');
		}
		return $pdoStatement;
	}
	
	/**
	 * Récupère le/la signature suivant(e) d'un jeu de résultats
	 * @param $pdo PDO 
	 * @param $pdoStatement PDOStatement 
	 * @param $easyload bool activer le chargement rapide ?
	 * @return Signature signature sur le livre d'or
	 */
	public static function fetch(PDO $pdo,PDOStatement $pdoStatement,$easyload=false)
	{
		// Extraire les valeurs
		$values = $pdoStatement->fetch();
		if (!$values) { return null; }
		list($idsignature,$prenom,$date,$email,$message) = $values;
		
		// Construire le/la signature
		return isset(SignatureBase::$easyload[$idsignature]) ? SignatureBase::$easyload[$idsignature] :
		       new Signature($pdo,$idsignature,$prenom,strtotime($date),$email,$message,$easyload);
	}
	
	/**
	 * Compter les signatures
	 * @param $pdo PDO 
	 * @return int nombre de signatures
	 */
	public static function count(PDO $pdo)
	{
		if (!($pdoStatement = $pdo->query('SELECT COUNT(idsignature) FROM contestorg_signature'))) {
			throw new Exception('Erreur lors du comptage des signatures dans la base de données');
		}
		return $pdoStatement->fetchColumn();
	}
	
	/**
	 * Supprimer le/la signature
	 * @return bool opération réussie ?
	 */
	public function delete()
	{
		// Supprimer le/la signature
		$pdoStatement = $this->pdo->prepare('DELETE FROM contestorg_signature WHERE idsignature = ?');
		if (!$pdoStatement->execute(array($this->getIdsignature()))) {
			throw new Exception('Erreur lors de la supression d\'un(e) signature dans la base de données');
		}
		
		// Opération réussie ?
		return $pdoStatement->rowCount() == 1;
	}
	
	/**
	 * Mettre à jour un champ dans la base de données
	 * @param $fields array 
	 * @param $values array 
	 * @return bool opération réussie ?
	 */
	protected function _set($fields,$values)
	{
		// Préparer la mise à jour
		$updates = array();
		foreach ($fields as $field) {
			$updates[] = $field.' = ?';
		}
		
		// Mettre à jour le champ
		$pdoStatement = $this->pdo->prepare('UPDATE signature SET '.implode(', ', $updates).' WHERE idsignature = ?');
		if (!$pdoStatement->execute(array_merge($values,array($this->getIdsignature())))) {
			throw new Exception('Erreur lors de la mise à jour d\'un champ d\'un(e) signature dans la base de données');
		}
		
		// Opération réussie ?
		return $pdoStatement->rowCount() == 1;
	}
	
	/**
	 * Mettre à jour tous les champs dans la base de données
	 * @return bool opération réussie ?
	 */
	public function update()
	{
		return $this->_set(array('prenom','date','email','message'),array($this->prenom,date('Y-m-d H:i:s',$this->date),$this->email,$this->message));
	}
	
	/**
	 * Récupérer le/la idsignature
	 * @return int 
	 */
	public function getIdsignature()
	{
		return $this->idsignature;
	}
	
	/**
	 * Récupérer le/la prenom
	 * @return string prenom de la personne qui a signé le livre d'or
	 */
	public function getPrenom()
	{
		return $this->prenom;
	}
	
	/**
	 * Définir le/la prenom
	 * @param $prenom string prenom de la personne qui a signé le livre d'or
	 * @param $execute bool exécuter la requête update ?
	 * @return bool opération réussie ?
	 */
	public function setPrenom($prenom,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->prenom = $prenom;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array('prenom'),array($prenom)) : true;
	}
	
	/**
	 * Récupérer le/la date
	 * @return int date du message
	 */
	public function getDate()
	{
		return $this->date;
	}
	
	/**
	 * Définir le/la date
	 * @param $date int date du message
	 * @param $execute bool exécuter la requête update ?
	 * @return bool opération réussie ?
	 */
	public function setDate($date,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->date = $date;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array('date'),array(date('Y-m-d H:i:s',$date))) : true;
	}
	
	/**
	 * Récupérer le/la email
	 * @return string email de la personne qui a signé le livre d'or
	 */
	public function getEmail()
	{
		return $this->email;
	}
	
	/**
	 * Définir le/la email
	 * @param $email string email de la personne qui a signé le livre d'or
	 * @param $execute bool exécuter la requête update ?
	 * @return bool opération réussie ?
	 */
	public function setEmail($email,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->email = $email;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array('email'),array($email)) : true;
	}
	
	/**
	 * Récupérer le/la message
	 * @return string message laisser par la personne qui a signé le livre d'or
	 */
	public function getMessage()
	{
		return $this->message;
	}
	
	/**
	 * Définir le/la message
	 * @param $message string message laisser par la personne qui a signé le livre d'or
	 * @param $execute bool exécuter la requête update ?
	 * @return bool opération réussie ?
	 */
	public function setMessage($message,$execute=true)
	{
		// Sauvegarder dans l'objet
		$this->message = $message;
		
		// Sauvegarder dans la base de données (ou pas)
		return $execute ? $this->_set(array('message'),array($message)) : true;
	}
}

?>
