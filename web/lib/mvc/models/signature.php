<?php

/**
 * @class Signature
 * @date 09/05/2011 (dd/mm/yyyy)
 */
class Signature extends SignatureBase
{
	/**
	 * Sélectionner tous les messages de manière paginée et triés par date
	 * @param $pdo PDO 
	 * @param from int numero de message de début
	 * @param to int numero de message de fin
	 * @return PDOStatement 
	 */
	public static function paginate(PDO $pdo,$from,$to)
	{
		$pdoStatement = SignatureBase::_select($pdo,null,'s.date DESC',$from.', '.$to);
		if (!$pdoStatement->execute()) {
			throw new Exception('Erreur lors du chargement des messages depuis la base de données');
		}
		return $pdoStatement;
	}
}

?>
