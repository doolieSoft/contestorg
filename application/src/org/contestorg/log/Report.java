package org.contestorg.log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.contestorg.common.OperationAbstract;
import org.contestorg.common.OperationRunnableAbstract;
import org.contestorg.interfaces.IOperation;
import org.contestorg.out.HTTPHelper;

/**
 * Rapport d'erreur
 */
public class Report 
{
	/** Description de l'erreur */
	private String description;
	
	/**
	 * Constructeur
	 * @param description description de l'erreur
	 */
	public Report(String description) {
		// Retenir la description
		this.description = description;
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
			parameters.put("description", description);

			// Construire la liste des fichiers
			Map<String, File> files = new HashMap<String, File>();
			files.put("log", new File("log.txt"));
			
			// Envoie du rapport d'erreur
			HTTPHelper.Browser browser = new HTTPHelper.Browser();
			browser.post("http://localhost", 80, "/Projets/ContestOrg/api/error", parameters, files);
			
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

