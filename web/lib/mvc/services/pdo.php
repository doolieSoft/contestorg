<?php

class PdoService extends Service
{
	// PDO
	private $pdo;
	
	// Démarrer le service
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
	
	// Récupérer la ressource du service
	public function getRessource() {
		return $this->pdo;
	}
	
	// Arreter le service
	public function stop() {
		
	}
}