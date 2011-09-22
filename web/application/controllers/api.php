<?php

class ApiController extends Controller
{
	
	// Action retournant la version actuelle de l'application
	public function versionAction() {
		// TODO Retourner un XML décrivant la version courante de ContestOrg
		echo 'En construction...';
	}
	
	// Action enregistrant une erreur survenue dans l'application
	public function errorAction() {
		// TODO Enregistrer en base de données le fichier de log
		echo 'En construction...';
	}
	
}