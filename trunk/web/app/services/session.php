<?php

/**
 * Service d'accès à la session
 */
class SessionService extends Service
{
	
	/**
	 * @see Service::start()
	 */
	public function start() {
		// Démarrer la session
		if (!session_start()) {
			// Erreur
			throw new Exception('Error while start session.');
		}
	}
	
	/**
	 * @see Service::getRessource()
	 */
	public function getRessource() {
		// Retourner la variable globale de la session
		return $_SESSION;
	}
	
	/**
	 * @see Service::stop()
	 */
	public function stop() {
		
	}
}