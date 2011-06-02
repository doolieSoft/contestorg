<?php

class TelechargerController extends Controller
{
	
	// Action par défaut
	public function indexAction() {
		// Require des signatures et du paginator
		require_once('signature_base.php');
		require_once('signature.php');
		require_once('paginator.php');
		
		// Récupérer la configuration et pdo
		$conf = Application::getService('conf');
		$pdo = Application::getService('pdo');
		
		// Construire le formulaire du livre d'or
		require_once('form.php');
		$this->view->form = new Form(Form::METHOD_POST);
		$this->view->form->add(new FormText('prenom', 'Votre prénom', FormText::TYPE_MONOLINE, 50, 20));
		$this->view->form->add(new FormText('email', 'Votre email', FormText::TYPE_MONOLINE, 50, 130));
		$this->view->form->add(new FormText('message', 'Votre message', FormText::TYPE_MULTILINE, array(50,6), 2000));
		//$this->view->form->add(new FormCaptcha($conf['RECAPTCHA']['PUBLIC_KEY'],$conf['RECAPTCHA']['PRIVATE_KEY']));
		$this->view->form->add(new FormSubmit());
		
		// Vérification de l'email
		function form_check_email(FormText $email) {
			return filter_var($email->getValue(), FILTER_VALIDATE_EMAIL) !== false;
		}
		$this->view->form->email->setCallback('form_check_email');
		
		// Valider le formulaire
		if($this->view->form->validate()) {
			// Ajouter la signature
			Signature::create($pdo, $this->view->form->prenom->getValue(), time(), $this->view->form->email->getValue(), $this->view->form->message->getValue());
		
			// Prévenir la vue du succès de l'opération
			$this->view->success = true;
		}
		
		// Récupérer le numéro de page
		$this->view->page = Application::getRequest()->getParameter('page',0);
		
		// Nombre de signatures par page
		$count = 5;
		
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