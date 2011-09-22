<?php

/**
 * Controller class
 */
abstract class Controller
{
	/**
	 * Run a controller
	 * @param $request Request request
	 * @param $view View view
	 */
	public static function run(Request $request,$view=null)
	{
		// Get configuration
		$configuration = Configuration::getInstance(
			$request->getModuleName(),
			$request->getControllerName(),
			$request->getActionName()
		);

		// Get controller/action name
		$controllerName = $request->getControllerName();
		$actionName = $request->getActionName();

		// Construct view
		if($view == null) {
			$view = new View();
		}

		// Get controller path
		$controllerPath = $configuration->getControllerPath($controllerName);
		
		// Check if controller exists
		if(file_exists($controllerPath)) {
			// Require controller
			if(!class_exists(ucfirst($controllerName).'Controller')) require($controllerPath);
				
			// Construct controller
			$controller = eval('return new '.ucfirst($controllerName).'Controller();');

			// Send request and view to controller
			$controller->request = $request;
			$controller->view = $view;

			// Check if action exists
			if(method_exists($controller,$actionName.'Action')) {
				// Run action
				call_user_func(array($controller,$actionName.'Action'));
			} elseif($view != null) {
				// Get view path
				$viewPath = $configuration->getViewPath($controllerName, $actionName);
				
				// Get layout name
				$layoutName = $configuration->get(Configuration::LAYOUT_DEFAULT);
				
				// Check if a layout is defined
				if($layoutName == null) {
					// Render view
					$view->render($request,$viewPath);
				} else {
					// Get layout path
					$layoutPath = $configuration->getLayoutPath($layoutName);
				
					// Construct layout
					$layout = new Layout($view);
				
					// Render layout
					$layout->render($request,$layoutPath,$viewPath);
				}
			} else {
				// Error
				Application::error('Action "'.$actionName.'" does not exist in controller "'.$controllerName.'".');
			}
		} elseif($view != null) {
			// Get view path
			$viewPath = $configuration->getViewPath($controllerName, $actionName);
			
			// Get layout name
			$layoutName = $configuration->get(Configuration::LAYOUT_DEFAULT);
			
			// Check if a layout is defined
			if($layoutName == null) {
				// Render view
				$view->render($request,$viewPath);
			} else {
				// Get layout path
				$layoutPath = $configuration->getLayoutPath($layoutName);
			
				// Construct layout
				$layout = new Layout($view);
			
				// Render layout
				$layout->render($request,$layoutPath,$viewPath);
			}
		} else {
			// Error
			Application::error('Controller "'.$controllerName.'" does not exist.');
		}
	}

	/** @var $view View view */
	protected $view;

	/** @var $request Request request */
	protected $request;

	/**
	 * Render view
	 * @param $actionName string actionName
	 * @param $controllerName string controllerName
	 * @param $moduleName string moduleName
	 * @param $layoutName string layoutName
	 */
	protected function render($actionName=null,$controllerName=null,$moduleName=null,$layoutName=null,$return=false)
	{

		// Get module/controller/action name
		if($moduleName == null) {
			$moduleName = $this->request->getModuleName();
			if($controllerName == null) {
				$controllerName = $this->request->getControllerName();
				if($actionName == null) {
					$actionName = $this->request->getActionName();
				}
			}
		}

		// Get configuration
		$configuration = Configuration::getInstance($moduleName,$controllerName,$actionName);

		// Get view path
		$viewPath = $configuration->getViewPath($controllerName, $actionName);

		// Get layout name
		if($layoutName !== false && $layoutName === null) {
			$layoutName = $configuration->get(Configuration::LAYOUT_DEFAULT);
		}
		
		// Start buffering
		if($return) {
			ob_start();
		}

		// Check if a layout is defined
		if($layoutName === false || $layoutName === null) {
			// Render view
			$this->view->render($this->request,$viewPath);
		} else {
			// Get layout path
			$layoutPath = $configuration->getLayoutPath($layoutName);
				
			// Construct layout
			$layout = new Layout($this->view);
				
			// Render layout
			$layout->render($this->request,$layoutPath,$viewPath);
		}
		
		// Stop buffering
		if($return) {
			return ob_get_clean();
		}
	}

	/**
	 * Forward request
	 * @param $actionName string actionName
	 * @param $controllerName string controllerName
	 * @param $moduleName string moduleName
	 */
	protected function forward($actionName=null,$controllerName=null,$moduleName=null)
	{
		// Redefine request module/controller/action
		if($actionName != null) {
			$this->request->setActionName($actionName);
			if($controllerName != null) {
				$this->request->setControllerName($controllerName);
				if($moduleName != null) {
					$this->request->setModuleName($moduleName);
				}
			}
		}

		// Run controller
		Controller::run($this->request,$this->view);
	}
}