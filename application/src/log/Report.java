package log;

import interfaces.IOperation;

import java.util.Stack;

import common.OperationAbstract;
import common.OperationRunnableAbstract;

public class Report {
	
	// Description du problème
	private String description;
	
	// Pile des exceptions
	private Stack<Exception> exceptions = new Stack<Exception>();
	
	// Constructeur
	public Report(String description) {
		// Retenir la description
		this.description = description;
	}
	public Report(String description, Exception... exceptions) {
		// Appeller le constructeur principal
		this(description);
		
		// Ajouter les exceptions
		for(Exception exception : exceptions) {
			this.pushException(exception);
		}
	}
	
	// Ajouter une exception
	public void pushException(Exception exception) {
		this.exceptions.push(exception);
	}
	
	// Envoyer le rapport
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
	
	// Classe permettant l'envoi du rapport
	private class Send extends OperationRunnableAbstract {
		// Implémentation de run
		@Override
		public void run () {
			// Envoi du rapport
			this.fireMessage("Envoi du rapport ..."); // TODO Envoie du rapport d'erreur sur le site web de contestorg
			
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

		// Implémentation de clean
		@Override
		protected void clean () {
		}
	}
	
}

