package org.contestorg.log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.contestorg.common.OperationAbstract;
import org.contestorg.common.OperationRunnableAbstract;
import org.contestorg.common.Tools;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.interfaces.IOperation;
import org.contestorg.models.FrontModel;
import org.contestorg.out.HTTPHelper;
import org.contestorg.out.PersistanceXML;
import org.contestorg.out.XMLHelper;

/**
 * Rapport d'erreur
 */
public class Report 
{
	/** Description de l'erreur */
	private String description;
	
	/** Exceptions liées à l'erreur */
	private Exception[] exceptions;
	
	/**
	 * Constructeur
	 * @param description description de l'erreur
	 * @param exceptions exceptions liées à l'erreur
	 */
	public Report(String description, Exception[] exceptions) {
		this.description = description;
		this.exceptions = exceptions;
	}
	
	/**
	 * Envoyer le rapport d'erreur
	 * @return opération de l'envoi
	 */
	public IOperation send() {
		// Créer et retourner l'opération
		return new OperationAbstract() {
			// Runnable
			private OperationRunnableAbstract runnable = new Send();
			
			// Implémentation de getRunnable
			@Override
			public OperationRunnableAbstract getRunnable () {
				return this.runnable;
			}
		};
	}
	
	/**
	 * Classe permettant l'envoi du rapport d'erreur
	 */
	private class Send extends OperationRunnableAbstract {
		/**
		 * @see OperationRunnableAbstract#run()
		 */
		@Override
		public void run () {
			// Envoi du rapport d'erreur
			this.fireMessage("Envoi du rapport ...");
			
			// Construire la liste des paramètres
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("erreur_description", description);
			if(exceptions != null && exceptions.length != 0) {
				for(int i=0;i<exceptions.length;i++) {
					parameters.put("erreur_exception"+i+"_message", exceptions[i].getMessage());
					parameters.put("erreur_exception"+i+"_stacktrace", Tools.getStackTrace(exceptions[i]));
				}
			}
			parameters.put("client_version", ContestOrg.VERSION);
			if(FrontModel.get().getConcours() != null) {
				try {
					parameters.put("client_xml", XMLHelper.toString(PersistanceXML.getConcoursDocument()));
				} catch(Exception e) {
					parameters.put("client_xml", "Exception soulevé lors de la récupération des données du tournoi au format XML :\r\n\r\n"+Tools.getStackTrace(e));					
				}
			}
			parameters.put("environnement_os", System.getProperty("os.name"));
			parameters.put("environnement_java", System.getProperty("java.version"));

			// Construire la liste des fichiers
			Map<String, File> files = new HashMap<String, File>();
			files.put("client_log", new File("log.txt"));
			
			// Envoie du rapport d'erreur
			HTTPHelper.Browser browser = new HTTPHelper.Browser();
			browser.post("http://www.cyrilperrin.fr", 80, "/contestorg/api/error", parameters, files);
			
			// Avancement
			this.fireAvancement(1.0);
			
			// Opération réussie
			this.reussite("Rapport envoyé !");
		}

		/**
		 * @see OperationRunnableAbstract#clean()
		 */
		@Override
		protected void clean () {
		}
	}
}

