package org.contestorg.log;

import org.apache.log4j.Logger;
import org.contestorg.controllers.ContestOrg;

/**
 * Classe permettant de récupérer le logger
 */
public class Log
{
	/** Instance unique du logger */
	private static Logger logger = Logger.getLogger(ContestOrg.class);
	
	/**
	 * Récupérer le logger
	 * @return logger
	 */
	public static Logger getLogger() {
		// Retourner le logger
		return Log.logger;
	}
}
