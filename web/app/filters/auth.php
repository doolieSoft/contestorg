<?php

/**
 * Filtre vérifiant si l'utilisateur est authentifié sur certaines pages
 */
class AuthFilter extends Filter
{
	/**
	 * @see Filter::process()
	 */
	public function process() {
		// Continuer l'éxecution
		$this->forward();
	}
}