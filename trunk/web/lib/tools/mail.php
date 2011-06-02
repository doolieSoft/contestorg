<?php

/**
 * class to send emails
 * @author Cyril Perrin
 * @license LGPL v3
 * @version 2011-05-16
 */
class Mail {
	
	/** string newline */
	const NEWLINE = "\r\n";
	
	/** string subjet */
	private $subject;
	
	/** array headers */
	private $headers = array();
	
	/** string message content */
	private $content;
	
	/**
	 * Constructor
	 */
	public function __construct() {
		$this->setHeader('Mime-Version', '1.0');
		$this->setHeader('Date', date('r'));
		$this->setHeader('Content-type', 'text/html;charset=utf-8');
	}
	
	/**
	 * Send email to recipients
	 * @param $recipients string|array recipients
	 * @param $names string|array names
	 * @return bool successfull operation ?
	 */
	public function send($recipients,$names=array()) {
		// Check if recepients and names are arrays
		$recipients = is_array($recipients) ? $recipients : array($recipients);
		$names = is_array($names) ? $names : array($names);
		
		// Add names into recipients
		if($names) {
			foreach($recipients as $key => $recipient) {
				if(array_key_exists($key, $names)) {
					$recipients[$key] = $names[$key].' <'.$recipients[$key].'>'; 
				}
			}
		}
		
		// Send email
		return mail(implode(', ',$recipients),$this->subject,$this->content,implode(self::NEWLINE,$this->headers));
	}
	
	/**
	 * Set subject
	 * @param $subject string subject
	 */
	public function setSubjet($subject) {
		$this->subject = $subject;
	}
	
	/**
	 * Set content
	 * @param $subject string content
	 */
	public function setContent($content) {
		$this->content = $content;
	}
	
	/**
	 * Set header
	 * @param name string name
	 * @param value string value
	 */
	private function setHeader($name,$value) {
		$this->headers[$name] = $name.': '.$value; 
	}
	
	/**
	 * Set from
	 * @param $email string email
	 * @param $name string name
	 */
	public function setFrom($email,$name=null) {
		$this->setHeader('From', $name === null ? $email : '"'.$name.'" <'.$email.'>');
	}
	
	/**
	 * Set reply to
	 * @param $email string email
	 */
	public function setReplyTo($email,$name=null) {
		$this->setHeader('Reply-To', $name === null ? $email : '"'.$name.'" <'.$email.'>');
	}
	
	/**
	 * Set carbon copy
	 * @param $emails array emails in carbon copy 
	 */
	public function setCc($emails) {
		$this->setHeader('Cc', implode(', ',$emails));
	}
	
	/**
	 * Set blind carbon copy
	 * @param $emails array emails in blind carbon copy 
	 */
	public function setBcc($emails) {
		$this->setHeader('Bcc', implode(', ',$emails));
	}
}