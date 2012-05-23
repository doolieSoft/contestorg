<?php

// Forcer l'utilisation des cookies pour la session
ini_set('session.use_only_cookies', '1');

// Définir la racine
define('ROOT_DIR', realpath(dirname(__FILE__).'/..').'/');

// Ajouter des répertoires dans le chemin utilisé pour les includes et requires
set_include_path(implode(PATH_SEPARATOR, array(ROOT_DIR.'lib',ROOT_DIR.'app/forms',get_include_path())));

// Récupérer la librairie MVC
require('mvc/require.php');

// Require des classes métier
require(ROOT_DIR.'app/models/requires.php');

// Définir les routes
Request::addRoute(new Route(
	array(
		':controller/:action:parameters',
		array(
			':parameters' => array(
				'/:name/:value'
			)
		)
	),
	array(
		'(:controller(/:action(:parameters)?)?)?/?',
		array(
			':controller' => '[a-zA-Z][a-zA-Z0-9]*',
			':action' => '[a-zA-Z][a-zA-Z0-9]*',
			':parameters' => array(
				'/?:name(/:value)?', array(
					':name' => '[a-zA-Z][a-zA-Z0-9]*',
					':value' => '[^\/]*'
				)
			)
		)
	)
));

// Définir le chemin de l'application
Configuration::getInstance()->set(Configuration::PATH_MODULE,ROOT_DIR.'app/');

// Définir le layout par défaut
Configuration::getInstance()->set(Configuration::LAYOUT_DEFAULT,'main');

// Démarrer le service de log
Application::startService('log');

// Récupérer la configuration
$conf = Application::getService('conf');

// Démarrer l'application
Application::run($conf['MISC']['DEV'] ? Application::MODE_DEVELOPMENT : Application::MODE_PRODUCTION);