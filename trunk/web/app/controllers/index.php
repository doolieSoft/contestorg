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
			
		// Construire le formulaire de contact
		require_once('tools/form.php');
		$this->view->form = new Form(Form::METHOD_POST);
		$this->view->form->add(new FormText('nom', 'Votre nom', FormText::TYPE_MONOLINE, 50, 20));
		$this->view->form->add(new FormText('email', 'Votre email', FormText::TYPE_MONOLINE, 50, 130));
		$this->view->form->add(new FormText('sujet', 'Votre sujet', FormText::TYPE_MONOLINE, 50, 100));
		$this->view->form->add(new FormText('message', 'Votre message', FormText::TYPE_MULTILINE, array(50,6), 2000));
		// $this->view->form->add(new FormCaptcha($conf['RECAPTCHA']['PUBLIC_KEY'],$conf['RECAPTCHA']['PRIVATE_KEY']));
		$this->view->form->add(new FormSubmit('envoyer','Envoyer'));
		
		// Vérification de l'email
		function form_check_email(FormText $email) {
			return filter_var($email->getValue(), FILTER_VALIDATE_EMAIL) !== false;
		}
		$this->view->form->email->setCallback('form_check_email');
		
		// Valider le formulaire
		if($this->view->form->validate()) {
			// Envoyer l'email
			require_once('tools/mail.php');
			require_once('tools/bbcode.php');
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