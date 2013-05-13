<?php

/**
 * Form date
 */
class FormDate extends FormElement
{
	// int Timestamp
	private $timestamp;
	
	// FormSelect day
	private $selectDay;
	
	// FormSelect month
	private $selectMonth;
	
	// FormSelect year
	private $selectYear;
	
	// int Start timestamp
	private $start;
	
	// int End timestamp
	private $end;
	
	/**
	 * Constructor
	 * @param $name string name
	 */
	public function __construct($name,$description=null,$init=null,$start=null,$end=null,$attributes=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$attributes,$init);
		
		// Save start and end timestamps
		$this->start = $start;
		$this->end = $end;
		
		// Year from and to
		$yearFrom = $start === null ? 1930 : date('Y',$start);
		$yearTo = $end === null ? date('Y') : date('Y',$end);
		
		// Create selects
		$days = array('--');
		for($i=1;$i<=31;++$i) $days[$i] = str_pad($i,2,0,STR_PAD_LEFT); 
		$this->selectDay = new FormSelect($name.'_day',$days,null,false,true,null,$init !== null ? date('j',$init) : null);
		$months = array('--');
		for($i=1;$i<=12;++$i) $months[$i] = str_pad($i,2,0,STR_PAD_LEFT); 
		$this->selectMonth = new FormSelect($name.'_month',$months,null,false,true,null,$init !== null ? date('n',$init) : null);
		$years = array('----');
		for($i=$yearFrom;$i<=$yearTo;++$i) $years[$i] = $i; 
		$this->selectYear = new FormSelect($name.'_year',$years,null,false,true,null,$init !== null ? date('Y',$init) : null);
	}
	
	/**
	 * @see FormElement::validate($method)
	 */
	public function validate($method)
	{	
		// Validate selects
		$this->selectDay->validate($method);
		$this->selectMonth->validate($method);
		$this->selectYear->validate($method);
		
		// Is submited ?
		$this->isSubmitted =
			$this->selectDay->isSubmitted() && $this->selectDay->getValue() != 0 &&
			$this->selectMonth->isSubmitted() && $this->selectMonth->getValue() != 0 &&
			$this->selectYear->isSubmitted() && $this->selectYear->getValue() != 0;
		if(!$this->isSubmitted) { return false; }
		
		// Check selects
		$this->isValid =  $this->selectDay->isValid() && $this->selectMonth->isValid() && $this->selectYear->isValid();
		if(!$this->isValid) { return false; }

		// Check date
		$this->isValid = checkdate($this->selectMonth->getValue(),$this->selectDay->getValue(),$this->selectYear->getValue());
		if(!$this->isValid) { return false; }
		
		// Get timestamp
		$this->timestamp = mktime(0,0,0,$this->selectMonth->getValue(),$this->selectDay->getValue(),$this->selectYear->getValue());
		
		// Check period
		if($this->start !== null || $this->end !== null) {
			// Check timestamp
			$this->isValid =  ($this->start === null || $this->start >= $this->timestamp) &&
			                  ($this->end === null || $this->timestamp >= $this->end);
		}
	}
	
	/**
	 * Get timestamp
	 * @return int timestamp
	 */
	public function getTimestamp()
	{
		return $this->timestamp;
	}
	
	/**
	 * @see FormElement::__toString()
	 */
	public function __toString()
	{
		return $this->before.$this->selectDay->__toString().' / '.$this->selectMonth->__toString().' / '.$this->selectYear->__toString().$this->after;
	}
}