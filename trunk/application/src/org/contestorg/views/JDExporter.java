package org.contestorg.views;


import java.awt.Window;
import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.contestorg.controlers.ContestOrg;
import org.contestorg.interfaces.IEndListener;
import org.contestorg.interfaces.IOperation;


@SuppressWarnings("serial")
public class JDExporter extends JDPattern
{
	// Panel du thème
	protected JPTheme jp_theme;
	
	// Constructeur
	public JDExporter(Window w_parent,String categorie, String nomCategorie, String nomPoule, Integer numeroPhase) {
		// Appeller le constructeur du parent
		super(w_parent, "Exporter");
		
		// FIXME Transmettre les informations nomCategorie, nomPoule et numeroPhase au panel
		
		// Informations sur le thème
		this.jp_contenu.add(ViewHelper.title("Thème", ViewHelper.H1));
		this.jp_theme = new JPTheme(this,ContestOrg.get().getCtrlOut().getThemesExportation(categorie),true);
		this.jp_contenu.add(this.jp_theme);
		if(this.jp_theme.isError()) {
			this.jb_valider.setEnabled(false);
		}
		
		// Pack
		this.pack();
	}

	// Implémentation de ok
	@Override
	protected void ok () {
		// Vérifier si le thème est correcte
		if(this.jp_theme.getInfosModelTheme() != null) {
			// Récupérer le chemin de destination
			String chemin;
			if(this.jp_theme.getNbFichiers() == 1) {
				// Récupérer la fichier cible par défaut
				File defaut = new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()+File.separator+this.jp_theme.getCible());
				
				// Récupérer le chemin du fichier cible
				chemin = ViewHelper.sauvegarder(this, "Enregistrer le résultat de l'exportation", null, null, defaut);
				
				// Vérifier si le chemin a bien été donné
				if(chemin != null) {
					// Récupérer le fichier cible correspondant au chemin
					File fichier = new File(chemin);
					
					// Récupérer et placer le chemin du fichier
					this.jp_theme.setCible(fichier.getName());
					
					// Récupérer le chemin du répertoire cible
					chemin = fichier.getParent();
				}
			} else {
				// Récupérer le chemin du répertoire cible
				chemin = ViewHelper.selectionner(this, "Séléctionner le répertoire de destination");
			}
			
			// Vérifier si le chemin a bien été donné
			if(chemin != null) {
				// S'assurer que le chemin finisse par un séparateur de fichier
				chemin = chemin.endsWith(File.separator) ? chemin : chemin+File.separator;
				
				// Récupérer l'opération
				IOperation operation = ContestOrg.get().getCtrlOut().getLancerExportationOperation(chemin, this.jp_theme.getInfosModelTheme());
				
				// Créer la fenêtre associée à l'opération
				JDOperation jd_operation = new JDOperation(this,"Exportation",operation,true,true);
				
				// Démarrer l'opération
				operation.operationStart();
				
				// Fermer la fenêtre à la fin de l'exportation
				jd_operation.addListener(new IEndListener() {
					@Override
					public void end () {
						quit();
					}
				});
				
				// Afficher la fenêtre
				jd_operation.setVisible(true);
			}
		}
	}
	
	// Implémentation de quit
	@Override
	protected void quit () {
		// Masquer la fenêtre
		this.setVisible(false);
	}
	
}
