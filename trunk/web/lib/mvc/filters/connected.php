<?php

class ConnectedFilter extends Filter
{
	// Implémentation de filter
	public function filter() {
		// Récupérer la requête
		$request = Application::getRequest();
		
		// Continuer l'éxecution
		Controller::run($request->getAction(),$request->getController());
	}
}