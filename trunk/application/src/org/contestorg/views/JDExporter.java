package org.contestorg.views;

import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.filechooser.FileSystemView;

import org.contestorg.common.Tools;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.Theme;
import org.contestorg.interfaces.IEndListener;
import org.contestorg.interfaces.IOperation;

/**
 * Boîte de dialogue d'exportation
 */
@SuppressWarnings("serial")
public class JDExporter extends JDPattern implements ItemListener
{
	/** Panel du thème */
	protected JPTheme jp_theme;
	
	/** Liste des catégories */
	private JComboBox<String> jcb_categorie = new JComboBox<String>();
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param categorie catégorie des thèmes d'exportation
	 * @param nomCategorie nom de la catégorie séléctionnée
	 * @param nomPoule nom de la poule séléctionnée
	 * @param numeroPhase nom de la poule séléctionnée
	 */
	public JDExporter(Window w_parent,String categorie, String nomCategorie, String nomPoule, Integer numeroPhase) {
		// Appeller le constructeur du parent
		super(w_parent, "Exporter");
		
		// FIXME Transmettre les informations nomCategorie, nomPoule et numeroPhase au panel
		
		// Ajouter la liste des catégorie
		this.jcb_categorie.addItem("Toutes");
		this.jcb_categorie.addItem(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs");
		this.jcb_categorie.addItem("Phases qualificatives");
		this.jcb_categorie.addItem("Phases éliminatoires");
		
		// Séléctionner la catégorie courante
		this.jcb_categorie.setSelectedIndex(Tools.stringCase(categorie, Theme.CATEGORIE_PARTICIPANTS, Theme.CATEGORIE_PHASES_QUALIFICATIVES, Theme.CATEGORIE_PHASES_ELIMINATOIRES)+1);
		
		// Ecouter la liste des catégories
		this.jcb_categorie.addItemListener(this);
		
		// Catégorie
		this.jp_contenu.add(ViewHelper.title("Catégorie", ViewHelper.H1));
		JLabel[] jls_categorie = { new JLabel("Catégorie : ") };
		JComponent[] jcs_categorie = { this.jcb_categorie };
		this.jp_contenu.add(ViewHelper.inputs(jls_categorie, jcs_categorie));
		
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

	/**
	 * @see JDPattern#ok()
	 */
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
	
	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Masquer la fenêtre
		this.setVisible(false);
	}

	/**
	 * @see ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void itemStateChanged (ItemEvent event) {
		switch(this.jcb_categorie.getSelectedIndex()) {
			case 0:
				this.jp_theme.setThemes(ContestOrg.get().getCtrlOut().getThemesExportation(),true);
				break;
			case 1:
				this.jp_theme.setThemes(ContestOrg.get().getCtrlOut().getThemesExportation(Theme.CATEGORIE_PARTICIPANTS),true);
				break;
			case 2:
				this.jp_theme.setThemes(ContestOrg.get().getCtrlOut().getThemesExportation(Theme.CATEGORIE_PHASES_QUALIFICATIVES),true);
				break;
			case 3:
				this.jp_theme.setThemes(ContestOrg.get().getCtrlOut().getThemesExportation(Theme.CATEGORIE_PHASES_ELIMINATOIRES),true);
				break;
		}
		
		// Pack
		this.pack();
	}
	
}
