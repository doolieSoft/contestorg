package org.contestorg.views;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création/édition d'un match des phases qualificatives
 */
@SuppressWarnings("serial")
public class JDMatchPhasesQualifsAbstract extends JDPattern implements ItemListener
{
	/** Collecteur des informations du match */
	private ICollector<Triple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs>> collector;
	
	// Entrées
	
	/** Nom de la catégorie de destination */
	private String nomCategorie;
	
	/** Nom de la poule de destination */
	private String nomPoule;
	
	/** Numéro de la phase qualificative de destination */
	private int numeroPhase;
	
	/** Participant A */
	protected JComboBox<String> jcb_participantA;
	
	/** Participant B */
	protected JComboBox<String> jcb_participantB;
	
	/** Résultat du participant A */
	protected JComboBox<String> jcb_resultatA;
	
	/** Résultat du participant B */
	protected JComboBox<String> jcb_resultatB;
	
	/** Panel des objectifs remportés */
	protected JPObjectifs jp_objectifs;
	
	/** Détails */
	protected JTextArea jta_details;
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector collecteur des informations du match
	 * @param nomCategorie nom de la catégorie de destination
	 * @param nomPoule nom de la poule de destination
	 * @param numeroPhase numéro de la phase qualificative de destination
	 */
	public JDMatchPhasesQualifsAbstract(Window w_parent, String titre, ICollector<Triple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, int numeroPhase) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir le collector, la catégorie, la poule et la phase qualificative
		this.collector = collector;
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
		this.numeroPhase = numeroPhase;
		
		// Participants
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs", ViewHelper.H1));
		
		ArrayList<String> participants = ContestOrg.get().getCtrlPhasesQualificatives().getListeParticipantsParticipants(this.nomCategorie,this.nomPoule);
		JPanel jp_participants = new JPanel(new GridLayout(1,2));
		this.jcb_participantA = new JComboBox<String>(participants.toArray(new String[participants.size()]));
		this.jcb_participantA.addItem(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome");
		this.jcb_participantB = new JComboBox<String>(participants.toArray(new String[participants.size()]));
		this.jcb_participantB.addItem(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome");
		jp_participants.add(this.jcb_participantA);
		jp_participants.add(this.jcb_participantB);
		this.jp_contenu.add(jp_participants);
		
		// Résultats
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.title("Résultats", ViewHelper.H1));
		
		JPanel jp_resultat = new JPanel(new GridLayout(1,2));
		String[] resultats = { "Attente", "Victoire", "Egalité", "Défaite", "Forfait" };
		this.jcb_resultatA = new JComboBox<String>(resultats);
		this.jcb_resultatB = new JComboBox<String>(resultats);
		jp_resultat.add(this.jcb_resultatA);
		jp_resultat.add(this.jcb_resultatB);
		this.jp_contenu.add(jp_resultat);

		this.jcb_resultatA.addItemListener(this);
		this.jcb_resultatB.addItemListener(this);
		
		// Objectifs remportés
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_objectifs = new JPObjectifs();
		this.jp_contenu.add(this.jp_objectifs);
		
		// Détails
		this.jp_contenu.add(ViewHelper.title("Détails", ViewHelper.H1));
		
		this.jta_details = new JTextArea();
		this.jta_details.setLineWrap(true);
		this.jta_details.setWrapStyleWord(true);
		this.jta_details.setRows(5);
		this.jp_contenu.add(new JScrollPane(this.jta_details));
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer les données
		String nomParticipantA = this.jcb_participantA.getSelectedIndex() == this.jcb_participantA.getItemCount()-1 ? null : (String)this.jcb_participantA.getSelectedItem();
		String nomParticipantB = this.jcb_participantB.getSelectedIndex() == this.jcb_participantB.getItemCount()-1 ? null : (String)this.jcb_participantB.getSelectedItem();
		int resultatA = 0;
		switch(this.jcb_resultatA.getSelectedIndex()) {
			case 0:
				resultatA = InfosModelParticipation.RESULTAT_ATTENTE;
				break;
			case 1:
				resultatA = InfosModelParticipation.RESULTAT_VICTOIRE;
				break;
			case 2:
				resultatA = InfosModelParticipation.RESULTAT_EGALITE;
				break;
			case 3:
				resultatA = InfosModelParticipation.RESULTAT_DEFAITE;
				break;
			case 4:
				resultatA = InfosModelParticipation.RESULTAT_FORFAIT;
				break;
		}
		int resultatB = 0;
		switch(this.jcb_resultatB.getSelectedIndex()) {
			case 0:
				resultatB = InfosModelParticipation.RESULTAT_ATTENTE;
				break;
			case 1:
				resultatB = InfosModelParticipation.RESULTAT_VICTOIRE;
				break;
			case 2:
				resultatB = InfosModelParticipation.RESULTAT_EGALITE;
				break;
			case 3:
				resultatB = InfosModelParticipation.RESULTAT_DEFAITE;
				break;
			case 4:
				resultatB = InfosModelParticipation.RESULTAT_FORFAIT;
				break;
		}
		String details = this.jta_details.getText().trim();
		
		// Vérifier les données
		boolean erreur = false;
		if(nomParticipantA == null && nomParticipantB == null || nomParticipantA != null && nomParticipantA.equals(nomParticipantB)) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Une équipe ne peut pas avoir un match avec elle même." : "Un joueur ne peut pas avoir un match avec lui même.");
		} else if(this instanceof JDMatchPhasesQualifsCreer) {
			if(ContestOrg.get().getCtrlPhasesQualificatives().isParticipantPhaseQualif(this.nomCategorie,this.nomPoule,this.numeroPhase,nomParticipantA)) {
				if(!ViewHelper.confirmation(this, (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" "+nomParticipantA+" participe déjà à la phase qualificative. Désirez-vous continuer ?")) {
					erreur = true;
				}
			}
			if(!erreur && ContestOrg.get().getCtrlPhasesQualificatives().isParticipantPhaseQualif(this.nomCategorie,this.nomPoule,this.numeroPhase,nomParticipantB)) {
				if(!ViewHelper.confirmation(this, (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" "+nomParticipantB+" participe déjà à la phase qualificative. Désirez-vous continuer ?")) {
					erreur = true;
				}
			}
		}
		if(resultatA == InfosModelParticipation.RESULTAT_ATTENTE && resultatB != InfosModelParticipation.RESULTAT_ATTENTE || resultatA != InfosModelParticipation.RESULTAT_ATTENTE && resultatB == InfosModelParticipation.RESULTAT_ATTENTE ) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, "Le résultat du match n'est pas cohérent.");
		} else if(resultatA == InfosModelParticipation.RESULTAT_EGALITE && resultatB != InfosModelParticipation.RESULTAT_EGALITE || resultatA != InfosModelParticipation.RESULTAT_EGALITE && resultatB == InfosModelParticipation.RESULTAT_EGALITE) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, "Le résultat du match n'est pas cohérent.");
		}
		if(resultatA == InfosModelParticipation.RESULTAT_VICTOIRE && resultatB == InfosModelParticipation.RESULTAT_VICTOIRE) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, "Il ne peut y avoir qu'un seul vainqueur.");
		}
		
		// Transmettre les données au collector
		if(!erreur) {
			// Récupérer la liste des prix remportés
			if(resultatA == InfosModelParticipation.RESULTAT_ATTENTE || resultatB == InfosModelParticipation.RESULTAT_ATTENTE) {
				this.jp_objectifs.clear();
			} else {
				this.jp_objectifs.collect();
			}
			TrackableList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesA = this.jp_objectifs.getObjectifsRemportesA();
			TrackableList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesB = this.jp_objectifs.getObjectifsRemportesB();
			
			// Créer les informations de participation
			Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation> participationA = new Triple<String, TrackableList<Pair<String,InfosModelObjectifRemporte>>, InfosModelParticipation>(nomParticipantA, objectifsRemportesA, new InfosModelParticipation(resultatA));
			Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation> participationB = new Triple<String, TrackableList<Pair<String,InfosModelObjectifRemporte>>, InfosModelParticipation>(nomParticipantB, objectifsRemportesB, new InfosModelParticipation(resultatB));
			
			// Transmettre les données au collector
			this.collector.collect(new Triple<Triple<String,TrackableList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Triple<String,TrackableList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, InfosModelMatchPhasesQualifs>(participationA , participationB, new InfosModelMatchPhasesQualifs(null,details)));
		}
	}
	
	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}

	/**
	 * @see ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void itemStateChanged (ItemEvent event) {
		if(event.getStateChange() == ItemEvent.SELECTED) {
			// Modifier la liste non modifiée si nécéssaire
			JComboBox<String> jcb_event = event.getSource() == this.jcb_resultatA ? this.jcb_resultatA : this.jcb_resultatB;
			JComboBox<String> jcb_other = event.getSource() == this.jcb_resultatA ? this.jcb_resultatB : this.jcb_resultatA;
			switch(jcb_event.getSelectedIndex()) {
				case 0: // Attente
					jcb_other.setSelectedIndex(0);
					break;
				case 1: // Victoire
					if(jcb_other.getSelectedIndex() == 0) {
						jcb_other.setSelectedIndex(3);
					}
					break;
				case 2: // Egalite
					jcb_other.setSelectedIndex(2);
					break;
				case 3: // Defaite
					if(jcb_other.getSelectedIndex() == 0) {
						jcb_other.setSelectedIndex(1);
					}
					break;
				case 4: // Forfait
					break;
			}
		}
	}
	
}
