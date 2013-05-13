<?php

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
	 * @param $attributes string attributes
	 */
	public function __construct($name,$description=null,$inputSize=null,$fileSize=null,$extensions=null,$mimetypes=null,$maxWidth=null,$maxHeight=null,$maxResize=true,$minWidth=null,$minHeight=null,$minResize=false,$attributes=null)
	{
		// Call parent constructor
		parent::__construct($name,$description,$inputSize,$fileSize,$extensions,$mimetypes,$attributes);
		
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
				$ratio = 0;
				if($this->maxWidth == null) {
					$ratio = $this->maxHeight/$this->height;
				} else if($this->minHeight == null) {
					$ratio = $this->maxWidth/$this->width;
				} else {
					$ratio = min($this->maxWidth/$this->width,$this->maxHeight/$this->height);
				}

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
				$ratio = 0;
				if($this->minWidth == null) {
					$ratio = $this->minHeight/$this->height;
				} else if($this->minHeight == null) {
					$ratio = $this->minWidth/$this->width;
				} else {
					$ratio = max($this->minWidth/$this->width,$this->minHeight/$this->height);
				}

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
	 * @return bool|string false if unsuccessful operation, else file path
	 */
	public function moveTo($path,$name=null,$overwrite=false)
	{
		// Call parent moveTo method
		if(!($path = parent::moveTo($path,$name,$overwrite))) {
			return false;
		}
		
		// Resize
		if(!($infos = FormImage::resize($path,$this->maxWidth,$this->maxHeight,$this->minWidth,$this->minHeight))) {
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
		
		// Return path and dimensions
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
			$ratio = 0;
			if($maxWidth == null) {
				$ratio = $maxHeight/$oldHeight;
			} else if($maxHeight == null) {
				$ratio = $maxWidth/$oldWidth;
			} else {
				$ratio = min($maxWidth/$oldWidth,$maxHeight/$oldHeight);
			}
			
			// Check if new dimensions don't exceed minimum
			if($minWidth != null && $oldWidth*$ratio < $minWidth || $minHeight != null && $oldHeight*$ratio < $minHeight) {
				return false;
			}
		} elseif($minWidth != null && $oldWidth < $minWidth || $minHeight != null && $oldHeight < $minHeight) {
			// Ratio
			$ratio = 0;
			if($minWidth == null) {
				$ratio = $minHeight/$oldHeight;
			} else if($minHeight == null) {
				$ratio = $minWidth/$oldWidth;
			} else {
				$ratio = max($minWidth/$oldWidth,$minHeight/$oldHeight);
			}
			
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