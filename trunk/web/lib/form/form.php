<?php

/**
 * Form
 */
class Form
{	
	// Methods
	const METHOD_POST = 'post';
	const METHOD_GET = 'get';
	
	// string method
	private $method;
	
	// string action
	private $action;
	
	// array elements
	private $elements = array();
	
	// array required elements
	private $required = array();
	
	// array group elements
	private $group = array();
	
	// bool file input in elements ?
	private $file = false;
	
	// bool submited form ?
	private $isSubmitted = false;
	
	// bool valid form ?
	private $isValid = false;
	
	// string attributes
	private $attributes = null;
	
	// string callback to valid all elements
	private $callback = null;
	
	// IFormRenderer form renderer
	private $renderer = null;
	
	// string error message
	private $error = null;
	
	// string error messages start
	private static $errorStart = '<span style="color:red;">';
	
	// string error messages end
	private static $errorEnd = '</span>';
	
	// string message displayed when a element is not submited
	private static $msgRequired = 'Required';
	
	// string message displayed when a element is not valid
	private static $msgInvalid = 'Invalid';
	
	/**
	 * Constructor
	 * @param $method string method
	 * @param $action string action
	 * @param $callback string callback to valid element
	 * @param $renderer IFormRenderer form renderer
	 */
	public function __construct($method=Form::METHOD_POST,$action='#',$callback=null,IFormRenderer $renderer=null)
	{
		// Set attributes
		$this->method = $method;
		$this->action = $action;
		$this->callback = $callback;
		$this->renderer = $renderer === null ? new FormRendererTable() : $renderer;
	}

	/**
	 * Get form start
	 * @return string form start
	 */
	public function getStart()
	{
		return '<form method="'.$this->method.'" action="'.$this->action.'"'.($this->file ? ' enctype="multipart/form-data"' : '').($this->attributes != null ? ' '.$this->attributes : '').'>';
	}
	
	/**
	 * Get form end
	 * @return string form end
	 */
	public function getEnd()
	{
		return '</form>';
	}
	
	/**
	 * Return elements
	 * @return array elements
	 */
	public function getElements()
	{
		return $this->elements;
	}
	
	/**
	 * Get error for an element
	 * @param $element Form|FormElement element
	 * @return string error
	 */
	public function getError($element)
	{
		if($element instanceof Form) {
			if($this->error != null) {
				return self::$errorStart.$this->error.self::$errorEnd;
			}
			return null;
		}
		if($this->isSubmitted && !($element instanceof FormCaptcha)) {
			if($this->required[$element->getId()] && !$element->isSubmitted()) {
				return self::$errorStart.self::$msgRequired.self::$errorEnd;
			} else if($element->isSubmitted() && !$element->isValid()) {
				return self::$errorStart.($element->getError() != null ? $element->getError() : self::$msgInvalid).self::$errorEnd;
			}
		}
		return null;
	}
	
	/**
	 * Get element group
	 * @param $element FormElement element
	 * @return string|array element group
	 */
	public function getGroup(FormElement $element)
	{
		return isset($this->group[$element->getId()]) ? $this->group[$element->getId()] : null;
	}
	
	/**
	 * Set action
	 * @param $action string action
	 */
	public function setAction($action)
	{
		$this->action = $action;
	}
	
	/**
	 * Set callback
	 * @param $callback string callback
	 */
	public function setCallback($callback)
	{
		$this->callback = $callback;
	}
	
	/**
	 * Set renderer
	 * @param $renderer IFormRenderer form renderer
	 */
	public function setRenderer(IFormRenderer $renderer)
	{
		$this->renderer = $renderer;
	}
	
	/**
	 * Set error
	 * @param $error string error
	 */
	public function setError($error) 
	{
		$this->error = $error;
	}
	
	/**
	 * Add attributes
	 */
	public function addAttributes($attributes)
	{
		if($this->attributes == null) {
			$this->attributes = $attributes;
		} else {
			$this->attributes .= ' '.$attributes;
		}
	}
	
	/**
	 * Add an element
	 * @param $element FormElement element to add
	 * @param $required bool required element ?
	 * @param $group string|array element group
	 * @return FormElement element added
	 */
	public function add(FormElement $element,$required=true,$group=null)
	{
		// File input ?
		if($element instanceof FormFile) {
			$this->file = true;
		}
		
		// Add element, required and group to arrays
		$this->elements[$element->getId()] = $element;
		$this->required[$element->getId()] = $required;
		$this->group[$element->getId()] = is_null($group) || is_array($group) ? $group : array($group);
		
		// Return element
		return $element;
	}
	
	/**
	 * Add HTML content
	 * @param $html string HTML content
	 * @param $group string|array element group
	 */
	public function addHTML($html,$group=null)
	{
		$this->add(new FormHTML($html),false,$group);
	}
	
	/**
	 * Know if an element is required
	 * @param $element FormElement element
	 * @return bool element is required ?
	 */
	public function isRequired(FormElement $element)
	{
		return isset($this->group[$element->getId()]);
	}
	
	/**
	 * Know if form has been submited
	 * @return bool submited form ?
	 */
	public function isSubmitted()
	{
		return $this->isSubmitted;
	}
	
	/**
	 * Know if form is valid 
	 * @return bool valid form ?
	 */
	public function isValid()
	{
		return $this->valid;
	}
	
	/**
	 * Validate form
	 * @param $display bool display form if necessary ?
	 * @return bool true if form is valid
	 */
	public function validate($display=false)
	{
		// Init submited variable
		$submited = false;
		
		// Validate each button
		$nbButtons = 0;
		foreach($this->elements as $key => $element) {
			if($element instanceof FormSubmit) {
				$element->validate($this->method);
				$nbButtons++;
			}
		}
		
		// Check if there is buttons
		if($nbButtons != 0) {
			// Check if a button has been submited
			foreach($this->elements as $element) {
				$submited = $submited || $element instanceof FormSubmit && $element->isValid();
			}
		} else {
			$submited = true;
		}
		
		// Set submited attribute
		$this->isSubmitted = $submited;
		
		// Init valid variable
		$valid = $submited;
				
		// Check if all elements are submited and valid
		if($submited) {
			// Validate each elements
			foreach($this->elements as $element) {
				if(!($element instanceof FormSubmit)) {
					$element->validate($this->method);
				}
			}
			
			// Check if all elements are valid
			foreach($this->elements as $element) {
				$valid = ($element instanceof FormSubmit || $element->isSubmitted() && $element->isValid() || !$element->isSubmitted() && !$this->required[$element->getId()]) && $valid;
			}
			
			// Valid callback if necessary
			if($this->callback != null && $valid) {
				$valid = call_user_func_array($this->callback,array_merge($this->elements,array($this)));
			}
		}
		
		// Set valid attribute
		$this->valid = $valid;
		
		// Display form if necessary
		if($display && !$valid) {
			echo $this;
		}
		
		// Return bool
		return $valid;
	}

	/**
	 * Get an element by name
	 * @param $name string name
	 * @return FormElement element
	 */
	public function __get($name)
	{
		foreach($this->elements as $element) {
			if($element->getName() == $name) {
				return $element;
			}
		}
		return null;
	}
	
	/**
	 * To string
	 * @return string string form in HTML
	 */
	public function __toString()
	{
		// Render form
		return $this->renderer->renderForm($this);
	}
	
	/**
	 * Set error messages start
	 * @param $errorStart string error messages start
	 */
	public static function setErrorStart($errorStart)
	{
		self::$errorStart = $errorStart;
	}
	
	/**
	 * Set error messages start
	 * @param $errorStart string error messages start
	 */
	public static function setErrorEnd($errorEnd)
	{
		self::$errorEnd = $errorEnd;
	}
		
	/**
	 * Set message required
	 * @param $msg string message displayed when a element is not submited
	 */
	public static function setMsgRequired($msg)
	{
		self::$msgRequired = $msg;
	}
	
	/**
	 * Set message required
	 * @param $msg string message displayed when a element is not valid
	 */
	public static function setMsgInvalid($msg)
	{
		self::$msgInvalid = $msg;
	}
}