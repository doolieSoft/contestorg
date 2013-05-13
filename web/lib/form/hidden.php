<?php

/**
 * Form hidden
 */
class FormHidden extends FormInput
{
	/**
	 * Constructor
	 * @param $name string name
	 */
	public function __construct($name,$value=null,$attributes=null)
	{
		// Call parent constructor
		parent::__construct($name,null,$attributes,$value);
	}
	
	/**
	 * @see FormInput::setValue($value)
	 */
	public function setValue($value)
	{
		// Check if value is a string
		return parent::setValue($value) && is_string($value);
	}
	
	/**
	 * @see FormElement::__toString()
	 */
	public function __toString()
	{
		return $this->before.'<input type="hidden" name="'.$this->name.'" value="'.htmlspecialchars($this->value).'" '.($this->attributes != null ? $this->attributes.' ' : '').'/>'.$this->after;
	}
}