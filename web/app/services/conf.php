<?php

class ConfService extends Service
{
	// Configuration
	private $conf;
	
	// Démarrer le service
	public function start() {
		// Parser le fichier de configuration
		$this->conf = parse_ini_file(ROOT_DIR.'conf/app.ini',true);
	}
	
	// Récupérer la ressource du service
	public function getRessource() {
		return $this->conf;
	}
	
	// Arreter le service
	public function stop() {
		
	}
}