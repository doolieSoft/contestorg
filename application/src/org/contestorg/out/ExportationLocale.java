package org.contestorg.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.contestorg.common.ContestOrgWarningException;
import org.contestorg.common.OperationAbstract;
import org.contestorg.common.OperationRunnableAbstract;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.IOperation;

/**
 * Exportation locale
 */
public class ExportationLocale extends ExportationAbstract
{
	
	/** Thème */
	private InfosModelTheme theme;
	
	/** Chemin */
	private String chemin;
	
	/**
	 * Constructeur
	 * @param theme thème
	 * @param chemin chemin
	 */
	public ExportationLocale(InfosModelTheme theme, String chemin) {
		// Retenir les informations
		this.theme = theme;
		this.chemin = chemin;
	}
	
	/**
	 * @see ExportationAbstract#export()
	 */
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
	
	/**
	 * Classe permettant l'export
	 */
	private class Export extends OperationRunnableAbstract
	{
		/** Ressources */
		private ArrayList<ThemeRessourceAbstract> ressources;
		
		/**
		 * @see Runnable#run()
		 */
		@Override
		public void run () {
			// Générer les ressources
			this.fireMessage("Génération des ressources ...");
			try {
				this.ressources = ThemeRessourceAbstract.getRessources(theme,false);
			} catch (ContestOrgWarningException e) {
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
		
		/**
		 * @see OperationRunnableAbstract#clean()
		 */
		@Override
		protected void clean () {
			// Nettoyer les ressources
			if(this.ressources != null) {
				for (ThemeRessourceAbstract ressource : this.ressources) {
					ressource.clean();
				}
			}
		}
	}
	
}
