<?php

class UrlHelper
{
	public function url($parameters=null,$actionName=null,$controllerName=null,$reset=true)
	{
		// Get current action necessary
		if($actionName == null && $controllerName == null) {
			$actionName = Application::getRequest()->getAction();
		}
		
		// Get current controller if necessary
		if($controllerName == null) {
			$controllerName = Application::getRequest()->getController();
		}
		
		// Return url
		return Request::url($controllerName,$actionName,$parameters,$reset);
	}
}