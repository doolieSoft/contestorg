<?php

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