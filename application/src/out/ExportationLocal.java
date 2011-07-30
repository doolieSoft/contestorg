package out;

import infos.InfosModelTheme;
import interfaces.IOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import common.OperationAbstract;
import common.OperationRunnableAbstract;

public class ExportationLocal extends ExportationAbstract
{
	
	// Thème
	private InfosModelTheme theme;
	
	// Chemin
	private String chemin;
	
	// Constructeur
	public ExportationLocal(InfosModelTheme theme, String chemin) {
		// Retenir les informations
		this.theme = theme;
		this.chemin = chemin;
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
			this.fireAvancement(0.3); // Avancement à 30%
			
			// Copier les ressources dans le chemin de destination
			this.fireMessage("Copie des ressources dans le repertoire de destination ...");
			for (int i=0;i<this.ressources.size();i++) {
				// Initialiser les flux
				FileInputStream streamSource = null;
				FileOutputStream streamResult = null;
				
				// Vérouiller la ressource
				this.ressources.get(i).lock();
				
				// Copier la ressource
				try {					
					// Alimenter les flux
					streamSource = new FileInputStream(new File(this.ressources.get(i).getFichier().getPath()));
					streamResult = new FileOutputStream(new File(chemin + this.ressources.get(i).getCible()));
					
					// Copier la ressource
					byte buffer[] = new byte[512 * 1024];
					int octets;
					while ((octets = streamSource.read(buffer)) != -1) {
						streamResult.write(buffer, 0, octets);
					}
				} catch (FileNotFoundException e) {
					this.echec("Erreur de la copie des ressource dans le repertoire de destination.");
					return;
				} catch (IOException e) {
					this.echec("Erreur de la copie des ressource dans le repertoire de destination.");
					return;
				} finally {
					// Fermer les flux
					if(streamSource != null) {
						try { streamSource.close(); } catch (IOException e) { }
					}
					if(streamResult != null) {
						try { streamResult.close(); } catch (IOException e) { }
					}
				}
				
				// Dévérouiller la ressource
				this.ressources.get(i).unlock();
				
				// Avancement
				this.fireAvancement(0.3+0.7*(i/this.ressources.size()));
			}
			this.fireMessage("Ressource copiées.");
			this.fireAvancement(1.0); // Avancement à 100%
			
			// Opération réussie
			this.reussite("Exportation terminée !");
		}
		
		// Implémentation de clean
		@Override
		protected void clean () {
			// Nettoyer les ressources
			if(this.ressources != null) {
				for (RessourceAbstract ressource : this.ressources) {
					ressource.clean();
				}
			}
		}
	}
	
}
