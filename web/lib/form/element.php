<?php

/**
 * Class to extend to become a form element
 */
abstract class FormElement
{
	// int id counter
	private static $counter = 0;
	
	// int id
	private $id;
	
	// string name
	protected $name;
	
	// string description
	protected $description;
	
	// string error message
	private $error = null;
	
	// string attributes
	protected $attributes;
	
	// string before
	protected $before = '';
	
	// string after
	protected $after = '';
	
	// bool submited element ?
	protected $isSubmitted = false;
	
	// bool valid element ?
	protected $isValid = false;
	
	// string callback
	private $callback = null;
	
	// array parameters
	private $parameters = array();
	
	/**
	 * Constructor
	 * @param $name string name
	 * @param $description string description
	 * @param $attributes string attributes
	 */
	public function __construct($name,$description=null,$attributes=null)
	{
		// Set attributes
		$this->id = self::$counter++;
		$this->name = $name;
		$this->description = $description;
		$this->attributes = $attributes;
	}
	
	/**
	 * Get id
	 * return int id
	 */
	public function getId()
	{
		return $this->id;
	}
	
	/**
	 * Get description
	 * @return string description
	 */
	public function getDescription()
	{
		return $this->description;
	}
	
	/**
	 * Know if element is submited
	 * @return bool submited element ?
	 */
	public function isSubmitted()
	{
		return $this->isValid || $this->isSubmitted;
	}
	
	/**
	 * Get error message
	 * @return string error message
	 */
	public function getError()
	{
		return $this->error;
	}
	
	/**
	 * Get element name
	 * @return string name
	 */
	public function getName()
	{
		return $this->name;
	}
	
	/**
	 * Set error message
	 * @param string $error error message
	 */
	public function setError($error) 
	{
		$this->isSubmitted = true;
		$this->isValid = false;
		$this->error = $error;
	}
	
	/**
	 * Know if element is valid
	 * @return bool valid element ?
	 */
	public function isValid()
	{
		return $this->isValid = $this->isValid && $this->checkCallback();
	}
	
	/**
	 * Set callback to valid value
	 * @param $callback string callback
	 * @param $parameters array parameters
	 */
	public function setCallback($callback,$parameters=array())
	{
		$this->callback = $callback;
		$this->parameters = $parameters;
	}
	
	/**
	 * Call callback to valid value
	 * @return bool value valided by callback ?
	 */
	private function checkCallback()
	{
		if($this->callback != null) {
			if(!call_user_func_array($this->callback,array_merge(array($this),$this->parameters))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Validate element
	 * @param string method used by form
	 */
	abstract public function validate($method);
	
	/**
	 * Add attributes
	 */
	public function addAttributes($attributes)
	{
		if($this->attributes == null) { $this->attributes = ''; } else { $this->attributes .= ' '; }
		$this->attributes .= $attributes;
	}
	
	/**
	 * Set enclose
	 */
	public function setEnclose($before,$after='')
	{
		$this->before = $before;
		$this->after = $after;
	}
	
	/**
	 * To string
	 */
	abstract public function __toString();
}