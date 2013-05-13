<?php

/**
 * Class to extend to become a form input
 */
abstract class FormInput extends FormElement
{
	// ? value
	protected $value;
	
	// array regular expression
	private $regexp = null;
	
	// FormInput input must have same value
	private $same = null;
	
	/**
	 * Constructor
	 * @param $name string name
	 * @param $description string description
	 * @param $attributes string attributes
	 * @param $init ? init value
	 */
	public function __construct($name,$description=null,$attributes=null,$init=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$attributes);
		
		// Value
		$this->value = $init;
	}
	
	/**
	 * Get value
	 * @return ? value
	 */
	public function getValue()
	{
		return $this->value;
	}
	
	/**
	 * Set value
	 * @param ? value
	 * @return bool valid value ?
	 */
	public function setValue($value)
	{
		// Set value
		$this->value = $value;
		
		// Check if value is valid
		return $this->regexp == null || preg_match($this->regexp,$this->value) != 0;
	}
	
	/**
	 * Set regexp
	 * @param $regexp string regular expression to valid value
	 */
	public function setRegexp($regexp)
	{
		$this->regexp = $regexp;
	}
	
	/**
	 * Set same
	 * @param $same FormInput input must have same value
	 */
	public function setSame(FormInput $same)
	{
		$this->same = $same;
	}
	
	/**
	 * See FormElement::isValid
	 */
	public function isValid()
	{
		return parent::isValid() && ($this->same == null || $this->value == $this->same->value);
	}
	
	/**
	 * See FormElement::validate($method)
	 */
	public function validate($method)
	{
		// Is submited ?
		if($method == Form::METHOD_GET) {
			$this->isSubmitted = isset($_GET[$this->name]) && $_GET[$this->name] !== '';
		} else {
			$this->isSubmitted = isset($_POST[$this->name]) && $_POST[$this->name] !== '';
		}
		
		// Set value/Is valid ?
		$this->isValid = $this->isSubmitted && $this->setValue($method == Form::METHOD_GET ? $_GET[$this->name] : $_POST[$this->name]);
	}
}