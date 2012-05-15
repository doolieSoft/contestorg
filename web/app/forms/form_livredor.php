<?php

// Initialisation de l'outil form
require_once('init.php');

/**
 * Formulaire du livre d'or
 */
class FormLivredor extends Form
{
	/**
	 * Constructeur
	 */
	public function __construct() {
		// Appeller le constructeur parent
		parent::__construct();
		
		// Ajouter les champs
		$this->add(new FormText('prenom', 'Votre prénom', FormText::TYPE_MONOLINE, 50, 20));
		$this->add(new FormText('email', 'Votre email', FormText::TYPE_MONOLINE, 50, 130));
		$this->add(new FormText('message', 'Votre message', FormText::TYPE_MULTILINE, array(50,6), 2000));
		//$this->add(new FormCaptcha($conf['RECAPTCHA']['PUBLIC_KEY'],$conf['RECAPTCHA']['PRIVATE_KEY']));
		$this->add(new FormSubmit('valider','Valider'));
		
		// Vérification de l'email
		$this->email->setCallback(function (FormText $email) {
			return filter_var($email->getValue(), FILTER_VALIDATE_EMAIL) !== false;
		});
	}
}