package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelProprietePossedee;

/**
 * Boîte de dialogue d'importation de participants
 */
@SuppressWarnings("serial")
public class JDImporterParticipants extends JDPattern
{
	/** Panel des catégories et poules */
	private JPCategoriePoule jp_categoriePoule = new JPCategoriePoule();
	
	/** Bouton de choix du fichier */
	private JButton jb_fichier = new JButton("Séléctionner", new ImageIcon("img/farm/16x16/folder.png"));
	
	/** Panel des participants */
	private JPanel jp_participants = new JPanel();
	
	/** Liste des participants */
	private ArrayList<InfosModelParticipant> participants = new ArrayList<InfosModelParticipant>();
	
	/** Liste des propriétés */
	private ArrayList<TrackableList<Pair<String,InfosModelProprietePossedee>>> proprietes = new ArrayList<TrackableList<Pair<String,InfosModelProprietePossedee>>>();
	
	/** Cases à cocher des participants */
	private ArrayList<JCheckBox> cs_participants = new ArrayList<JCheckBox>();
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param nomCategorie nom de la catégorie séléctionnée
	 * @param nomPoule nom de la poule séléctionnée
	 */
	public JDImporterParticipants(Window w_parent, String nomCategorie, String nomPoule) {
		// Appeller le constructeur du panret
		super(w_parent, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Importer des équipes" : "Importer des joueurs");
		
		// Nom de la catégorie et de la poule
		this.jp_contenu.add(this.jp_categoriePoule);
		
		// Choix du fichier
		this.jp_contenu.add(ViewHelper.title("Choix du fichier", ViewHelper.H1));
		this.jp_contenu.add(ViewHelper.left(this.jb_fichier));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.pinformation("Rendez-vous dans l'aide pour connaître le format de fichier à utiliser.","doc/Aide.pdf","Ouvrir l'aide au format PDF"));
		
		this.jb_fichier.addActionListener(this);
		
		// Participants trouvés
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes trouvées" : "Joueurs trouvés", ViewHelper.H1));
		
		this.jp_participants = new JPanel();
		this.jp_participants.setLayout(new BoxLayout(this.jp_participants, BoxLayout.Y_AXIS));
		
		JPanel jp_participants_largeur = new JPanel(new BorderLayout());
		jp_participants_largeur.add(this.jp_participants,BorderLayout.NORTH);
		
		JScrollPane jsp_participants = new JScrollPane(jp_participants_largeur);
		jsp_participants.setPreferredSize(new Dimension(jsp_participants.getPreferredSize().width, 200));
		
		this.jp_contenu.add(jsp_participants);
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer la catégorie et la poule de destination
		String categorie = this.jp_categoriePoule.getCategorie();
		String poule = this.jp_categoriePoule.getPoule();
		
		// Liste des prix vides
		TrackableList<String> prix = new TrackableList<String>();
		
		// Demander l'ajout des participants
		for(int i=0;i<this.participants.size();i++) {
			// Vérifier si la case à cocher est bien cochée
			if(this.cs_participants.get(i).isSelected()) {
				// Demander l'ajout du participant
				ContestOrg.get().getCtrlParticipants().addParticipant(new Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String,InfosModelProprietePossedee>>, TrackableList<String>>(categorie, poule, this.participants.get(i), this.proprietes.get(i), prix));
			}
		}
		
		// Fermer la fenêtre
		this.quit();
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
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed (ActionEvent event) {
		if(event.getSource() == this.jb_fichier) {
			// Demander le chemin du fichier à l'utilisateur
			String chemin = ViewHelper.ouvrir(this, "Séléctionner", FiltreFichier.ext_xml, FiltreFichier.des_xml);
			
			// Vérifier si l'utilisateur a défini un chemin
			if(chemin != null) {
				// Effacer la liste des participants et des propriétés
				this.participants.clear();
				this.proprietes.clear();
				
				// Demander la liste des participants et leurs propriétés contenus dans le fichier
				for(Pair<InfosModelParticipant, ArrayList<Pair<String, InfosModelProprietePossedee>>> participantProprietes : ContestOrg.get().getCtrlOut().importerParticipants(chemin)) {
					// Ajouter le participant
					this.participants.add(participantProprietes.getFirst());
					
					// Ajouter les propriétés
					TrackableList<Pair<String,InfosModelProprietePossedee>> proprietes = new TrackableList<Pair<String,InfosModelProprietePossedee>>();
					for(Pair<String, InfosModelProprietePossedee> propriete : participantProprietes.getSecond()) {
						proprietes.add(propriete);
					}
					this.proprietes.add(proprietes);
				}
				
				// Rafraichir la liste des participants
				this.refreshParticipants();
			}
		} else {
			// Appeller le actionPerformed du parent
			super.actionPerformed(event);
		}
	}
	
	/**
	 * Rafraichir la liste des participants
	 */
	public void refreshParticipants() {
		// Vider le panel des participants
		this.jp_participants.removeAll();
		
		// Mettre à jour la liste des participants
		this.cs_participants.clear();
		if(this.participants == null) {
			ViewHelper.derror(this, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Erreur lors de l'importation des équipes" : "Erreur lors de l'importation des joueurs");
		} else if(this.participants.size() == 0) {
			ViewHelper.derror(this, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Aucune équipe n'a été trouvé" : "Aucun joueur n'a été trouvé");
		} else {
			// Noms des participants ajoutés
			ArrayList<String> noms = new ArrayList<String>();
			
			// Mettre à jour la liste des participants
			for(InfosModelParticipant participant : new ArrayList<InfosModelParticipant>(this.participants)) {
				// Vérifier si le participant n'existe pas déjà
				if(ContestOrg.get().getCtrlParticipants().isParticipantExiste(participant.getNom())) {
					// Erreur
					ViewHelper.derror(this, (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" \""+participant.getNom()+"\" existe déjà.");
					
					// Retirer le participant de la liste
					this.participants.remove(participant);
				} else if(noms.contains(participant.getNom())) {
					// Erreur
					ViewHelper.derror(this, (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" \""+participant.getNom()+"\" a été retrouvé plusieurs fois dans le fichier.");
					
					// Retirer le participant de la liste
					this.participants.remove(participant);
				} else {
					// Créer la case à cocher et l'ajouter dans la liste et le panel
					JCheckBox jc_participant = new JCheckBox(participant.getNom());
					jc_participant.setSelected(true);
					this.cs_participants.add(jc_participant);
					this.jp_participants.add(jc_participant);
					
					// Comptabiliser le nom du participant
					noms.add(participant.getNom());
				}
			}
		}
		
		// Rafraichir le panel
		this.jp_participants.revalidate();
		
	}
}
