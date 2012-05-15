<?php

// Initialisation de l'outil form
require_once('init.php');

/**
 * Formulaire de contact
 */
class FormContact extends Form
{
	/**
	 * Constructeur
	 */
	public function __construct() {
		// Appeller le constructeur parent
		parent::__construct();
		
		// Ajouter les champs
		$this->add(new FormText('nom', 'Votre nom', FormText::TYPE_MONOLINE, 50, 20));
		$this->add(new FormText('email', 'Votre email', FormText::TYPE_MONOLINE, 50, 130));
		$this->add(new FormText('sujet', 'Votre sujet', FormText::TYPE_MONOLINE, 50, 100));
		$this->add(new FormText('message', 'Votre message', FormText::TYPE_MULTILINE, array(50,6), 2000));
		// $this->add(new FormCaptcha($conf['RECAPTCHA']['PUBLIC_KEY'],$conf['RECAPTCHA']['PRIVATE_KEY']));
		$this->add(new FormSubmit('envoyer','Envoyer'));
		
		// Vérification de l'email
		$this->email->setCallback(function (FormText $email) {
			return filter_var($email->getValue(), FILTER_VALIDATE_EMAIL) !== false;
		});
	}
}