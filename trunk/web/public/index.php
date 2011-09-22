<?php

// Forcer l'utilisation des cookies pour la session
ini_set('session.use_only_cookies', '1');

// Définir la racine
define('ROOT_DIR', realpath(dirname(__FILE__).'/..').'/');

// Ajouter des répertoires dans le chemin utilisé pour les includes et requires
set_include_path(implode(PATH_SEPARATOR, array(ROOT_DIR.'lib',ROOT_DIR.'app/models',get_include_path())));

// Inclure l'application
require('mvc/require.php');

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
		'(:controller(/:action(:parameters)?)?)?',
		array(
			':controller' => '[a-zA-Z][a-zA-Z0-9]*',
			':action' => '[a-zA-Z][a-zA-Z0-9]*',
			':parameters' => array(
				'/:name(/:value)?', array(
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

// Définir le filtre par défaut
Configuration::getInstance()->set(Configuration::FILTERS_DEFAULT,array('connected'));

// Démarrer les services de log et de session
Application::startService('log');
Application::startService('session');

// Démarrer l'application
Application::run();