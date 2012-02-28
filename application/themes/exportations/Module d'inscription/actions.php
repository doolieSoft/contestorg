<?php

// Require du script d'initialisation
require('initialisation.php');

// Vérifier si l'action a bien été définie
if(isset($_GET['action'])) {
	// Déconnexion
	if($_GET['action'] == 'deconnexion') {	
		// Détruire la session courante
		session_destroy();
		
		// Rediriger l'utilisateur sur l'accueil
		header('location:index.php');
		
		// Arrêter le script
		exit;
	}
	
	// Vérifier si l'utilisateur est connecté en tant qu'organisateur
	if(isset($_SESSION['organisateur'])) {
		// Récupérer une instance de xpath
		$xpath = new DOMXPath($document);
		
		// Récupérer l'action désirée
		switch($_GET['action']) {
			// Modifier le statut d'un participant
			case 'participant-modifier-statut':
				// Récupérer le participant
				$participant = $xpath->query('//inscription/listeParticipants/participant[@id='.$_GET['participant'].']')->item(0);
				
				// Modifier le statut du participant
				$participant->getAttributeNode('refStatut')->value = $_GET['statut'];
				break;
				
			// Supprimer un participant
			case 'participant-supprimer':
				// Récupérer le participant
				$participant = $xpath->query('//inscription/listeParticipants/participant[@id='.$_GET['participant'].']')->item(0);
				
				// Supprimer le participant
				$participant->parentNode->removeChild($participant);
				break;
				
			// Ajouter un statut
			case 'statut-ajouter':
				// Récupérer la liste des statuts
				$statuts = $xpath->query('//inscription/listeStatuts')->item(0);
				
				// Récupérer l'identifiant
				$dernier = $xpath->query('//inscription/listeStatuts/statut[last()]');
				$id = $dernier->length == 0 ? 0 : $dernier->item(0)->getAttribute('id')+1;
				
				// Ajouter le statut
				$statut = $statuts->appendChild($document->createElement('statut'));
				$statut->appendChild($document->createAttribute('id'))->value = $id;
				$statut->appendChild($document->createAttribute('nom'))->value = $_GET['nom'];
				$statut->appendChild($document->createAttribute('type'))->value = 'refuse';
				break;
				
			// Modifier le nom d'un statut
			case 'statut-modifier-nom':
				// Récupérer le statut
				$statut = $xpath->query('//inscription/listeStatuts/statut[@id='.$_GET['statut'].']')->item(0);
				
				// Modifier le nom du statut
				$statut->getAttributeNode('nom')->value = $_GET['nom'];
				break;
			
			// Modifier le type d'un statut
			case 'statut-modifier-type':
				// Récupérer le statut
				$statut = $xpath->query('//inscription/listeStatuts/statut[@id='.$_GET['statut'].']')->item(0);
				
				// Modifier le type du statut
				$type = $statut->getAttributeNode('type');
				$type->value = $type->value == 'accepte' ? 'refuse' : 'accepte';
				break;
			
			// Modifier les statuts suivant d'un statut
			case 'statut-modifier-suivants':
				// Récupérer le statut
				$statut = $xpath->query('//inscription/listeStatuts/statut[@id='.$_GET['statut'].']')->item(0);
				
				// Réinitialiser la liste des suivants
				if($statut->getElementsByTagName('listeSuivants')->length != 0) {
					$statut->removeChild($statut->getElementsByTagName('listeSuivants')->item(0));
				}
				$suivants = $statut->appendChild($document->createElement('listeSuivants'));
				
				// Ajouter les nouveaux statuts suivants
				foreach(array_map('trim',explode('-',$_GET['suivants'])) as $suivant) {
					if($suivant != '') {
						$suivants->appendChild($document->createElement('suivant'))->appendChild($document->createAttribute('refStatut'))->value = $suivant;
					}
				}
				break;
				
			// Supprimer un statut
			case 'statut-supprimer':
				// Récupérer le statut
				$statut = $xpath->query('//inscription/listeStatuts/statut[@id='.$_GET['statut'].']')->item(0);
				
				// Supprimer le statut
				$statut->parentNode->removeChild($statut);
				
				// Supprimer les statuts suivants
				foreach($xpath->query('//inscription/listeStatuts/statut/listeSuivants/suivant[@refStatut='.$_GET['statut'].']') as $suivant) {
					$suivant->parentNode->removeChild($suivant);
				}
				break;
				
			// Vider la liste des participants
			case 'participants-vider':
				// Supprimer tous les participants
				foreach($xpath->query('//inscription/listeParticipants/participant') as $participant) {
					$participant->parentNode->removeChild($participant);
				}
				break;
				
			// Télécharger la liste des équipes
			case 'telecharger':
				// Créer le document d'exportation
				$documentExport = new DOMDocument('1.0');
				
				// Ajouter le noeud racine
				$root = $documentExport->appendChild($documentExport->createElement('listeParticipants'));
				
				// Ajouter les participants
				foreach($xpath->query('//inscription/listeStatuts/statut[@type=\'accepte\']') as $statut) {
					foreach($xpath->query('//inscription/listeParticipants/participant[@refStatut='.$statut->getAttribute('id').']') as $participant) {
						$participantExport = $root->appendChild($documentExport->createElement('participant'));
						$participantExport->appendChild($documentExport->createAttribute('nom'))->value = $participant->getAttribute('nom');
						$participantExport->appendChild($documentExport->createAttribute('ville'))->value = $participant->getAttribute('ville');
						$participantExport->appendChild($documentExport->createAttribute('details'))->value = $participant->getAttribute('details');
					}
				}
				
				// Afficher le XML sur la sortie
				header('content-type: application/force-download');
				header('content-disposition: attachment; filename="Participants.xml"'); 
				echo $documentExport->saveXML();
				
				// Arrêter le script
				exit;
				break;
		}
		
		// Sauvegarder les modifications effectuées
		$document->save('donnees.xml');
		
		// Rediriger l'utilisateur sur la page d'organisation
		header('location:organisateurs.php');
		
		// Arrêter le script
		exit;
	}
}

// Rediriger l'utilisateur sur l'accueil
header('location:index.php');