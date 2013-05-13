<?php

/**
 * Signature sur le livre d'or
 * @name Signature
 * @version 22/09/2011 (dd/mm/yyyy)
 */
class Signature extends SignatureBase
{
	
	/**
	 * Sélectionner les signatures de manière paginée et triés par date
	 * @param $pdo PDO 
	 * @param from int indice de début
	 * @param to int indice de fin
	 * @return PDOStatement 
	 */
	public static function paginate(PDO $pdo,$from,$to)
	{
		$pdoStatement = SignatureBase::_select($pdo,null,self::FIELDNAME_DATE.' DESC',$from.', '.($to-$from));
		if (!$pdoStatement->execute()) {
			throw new Exception('Erreur lors du chargement des signatures de manière paginée depuis la base de données');
		}
		return $pdoStatement;
	}
	
}

