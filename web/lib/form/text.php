<?php

/**
 * Form text
 */
class FormText extends FormInput
{	
	// Types
	const TYPE_MONOLINE = 1;
	const TYPE_MULTILINE = 2;
	const TYPE_PASSWORD = 3;
	
	// bool type
	private $type;
	
	// int|array size of text input
	private $inputSize;
	
	// int max
	private $textSize;
	
	// string placeholder
	private $placeholder;
	
	/**
	 * Constructor
	 * @param $name string name
	 * @param $description string description
	 * @param $type int type
	 * @param $inputSize int|array size of text input : an integer if monoline, an array if multiline like array(cols,rows)
	 * @param $textSize int maximal number of caracters
	 * @param $init string init value 
	 * @param $placeholder string placeholder 
	 * @param $attributes string attributes
	 */
	public function __construct($name,$description=null,$type=FormText::TYPE_MONOLINE,$inputSize=null,$textSize=null,$init=null,$placeholder=null,$attributes=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$attributes,$init);
		
		// Set attributes
		$this->type = $type;
		$this->inputSize = $inputSize;
		$this->textSize = $textSize;
		$this->placeholder = $placeholder;
	}
	
	/**
	 * @see FormElement::validate($method)
	 */
	public function validate($method)
	{
		// Call parent validate method
		parent::validate($method);
		if(!$this->isValid) { return false; }
		
		// Additional check
		$this->isValid =  $this->textSize == null || strlen($this->getValue()) <= $this->textSize;
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
		// Input or textarea
		if($this->type == FormText::TYPE_MONOLINE || $this->type == FormText::TYPE_PASSWORD) { // Monoline
			return $this->before.'<input type="'.($this->type == FormText::TYPE_MONOLINE ? 'text' : 'password').'" '.
			              'name="'.$this->name.'" '.
			              ($this->getValue() != null ? 'value="'.htmlspecialchars($this->getValue()).'" ' : '').
			              ($this->inputSize != null ? 'size="'.$this->inputSize.'" ' : '').
			              ($this->textSize != null ? 'maxlength="'.$this->textSize.'" ' : '').
			              ($this->placeholder != null ? 'placeholder="'.$this->placeholder.'" ' : '').
			              ($this->attributes != null ? $this->attributes.' ' : '').'/>'.$this->after;
		} else { // Multiline
			return $this->before.'<textarea name="'.$this->name.'" '.
			                 ($this->inputSize != null ? 'cols="'.$this->inputSize[0].'" rows="'.$this->inputSize[1].'" ' : '').
			                 ($this->textSize != null ? 'onchange="if(this.value.length > '.$this->textSize.') this.value = this.value.substr(0,'.$this->textSize.');" ' : '').
			              	 ($this->placeholder != null ? 'placeholder="'.$this->placeholder.'" ' : '').
			                 ($this->attributes != null ? $this->attributes.' ' : '').'>'.
			       ($this->getValue() != null ? htmlspecialchars($this->getValue()) : '').
			       '</textarea>'.$this->after;
		}
	}
}