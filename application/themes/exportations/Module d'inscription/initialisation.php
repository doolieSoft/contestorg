<?php

// Démarrer le mécanisme de session
session_start();

// Require de la librairie d'édition de formulaire
require('form.php');

// Messages pour le formulaire
Form::setErrorStart('<span class="error">');
Form::setErrorEnd('</span>');
Form::setMsgRequired('Champ requis');
Form::setMsgInvalid('Champ invalide');

// Vérifier si le fichier de données existe
if(!file_exists('donnees.xml')) {
	// Créer le document
	$document = new DOMDocument('1.0');
	
	// Ajouter le noeud racine
	$root = $document->appendChild($document->createElement('inscription'));
	
	// Ajouter la liste des statuts
	$statuts = $root->appendChild($document->createElement('listeStatuts'));
	
	// Ajouter le statut "En attente de validation"
	$statut = $statuts->appendChild($document->createElement('statut'));
	$statut->appendChild($document->createAttribute('id'))->value = 0;
	$statut->appendChild($document->createAttribute('nom'))->value = 'En attente de validation';
	$statut->appendChild($document->createAttribute('type'))->value = 'refuse';
	$suivants = $statut->appendChild($document->createElement('listeSuivants'));
	$suivants->appendChild($document->createElement('suivant'))->appendChild($document->createAttribute('refStatut'))->value = 1;
	$suivants->appendChild($document->createElement('suivant'))->appendChild($document->createAttribute('refStatut'))->value = 2;
	
	// Ajouter le statut "Participant validé"
	$statut = $statuts->appendChild($document->createElement('statut'));
	$statut->appendChild($document->createAttribute('id'))->value = 1;
	$statut->appendChild($document->createAttribute('nom'))->value = 'Participant accepté';
	$statut->appendChild($document->createAttribute('type'))->value = 'accepte';
	$suivants = $statut->appendChild($document->createElement('listeSuivants'));
	$suivants->appendChild($document->createElement('suivant'))->appendChild($document->createAttribute('refStatut'))->value = 0;
	
	// Ajouter le statut "Participant refusé"
	$statut = $statuts->appendChild($document->createElement('statut'));
	$statut->appendChild($document->createAttribute('id'))->value = 2;
	$statut->appendChild($document->createAttribute('nom'))->value = 'Participant refusé';
	$statut->appendChild($document->createAttribute('type'))->value = 'refuse';
	$suivants = $statut->appendChild($document->createElement('listeSuivants'));
	$suivants->appendChild($document->createElement('suivant'))->appendChild($document->createAttribute('refStatut'))->value = 0;
	
	// Ajouter la liste des participants
	$root->appendChild($document->createElement('listeParticipants'));

	// Sortie correctement formatée
	$document->formatOutput = true;
	
	// Crer le fichier XML
	$document->save('donnees.xml');
} else {
	// Charger le document
	$document = new DOMDocument();
	$document->formatOutput = true;
	$document->load('donnees.xml');
}