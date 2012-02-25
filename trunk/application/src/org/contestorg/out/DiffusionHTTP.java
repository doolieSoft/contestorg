package org.contestorg.out;

import java.util.ArrayList;

import org.contestorg.common.OperationAbstract;
import org.contestorg.common.OperationRunnableAbstract;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.IOperation;
import org.contestorg.interfaces.IOperationListener;
import org.contestorg.log.Log;

/**
 * Diffusion HTTP
 */
public class DiffusionHTTP extends DiffusionAbstract implements IOperationListener
{
	
	/** Thème */
	private InfosModelTheme theme;
	
	/** Port */
	private int port;

	/** Serveur HTTP */
	private HTTPHelper.Server server;
	
	/**
	 * Constructeur
	 * @param theme thème
	 * @param port port
	 */
	public DiffusionHTTP(InfosModelTheme theme, int port) {
		// Retenir les informations
		this.port = port;
		this.theme = theme;
	}

	/**
	 * @see DiffusionAbstract#getStartOperation()
	 */
	public IOperation getStartOperation () {
		// Créer l'opération
		IOperation operation = new OperationAbstract() {
			@Override
			public OperationRunnableAbstract getRunnable () {
				return new Start();
			}
		};
		
		// Ecouter l'opération
		operation.addListener(this);
		
		// Retourner l'opération
		return operation;
	}
	
	/**
	 * @see DiffusionAbstract#getStopOperation()
	 */
	public IOperation getStopOperation() {
		// Créer l'opération
		IOperation operation = new OperationAbstract() {
			@Override
			public OperationRunnableAbstract getRunnable () {
				return new Stop();
			}
		};
		
		// Ecouter l'opération
		operation.addListener(this);
		
		// Retourner l'opération
		return operation;
	}
	
	/**
	 * Classe permettant le démarrage du serveur HTTP
	 */
	private class Start extends OperationRunnableAbstract {
		/**
		 * @see Runnable#run()
		 */
		@Override
		public void run () {
			try {
				// Générer les ressources
				this.fireMessage("Génération des ressources ...");
				ArrayList<RessourceAbstract> ressources;
				try {
					ressources = RessourceAbstract.getRessources(theme,true);
				} catch (ContestOrgOutException e) {
					this.echec(e.getMessage());
					return;
				}
				if(ressources == null || ressources.size() == 0) {
					this.echec("Aucune ressource valide n'a été trouvée dans le thème.");
					return;
				}
				this.fireMessage("Ressource générées.");
				this.fireAvancement(0.2); // Avancement à 20%
				
				// Créer le serveur HTTP
				server = new HTTPHelper.Server(port);
				
				// Initialiser le serveur HTTP
				this.fireMessage("Tentative d'initialisation du serveur HTTP ...");
				if(!server.init()) {
					this.echec("Erreur d'initialisation du serveur HTTP (vérifiez si le port n'est pas occupé).");
					return;
				}
				this.fireAvancement(0.4); // Avancement à 40%
				
				// Ajouter les ressouces du thème
				this.fireMessage("Ajout des ressources dans le serveur HTTP ...");
				for(RessourceAbstract ressource : ressources) {
					// Ajouter la ressource
					server.addRessource(ressource);
				}
				this.fireAvancement(0.6); // Avancement à 60%
				
				// Démarrer le serveur HTTP
				this.fireMessage("Démarrage du serveur HTTP ...");
				server.start();
				this.fireMessage("Serveur HTTP démarré.");
				this.fireAvancement(1.0); // Avancement à 100%
				
				// Opération réussie
				this.reussite("Diffusion démarrée !", false);
			} catch(Exception e) {
				// Log de l'erreur
				Log.getLogger().error("Erreur lors du démarrage de la diffusion.", e);
				
				// Echec de l'opération
				this.echec("Erreur lors du démarrage de la diffusion.");
			}
		}
		
		/**
		 * @see OperationRunnableAbstract#clean()
		 */
		@Override
		protected void clean () {
			// Arreter le serveur HTTP
			if(server != null && server.isStarted()) {
				server.stop();
			}
		}
	}

	/**
	 * Classe permettant l'arret du serveur HTTP
	 */
	private class Stop extends OperationRunnableAbstract {
		// Implémentation de run
		@Override
		public void run () {
			// Arreter le serveur
			this.fireMessage("Arret du serveur HTTP ...");
			if(server != null) {
				server.stop();
			}
			this.fireMessage("Serveur HTTP arreté.");
			this.fireAvancement(1.0); // Avancement à 100%
			
			// Opération réussie
			this.reussite("Diffusion arretée !");
		}
		
		// Implémentation de clean
		@Override
		protected void clean () {
		}
	}

	// Implémentation de IOperationListener
	@Override
	public void progressionAvancement (double progression) {
	}
	@Override
	public void progressionMessage (String message) {
	}
	@Override
	public void progressionFin () {
	}
	@Override
	public void operationReussite () {
		if(this.server != null && this.server.isStarted()) {
			this.fireStart();
		} else {
			this.fireStop();
		}
	}
	@Override
	public void operationEchec () {
	}
	@Override
	public void operationArret () {
	}
}
