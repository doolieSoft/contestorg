<?php

/**
 * Application class
 */
class Application
{
	// Modes
	const MODE_DEVELOPMENT = 1;
	const MODE_PRODUCTION = 2;

	/** @var $mode int mode */
	private static $mode;
	
	/**
	 * Run application
	 * @param $mode int mode
	 */
	public static function run($mode=self::MODE_DEVELOPMENT)
	{	
		// Save mode
		self::$mode = $mode;
		
		// Parse URL
		$request = Request::parseURL();

		// Check if request is valid
		if(!$request) {
			self::error('Invalid request.');
		}
		
		// Start session
		if (!session_start()) {
			self::error('Error while start session.');
		}
		
		// Import ressources
		foreach($_SESSION as $ressourceName => $ressourceValue) {
			self::$ressources[$ressourceName] = $ressourceValue;
		}
		
		// Get configuration
		$configuration = Configuration::getInstance(
			$request->getModuleName(),
			$request->getControllerName(),
			$request->getActionName()
		);
		
		// Get filters names
		$filtersNames = $configuration->get(Configuration::FILTERS_DEFAULT);
		
		// Start buffering
		ob_start();

		// Check if filters are defined
		if(!$filtersNames) {
			// Run controller
			Controller::run($request);
		} else {
			// Run filters
			Filter::run($request,$filtersNames);
		}

		// Stop services
		self::stopServices();
		
		// Flush buffering
		ob_end_flush();
	}

	/**
	 * Report an error
	 * @param $message string error message
	 */
	public static function error($message=null)
	{
		// Stop services
		self::stopServices();
		
		// Redirect on error page
		Request::redirect(Request::buildURL(null,'error','error',$message === null ? null : array('message' => $message)));
	}
	
	/**
	 * Get mode
	 * @return int mode
	 */
	public static function getMode()
	{
		return self::$mode;
	}

	// Ressources

	/** @var $ressources array ressources */
	private static $ressources = array();

	/**
	 * Get a ressource
	 * @param $ressourceName string ressource name
	 * @return ? ressource
	 */
	public static function getRessource($ressourceName)
	{
		return isset(self::$ressources[$ressourceName]) ? self::$ressources[$ressourceName] : null;
	}

	/**
	 * Add a ressource
	 * @param $ressourceName string ressource name
	 * @param $ressourceValue ? ressource value
	 * @param $persistant bool put ressource into session ?
	 */
	public static function addRessource($ressourceName,$ressourceValue,$persistant=false)
	{
		// Save ressource
		self::$ressources[$ressourceName] = $ressourceValue;
		
		// Put ressource into session if necessary
		if($persistant) {
			$_SESSION[$ressourceName] = $ressourceValue;
		}
	}
	
	/**
	 * Get ressources session id
	 * @return string session id
	 */
	public static function getRessourcesSessionId()
	{
		return session_id();
	}
	
	/**
	 * Set ressources session id
	 * @param $id string session id
	 */
	public static function setRessourcesSessionId($id)
	{
		session_id($id);
	}

	// Services

	/** @var $services array services */
	private static $services = array();

	/**
	 * Get a service
	 * @param $serviceName string service name
	 * @return Service service
	 */
	public static function getService($serviceName)
	{
		// Check if service is started
		if(!isset(self::$services[$serviceName])) {
			// Start service
			self::startService($serviceName);
		}

		// Return service's ressource
		return self::$services[$serviceName]->getRessource();
	}

	/**
	 * Start a service
	 * @param $serviceName string service name
	 */
	public static function startService($serviceName)
	{
		// Get service
		if($service = Service::init($serviceName)) {
			// Start service
			$service->start();
			
			// Save service
			self::$services[$serviceName] = $service;
		} else {
			// Error
			Application::error(Application::getMode() == Application::MODE_DEVELOPMENT ? 'Service "'.$serviceName.'" is unknown.' : 'Error 500');
		}
	}

	/**
	 * Stop services
	 */
	private static function stopServices()
	{
		// Stop services
		$services = array_reverse(self::$services);
		foreach($services as $service) {
			$service->stop();
			array_pop(self::$services);
		}
	}
}