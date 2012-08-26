<?php

/**
 * Controleur des erreurs
 */
class ErrorController extends AbstractController
{
	/**
	 * Action appelée lors d'une erreur
	 */
	public function errorAction()
	{
		// Récupérer le message d'erreur
		$this->view->message = $this->request->getParameter('message','Une erreur s\'est produite.');
		
		// Render sur la vue associée à l'action
		$this->render();
	}
}