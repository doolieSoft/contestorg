<?php

/**
 * Form submit
 */
class FormSubmit extends FormElement
{	
	// string text
	private $text;
	
	/**
	 * Constructor
	 * @param $name string name
	 * @param $text string text
	 * @param $attributes string attributes 
	 */
	public function __construct($name='submit',$text=null,$attributes=null)
	{
		// Call parent constructor
		parent::__construct($name,null,$attributes);
		
		// Set text
		$this->text = $text;
	}
	
	/**
	 * @see FormElement::validate($method)
	 */
	public function validate($method)
	{
		$this->isValid = $this->isSubmitted = $method == Form::METHOD_GET ? isset($_GET[$this->name]) : isset($_POST[$this->name]);
	}
	
	/**
	 * @see FormElement::__toString()
	 */
	public function __toString()
	{
		return $this->before.'<input type="submit" name="'.$this->name.'" '.($this->text != null ? 'value="'.$this->text.'" ' : '').($this->attributes != null ? $this->attributes.' ' : '').'/>'.$this->after;
	}
}