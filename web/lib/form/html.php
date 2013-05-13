<?php

/**
 * Form HTML
 */
class FormHTML extends FormElement
{
	// string html
	private $html;
	
	/**
	 * Constructor
	 * @param $html string html
	 */
	public function __construct($html)
	{
		// Call parent constructor
		parent::__construct(null,null,null);
		
		// Save HTML
		$this->html = $html;
	}
	
	/**
	 * @see FormElement::validate($method)
	 */
	public function validate($method)
	{
	}
	
	/**
	 * @see FormElement::__toString()
	 */
	public function __toString()
	{
		return $this->html;
	}
	
}