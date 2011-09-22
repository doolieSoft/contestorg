<?php

class LogService extends Service
{
	// Logger
	private $logger;
	
	// Démarrer le service
	public function start() {
		// Importer les classes de log
		require('tools/log.php');
		require('tools/log_data.php');
		
		// Récupérer le service de configuration
		$conf = Application::getService('conf');

		// Créer le logger
		$log_data = new LogData_File_XML(ROOT_DIR.$conf['LOG']['PATH'],$conf['LOG']['DURATION'],$conf['LOG']['NB_MAX']);
		$this->logger = new Log($log_data,$conf['MISC']['DEV']?E_ALL:0,E_ALL&~E_NOTICE,E_ALL&~E_NOTICE,$conf['MISC']['DEV']);
		if (!$conf['MISC']['DEV']) {
			$this->logger->addErrorsPage(Request::url('error','error',null),'Request::redirect');
			$this->logger->setExceptionsPage(Request::url('error','error',null),'Request::redirect');
		}
		
		// Errors and exceptions handler
		set_error_handler(array($this->logger,'addError'));
		set_exception_handler(array($this->logger,'addException'));
	}
	
	// Récupérer la ressource du service
	public function getRessource() {
		return $this->logger;
	}
	
	// Arreter le service
	public function stop() {
		
	}
}