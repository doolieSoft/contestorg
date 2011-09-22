<?php

class ConfService extends Service
{
	// Configuration
	private $conf;
	
	// Démarrer le service
	public function start() {
		// Importer les classes de configuration
		require('tools/conf.php');
		require('tools/conf_data.php');
		
		// Charger la configuration
		$cong_data = new ConfData_Ini(ROOT_DIR.'configuration/configuration.ini');
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