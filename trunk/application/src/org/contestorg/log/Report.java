package org.contestorg.log;


import java.util.Stack;

import org.contestorg.common.OperationAbstract;
import org.contestorg.common.OperationRunnableAbstract;
import org.contestorg.interfaces.IOperation;

/**
 * Rapport d'erreur
 */
public class Report 
{
	/** Description de l'erreur */
	private String description;
	
	/** Pile des exceptions */
	private Stack<Exception> exceptions = new Stack<Exception>();
	
	/**
	 * Constructeur
	 * @param description description de l'erreur
	 */
	public Report(String description) {
		// Retenir la description
		this.description = description;
	}
	
	/**
	 * Constructeur
	 * @param description description de l'erreur
	 * @param exceptions liste des exceptions
	 */
	public Report(String description, Exception... exceptions) {
		// Appeller le constructeur principal
		this(description);
		
		// Ajouter les exceptions
		for(Exception exception : exceptions) {
			this.pushException(exception);
		}
	}
	
	/**
	 * Ajouter une exception
	 * @param exception exception
	 */
	public void pushException(Exception exception) {
		this.exceptions.push(exception);
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
			
			// TODO Envoie du rapport d'erreur sur le site web de contestorg
			try {
				Thread.sleep(1000);
				this.fireAvancement(0.2);
				Thread.sleep(1000);
				this.fireAvancement(0.4);
				Thread.sleep(1000);
				this.fireAvancement(0.6);
				Thread.sleep(1000);
				this.fireAvancement(0.8);
				Thread.sleep(1000);
				this.fireAvancement(1.0);
			} catch (InterruptedException e) {
			}
			
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

