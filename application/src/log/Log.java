package log;

import org.apache.log4j.Logger;

import controlers.ContestOrg;

public class Log
{

	// Instance unique du logger
	private static Logger logger = Logger.getLogger(ContestOrg.class);
	
	// Récupérer le logger
	public static Logger getLogger() {
		// Retourner le logger
		return Log.logger;
	}
}
