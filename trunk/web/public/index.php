<?php

// Forcer l'utilisation des cookies pour la session
ini_set('session.use_only_cookies', '1');

// Définir la racine
define('ROOT_DIR', realpath(dirname(__FILE__).'/..').'/');

// Ajouter des répertoires dans le chemin utilisé pour les includes et requires
set_include_path(implode(PATH_SEPARATOR, array(ROOT_DIR.'lib/tools',ROOT_DIR.'lib/mvc/models',get_include_path())));

// Inclure l'application
require(ROOT_DIR.'lib/mvc/application.php');

// Retirer "public/" de l'url de base
Request::setBaseUrl(substr(Request::getBaseUrl(),0,-7));

// Démarrer les services de log et de session
Application::startService('log');
Application::startService('session');

// Transmettre les requetes au filtre de connexion
Application::setFilterName('connected');

// Démarrer l'application
Application::run();