<?php

/**
 * PHP tool to apply MVC pattern
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2011-05-19
 */

/**
 * Application class
 */
class Application
{
	// Paths
	private static $controllersPath;
	private static $viewsPath;
	private static $layoutsPath;
	private static $partialsPath;
	private static $helpersPath;
	private static $filtersPath;
	private static $servicesPath;
	
	// Get paths
	public static function getControllerPath($controllerName) { return self::$controllersPath.$controllerName.'.php'; }
	public static function getViewPath($controllerName,$actionName) { return self::$viewsPath.$controllerName.'/'.$actionName.'.phtml'; }
	public static function getLayoutPath($layoutName) { return self::$layoutsPath.$layoutName.'.phtml'; }
	public static function getPartialPath($partialName) { return self::$partialsPath.$partialName.'.phtml'; }
	public static function getHelperPath($helperName) { return self::$helpersPath.$helperName.'.php'; }
	public static function getFilterPath($filterName) { return self::$filtersPath.$filterName.'.php'; }
	public static function getServicePath($serviceName) { return self::$servicesPath.$serviceName.'.php'; }
	
	// Set paths
	public static function setControllersPath($path) { self::$controllersPath = $path; }
	public static function setViewsPath($path) { self::$viewsPath = $path; }
	public static function setLayoutsPath($path) { self::$layoutsPath = $path; }
	public static function setPartialsPath($path) { self::$partialsPath = $path; }
	public static function setHelpersPath($path) { self::$helpersPath = $path; }
	public static function setFiltersPath($path) { self::$filtersPath = $path; }
	public static function setServicesPath($path) { self::$servicesPath = $path; }
	
	// Layout and filter name
	private static $layoutName = null;
	private static $filterName = null;
	
	// Get layout and filter name
	public static function getLayoutName() { return self::$layoutName; }
	public static function getFilterName() { return self::$filterName; }
	
	// Set layout and filter name
	public static function setLayoutName($layoutName) { self::$layoutName = $layoutName; }
	public static function setFilterName($filterName) { self::$filterName = $filterName; }
	
	// Request
	private static $request = null;
	
	// Get request
	public static function getRequest() { return self::$request; }
		
	// Run application
	public static function run()
	{		
		$start = microtime(true);
		
		// Start output buffering
		ob_start();
		
		// Get request
		self::$request = new Request();
		
		// Check if request is valid
		if(!self::$request->isValid()) {
			self::error('Page not found.');
		}
		
		// Check if filter is set
		if(self::$filterName == null) {
			// Run controller
			Controller::run(self::$request->getAction(),self::$request->getController());
		} else {
			// Run filter
			Filter::run(self::$filterName);
		}
		
		// Stop started services
		self::stopServices();
		
		// Display performance
		/*
		ob_end_clean();
		echo 'Time spent to generate view : <b>',round(microtime(true)-$start,4),'s</b>';
		//*/
		
		// Empty buffer
		ob_end_flush();
	}
	
	// Ressources
	private static $ressources = array();
	public static function getRessource($name) { return isset(self::$ressources[$name]) ? self::$ressources[$name] : null; }
	public static function addRessource($name,$ressource) { self::$ressources[$name] = $ressource; }
	
	// Services
	private static $services = array();
	public static function getService($name)
	{
		// Check if service is started
		if(!isset(self::$services[$name])) {
			// Start service
			self::startService($name);
		}
		
		// Return service's ressource
		return self::$services[$name]->getRessource();
	}
	public static function startService($name) {
		// Get service path
		$path = self::getServicePath($name);
		
		// Check if service exists
		if(file_exists($path)) {
			// Require service
			require($path);
				
			// Create service
			self::$services[$name] = eval('return new '.ucfirst($name).'Service();');
			
			// Start service
			self::$services[$name]->start();
		} else {
			// Error
			Application::error('Service "'.$name.'" is unknown.');
		}
	}
	private static function stopServices()
	{
		// Stop all services
		$services = array_reverse(self::$services);
		foreach(self::$services as $service) {
			$service->stop();
		}
	} 
	
	// Error
	public static function error($message=null) {
		// Stop services
		self::stopServices();
		
		// Redirect on error page
		Request::redirect(Request::url('error','error',$message === null ? null : array('message' => $message)));
	}
}

/**
 * Request class
 */
class Request
{
	// Base url
	private static $baseUrl = '/';
	
	// Url pattern
	private static $patternParse = array('(:c(\/:a:p?)?\/?)?(\\?.*)?','\/:n\/:v');
	private static $patternBuild = array(':c/:a:p','/:n/:v');
	
	// Regular expression for element names and values
	private static $regexpName = '([a-zA-Z][a-zA-Z0-9-]*?)';
	private static $regexpValue = '([^\/\\\]+?)';
	
	// Default controller and action
	private static $defaultController = 'index';
	private static $defaultAction = 'index';
	
	// Methode to configure request
	public static function setBaseUrl($baseUrl) { self::$baseUrl = $baseUrl; }
	public static function setPattern($patternParse) { self::$patternParse = $patternParse; }
	public static function setDefaultController($controller) { self::$defaultController = $controller; }
	public static function setDefaultAction($action) { self::$defaultAction = $action; }
	
	// Controller, action and parameters
	private $controllerName;
	private $actionName;
	private $parameters;
	
	// Valid request ?
	private $isValid = true;
	
	// Constructor
	public function __construct()
	{
		// Controller, action and parameters position
		$controllerPos = strpos(self::$patternParse[0],':c');
		$actionPos = strpos(self::$patternParse[0],':a');
		$parametersPos = strpos(self::$patternParse[0],':p');
		
		// Controller, action and parameters regular expression
		$controllerRegexp = self::$regexpName;
		$actionRegexp = self::$regexpName;
		$parametersRegexp = '('.str_replace(array(':n',':v'),array(self::$regexpName,self::$regexpValue),self::$patternParse[1]).')*';
		
		// Blocs quantity in controller, action and parameters
		$controllerBlocs = substr_count($controllerRegexp,'(');
		$actionBlocs = substr_count($controllerRegexp,'(');
		$parametersBlocs = substr_count($controllerRegexp,'(');
		
		// Bloc number for controller, action and parameters
		$controllerNum = !$controllerPos ? 0 : substr_count(self::$patternParse[0],'(',0,$controllerPos) + ($actionPos < $controllerPos ? $actionBlocs : 0) + ($parametersPos < $controllerPos ? $parametersBlocs : 0);
		$actionNum = !$actionPos ? 0 : substr_count(self::$patternParse[0],'(',0,$actionPos) + ($controllerPos < $actionPos ? $controllerBlocs : 0) + ($parametersPos < $actionPos ? $parametersBlocs : 0);
		$parametersNum = !$parametersPos ? 0 : substr_count(self::$patternParse[0],'(',0,$parametersPos) + ($controllerPos < $parametersPos ? $controllerBlocs : 0) + ($actionPos < $parametersPos ? $actionBlocs : 0);
		
		// Prepare part one of pattern url
		self::$patternParse[0] = str_replace(array(':c',':a',':p'),array($controllerRegexp,$actionRegexp,$parametersRegexp),self::$patternParse[0]);
				
		// Get url
		$url = urldecode(substr($_SERVER['REQUEST_URI'],strlen(self::$baseUrl)));
		
		// Map url to extract controller, action and parameters
		if(preg_match('/^'.self::$patternParse[0].'$/',$url,$matches)) {
			// Get controller and action
			$this->controllerName = !empty($matches[1+$controllerNum]) ? $matches[1+$controllerNum] : self::$defaultController;
			$this->actionName = !empty($matches[1+$actionNum]) ? $matches[1+$actionNum] : self::$defaultAction;
			
			// Get parameters
			$this->parameters = array();
			if(isset($matches[1+$parametersNum])) {
				// Name and value parameters position
				$namePos = strpos(self::$patternParse[1],':n');
				$valuePos = strpos(self::$patternParse[1],':v');
				
				// Name and value parameters regular expression
				$nameRegexp = self::$regexpName;
				$valueRegexp = self::$regexpValue;
				
				// Blocs quantity in name and value parameters
				$nameBlocs = substr_count($nameRegexp,'(');
				$valueBlocs = substr_count($valueRegexp,'(');
		
				// Bloc number for name and value parameters
				$nameNum = !$namePos ? 0 : substr_count(self::$patternParse[1],'(',0,$namePos) + ($valuePos < $namePos ? $valueBlocs : 0);
				$valueNum = !$valuePos ? 0 : substr_count(self::$patternParse[1],'(',0,$valuePos) + ($namePos < $valuePos ? $nameBlocs : 0);
				
				// Prepare part two of pattern url
				self::$patternParse[1] = str_replace(array(':n',':v'),array($nameRegexp,$valueRegexp),self::$patternParse[1]);
				
				// Map url to extract parameters
				if($nbParameters = preg_match_all('/^'.self::$patternParse[1].'$/',$matches[1+$parametersNum],$matches)) {
					for($i=0;$i<$nbParameters;++$i) {
						$this->parameters[$matches[1+$nameNum][$i]] = $matches[1+$valueNum][$i];
					}
				}
			}
		} else {
			// Request is not valid
			$this->isValid = false;
		}
	}
	
	// Build url
	public static function url($controllerName=null,$actionName=null,$parameters=null,$reset=true)
	{
		// Default controller and action if necessary
		if($controllerName === null) { $controllerName = self::$defaultController; }
		if($actionName === null) { $actionName = self::$defaultAction; }
		
		// Add current parameters if necessary
		if(!$reset) {
			$parameters = $parameters != null ? array_merge(Application::getRequest()->getParameters(),$parameters) : Application::getRequest()->getParameters();
		}
		
		// Build parameters list
		if($parameters) {
			foreach($parameters as $name => $value) {
				$parameters[$name] = str_replace(array(':n',':v'),array($name,urlencode($value)),self::$patternBuild[1]);
			}
			$parameters = implode($parameters);
		} else {
			$parameters = null;
		}
		
		// Build url and return it
		return self::$baseUrl.str_replace(array(':c',':a',':p'),array($controllerName,$actionName,$parameters),self::$patternBuild[0]);
	}
	
	// Getters
	public function getController() { return $this->controllerName; }
	public function getAction() { return $this->actionName; }
	public function getParameter($name,$defaut=null) { return isset($this->parameters[$name]) ? $this->parameters[$name] : $defaut; }
	public function getParameters() { return $this->parameters; }
	public function isValid() { return $this->isValid; }
	
	// Get base url
	public static function getBaseUrl() { return self::$baseUrl; }
	
	// Redirect to another url
	public static function redirect($url) { header('Location: '.$url); exit; }
}

/**
 * Controller class
 */
abstract class Controller
{	
	// Allow inexistant actions ?
	private static $allowInexistantAction = true;
	public static function setAllowInexistantAction($value) { self::$allowInexistantAction = $value; }
	
	// Run a controller
	public static function run($actionName=null,$controllerName=null,$view=null)
	{
		// Get actionName and controllerName if necessary
		if($actionName === null) $actionName = Application::getRequest()->getAction();
		if($controllerName === null) $controllerName = Application::getRequest()->getController();
		
		// Get path view
		$viewPath = Application::getViewPath($controllerName,$actionName);
		
		// Construct view
		if($view == null) {
			if(Application::getLayoutName() != null) {
				$view = new Layout($viewPath,Application::getLayoutPath(Application::getLayoutName()));
			} else {
				$view = new View($viewPath);
			}
		}
		
		// Get controller path
		$controllerPath = Application::getControllerPath($controllerName);
		
		// Check if controller exists
		if(file_exists($controllerPath)) {			
			// Get controller definition
			if(!class_exists(ucfirst($controllerName).'Controller')) require($controllerPath);
			
			// Construct controller
			$controller = eval('return new '.ucfirst($controllerName).'Controller();');
			
			// Init controller
			$controller->init($view);
			
			// Check if action exists
			if(method_exists($controller,$actionName.'Action')) {				
				// Run action
				call_user_func(array($controller,$actionName.'Action'));
			} else if(self::$allowInexistantAction) {
				// Render view
				$view->render();
			} else {
				// Error
				Application::error('Action "'.$actionName.'" is unknown in controller "'.$controllerName.'".');
			}
		} else {
			// Error
			Application::error('Controller "'.$controllerName.'" is unknown.');
		}
	}
	
	// Current view
	protected $view;
	
	// Init controller
	protected function init(View $view)
	{
		$this->view = $view;
	}
	
	// Render current view or another
	protected function render($actionName=null)
	{
		// Change view
		if($actionName !== null) {
			$this->view->setPath(Application::getViewPath(Application::getRequest()->getController(),$actionName));
		}
		
		// Render view
		$this->view->render();
	}
	
	// Forward
	protected function forward($actionName=null,$controllerName=null)
	{
		// Define controller and action name if necessary
		if($actionName === null) $actionName = Application::getRequest()->getAction();
		if($controllerName === null) $controllerName = Application::getRequest()->getController();
		
		// Change view path
		$this->view->setPath(Application::getViewPath($controllerName, $actionName));
		
		// Run controller
		Controller::run($actionName,$controllerName,$this->view);
	}
}

/**
 * Filter class
 */
abstract class Filter
{
	
	/**
	 * Call a filter
	 */
	public static function run($name)
	{
		// Get filter path
		$path = Application::getFilterPath($name);
		
		// Check if filter exists
		if(file_exists($path)) {			
			// Get filter definition
			if(!class_exists(ucfirst($name).'Filter')) require($path);
			
			// Construct filter
			$filter = eval('return new '.ucfirst($name).'Filter();');
			
			// Filter
			$filter->filter();
		} else {
			// Error
			Application::error('Filter "'.$name.'" is unknown.');
		}
	}
	
}

/**
 * Service class
 */
abstract class Service
{
	/**
	 * Start service
	 */
	abstract public function start();
	
	/**
	 * Get service's ressource
	 */
	abstract public function getRessource();
	
	/**
	 * Stop service
	 */
	abstract public function stop();
}

/**
 * View class
 */
class View
{
	// "Hidden" data from template script
	protected $_hidden = array('viewPath' => null);
	
	// Constructor
	public function __construct($viewPath=null)
	{
		$this->_hidden['viewPath'] = $viewPath;
	}
	
	// Render view
	public function render($viewPath=null)
	{
		// Path to use
		$viewPath = $viewPath === null ? $this->_hidden['viewPath'] : $viewPath;
		
		// Check is file exists before require it
		if(file_exists($viewPath)) {
			require($viewPath);
		} else {
			Application::error('View "'.basename($viewPath).'" is unknown.');
		}
	}
	
	// Get view path
	public function getPath()
	{
		return $this->_hidden['viewPath'];
	}
	
	// Change view path
	public function setPath($viewPath)
	{
		$this->_hidden['viewPath'] = $viewPath;
	}
	
	// Magic getters and setters
	public function __get($name)
	{
		return isset($this->$name) ? $this->$name : null;
	}
	public function __set($name,$value)
	{
		if($name == '_hidden') { throw new Exception('Not accessible attribute.'); }
		$this->$name = $value;
	}
	
	// Magic method
	public function __call($helperName,$parameters)
	{
		// Get helper path
		$helperPath = Application::getHelperPath($helperName);
		
		// Check if controller exists
		if(file_exists($helperPath)) {
			// Get helper definition
			if(!class_exists(ucfirst($helperName).'Helper')) require($helperPath);
			
			// Construct helper
			$helper = eval('return new '.ucfirst($helperName).'Helper();');
			
			// Check if helper method exists
			if(method_exists($helper,$helperName)) {
				// Run helper
				return call_user_func_array(array($helper,$helperName),$parameters);
			} else {
				// Error
				Application::error('Helper "'.$helperName.'" is unknown.');
			}
		} else {
			// Error
			Application::error('Helper "'.$helperName.'" is unknown.');
		}
	}
	
	// Use partial view
	public function partial($partialName,$parameters=array())
	{
		// Get partial path
		$partialPath = Application::getPartialPath($partialName);
		
		// Check is file exists
		if(!file_exists($partialPath)) {
			Application::error('Partial "'.$partialName.'" is unknown.');
		}
		
		// Construct partial view
		$partial = new Partial($partialPath);
		
		// Set parameters to partial view
		$partial->view = $this;
		foreach($parameters as $name => $value) {
			$partial->$name = $value;
		}
		
		// Render partial view
		$partial->render();
	}
}

/**
 * Layout class
 */
class Layout extends View
{
	// View content
	private $content = null;
	
	// Constructor
	public function __construct($viewPath,$layoutPath)
	{
		$this->_hidden['viewPath'] = $viewPath;
		$this->_hidden['layoutPath'] = $layoutPath;
	}
	
	// Magic method
	public function __call($name,$parameters)
	{
		if($name == 'content') { $this->content(); }
		return parent::__call($name,$parameters);
	}
	
	// Get layout path
	public function getLayoutPath()
	{
		return $this->_hidden['layoutPath'];
	}
	
	// Render view
	public function render()
	{
		// Start output buffering
		ob_start();
		
		// Render view
		parent::render();
		
		// Put buffering into content
		$this->content = ob_get_clean();
		
		// Check is file exists before require it
		if(file_exists($this->_hidden['layoutPath'])) {
			require($this->_hidden['layoutPath']);
		} else {
			Application::error('Layout "'.basename($this->_hidden['layoutPath']).'" is unknown');
		}
	}
}

/**
 * Partial class
 */
class Partial extends View
{
	
}

// Configure application
Application::setControllersPath(dirname(__FILE__).'/controllers/');
Application::setViewsPath(dirname(__FILE__).'/views/scripts/');
Application::setLayoutsPath(dirname(__FILE__).'/views/layouts/');
Application::setPartialsPath(dirname(__FILE__).'/views/partials/');
Application::setHelpersPath(dirname(__FILE__).'/views/helpers/');
Application::setFiltersPath(dirname(__FILE__).'/filters/');
Application::setServicesPath(dirname(__FILE__).'/services/');
Application::setLayoutName('main');

// Define base url
Request::setBaseUrl(dirname($_SERVER['SCRIPT_NAME']).'/');