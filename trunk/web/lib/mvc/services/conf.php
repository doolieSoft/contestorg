<?php

class ConfService extends Service
{
	// Configuration
	private $conf;
	
	// Démarrer le service
	public function start() {
		// Importer les classes de configuration
		require(ROOT_DIR.'lib/tools/conf.php');
		require(ROOT_DIR.'lib/tools/conf_data.php');
		
		// Charger la configuration
		$cong_data = new ConfData_Ini(ROOT_DIR.'conf/conf.ini');
		$this->conf = new Conf($cong_data);
	}
	
	// Récupérer la ressource du service
	public function getRessource() {
		return $this->conf;
	}
	
	// Arreter le service
	public function stop() {
		
	}
}