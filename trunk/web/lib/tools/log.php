<?php

/**
 * Class to logging events
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2010-01-01
 */
class Log
{	
	// Data
	private $logData = array();
	
	// Errors
	private $errorsDisplay;
	private $errorsSave;
	private $errorsStop;
	private $errorsRedirect = array();
	private $errorsRedirectPage = array();
	private $errorsRedirectCallBack = array();
	
	// Exceptions
	private $exceptionsDisplay;
	private $exceptionsRedirectPage;
	private $exceptionsRedirectCallBack;
	
	// Categories
	const ERROR = 1;
	const EXCEPTION = 2;
	const MESSAGE = 3;
	
	/**
	 * Constructor
	 * @param $pData ILogData object will be called to add message
	 * @param $pDisplayErrors int error types must be displayed, use bit operators to combine values, default : E_ALL&~E_NOTICE
	 * @param $pSaveErrors int error type must be saved, use bit operators to combine values, default : E_ALL&~E_NOTICE
	 * @param $pStopErrors int error type must stop script execution, use bit operators to combine values, default : E_ALL&~E_NOTICE
	 * @param $pDisplayExceptions bool display exceptions ?
	 */
	public function __construct(ILogData $pData,$pDisplayErrors=30711,$pSaveErrors=30711,$pStopErrors=30711,$pDisplayExceptions=true)
	{
		$this->logData[] = $pData;
		$this->errorsDisplay = $pDisplayErrors;
		$this->errorsSave = $pSaveErrors;
		$this->errorsStop = $pStopErrors;
		$this->exceptionsDisplay = $pDisplayExceptions;
	}
	
	/**
	 * Add an entry into data
	 * @param $pCategory ? category of entry
	 * @param $pLogArray array information about entry
	 * @param $pError bool is it an error ?
	 */
	private function add($pCategory,$pLogArray)
	{	
		// Complete information array
		$pLogArray['ip'] = $_SERVER['REMOTE_ADDR'];
		$pLogArray['url'] = urldecode($_SERVER['REQUEST_URI']);
		
		// Add information
		foreach($this->logData as $data) {
			$data->add($pCategory,$pLogArray);
		}
	}
	
	/**
	 * Add a simple message into data
	 * @param $pCategory string category of message
	 * @param $pMessage string message
	 */
	public function addMessage($pCategory,$pMessage)
	{
		// Prepare log array
		$log = array();
		$log['category'] = $pCategory;
		$log['message'] = $pMessage;
		
		// Add log
		$this->add(Log::MESSAGE,$log);
	}
	
	/**
	 * Handling error method
	 */
	public function addError($pCode, $pMessage, $pFile, $pLine)
	{
		// Save error if necessary
		if($this->errorsSave&$pCode) {
			// Prepare log array
			$log = array();
			$log['code'] = $pCode;
			$log['message'] = $pMessage;
			$log['file'] = $pFile;
			$log['line'] = $pLine;
			
			// Add log
			$this->add(Log::ERROR,$log);
		}
		
		// Redirect if necessary
		if(isset($this->errorsRedirect)) {
			foreach($this->errorsRedirect as $key => $errorsRedirect) {
				if($errorsRedirect&$pCode) {
					call_user_func($this->errorsRedirectCallBack[$key],$this->errorsRedirectPage[$key]);
				}
			}
		}
		
		// Display if necessary
		if($this->errorsDisplay&$pCode) {
			echo '<p><b>Error ('.$pCode.') !</b> '.$pMessage.' in file <b>'.$pFile.'</b> at line <b>'.$pLine.'</b></p>',
			     '<p><b>Debug backtrace :</b></p><pre>'.print_r(debug_backtrace(),true).'</pre>';
		}
		
		// Stop script execution if necessary
		if($this->errorsStop&$pCode) { exit; }
		
		// Don't call default php handler
		return true;
	}
	
	/**
	 * Handling exception method
	 */
	public function addException(Exception $pException)
	{
		// Prepare log array
		$log = array();
		$log['code'] = $pException->getCode();
		$log['message'] = $pException->getMessage();
		$log['file'] = $pException->getFile();
		$log['line'] = $pException->getLine();
		
		// Add log
		$this->add(Log::EXCEPTION,$log);
		
		// Redirect if necessary
		if(isset($this->exceptionsRedirectCallBack,$this->exceptionsRedirectPage)) {
			call_user_func($this->exceptionsRedirectCallBack,$this->exceptionsRedirectPage);
		}
		
		// Display if necessary
		if($this->exceptionsDisplay) {
			echo '<p><b>',get_class($pException),' ('.$pException->getCode().') !</b> '.$pException->getMessage().' in file <b>'.$pException->getFile().'</b> at line <b>'.$pException->getLine().'</b></p>',
			     '<p><b>Debug backtrace :</b></p><pre>'.print_r(debug_backtrace(),true).'</pre>';
		}
	}
	
	/**
	 * Add data object
	 * @param ILogData $pData object will be called to add message
	 */
	public function addLogData(ILogData $pData)
	{
		$this->logData[] = $pData;
	}
	
	/**
	 * Add page wich user will be redirected when errors given happen
	 * @param $pPage string page where visitor will be redirected when errors happen
	 * @param $pCallBack callback callback will be called to redirect user
	 * @param $pErrors int error types, use bit operators to combine values, default : E_ERROR&~E_NOTICE&~E_WARNING
	 */
	public function addErrorsPage($pPage,$pCallBack,$pErrors=30709)
	{
		if(is_callable($pCallBack)) {
			$this->errorsRedirect[] = $pErrors;
			$this->errorsRedirectPage[] = $pPage;
			$this->errorsRedirectCallBack[] = $pCallBack;
		} else {
			throw new Exception('CallBack for errors page given isn\'t correct.');
		}
	}
	
	/**
	 * Add page wich user will be redirected when exceptions happen
	 * @param $pPage string page where user will be redirected when errors happen
	 * @param $pCallBack callback callback will be called to redirect user
	 */
	public function setExceptionsPage($pPage,$pCallBack)
	{
		if(is_callable($pCallBack)) {
			$this->exceptionsRedirectPage = $pPage;
			$this->exceptionsRedirectCallBack = $pCallBack;
		} else {
			throw new Exception('CallBack given for exceptions page isn\'t correct.');
		}
	}
}

/**
 * Interface to implement to become a log strategy
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2010-01-01
 */
interface ILogData
{
	/**
	 * Add a log into data structure
	 * @param $category ? category (Log::ERROR, Log::EXCEPTION, Log::MESSAGE)
	 * @param $array array data array
	 */
	public function add($category,$array);
}

?>