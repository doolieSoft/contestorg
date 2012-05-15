<?php

/**
 * Controleur des téléchargements
 */
class TelechargerController extends Controller
{
	
	/**
	 * Action pour la liste des téléchargements
	 */
	public function indexAction() {
		// Require du paginator
		require_once('paginator.php');
		
		// Récupérer la configuration et pdo
		$conf = Application::getService('conf');
		$pdo = Application::getService('pdo');
		
		// Récupérer le formulaire du livre d'or
		require_once('form_livredor.php');
		$this->view->form = new FormLivredor();
		
		// Valider le formulaire
		if($this->view->form->validate()) {
			// Ajouter la signature
			Signature::create($pdo, $this->view->form->prenom->getValue(), time(), $this->view->form->email->getValue(), $this->view->form->message->getValue());
		
			// Prévenir la vue du succès de l'opération
			$this->view->success = true;
		}
		
		// Récupérer le numéro de page
		$this->view->page = $this->request->getParameter('page',0);
		
		// Nombre de signatures par page
		$count = 8;
		
		// Récupérer la liste des signatures
		$statement = Signature::paginate($pdo, $this->view->page*$count, $this->view->page*$count+$count);
		$signatures = array();
		while($signature = Signature::fetch($pdo, $statement)) {
			$signatures[] = $signature;
		}
		
		// Créer le paginateur
		$this->view->paginator = new Paginator($signatures, $this->view->page, ceil(Signature::count($pdo)/$count));
		
		// Render sur la vue associée à l'action
		$this->render();
	}
	
}