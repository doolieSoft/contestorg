<?php

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
	 * @param $attributes string attributes
	 */
	public function __construct($name,$description=null,$inputSize=null,$fileSize=null,$extensions=null,$mimetypes=null,$attributes=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$attributes);
		
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
		return $this->isSubmitted ? ($this->saved ? basename($this->path) : $_FILES[$this->name]['name']) : null;
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
		               ($this->attributes != null ? $this->attributes.' ' : '').'/>'.$this->after;
	}
}