<?php

/**
 * Controleur de l'API
 */
class ApiController extends AbstractController
{
	
	/**
	 * Action affichant les versions de l'application
	 */
	public function versionsAction() {
		// Require de l'outil array_2_xml
		require('array_2_xml.php');
		
		// Afficher les versions de l'application
		header('content-type:text/xml');
		echo array_2_xml('listeVersions',array(
			array(
				'version',
				'@numero' => '2.3.1',
				'@date' => '12/06/2012',
				'listeTelechargements' => array(
					array(
						'telechargement',
						'@nom' => 'Version EXE (Windows)',
						'@url' => 'http://contestorg.googlecode.com/files/ContestOrg-2.3.1-exe.zip'
					),
					array(
						'telechargement',
						'@nom' => 'Version JAR (Linux, Mac)',
						'@url' => 'http://contestorg.googlecode.com/files/ContestOrg-2.3.1-jar.zip'
					)
				),
				'listeAnomalies' => array(
					array(
						'anomalie',
						'@description' => 'Erreur lors de la saisie des résultats d\'un match',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=58'
					)
				)
			),
			array(
				'version',
				'@numero' => '2.3.0',
				'@date' => '11/06/2012',
				'listeTelechargements' => array(
					array(
						'telechargement',
						'@nom' => 'Version EXE (Windows)',
						'@url' => 'http://contestorg.googlecode.com/files/ContestOrg-2.3.0-exe.zip'
					),
					array(
						'telechargement',
						'@nom' => 'Version JAR (Linux, Mac)',
						'@url' => 'http://contestorg.googlecode.com/files/ContestOrg-2.3.0-jar.zip'
					)
				),
				'listeEvolutions' => array(
					array(
						'evolution',
						'@description' => 'Exportation "Liste des matchs des phases qualificatives en PDF"',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=26'
					),
					array(
						'evolution',
						'@description' => 'Exportation "Liste des matchs des phases qualificatives non jouées en PDF"',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=50'
					),
					array(
						'evolution',
						'@description' => 'Renommer l\'exportation "Liste des matchs des prochaines phases qualificatives en PDF"',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=51'
					),
					array(
						'evolution',
						'@description' => 'Exportation "Résultats des matchs des phases qualificatives en PDF"',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=53'
					),
					array(
						'evolution',
						'@description' => 'Diffusion du podium',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=23'
					),
					array(
						'evolution',
						'@description' => 'Implémenter la définition de la date d\'un match',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=33'
					),
					array(
						'evolution',
						'@description' => 'Implémenter la définition du lieu d\'un match',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=35'
					)
				),
				'listeAnomalies' => array(
					array(
						'anomalie',
						'@description' => 'Lien vers l\'aide invalide',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=48'
					),
					array(
						'anomalie',
						'@description' => 'Problème lors de la génération des phases éliminatoires',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=54'
					),
					array(
						'anomalie',
						'@description' => 'Sauvegarder sous l\'environnement Mac-OS',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=55'
					),
					array(
						'anomalie',
						'@description' => 'Erreur lors de la modification des catégories',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=57'
					)
				)
			),
			array(
				'version',
				'@numero' => '2.0.0',
				'@date' => '26/03/2012',
				'listeTelechargements' => array(
					array(
						'telechargement',
						'@nom' => 'Version EXE (Windows)',
						'@url' => 'http://contestorg.googlecode.com/files/ContestOrg-2.0.0-exe.zip'
					),
					array(
						'telechargement',
						'@nom' => 'Version JAR (Linux, Mac)',
						'@url' => 'http://contestorg.googlecode.com/files/ContestOrg-2.0.0-jar.zip'
					)
				),
				'listeEvolutions' => array(
					array(
						'evolution',
						'@description' => 'Remplissement automatique du nom des diffusions et exportations',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=26'
					),
					array(
						'evolution',
						'@description' => 'Utilisation du terme "participant" à la place du terme "equipe" dans le code',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=27'
					),
					array(
						'evolution',
						'@description' => 'Module d\'inscription des équipes',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=5'
					),
					array(
						'evolution',
						'@description' => 'Paramètre de thème booléen',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=42'
					),
					array(
						'evolution',
						'@description' => 'Ne plus obliger la sélection d\'une poule pour générer une phase qualificative',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=7'
					),
					array(
						'evolution',
						'@description' => 'Changement de catégorie dans exportation à la volée',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=19'
					),
					array(
						'evolution',
						'@description' => 'Rétro-compatibilité sur la DTD des fichiers de concours',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=29'
					),
					array(
						'evolution',
						'@description' => 'Exportation d\'un rapport journalistique',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=3'
					),
					array(
						'evolution',
						'@description' => 'Vérification automatique d\'une nouvelle version',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=1'
					),
					array(
						'evolution',
						'@description' => 'Envoi d\'un rapport d\'erreur',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=4'
					),
					array(
						'evolution',
						'@description' => 'Affectation automatique des équipes aux poules par "chapeaux"',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=13'
					)
				),
				'listeAnomalies' => array(
					array(
						'anomalie',
						'@description' => 'Correction d\'un problème de chargement du chargement du tournoi',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=37'
					),
					array(
						'anomalie',
						'@description' => 'Empêcher la suppression d\'un participant s\'il participe aux phases éliminatoires',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=41'
					),
					array(
						'anomalie',
						'@description' => 'Erreur sur la gestion des poules',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=34'
					),
					array(
						'anomalie',
						'@description' => 'Manque d\'informations sur les lieux dans le site web généré',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=6'
					)
				),
				'listeRevues' => array(
					array(
						'revue',
						'@description' => 'Code entièrement commenté',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=30'
					)
				),
				'listeTaches' => array(
					array(
						'tache',
						'@description' => 'Création d\'une liste de diffusion',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=24'
					),
					array(
						'tache',
						'@description' => 'Ecrire une FAQ',
						'@ticket' => 'http://code.google.com/p/contestorg/issues/detail?id=25'
					)
				)
			),
			array(
				'version',
				'@numero' => '2.0.0b',
				'@date' => '15/05/2011',
				'listeEvolutions' => array(
					array(
						'evolution',
						'@description' => 'Journalisation l\'ouverture et la fermeture de l\'application'
					)
				),
				'listeAnomalies' => array(
					array(
						'anomalie',
						'@description' => 'Changement du type des informations de programmation des matchs en décimale'
					),
					array(
						'anomalie',
						'@description' => 'Inversement de l\'ajout des points des objectifs avec les points de victoire/égalite/défaite'
					),
					array(
						'anomalie',
						'@description' => 'Empêchement de la séléction de plusieurs matchs dans l\'onglet des phases qualificatives'
					),
					array(
						'anomalie',
						'@description' => 'Correction de problèmes liés aux encodages de caractères des exportations/diffusions'
					),
					array(
						'anomalie',
						'@description' => 'Correction d\'un bogue lors de la modification des catégories'
					),
					array(
						'anomalie',
						'@description' => 'Correction d\'un bogue sur les arborescences des catégories, poules et phases qualificatives'
					),
					array(
						'anomalie',
						'@description' => 'Correction d\'un bogue lié aux chemins entre Windows et Linux dans le fichier du tournoi'
					)
				)
			),
			array(
				'version',
				'@numero' => '2.0.0a',
				'@date' => '26/04/2011'
			)
		));
	}
	
	/**
	 * Action enregistrant une erreur survenue dans l'application
	 */
	public function errorAction() {
		// Formulaire de soumission d'erreur
		require_once('form/require.php');
		$form = new Form(Form::METHOD_POST);
		$erreurDescription = $form->add(new FormText('erreur_description'));
		$erreurExceptionsMessages = $form->add(new FormSequence(
			'error_exceptions_messages','/^erreur_exception[0-9]+_message$/',
			function($name) { return new FormText($name); }
		),false);
		$erreurExceptionsStacktraces = $form->add(new FormSequence(
			'error_exceptions_stacktraces','/^erreur_exception[0-9]+_stacktrace$/',
			function($name) { return new FormText($name); }
		),false);
		$clientLOG = $form->add(new FormFile('client_log'));
		$clientXML = $form->add(new FormText('client_xml'),false);
		$clientVersion = $form->add(new FormText('client_version'));
		$environnementOS = $form->add(new FormText('environnement_os'));
		$environnementJAVA = $form->add(new FormText('environnement_java'));
		
		// Valider le formulaire
		if($form->validate()) {
			// Récupérer les données des exceptions
			$messages = $erreurExceptionsMessages->getElements();
			$stacktraces = $erreurExceptionsStacktraces->getElements();
			if(count($messages) != count($stacktraces)) {
				Application::error();
			}
			
			// Récupérer PDO
			$pdo = Application::getService('pdo');
			
			// Enregistrer l'erreur
			$erreur = Erreur::create(
				$pdo,
				time(),
				$erreurDescription->getValue(),
				file_get_contents($clientLOG->getPath()),
				$clientVersion->getValue(),
				$clientXML->getValue(),
				$environnementOS->getValue(),
				$environnementJAVA->getValue()
			);
			
			// Enregistrer les exceptions associées à l'erreur
			for($i=0;$i<count($messages);$i++) {
				ErreurException::create(
					$pdo,
					$erreur,
					$messages['erreur_exception'.$i.'_message']->getValue(),
					$stacktraces['erreur_exception'.$i.'_stacktrace']->getValue()
				);
			}
		} else {
			// Ancien formulaire de soumission d'erreur
			$oldForm = new Form(Form::METHOD_POST);
			$erreurDescription = $oldForm->add(new FormText('description'));
			$clientLOG = $oldForm->add(new FormFile('log'));
			
			// Valider l'ancien formulaire
			if($oldForm->validate()) {
				// Enregistrer l'erreur
				Erreur::create(
					Application::getService('pdo'),
					time(),
					$erreurDescription->getValue(),
					file_get_contents($clientLOG->getPath()),
					'<= 2.3.1'
				);
			} else {
				Application::error();
			}
		}
	}
	
}