<?php

/**
 * Class to extend to become conf strategy
 * @author Cyril Perrin
 * @license LGPL v3 
 * @version 2009-10-14
 */
abstract class ConfData implements Iterator, Countable, ArrayAccess
{
	// Attributes
	protected $data = array();
	protected $modifAllow;
	protected $autoSave;
	
	// Constructor/destructor
	public function __construct($pModifAllow=false,$pAutoSave=false)
	{
		$this->modifAllow = $pModifAllow;
		$this->autoSave = $pAutoSave;
	}
	public function __destruct()
	{
		if($this->autoSave) $this->save();
	}
	
	// Load/save
	public abstract function save();
	public abstract function load();
	
	// Iterator implementation
	public function rewind() { reset($this->data); }
	public function key() { return key($this->data); }
	public function next() { next($this->data); }
	public function current() { return current($this->data); }
	public function valid() { return current($this->data)!==false; }
	
	// Countable implementation
	public function count() { return count($this->data); }
	
	// ArrayAccess implementation
	public function offsetExists($key) { return isset($this->data[$key]); }
	public function offsetGet($key) { return $this->data[$key]; }
	public function offsetSet($key, $value) { if($this->modifAllow) $this->data[$key] = $value; }
	public function offsetUnset($key) { unset($this->data[$key]); }
}

/**
 * Strategy to load and save conf from INI file
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2009-10-14
 */
class ConfData_Ini extends ConfData
{
	// Attributes
	private $filePath;
	
	// Constructor
	public function __construct($pFilePath,$pModifAllow=false,$pAutosave=false)
	{
		parent::__construct($pModifAllow,$pAutosave);
		if(!file_exists($pFilePath) && !is_file($pFilePath)) { throw new Exception('File \''.$pFilePath.'\' doesn\'t exist.'); }
        if(!is_readable($pFilePath)) { throw new Exception('Permission denied to read file \''.$pFilePath.'\'.'); }
        if($this->autoSave && !is_writable($pFilePath)) { throw new Exception('Permission denied to write into file \''.$pFilePath.'\'.'); }
		$this->filePath = $pFilePath;
	}
	
	// Load/save
	public function load()
	{
		$this->data = parse_ini_file($this->filePath,true);
	} 
	public function save()
	{
		$string = '';
		foreach($this->data as $category => $parameters) {
			$string .= PHP_EOL.'['.$category.']'.PHP_EOL;
			foreach($parameters as $name => $value) {
				$string .= $name.' = '.(is_bool($value) ? ($value ? 'On' : 'Off') : 
				                                          (is_int($value) ? $value :
				                                                            '"'.$value.'"')).PHP_EOL;
			}
		}
		$string = trim($string);
		file_put_contents($this->filePath, $string);
	}
}

?>