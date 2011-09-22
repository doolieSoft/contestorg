<?php

/**
 * Strategy to log into files
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2010-01-01
 */
class LogData_File implements ILogData
{
	
	// File path and handle
	protected $filePath;
	protected $fileHandle;
	
	// Constructor/destructor
	
	/**
	 * @param $folder string folder path of log files, ending by "/"
	 * @param $duration int duration of an log file in days
	 * @param $nbMax int maximum number of log files, -1 for unlimited
	 */
	public function __construct($folder,$duration=1,$nbMax=7)
	{
		// Open directory
		if(!is_dir($folder)) {
			throw new Exception('Folder \''.$folder.'\' doesn\'t exist.');
		}
		if(!($directory = opendir($folder))) {
			throw new Exception('Permission denied to read into folder \''.$folder.'\'.');
		}
		
		// List log files
		$tabFiles = array();
		while(($entry = readdir($directory)) !== false) { // List files
			if(sscanf(pathinfo($entry,PATHINFO_BASENAME),'%d-%d-%d.'.$this->getExtension(),$year,$month,$day) == 3) { // Log file !
				$tabFiles[mktime(0,0,0,$month,$day,$year)] = $folder.$entry;
			}
		}
		
		// Find path of current log file or "create" it
		if(count($tabFiles)) {
			ksort($tabFiles); // Sort files array by key (timestamp)
			reset($tabFiles); // Reset pointer
			if(time()-key($tabFiles) <= $duration*24*3600) {
				$this->filePath = current($tabFiles);
			}
		}
		if(!isset($this->filePath)) { // If file not found
			$this->filePath = $folder.date('Y-m-d').'.'.$this->getExtension();
		}
		
		// Clean log files folder
		if(count($tabFiles) && $nbMax != -1) {
			krsort($tabFiles); // Sort files array by key (timestamp)
			while(count($tabFiles) > $nbMax) {
				$oldFile = array_pop($tabFiles); // Delete old file in array
				unlink($oldFile); // Delete old file in files system
			}
		}
	}
	
	// Get file extension
	public function getExtension()
	{
		return 'log';
	}
	
	// Destructor
	public function __destruct()
	{
		if(isset($this->fileHandle)) {
			fclose($this->fileHandle);
		}
	}
	
	// Add
	public function add($category,$array)
	{
		// Open file if necessary
		if(!isset($this->fileHandle)) {
			if(!($this->fileHandle = fopen($this->filePath,'a+'))) {
				throw new Exception('Permission denied to write into file \''.$this->filePath.'\'.');
			}
		}
		
		// Prepare string
		$temp = array();
		foreach($array as $name => $value) {
			$temp[] = $name.' = "'.$value.'"';
		}
		if($category == Log::ERROR) { $categoryTxt = 'error'; }
		elseif($category == Log::EXCEPTION) { $categoryTxt = 'exception'; }
		elseif($category == Log::MESSAGE) { $categoryTxt = 'message'; }
		else { $categoryTxt = 'unknown'; }
		$string = '['.date('D, j M Y H:i:s').'|'.time().'] <'.$categoryTxt.'> '.implode(' ',$temp);
		
		// Add message
		fputs($this->fileHandle,$string.';'.PHP_EOL);
	}
}

/**
 * Strategy to log into XML files
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2010-01-01
 */
class LogData_File_XML extends LogData_File
{

	// Dom elements
	private $dom;
	private $exceptions;
	private $errors;
	private $messages;
	
	// Style sheet
	private $stylesheet;
	
	// Get file extension
	public function getExtension()
	{
		return 'xml';
	}

	// Redifinition of add method
	public function add($category,$array)
	{
		// Load xml if necessary
		if(!isset($this->dom)) {
			$this->dom = new DomDocument('1.0');
			$this->dom->formatOutput = true;	
			if(file_exists($this->filePath)) { // File already exists
				// Load document
				$this->dom->load($this->filePath);
				
				// Get exceptions, errors and messages nodes
				$this->exceptions = $this->dom->getElementsByTagName('exceptions')->item(0);
				$this->errors = $this->dom->getElementsByTagName('errors')->item(0);
				$this->messages = $this->dom->getElementsByTagName('messages')->item(0);
			} else { // File doesn't exists
				// Attache style sheet
				if($this->stylesheet != null) {
					$xsl = $this->dom->createProcessingInstruction('xml-stylesheet', 'type="text/xsl" href="'.$this->stylesheet.'"');
					$this->dom->appendChild($xsl);
				}
				
				// Create root
				$root = $this->dom->createElement('logs');
				$this->dom->appendChild($root);
				
				// Create exceptions, errors and messages nodes
				$this->exceptions = $this->dom->createElement('exceptions');
				$this->errors = $this->dom->createElement('errors');
				$this->messages = $this->dom->createElement('messages');
				$root->appendChild($this->exceptions);
				$root->appendChild($this->errors);
				$root->appendChild($this->messages);
			}
		}
		
		// Create element
		if($category == Log::EXCEPTION) { $element = $this->dom->createElement('exception'); }
		elseif($category == Log::ERROR) { $element = $this->dom->createElement('error'); }
		else { $element = $this->dom->createElement('message'); }
		
		// Fill element
		$element->setAttribute('date',date('D, j M Y H:i:s'));
		$element->setAttribute('time',time());
		foreach($array as $key => $value) {
			$element->setAttribute(utf8_encode($key),utf8_encode(strip_tags($value)));
		}
		
		// Insert element
		if($category == Log::EXCEPTION) { $categoryNode = $this->exceptions; }
		elseif($category == Log::ERROR) { $categoryNode = $this->errors; }
		else { $categoryNode = $this->messages; }
		$categoryNode->appendChild($element);
		
		// Save entry
		$this->dom->save($this->filePath);
	}
	
	/**
	 * Associate style sheet
	 * @param $filename string style sheet filename
	 */
	public function setStyleSheet($filename)
	{
		$this->stylesheet = $filename;
	}
	
	// Redifinition of destruct method
	public function __destruct() { }
	
}

?>