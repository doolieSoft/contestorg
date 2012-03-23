<?php

class ApiController extends Controller
{
	
	// Action affichant les versions de l'application
	public function versionsAction() {
		// Require de l'outil array_2_xml
		require('tools/array_2_xml.php');
		
		// Afficher les versions de l'application
		header('content-type:text/xml');
		echo array_2_xml('listeVersions',array(
			array(
				'version',
				'@numero' => '2.0.0',
				'@date' => '26/03/2012',
				'listeTelechargements' => array(
					array(
						'telechargement',
						'@nom' => 'Version EXE (Windows)',
						'@url' => 'http://exe/'
					),
					array(
						'telechargement',
						'@nom' => 'Version JAR (Linux, Mac)',
						'@url' => 'http://jar/'
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
	
	// Action enregistrant une erreur survenue dans l'application
	public function errorAction() {
		// Construire le formulaire de soumission d'erreur
		require_once('tools/form.php');
		$form = new Form(Form::METHOD_POST);
		$description = $form->add(new FormText('description'));
		$log = $form->add(new FormFile('log'));
		
		// Valider le formulaire
		if($form->validate()) {
			// Require des erreurs
			require_once('erreur_base.php');
			require_once('erreur.php');
			
			// Enregistrer en base de données le fichier de log
			Erreur::create(
				Application::getService('pdo'),
				$description->getValue(),
				file_get_contents($log->getPath())
			);
		}
	}
	
}