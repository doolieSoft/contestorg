<?php

/**
 * PHP tool to construct forms
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2012-06-23
 */

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
	
	// string error messages start
	private static $errorStart = '<span style="color:red;">';
	
	// string error messages end
	private static $errorEnd = '</span>';
	
	// string message displayed when a element is not submited
	private static $msgRequired = 'Required';
	
	// string message displayed when a element is not valid
	private static $msgInvalid = 'Invalid';
	
	// string extra
	private $extra = null;
	
	// string callback to valid all elements
	private $callback = null;
	
	// array parameters
	private $parameters = array();
	
	/**
	 * Constructor
	 * @param $method string method
	 * @param $action string action
	 * @param $callback string callback to valid element
	 */
	public function __construct($method=Form::METHOD_POST,$action='#',$callback=null)
	{
		// Set attributes
		$this->method = $method;
		$this->action = $action;
		$this->callback = $callback;
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
		$this->file = $this->file || $element instanceof FormFile;
		
		// Add element, required and group to arrays
		$this->elements[$element->getId()] = $element;
		$this->required[$element->getId()] = $required;
		$this->group[$element->getId()] = is_null($group) || is_array($group) ? $group : array($group);
		
		// Return element
		return $element;
	}
	
	/**
	 * Add html
	 * @param $html string html
	 * @param $group string|array element group
	 */
	public function addHTML($html,$group=null)
	{
		$this->add(new FormHTML($html),false,$group);
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
			if($this->callback != null) {
				$valid = $valid && call_user_func_array($this->callback,array_merge($this->elements,$this->parameters));				
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
	 * Set callback
	 * @param $callback string callback
	 */
	public function setCallback($callback,$parameters=array())
	{
		$this->callback = $callback;
		$this->parameters = $parameters;
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
	
	/**
	 * Add extra
	 */
	public function addExtra($extra) {
		if($this->extra == null) { $this->extra = ''; } else { $this->extra .= ' '; }
		$this->extra .= $extra;
	}
	
	/**
	 * Set action
	 * @param $action string action
	 */
	public function setAction($action) {
		$this->action = $action;
	}
	
	/**
	 * Get form start 
	 * @return string form start
	 */
	public function start()
	{		
		return '<form method="'.$this->method.'" action="'.$this->action.'"'.($this->file ? ' enctype="multipart/form-data"' : '').($this->extra != null ? ' '.$this->extra : '').'>';
	}
	
	/**
	 * Get form end
	 * @return string form end
	 */
	public function end()
	{
		return '</form>';
	}
	
	/**
	 * Display a group
	 * @param string $groupName
	 * @param array $groupElements
	 */
	private function displayGroup($groupName,$groupElements)
	{
		// Init string
		$string = '';
		
		// Fieldset
		if(is_string($groupName)) {
			$string .= '<fieldset><legend>'.$groupName.'</legend>'; 
		}
		
		// Table indicator
		$openedTable = false;
		           
		// Display group elements
		foreach($groupElements as $key => $element) {
			if(is_array($element)) {
				// Close table
				if($openedTable) {
					$string .= '</table>'."\n";
					$openedTable = false;
				}
				
				// Display group
				$string .= $this->displayGroup($key,$element);
			} else {
				// Open table
				if(!$openedTable) {
					$string .= '<table style="border-collapse:collapse;">'."\n".
					           (!is_string($groupName) ? '<tr>'."\n" : '');
					$openedTable = true;
				}
				
				// Display element
				$string .= $this->displayElement($element,is_string($groupName));
			}
		}
		
		// Close table
		if($openedTable) {
			$string .= (!is_string($groupName) ? '</tr>'."\n" : '').
					   '</table>'."\n";
		}
		
		// Fieldset
		if(is_string($groupName)) {
			$string .= '</fieldset>'; 
		}
		
		// Return string
		return $string;
	}
	
	/**
	 * Display an element
	 * @param FormElement $element element
	 * @param bool $tr
	 */
	private function displayElement(FormElement $element,$tr=true)
	{		
		// Init string
		$string = '';
		
		if($element instanceof FormHidden || $element instanceof FormSubmit) {
			$string .= $element->__toString();
		} else {
			// Get error
			$msg = $this->getError($element);
			if($msg != null) {
				$msg = ' '.$msg;
			}
			
			// Display element
			$string .= ($tr ? '<tr>' : '').
			             ($element->getDescription() != null ? '<td style="vertical-align:top;">'.$element->getDescription().' :</td>' : '').
			             '<td'.($element->getDescription() == null ? ' colspan="2"' : '' ).'>'.$element->__toString().$msg.'</td>'.
			           ($tr ? '</tr>' : '')."\n";
		}
		
		// Return string
		return $string;
	}
	
	/**
	 * Get error for an element
	 * @param $element FormElement element
	 * @return string error
	 */
	public function getError(FormElement $element)
	{
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
	 * Get an element by name
	 * @param FormElement element
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
	
	// To string
	public function __toString()
	{
		// Form start
		$string = $this->start()."\n";
		
		// Display hidden elements
		foreach($this->elements as $element) {
			if($element instanceof FormHidden) {
				$string .= $this->displayElement($element);
			}
		}
		
		// Group elements
		$groups = array();
		foreach($this->elements as $element) {
			// Get element group
			$elementGroup = $this->group[$element->getId()];
			
			// Add element to group
			if(!($element instanceof FormHidden) && !($element instanceof FormSubmit) && $elementGroup !== null) {
				$group =& $groups;
				while(count($elementGroup)) {
					$groupName = array_shift($elementGroup);
					if(!isset($group[$groupName])) {
						$group[$groupName] = array();
					}
					$group =& $group[$groupName];
				}
				$group[] = $element;
			}
		}
		
		// Display elements
		$doneGroups = array(); $openedTable = false;
		foreach($this->elements as $element) {
			if(!($element instanceof FormHidden) && !($element instanceof FormSubmit)) {
				// Get element group
				$elementGroup = $this->group[$element->getId()];
				
				// Display element or group
				if($elementGroup === null) {
					// Open table
					if(!$openedTable) {
						$string .= '<table>'."\n";
						$openedTable = true;
					}
					
					// Display element
					$string .= $this->displayElement($element);
				} else {
					// Get group name
					$groupName = array_shift($elementGroup);
					
					
					if(!in_array($groupName,$doneGroups)) {
						// Close table
						if($openedTable) {
							$string .= '</table>'."\n";
							$openedTable = false;
						}
						
						// Display group
						$string .= $this->displayGroup($groupName,$groups[$groupName])."\n";
						           
						// Group is done
						$doneGroups[] = $groupName;
					}
				}
			}
		}
		if($openedTable) {
			$string .= '</table>'."\n";
		}
		
		// Display submit elements
		foreach($this->elements as $element) {
			if($element instanceof FormSubmit) {
				$string .= $this->displayElement($element);
			}
		}
		
		// Form end
		$string .= $this->end()."\n";
		
		// Return string
		return $string;
	}
}

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
	
	// string extra
	protected $extra;
	
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
	 * @param $extra string extra
	 */
	public function __construct($name,$description=null,$extra=null)
	{
		// Set attributes
		$this->id = self::$counter++;
		$this->name = $name;
		$this->description = $description;
		$this->extra = $extra;
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
	 * Add extra
	 */
	public function addExtra($extra) {
		if($this->extra == null) { $this->extra = ''; } else { $this->extra .= ' '; }
		$this->extra .= $extra;
	}
	
	/**
	 * Set enclose
	 */
	public function setEnclose($before,$after='') {
		$this->before = $before;
		$this->after = $after;
	}
	
	/**
	 * To string
	 */
	abstract public function __toString();
}

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
	 * @param $extra string extra
	 * @param $init ? init value
	 */
	public function __construct($name,$description=null,$extra=null,$init=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$extra);
		
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
	protected function setValue($value)
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
		$this->isValid = ($this->isSubmitted = $method == Form::METHOD_GET ? isset($_GET[$this->name]) : isset($_POST[$this->name])) &&	// Is submited ?
		               	 ($this->setValue($method == Form::METHOD_GET ? $_GET[$this->name] : $_POST[$this->name]));						// Set value
	}
}

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
	 * @param $extra string extra 
	 */
	public function __construct($name='submit',$text=null,$extra=null)
	{
		// Call parent constructor
		parent::__construct($name,null,$extra);
		
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
		return $this->before.'<input type="submit" name="'.$this->name.'" '.($this->text != null ? 'value="'.$this->text.'" ' : '').($this->extra != null ? $this->extra.' ' : '').'/>'.$this->after;
	}
}

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
	
	// string reset
	private $reset;
	
	/**
	 * Constructor
	 * @param $name string name
	 * @param $description string description
	 * @param $type int type
	 * @param $inputSize int|array size of text input : an integer if monoline, an array if multiline like array(cols,rows)
	 * @param $textSize int maximal number of caracters
	 * @param $init string init value 
	 * @param $reset string reset value 
	 * @param $extra string extra
	 */
	public function __construct($name,$description=null,$type=FormText::TYPE_MONOLINE,$inputSize=null,$textSize=null,$init=null,$reset=null,$extra=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$extra,$init);
		
		// Set attributes
		$this->type = $type;
		$this->inputSize = $inputSize;
		$this->textSize = $textSize;
		$this->reset = $reset;
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
		$this->isValid =  ($this->reset == null || $this->getValue() != $this->reset) &&
		                  ($this->textSize == null || strlen($this->getValue()) <= $this->textSize);
		               
	}
	
	/**
	 * @see FormInput::setValue($value)
	 */
	protected function setValue($value)
	{
		// Operate on value
		$value = trim(get_magic_quotes_gpc() ? stripslashes($value) : $value);
		
		// Check if value is empty
		if($value == '') {
			// Consider input as not submited
			$this->isSubmitted = false;
			
			// Return false
			return false;
		}
		
		// Call parent setValue
		return parent::setValue($value);
	}
	
	/**
	 * @see FormElement::__toString()
	 */
	public function __toString()
	{
		// Reset
		$reset = ($this->reset == null ? '' : 'onfocus="if(this.value == \''.str_replace(array('\'','\\'),array('\\\'','\\\\'),$this->reset).'\') this.value = \'\';" '.
			                                  'onblur="if(this.value == \'\') this.value=\''.str_replace(array('\'','\\'),array('\\\'','\\\\'),$this->reset).'\';" ');
		
		// Input or textarea
		if($this->type == FormText::TYPE_MONOLINE || $this->type == FormText::TYPE_PASSWORD) { // Monoline
			return $this->before.'<input type="'.($this->type == FormText::TYPE_MONOLINE ? 'text' : 'password').'" '.
			              'name="'.$this->name.'" '.
			              ($this->getValue() != null ? 'value="'.htmlspecialchars($this->getValue()).'" ' : '').
			              ($this->inputSize != null ? 'size="'.$this->inputSize.'" ' : '').
			              ($this->textSize != null ? 'maxlength="'.$this->textSize.'" ' : '').
			              $reset.
			              ($this->extra != null ? $this->extra.' ' : '').'/>'.$this->after;
		} else { // Multiline
			return $this->before.'<textarea name="'.$this->name.'" '.
			                 ($this->inputSize != null ? 'cols="'.$this->inputSize[0].'" rows="'.$this->inputSize[1].'" ' : '').
			                 ($this->textSize != null ? 'onchange="if(this.value.length > '.$this->textSize.') this.value = this.value.substr(0,'.$this->textSize.');" ' : '').
			                 $reset.
			                 ($this->extra != null ? $this->extra.' ' : '').'>'.
			       ($this->getValue() != null ? htmlspecialchars($this->getValue()) : '').
			       '</textarea>'.$this->after;
		}
	}
}

/**
 * Form hidden
 */
class FormHidden extends FormInput
{
	/**
	 * Constructor
	 * @param $name string name
	 */
	public function __construct($name,$value=null,$extra=null)
	{
		// Call parent constructor
		parent::__construct($name,null,$extra,$value);
	}
	
	/**
	 * @see FormInput::setValue($value)
	 */
	protected function setValue($value)
	{
		// Call parent setValue
		return parent::setValue(trim(get_magic_quotes_gpc() ? stripslashes($value) : $value));
	}
	
	/**
	 * @see FormElement::__toString()
	 */
	public function __toString()
	{
		return $this->before.'<input type="hidden" name="'.$this->name.'" value="'.htmlspecialchars($this->value).'" '.($this->extra != null ? $this->extra.' ' : '').'/>'.$this->after;
	}
}

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
	public function __construct($name,$description=null,$init=null,$start=null,$end=null,$extra=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$extra,$init);
		
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
	public function getTimestamp() {
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
	 * @param $extra string extra
	 */
	public function __construct($name,$values,$description=null,$multiple=false,$list=true,$inputSize=null,$init=null,$extra=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$extra,$init != null || !$multiple ? $init : array());
		
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
		// Redifine validate or not
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
			                    ($this->extra != null ? ' '.$this->extra : '').'>';
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

/**
 * Form file
 */
class FormFile extends FormElement
{
	// string path
	protected $path = null;
	
	// int size of file input
	private $inputSize;
	
	// int maximal size of file in bytes
	private $fileSize;
	
	// array valid extensions
	private $extensions;
	
	// array valid mime types
	private $mimetypes;
	
	// int error code
	protected $errorCode = null;
	
	// bool saved ?
	protected $saved = false;
	
	// Error codes
	const ERROR_MISSING		= 1;
	const ERROR_EXTENSION	= 2;
	const ERROR_MIMETYPE	= 3;
	const ERROR_SIZE		= 4;
		
	/**
	 * Constructor
	 * @param $name string name
	 * @param $description string description
	 * @param $inputSize int size of file input
	 * @param $fileSize int maximal size of file in bytes
	 * @param $extensions array valid extensions
	 * @param $mimetypes array valid mime types
	 * @param $extra string extra
	 */
	public function __construct($name,$description=null,$inputSize=null,$fileSize=null,$extensions=null,$mimetypes=null,$extra=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$extra);
		
		// Set attributes
		$this->inputSize = $inputSize;
		$this->fileSize = $fileSize;
		$this->extensions = $extensions;
		$this->mimestypes = $mimetypes;
	}
	
	/**
	 * Get file name
	 * @return string file name
	 */
	public function getFileName()
	{
		return $this->isSubmitted ? $_FILES[$this->name]['name'] : null;
	}
	
	/**
	 * Get mime type
	 * @return string mime type
	 */
	public function getMimeType()
	{
		return $this->isSubmitted && function_exists('finfo_file') ? strtolower(finfo_file(finfo_open(FILEINFO_MIME_TYPE),$this->path)) : null;
	}
	
	/**
	 * Get extension
	 * @return string extension
	 */
	public function getExtension()
	{
		return $this->isSubmitted ? strtolower(pathinfo($_FILES[$this->name]['name'], PATHINFO_EXTENSION)) : null;
	}
	
	/**
	 * Get file size
	 * @return int size in bytes
	 */
	public function getSize()
	{
		return $this->isSubmitted ? $_FILES[$this->name]['size'] : null;
	}
	
	/**
	 * Get error code
	 * @return int error code
	 */
	public function getErrorCode()
	{
		return $this->errorCode;
	}
	
	/**
	 * Get file path
	 * @return string file path
	 */
	public function getPath()
	{
		return $this->path;
	}
	
	/**
	 * Move file
	 * @param $path string destination, ending by /
	 * @param $name string name, without extension 
	 * @param $overwrite bool overwrite if a file exists
	 * @return bool|string false if unsuccessful operation, else file path
	 */
	public function moveTo($path,$name=null,$overwrite=false)
	{
		// Complete path
		$path .= $name != null ? $name.'.'.$this->getExtension() : $this->getFileName();
		
		// Check if a file exists
		if(!$overwrite && file_exists($path)) {
			return false;
		}
		
		// Move file
		if($this->saved ? !rename($this->path,$path) : !move_uploaded_file($this->path,$path)) {
			return false;
		}
		
		// Updated attributes
		$this->path = $path;
		$this->saved = true;
		
		// Return file path
		return $path;
	}
	
	/**
	 * Copy file
	 * @param $path string destination, ending by /
	 * @param $name string name, without extension 
	 * @param $overwrite bool overwrite if a file exists
	 * @return bool|string false if unsuccessful operation, else file path
	 */
	public function copyTo($path,$name=null,$overwrite=false)
	{
		// Complete path
		$path .= $name != null ? $name.'.'.$this->getExtension() : $this->getFileName();
		
		// Check if a file exists
		if(!$overwrite && file_exists($path)) {
			return false;
		}
		
		// Copy file
		if(!copy($this->path,$path)) {
			return false;
		}
		
		// Return file path
		return $path;
	}
	
	/**
	 * @see FormElement::validate($method)
	 */
	public function validate($method)
	{
		// Is submited ?
		if(empty($_FILES[$this->name]['name']) || $_FILES[$this->name]['error'] != UPLOAD_ERR_OK) {
			$this->errorCode = FormFile::ERROR_MISSING;	return false; // Error code and return false
		}
		
		// isSubmitted to true
		$this->isSubmitted = true;
		
		// Path
		$this->path = $_FILES[$this->name]['tmp_name'];
		
		// Valid extension ?
		if($this->extensions != null && !in_array($this->getExtension(),$this->extensions)) {
			$this->errorCode = FormFile::ERROR_EXTENSION; return false; // Error code and return false
		}
		
		// Valid mimetype ?
		if($this->mimetypes != null && $this->getMimeType() != null && !in_array($this->getMimeType(),$this->mimetypes)) {
			$this->errorCode = FormFile::ERROR_MIMETYPE; return false; // Error code
		}
		
		// Valid size ?
		if($this->fileSize != null && $this->getSize() > $this->fileSize) {
			$this->errorCode = FormFile::ERROR_SIZE; return false; // Error code
		}
		
		// IsValid to true
		$this->isValid = true;
	}
	
	/**
	 * @see FormElement::__toString()
	 */
	public function __toString()
	{
		return $this->before.'<input type="file" name="'.$this->name.'" '.
		               ($this->inputSize != null ? $this->inputSize.' ' : '').
		               ($this->extra != null ? $this->extra.' ' : '').'/>'.$this->after;
	}
}

/**
 * Form image
 */
class FormImage extends FormFile
{
	// int width
	private $width;
	
	// int height
	private $height;
	
	// string type
	private $type;
	
	// int maximal width
	private $maxWidth;
	
	// int maximal height
	private $maxHeight;
	
	// bool resize image if dimensions exceed the maximum
	private $maxResize;
	
	// int minimal width
	private $minWidth;
	
	// int minimal height
	private $minHeight;
	
	// bool resize image if dimensions exceed the minimum
	private $minResize;
	
	// Error codes
	const ERROR_INVALID		= 5;
	const ERROR_DIMENSIONS 	= 6;
	
	/**
	 * Constructor
	 * @param $name string name
	 * @param $description string description
	 * @param $inputSize int size of file input
	 * @param $fileSize int maximal size of file in bytes
	 * @param $extensions array valid extensions
	 * @param $mimetypes array valid mime types
	 * @param $maxWidth int maximal width
	 * @param $maxHeight int maximal height
	 * @param $maxResize bool resize image if dimensions exceed the maximum
	 * @param $minWidth int minimal width
	 * @param $minHeight int minimal width
	 * @param $minResize bool resize image if dimensions exceed the minimum
	 * @param $extra string extra
	 */
	public function __construct($name,$description=null,$inputSize=null,$fileSize=null,$extensions=null,$mimetypes=null,$maxWidth=null,$maxHeight=null,$maxResize=true,$minWidth=null,$minHeight=null,$minResize=false,$extra=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$inputSize,$fileSize,$extensions,$mimetypes,$extra);
		
		// Set attributes
		$this->maxWidth = $maxWidth;
		$this->maxHeight = $maxHeight;
		$this->maxResize = $maxResize;
		$this->minWidth = $minWidth;
		$this->minHeight = $minHeight;
		$this->minResize = $minResize;
	}
	
	/**
	 * Get width
	 * @return int width
	 */
	public function getWidth()
	{
		return $this->width;
	}
	
	/**
	 * Get height
	 * @return int height
	 */
	public function getHeight()
	{
		return $this->height;
	}
	
	/**
	 * @see FormFile::validate($method)
	 */
	public function validate($method)
	{
		// Call parent validate method
		parent::validate($method);
		if(!$this->isValid()) { return false; }
		
		// Get image infos
		if(!($infos = getimagesize($this->path))) {
			$this->errorCode = FormImage::ERROR_INVALID;	// Error code
			 return $this->isValid = false;				// IsValid to false and return false
		}
		
		// Set width, height and type
		list($this->width,$this->height,$this->type) = $infos;
		
		// Exceed the maximum dimensions ?
		if($this->maxWidth != null && $this->width > $this->maxWidth || $this->maxHeight != null && $this->height > $this->maxHeight) {
			if(!$this->maxResize) {
				$this->errorCode = FormImage::ERROR_DIMENSIONS;	// Error Code
				return $this->isValid = false;				// IsValid to false and return false
			} elseif($this->minWidth != null || $this->minHeight != null) {
				// Ratio
				$ratio = min($this->maxWidth/$this->width,$this->maxHeight/$this->height);

				// Check if new dimensions don't exceed minimum
				if($this->minWidth != null && $this->width*$ratio < $this->minWidth || $this->minHeight != null && $this->height*$ratio < $this->minHeight) {
					$this->errorCode = FormImage::ERROR_DIMENSIONS;	// Error Code
					return $this->isValid = false;				// IsValid to false and return false
				}
			}
		}
		
		// Exceed the minimum dimensions ?
		if($this->minWidth != null && $this->width < $this->minWidth || $this->minHeight != null && $this->height < $this->minHeight) {
			if(!$this->minResize) {
				$this->errorCode = FormImage::ERROR_DIMENSIONS;	// Error Code
					return $this->isValid = false;				// IsValid to false and return false
			} elseif($this->maxWidth != null || $this->maxHeight != null) {
				// Ratio
				$ratio = max($this->minWidth/$this->width,$this->minHeight/$this->height);

				// Check if new dimensions don't exceed maximum
				if($this->minWidth != null && $this->width*$ratio < $this->minWidth || $this->minHeight != null && $this->height*$ratio < $this->minHeight) {
					$this->errorCode = FormImage::ERROR_DIMENSIONS;	// Error Code
					return $this->isValid = false;				// IsValid to false and return false
				}
			}
		}
		
		// IsValid to true
		$this->isValid = true;
	}
	
	/**
	 * MoveTo surcharge
	 * @param $path string destination, ending by /
	 * @param $name string name, without extension 
	 * @param $overwrite bool overwrite if a file exists
	 */
	public function moveTo($path,$name=null,$overwrite=false)
	{
		// Call parent moveTo method
		if(!($path = parent::moveTo($path,$name,$overwrite))) {
			return false;
		}
		
		// Resize
		if(!($infos = FormImage::resize($this->path,$this->maxWidth,$this->maxHeight,$this->minWidth,$this->minHeight))) {
			unlink($this->path);	// Delete file
			$this->path = null;		// Set path to null
			return false;			// Return false
		}
		list($this->width,$this->height) = $infos;
		
		// Return path
		return $path;
	}
	
	/**
	 * CopyTo surcharge
	 * @param $path string destination, ending by /
	 * @param $name string name, without extension 
	 * @param $overwrite bool overwrite if a file exists
	 * @param $maxWidth int width
	 * @param $maxHeight int height
	 * @param $minWidth int minimal width to don't exceed
	 * @param $minHeight int maximal or minimal height to don't exceed
	 * @return bool|array false if unsuccessful operation, else array like array($path,$width,$height)
	 */
	public function copyTo($path,$name=null,$overwrite=false,$maxWidth=null,$maxHeight=null,$minWidth=null,$minHeight=null)
	{
		// Call parent copyTo method
		if(!($path = parent::copyTo($path,$name,$overwrite))) {
			return false;
		}
		
		// Resize
		if(!($infos = FormImage::resize($path,$maxWidth,$maxHeight,$minWidth,$minHeight))) {
			return false;
		}
		
		return array($path,$infos[0],$infos[1]);
	}
	
	/**
	 * Resize image
	 * @param $path string path
	 * @param $maxWidth int width
	 * @param $maxHeight int height
	 * @param $minWidth int minimal width to don't exceed
	 * @param $minHeight int maximal or minimal height to don't exceed
	 * @return bool|array false if unsuccessful operation, else array like array($width,$height) 
	 */
	public static function resize($path,$maxWidth=null,$maxHeight=null,$minWidth=null,$minHeight=null)
	{
		// Get infos
		if(!($infos = getimagesize($path))) {
			return false;
		}
		list($oldWidth,$oldHeight,$type) = $infos;
		
		// Ratio
		if($maxWidth != null && $oldWidth > $maxWidth || $maxHeight != null && $oldHeight > $maxHeight) {
			// Ratio
			$ratio = min($maxWidth/$oldWidth,$maxHeight/$oldHeight);
			
			// Check if new dimensions don't exceed minimum
			if($minWidth != null && $oldWidth*$ratio < $minWidth || $minHeight != null && $oldHeight*$ratio < $minHeight) {
				return false;
			}
		} elseif($minWidth != null && $oldWidth < $minWidth || $minHeight != null && $oldHeight < $minHeight) {
			// Ratio
			$ratio = max($minWidth/$oldWidth,$minHeight/$oldHeight);
			
			// Check if new dimensions don't exceed maximum
			if($maxWidth != null && $oldWidth*$ratio > $maxWidth || $maxHeight != null && $oldHeight*$ratio > $maxHeight) {
				return false;
			}
		} else {
			return array($oldWidth,$oldHeight);
		}
		
		// New with and height
		$newWidth = $oldWidth*$ratio;
		$newHeight = $oldHeight*$ratio;
		
		// Load image
		switch($type) {
			case IMAGETYPE_JPEG : if(!($old = imagecreatefromjpeg($path))) { return false; } break;
			case IMAGETYPE_GIF : if(!($old = imagecreatefromgif($path))) { return false; } break;
			case IMAGETYPE_PNG : if(!($old = imagecreatefrompng($path))) { return false; } break;
			default : return false;
		}
		
		// Resize
		$new = imagecreatetruecolor($newWidth, $newHeight);
      	if(!$new || !imagecopyresampled($new, $old, 0, 0, 0, 0, $newWidth, $newHeight, $oldWidth, $oldHeight)) {
      		return false;
      	}
		
		// Save image
		switch($type) {
			case IMAGETYPE_JPEG : if(!imagejpeg($new,$path == null ? $this->path : $path)) { return false; } break;
			case IMAGETYPE_GIF : if(!imagegif($new,$path == null ? $this->path : $path)) { return false; } break;
			case IMAGETYPE_PNG : if(!imagepng($new,$path == null ? $this->path : $path)) { return false; } break;
		}
		
		// Return true
		return array($newWidth,$newHeight);
	}
	
}

/**
 * Form captcha
 */
class FormCaptcha extends FormElement
{
	// string public key
	private $publicKey;
	
	// string private key
	private $privateKey;
	
	// string error captcha
	private $errorCaptcha = null;
	
	/**
	 * Constructor
	 */
	public function __construct($publicKey,$privateKey)
	{
		// Call parent constructor
		parent::__construct(null,null,null);
		
		// Set attributes
		$this->publicKey = $publicKey;
		$this->privateKey = $privateKey;
	}
	
	/**
	 * @see FormElement::validate($method)
	 */
	public function validate($method)
	{
		// Submited ?
		$this->isSubmitted = $method == Form::METHOD_POST ? !empty($_POST['recaptcha_challenge_field']) && !empty($_POST['recaptcha_response_field']) :
		                                                   !empty($_GET['recaptcha_challenge_field']) && !empty($_GET['recaptcha_response_field']);
		if (!$this->isSubmitted) {
			return false;
		}
		
		// Challenge and response
		$user_challenge = $method == Form::METHOD_POST ? $_POST['recaptcha_challenge_field'] : $_GET['recaptcha_challenge_field'];
		$user_response = $method == Form::METHOD_POST ? $_POST['recaptcha_response_field'] : $_GET['recaptcha_response_field'];

		// Data
        $data = array (
        	'privatekey' => $this->privateKey,
        	'remoteip' => $_SERVER['REMOTE_ADDR'],
        	'challenge' => $user_challenge,
        	'response' => $user_response
        );
        
        // Prepare request
        $request = '';
        foreach ($data as $key => $value) {
        	$request .= $key.'='.urlencode(stripslashes($value)).'&';
        }
        $request = substr($request,0,strlen($request)-1);

        // Prepare http request
        $http_request = 'POST /recaptcha/api/verify HTTP/1.0'."\r\n".
                        'Host: www.google.com'."\r\n".
                        'Content-Type: application/x-www-form-urlencoded;'."\r\n".
                        'Content-Length: '.strlen($request)."\r\n".
                        'User-Agent: reCAPTCHA/PHP'."\r\n".
                        "\r\n".
                        $request;

        // Open socket
        if( false == ( $fs = @fsockopen('www.google.com', 80, $errno, $errstr, 10) ) ) {
        	throw new Exception('Could not open socket');
        }
        
        // Write
        fwrite($fs, $http_request);

        // Listen
        $response = '';
        while (!feof($fs)) {
        	$response .= fgets($fs, 1160);
        }
        
        // Close socket
        fclose($fs);
        
        // Check result
        $response = explode("\r\n\r\n",$response,2);
        $answers = explode("\n", $response[1]);
        if(trim($answers [0]) == 'true') {
        	$this->isValid = true;
        	$this->errorCaptcha = null;
        }  else {
        	$this->errorCaptcha = $answers[1];
        }
	}
	
	/**
	 * @see FormElement::__toString()
	 */
	public function __toString()
	{
		$error = $this->errorCaptcha != null ? '&amp;error='.$this->errorCaptcha : '';
		
        return $this->before.'<script type="text/javascript" src="http://www.google.com/recaptcha/api/challenge?k='.$this->publicKey.$error.'"></script>
		        <noscript>
	  		       <iframe src="http://www.google.com/recaptcha/api/noscript?k='.$this->publicKey.$error.'" height="300" width="500" frameborder="0"></iframe><br/>
	  		       <textarea name="recaptcha_challenge_field" rows="3" cols="40"></textarea>
	  		       <input type="hidden" name="recaptcha_response_field" value="manual_challenge"/>
		        </noscript>'.$this->after;
	}
}

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