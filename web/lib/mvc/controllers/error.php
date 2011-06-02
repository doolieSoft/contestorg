<?php

class ErrorController extends Controller
{
	// Action appellée lors d'une erreur
	public function errorAction()
	{
		// Récupérer le message d'erreur
		$this->view->message = Application::getRequest()->getParameter('message');
		
		// Vérifier si un message a bien été donné
		if($this->view->message === null) {
			$this->view->message = 'Une erreur s\'est produite.';
		}
		
		// Render sur la vue associée à l'action
		$this->render();
	}
}