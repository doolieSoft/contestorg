<?php

class ActualitesController extends Controller
{
	
	// Action affichant le flux RSS des actualités
	public function rssAction() {
		// Récupérer la configuration
		$conf = Application::getService('conf');
		
		// Require de l'outil RSS
		require_once('rss.php');
		
		// Initialiser le flux RSS
		$rss = new RSS('Actualités de ContestOrg',$conf['MISC']['URL'],'Flux des actualités concernant le logiciel d\'organisation de tournois ContestOrg','en','120',time());
		
		// Ajouter l'image aux flux RSS
		$rss->setImage('Logo de ContestOrg',Request::getBaseUrl().'media/logo.png',$conf['MISC']['URL'],'Logo de ContestOrg');
		
		// Ajouter les items au flux RSS
		// TODO
		
		// Afficher le flux RSS
		echo $rss;
	}
	
}