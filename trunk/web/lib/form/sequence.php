<?php

/**
 * Form sequence
 */
class FormSequence extends FormElement
{
	// string regular expression
	private $regexp;
	
	// callback form element provider
	private $callback;
	
	// array form elements
	private $elements = array();
	
	/**
	 * Constructor
	 * @param $name string name
	 * @param $regexp string regular expression
	 * @param $callback callback form element provider
	 */
	public function __construct($name,$regexp,$callback)
	{
		// Call parent constructor
		parent::__construct($name);
		
		// Set attributes
		$this->regexp = $regexp;
		$this->callback = $callback;
	}
	
	/**
	 * @see FormElement::validate()
	 */
	public function validate($method)
	{
		// Get matching names to regular expression 
		$names = array_filter(array_keys($method == Form::METHOD_GET ? $_GET : $_POST),array($this,'isMatchingName'));
		
		// If there are matching names to regular expression 
		if(count($names) != 0) {
			// Is valid/Is submitted
			$this->isValid = true;
			$this->isSubmitted = false;
			
			// Instanciate form elements
			foreach($names as $name) {
				// Get form element
				$element = call_user_func($this->callback,$name);
				
				// Save form element
				$this->elements[$name] = $element;
				
				// Validate form element
				$element->validate($method);
				
				// Is valid/Is submitted ?
				$this->isSubmitted = $this->isSubmitted || $element->isSubmitted();
				$this->isValid = $this->isValid &&  $element->isValid();
			}
		}
	}
	
	/**
	 * Is matching name to regular expression ?
	 * @param $name string name
	 * @return maching name to regular expression ?
	 */
	public function isMatchingName($name)
	{
		return preg_match($this->regexp, $name) == 1;
	}
	
	/**
	 * Get form elements
	 * @return array form elements
	 */
	public function getElements()
	{
		return $this->elements;
	}
	
	/**
	 * @see FormElement::__toString()
	 */
	public function __toString()
	{
		return '';
	}
}