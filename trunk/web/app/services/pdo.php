<?php

/**
 * Service de connexion à la base de données
 */
class PdoService extends Service
{
	/** @var $pdo PDO PDO */
	private $pdo;
	
	/**
	 * @see Service::start()
	 */
	public function start() {
		// Récupérer l'objet de configuration
		$conf = Application::getService('conf');
		
		// Tentative de connextion à la base de données
		try {
			$this->pdo = new PDO(
				'mysql:dbname='.$conf['DATABASE']['NAME'].';host='.$conf['DATABASE']['HOST'].';',
			    $conf['DATABASE']['USERNAME'],$conf['DATABASE']['PASSWORD']
			);
		} catch (PDOException $e) {
			throw new Exception('Error while login to database.');
		}
	}
	
	/**
	 * @see Service::getRessource()
	 */
	public function getRessource() {
		return $this->pdo;
	}
	
	/**
	 * @see Service::stop()
	 */
	public function stop() {
		
	}
}