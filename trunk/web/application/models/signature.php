<?php

/**
 * Signature sur le livre d'or
 * @name Signature
 * @version 22/09/2011 (dd/mm/yyyy)
 */
class Signature extends SignatureBase
{
	
	// Nom de la table
	const TABLENAME = 'contestorg_signature';
	
	// Nom des champs
	const FIELDNAME_IDSIGNATURE = 'idsignature';
	const FIELDNAME_PRENOM = 'prenom';
	const FIELDNAME_DATE = 'date';
	const FIELDNAME_EMAIL = 'email';
	const FIELDNAME_MESSAGE = 'message';
	
	/**
	 * Sélectionner tous les messages de manière paginée et triés par date
	 * @param $pdo PDO 
	 * @param from int numero de message de début
	 * @param to int numero de message de fin
	 * @return PDOStatement 
	 */
	public static function paginate(PDO $pdo,$from,$to)
	{
		$pdoStatement = SignatureBase::_select($pdo,null,self::FIELDNAME_DATE.' DESC',$from.', '.($to-$from));
		if (!$pdoStatement->execute()) {
			throw new Exception('Erreur lors du chargement des messages depuis la base de données');
		}
		return $pdoStatement;
	}
	
}

