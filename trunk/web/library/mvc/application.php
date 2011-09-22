<?php

/**
 * Application class
 */
class Application
{
	/**
	 * Run application
	 */
	public static function run()
	{		
		// Parse URL
		$request = Request::parseURL();

		// Check if request is valid
		if(!$request) {
			self::error('Invalid request.');
		}
		
		// Get configuration
		$configuration = Configuration::getInstance(
			$request->getModuleName(),
			$request->getControllerName(),
			$request->getActionName()
		);
		
		// Get filters names
		$filtersNames = $configuration->get(Configuration::FILTERS_DEFAULT);

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
		return isset(self::$ressources[$name]) ? self::$ressources[$name] : null;
	}

	/**
	 * Add a ressource
	 * @param $ressourceName string ressource name
	 * @param $ressource ressource
	 */
	public static function addRessource($ressourceName,$ressource)
	{
		self::$ressources[$ressourceName] = $ressource;
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
			Application::error('Service "'.$serviceName.'" is unknown.');
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