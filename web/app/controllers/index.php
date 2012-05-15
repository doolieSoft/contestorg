<?php

/**
 * Controleur principal
 */
class IndexController extends Controller 
{
	/**
	 * Action pour la page de contact
	 */
	public function contactAction() {
		// Récupérer la configuration
		$conf = Application::getService('conf');

		// Transmettre l'adresse email à la vue
		$this->view->email = $conf['MISC']['EMAIL'];
			
		// Récupérer le formulaire de contact
		require_once('form_contact.php');
		$this->view->form = new FormContact();
		
		// Valider le formulaire
		if($this->view->form->validate()) {
			// Envoyer l'email
			require_once('mail.php');
			require_once('bbcode.php');
			$mail = new Mail();
			$mail->setSubjet($this->view->form->sujet->getValue());
			$mail->setFrom($conf['MISC']['EMAIL'],'Equipe ContestOrg');
			$mail->setReplyTo($this->view->form->email->getValue(),$this->view->form->nom->getValue());
			$mail->setContent('<p>Un message a été envoyé via le formulaire de contact par <a href="mailto:'.$this->view->form->email->getValue().'">'.$this->view->form->nom->getValue().'</a> :</p><div style="padding-left:50px;">'.bbcode($this->view->form->message->getValue()).'</div>');
			$this->view->success = $mail->send($conf['MISC']['EMAIL'],'Equipe ContestOrg');
		}
		
		// Render sur la vue associée à l'action
		$this->render();
	}

}