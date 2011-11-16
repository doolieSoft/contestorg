package org.contestorg.views;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelEquipe;
import org.contestorg.infos.InfosModelProprieteEquipe;



@SuppressWarnings("serial")
public class JDImporterEquipes extends JDPattern
{
	// Panel des catégories et poules
	private JPCategoriePoule jp_categoriePoule = new JPCategoriePoule();
	
	// Bouton de choix du fichier
	private JButton jb_fichier = new JButton("Séléctionner", new ImageIcon("img/farm/16x16/folder.png"));
	
	// Panel des équipes
	private JPanel jp_equipes = new JPanel();
	
	// Liste des équipes
	private ArrayList<InfosModelEquipe> equipes = new ArrayList<InfosModelEquipe>();
	
	// Cases à cocher des équipes
	private ArrayList<JCheckBox> cs_equipes = new ArrayList<JCheckBox>();
	
	// Constructeur
	public JDImporterEquipes(Window w_parent, String nomCategorie, String nomPoule) {
		// Appeller le constructeur du panret
		super(w_parent, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Importer des équipes" : "Importer des joueurs");
		
		// Nom de la catégorie et de la poule
		this.jp_contenu.add(this.jp_categoriePoule);
		
		// Choix du fichier
		this.jp_contenu.add(ViewHelper.title("Choix du fichier", ViewHelper.H1));
		this.jp_contenu.add(ViewHelper.left(this.jb_fichier));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.pinformation("Rendez-vous dans l'aide pour récupérer le format de fichier à utiliser.","doc/Aide.pdf","Ouvrir l'aide au format PDF"));
		
		this.jb_fichier.addActionListener(this);
		
		// Equipes trouvées
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes trouvées" : "Joueurs trouvés", ViewHelper.H1));
		
		this.jp_equipes = new JPanel();
		this.jp_equipes.setLayout(new BoxLayout(this.jp_equipes, BoxLayout.Y_AXIS));
		
		JPanel jp_equipes_largeur = new JPanel(new BorderLayout());
		jp_equipes_largeur.add(this.jp_equipes,BorderLayout.NORTH);
		
		JScrollPane jsp_equipes = new JScrollPane(jp_equipes_largeur);
		jsp_equipes.setPreferredSize(new Dimension(jsp_equipes.getPreferredSize().width, 200));
		
		this.jp_contenu.add(jsp_equipes);
		
		// Pack
		this.pack();
	}

	// Implémentation de ok
	@Override
	protected void ok () {
		// Récupérer la catégorie et la poule de destination
		String categorie = this.jp_categoriePoule.getCategorie();
		String poule = this.jp_categoriePoule.getPoule();
		
		// Liste de propriété et de prix vides
		TrackableList<Pair<String,InfosModelProprieteEquipe>> proprietes = new TrackableList<Pair<String,InfosModelProprieteEquipe>>();
		TrackableList<String> prix = new TrackableList<String>();
		
		// Demander l'ajout des équipes
		for(int i=0;i<this.equipes.size();i++) {
			// Vérifier si la case à cocher est bien cochée
			if(this.cs_equipes.get(i).isSelected()) {
				// Demander l'ajout de l'équipe
				ContestOrg.get().getCtrlEquipes().addEquipe(new Quintuple<String, String, InfosModelEquipe, TrackableList<Pair<String,InfosModelProprieteEquipe>>, TrackableList<String>>(categorie, poule, this.equipes.get(i), proprietes, prix));
			}
		}
		
		// Fermer la fenêtre
		this.quit();
	}
	
	// Implémentation de quit
	@Override
	protected void quit () {
		// Masquer la fenetre
		this.setVisible(false);
	}
	
	// Surcharge de ActionListener
	public void actionPerformed (ActionEvent event) {
		if(event.getSource() == this.jb_fichier) {
			// Demander le chemin du fichier à l'utilisateur
			String chemin = ViewHelper.ouvrir(this, "Séléctionner", FiltreFichier.ext_xml, FiltreFichier.des_xml);
			
			// Vérifier si l'utilisateur a défini un chemin
			if(chemin != null) {
				// Demander la liste des équipes contenu dans le fichier
				this.equipes = ContestOrg.get().getCtrlOut().importerEquipes(chemin);
				
				// Rafraichir la liste des équipes
				this.refreshEquipes();
			}
		} else {
			// Appeller le actionPerformed du parent
			super.actionPerformed(event);
		}
	}
	
	// Rafraichir la liste des équipes
	public void refreshEquipes() {
		// Vider le panel des équipes
		this.jp_equipes.removeAll();
		
		// Mettre à jour la liste des équipes
		this.cs_equipes.clear();
		if(this.equipes == null) {
			ViewHelper.derror(this, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Erreur lors de l'importation des équipes" : "Erreur lors de l'importation des joueurs");
		} else if(this.equipes.size() == 0) {
			ViewHelper.derror(this, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Aucune équipe n'a été trouvé" : "Aucun joueur n'a été trouvé");
		} else {
			// Noms des équipes ajoutés
			ArrayList<String> noms = new ArrayList<String>();
			
			// Mettre à jour la liste des équipes
			for(InfosModelEquipe equipe : new ArrayList<InfosModelEquipe>(this.equipes)) {
				// Vérifier si l'équipe n'existe pas déjà
				if(ContestOrg.get().getCtrlEquipes().isEquipeExiste(equipe.getNom())) {
					// Erreur
					ViewHelper.derror(this, (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" \""+equipe.getNom()+"\" existe déjà.");
					
					// Retirer l'équipe de la liste
					this.equipes.remove(equipe);
				} else if(noms.contains(equipe.getNom())) {
					// Erreur
					ViewHelper.derror(this, (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" \""+equipe.getNom()+"\" a été retrouvé plusieurs fois dans le fichier.");
					
					// Retirer l'équipe de la liste
					this.equipes.remove(equipe);
				} else {
					// Créer la case à cocher et l'ajouter dans la liste et le panel
					JCheckBox jc_equipe = new JCheckBox(equipe.getNom());
					jc_equipe.setSelected(true);
					this.cs_equipes.add(jc_equipe);
					this.jp_equipes.add(jc_equipe);
					
					// Comptabiliser le nom de l'équipe
					noms.add(equipe.getNom());
				}
			}
		}
		
		// Rafraichir le panel
		this.jp_equipes.revalidate();
		
	}
}
