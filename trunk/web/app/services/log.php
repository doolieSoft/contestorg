<?php

/**
 * Service de logging de messages
 */
class LogService extends Service
{
	/** @var $logger Logger Logger */
	private $logger;
	
	/**
	 * @see Service::start()
	 */
	public function start() {
		// Importer les classes de log
		require('tools/logger.php');
		
		// Récupérer le service de configuration
		$conf = Application::getService('conf');

		// Créer le logger
		$log_data = new LogSaver_File_XML(ROOT_DIR.$conf['LOG']['PATH'],$conf['LOG']['DURATION'],$conf['LOG']['NB_MAX']);
		$this->logger = new Logger($log_data,$conf['MISC']['DEV']?E_ALL:0,E_ALL&~E_NOTICE,E_ALL&~E_NOTICE,$conf['MISC']['DEV']);
		if (!$conf['MISC']['DEV']) {
			$this->logger->addErrorsPage(Request::buildURL(null,'error','error'),'Request::redirect');
			$this->logger->setExceptionsPage(Request::buildURL(null,'error','error'),'Request::redirect');
		}
		
		// Errors and exceptions handler
		set_error_handler(array($this->logger,'addError'));
		set_exception_handler(array($this->logger,'addException'));
	}
	
	/**
	 * @see Service::getRessource()
	 */
	public function getRessource() {
		return $this->logger;
	}
	
	/**
	 * @see Service::stop()
	 */
	public function stop() {
		
	}
}