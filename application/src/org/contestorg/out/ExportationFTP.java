package org.contestorg.out;


import java.util.ArrayList;

import org.contestorg.common.OperationAbstract;
import org.contestorg.common.OperationRunnableAbstract;
import org.contestorg.infos.InfosConnexionFTP;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.IOperation;



public class ExportationFTP extends ExportationAbstract
{
	
	// Thème
	private InfosModelTheme theme;
	
	// Informations de connection FTP
	private InfosConnexionFTP infos;

	// Constructeur
	public ExportationFTP(InfosModelTheme theme, InfosConnexionFTP infos) {
		// Retenir les informations
		this.theme = theme;
		this.infos = infos;
	}

	// Implémentation de export
	@Override
	public IOperation export () {
		// Créer et retourner l'opération
		return new OperationAbstract() {
			@Override
			public OperationRunnableAbstract getRunnable () {
				return new Export();
			}
		};
	}
	
	// Classe permettant l'export
	private class Export extends OperationRunnableAbstract
	{
		// Ressources
		private ArrayList<RessourceAbstract> ressources;
		
		// Client FTP
		private FTPHelper.Client client;
		
		// Client FTP connecté ?
		private boolean clientConnected = false;
		
		// Implémentation de run
		@Override
		public void run () {
			// Générer les ressources
			this.fireMessage("Génération des ressources ...");
			try {
				this.ressources = RessourceAbstract.getRessources(theme,false);
			} catch (ContestOrgOutException e) {
				this.echec(e.getMessage());
				return;
			}
			if (this.ressources == null || this.ressources.size() == 0) {
				this.echec("Aucune ressource valide n'a été trouvée dans le thème.");
				return;
			}
			this.fireMessage("Ressource générées.");
			this.fireAvancement(0.2); // Avancement à 20%
			
			// Vérifier si le test doit etre annulé
			if(this.arret) {
				this.arret(); return;
			}
			
			// Créer le client FTP
			this.fireMessage("Initialisation du client FTP ...");
			this.client = new FTPHelper.Client();
			
			// Vérifier si le test doit etre annulé
			if(this.arret) {
				this.arret(); return;
			}
			
			// Se connecter au serveur
			this.fireMessage("Connexion au serveur FTP ...");
			if (!this.client.connect(infos.getHost(), infos.getPort())) {
				this.echec("Erreur lors de la connexion au serveur FTP (vérifiez l'hote et le port)");
				return;
			}
			this.clientConnected = true;
			this.fireAvancement(0.4); // Avancement à 40%
			
			// Vérifier si le test doit etre annulé
			if(this.arret) {
				this.arret(); return;
			}
	
			// S'identifier sur le serveur
			this.fireMessage("Identification sur le serveur FTP ...");
			if (!this.client.login(infos.getUsername(), infos.getPassword())) {
				this.echec("Erreur d'identification sur le serveur FTP (vérifiez vos identifiants)");
				return;
			}
			this.fireAvancement(0.5); // Avancement à 50%
			
			// Vérifier si le test doit etre annulé
			if(this.arret) {
				this.arret(); return;
			}
	
			// Choix du mode passif/actif
			this.fireMessage("Choix du mode passif/actif ...");
			switch (infos.getMode()) {
				case InfosConnexionFTP.MODE_ACTIF:
					this.client.setActiveMode();
					break;
				case InfosConnexionFTP.MODE_PASSIF:
					this.client.setPassiveMode();
					break;
			}
			
			// Vérifier si le test doit etre annulé
			if(this.arret) {
				this.arret(); return;
			}
			
			// Vérouiller les ressources
			for(int i=0;i<this.ressources.size();i++) {
				this.ressources.get(i).lock();
			}
			
			// Récupérer la taille totale des fichiers
			long total = 0;
			for(int i=0;i<this.ressources.size();i++) {
				total += this.ressources.get(i).getFichier().length();
			}
			
			// Envoyer le fichier de test sur le serveur FTP
			long length = 0;
			this.fireMessage("Envoie des ressources sur le serveur FTP ...");
			for(int i=0;i<this.ressources.size();i++) {				
				// Envoyer la ressource si nécéssaire
				if(!this.client.isFileExists(this.ressources.get(i).getFichier(), infos.getPath() + this.ressources.get(i).getCible())) {
					if (!this.client.sendFile(this.ressources.get(i).getFichier(), infos.getPath() + this.ressources.get(i).getCible())) {
						this.echec("Erreur lors de l'envoie du fichier de test sur le serveur FTP (vérifiez le chemin et les droits d'accès)");
						return;
					}
				}
				
				// Avancement
				this.fireAvancement(0.5+0.4*(length/total));
				
				// Comptabiliser la taille du fichier
				length += this.ressources.get(i).getFichier().length();
			}
			this.fireAvancement(0.9); // Avancement à 90%
			
			// Dévérouiller les ressources
			for(int i=0;i<this.ressources.size();i++) {
				this.ressources.get(i).unlock();
			}

			// Vérifier si le test doit etre annulé
			if(this.arret) {
				this.arret(); return;
			}
	
			// Se déconnecter du serveur FTP
			this.fireMessage("Deconnexion du serveur FTP ...");
			this.client.disconnect();
			this.clientConnected = false;
			this.fireMessage("Deconnecté du serveur FTP");
			this.fireAvancement(1.0); // Avancement à 100%
			
			// Opération réussie
			this.reussite("Exportation terminée !");
		}
		
		// Implémentation de clean
		@Override
		protected void clean () {
			// Se déconnecter du serveur FTP 
			if(this.client != null && this.clientConnected) {
				this.client.disconnect();
			}
			
			// Nettoyer les ressources
			if(this.ressources != null) {
				for (RessourceAbstract ressource : this.ressources) {
					ressource.clean();
				}
			}
		}
	}
}
