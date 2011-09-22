<?php

class SessionService extends Service
{
	
	// Démarrer le service
	public function start() {
		// Démarrer la session
		if (!session_start()) {
			// Erreur
			throw new Exception('Error while start session.');
		}
	}
	
	// Récupérer la ressource du service
	public function getRessource() {
		// Retourner la variable globale de la session
		return $_SESSION;
	}
	
	// Arreter le service
	public function stop() {
		
	}
}