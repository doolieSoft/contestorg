<?php

/**
 * Form select
 */
class FormSelect extends FormInput
{
	// array values
	private $values;
	
	// bool multiple select ?
	private $multiple;
	
	// bool select as list ?
	private $list;
	
	// int size of select input
	private $inputSize;
	
	/**
	 * Constructor
	 * @param $name string name
	 * @param $values array values
	 * @param $description string description
	 * @param $multiple bool multiple select ?
	 * @param $list bool select as list ?
	 * @param $inputSize int size of select input
	 * @param $init ?|array inital value(s)
	 * @param $attributes string attributes
	 */
	public function __construct($name,$values,$description=null,$multiple=false,$list=true,$inputSize=null,$init=null,$attributes=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$attributes,$init != null || !$multiple ? $init : array());
		
		// Set attributes
		$this->values = $values;
		$this->multiple = $multiple;
		$this->list = $list;
		$this->inputSize = $inputSize;
	}
	
	/**
	 * @see FormInput::validate($method)
	 */
	public function validate($method)
	{
		// Checkbox ?
		if($this->multiple && !$this->list) {
			// Submited
			$this->isSubmitted = true;
			
			// Selected values
			$selected = array(); $i = 0;
			foreach($this->values as $value => $description) {
				if($method == Form::METHOD_GET ?
				      isset($_GET[$this->name.'_'.$i]) && $_GET[$this->name.'_'.$i] == 1 :
				      isset($_POST[$this->name.'_'.$i]) && $_POST[$this->name.'_'.$i] == 1) {
					$selected[] = $value;
				}
				++$i;
			}		
			$this->isValid = $this->setValue($selected);
		} else {
			// Call parent validate method
			parent::validate($method);
		}
	}
	
	/**
	 * @see FormInput::setValue()
	 */
	public function setValue($value)
	{
		// Call parent setValue method
		if(parent::setValue($value)) {
			// List/Multiple ?
			if($this->list && $this->multiple) {
				// Check if value is an array
				if(!is_array($value)) {
					return false;
				}
				
				// Check if value are excepted values
				if(count(array_diff($value, array_keys($this->values))) != 0) {
					return false;
				}
			} else if(!$this->multiple) {
				// Check if value is a excepted value
				if(!in_array($value, array_keys($this->values))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * @see FormElement::__toString()
	 */
	public function __toString()
	{
		// Init string
		$string = '';
		
		// List or box
		if($this->list) { // List
			$string .= '<select name="'.$this->name.($this->multiple ? '[]' : '').'"'.
			                    ($this->multiple ? ' multiple="multiple" ' : '').
			                    ($this->inputSize != null ? ' size="'.$this->inputSize.'" ' : '').
			                    ($this->attributes != null ? ' '.$this->attributes : '').'>';
			foreach($this->values as $value => $description) {
				$string .= '<option value="'.htmlspecialchars($value).'" '.
				                    ($this->multiple && in_array($value,$this->getValue()) ||
				                     !$this->multiple && $this->getValue() == $value ?
				                     'selected="selected" ' : '').'>'.
				           $description.
				           '</option>';
			}
			$string .= '</select>';
		} else { // Box
			$string .= '<ul style="list-style:none;padding:0px;margin:0px;">';
			foreach($this->values as $value => $description) {
				$string .= '<li>';
				if($this->multiple) {
					$i = isset($i) ? $i+1 : 0;
					$string .= '<label><input type="checkbox" name="'.$this->name.'_'.$i.'" value="1" '.(in_array($value,$this->getValue()) ? 'checked="checked" ' : '').'/> '.$description.'</label>';
				} else {
					$string .= '<label><input type="radio" name="'.$this->name.'" value="'.htmlspecialchars($value).'" '.($this->getValue() == $value ? 'checked="checked" ' : '').'/> '.$description.'</label>';
				}
				$string .= '</li>';
			}
			$string .= '</ul>';
		}
		
		// Return string
		return $this->before.$string.$this->after;
	}
}