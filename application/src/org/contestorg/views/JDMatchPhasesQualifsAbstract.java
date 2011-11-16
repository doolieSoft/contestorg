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
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.interfaces.ICollector;



@SuppressWarnings("serial")
public class JDMatchPhasesQualifsAbstract extends JDPattern implements ItemListener
{
	// Collector
	private ICollector<Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs>> collector;
	
	// Catégorie, poule et phase qualificative
	private String nomCategorie;
	private String nomPoule;
	private int numeroPhase;
	
	// Equipes
	protected JComboBox jcb_equipeA;
	protected JComboBox jcb_equipeB;
	
	// Résultats
	protected JComboBox jcb_resultatA;
	protected JComboBox jcb_resultatB;
	
	// Objectifs remportés
	protected JPObjectifs jp_prix;
	
	// Details
	protected JTextArea jta_details;
	
	// Constructeur
	public JDMatchPhasesQualifsAbstract(Window w_parent, String titre, ICollector<Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, int numeroPhase) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir le collector, la catégorie, la poule et la phase qualificative
		this.collector = collector;
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
		this.numeroPhase = numeroPhase;
		
		// Equipes
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs", ViewHelper.H1));
		
		ArrayList<String> equipesPartcicipantes = ContestOrg.get().getCtrlPhasesQualificatives().getListeEquipesParticipantes(this.nomCategorie,this.nomPoule);
		JPanel jp_equipes = new JPanel(new GridLayout(1,2));
		this.jcb_equipeA = new JComboBox(equipesPartcicipantes.toArray(new String[equipesPartcicipantes.size()]));
		this.jcb_equipeA.addItem(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome");
		this.jcb_equipeB = new JComboBox(equipesPartcicipantes.toArray(new String[equipesPartcicipantes.size()]));
		this.jcb_equipeB.addItem(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome");
		jp_equipes.add(this.jcb_equipeA);
		jp_equipes.add(this.jcb_equipeB);
		this.jp_contenu.add(jp_equipes);
		
		// Résultats
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.title("Résultats", ViewHelper.H1));
		
		JPanel jp_resultat = new JPanel(new GridLayout(1,2));
		String[] resultats = { "Attente", "Victoire", "Egalité", "Défaite", "Forfait" };
		this.jcb_resultatA = new JComboBox(resultats);
		this.jcb_resultatB = new JComboBox(resultats);
		jp_resultat.add(this.jcb_resultatA);
		jp_resultat.add(this.jcb_resultatB);
		this.jp_contenu.add(jp_resultat);

		this.jcb_resultatA.addItemListener(this);
		this.jcb_resultatB.addItemListener(this);
		
		// Prix remportés
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_prix = new JPObjectifs();
		this.jp_contenu.add(this.jp_prix);
		
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

	// Implémentation de ok
	@Override
	protected void ok () {
		// Récupérer les données
		String nomEquipeA = this.jcb_equipeA.getSelectedIndex() == this.jcb_equipeA.getItemCount()-1 ? null : (String)this.jcb_equipeA.getSelectedItem();
		String nomEquipeB = this.jcb_equipeB.getSelectedIndex() == this.jcb_equipeB.getItemCount()-1 ? null : (String)this.jcb_equipeB.getSelectedItem();
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
		if(nomEquipeA == null && nomEquipeB == null || nomEquipeA != null && nomEquipeA.equals(nomEquipeB)) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Une équipe ne peut pas avoir un match avec elle même." : "Un joueur ne peut pas avoir un match avec lui même.");
		} else if(this instanceof JDMatchPhasesQualifsCreer) {
			if(ContestOrg.get().getCtrlPhasesQualificatives().isEquipeParticipantePhaseQualif(this.nomCategorie,this.nomPoule,this.numeroPhase,nomEquipeA)) {
				if(!ViewHelper.confirmation(this, (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" "+nomEquipeA+" participe déjà à la phase qualificative. Désirez-vous continuer ?")) {
					erreur = true;
				}
			}
			if(!erreur && ContestOrg.get().getCtrlPhasesQualificatives().isEquipeParticipantePhaseQualif(this.nomCategorie,this.nomPoule,this.numeroPhase,nomEquipeB)) {
				if(!ViewHelper.confirmation(this, (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" "+nomEquipeB+" participe déjà à la phase qualificative. Désirez-vous continuer ?")) {
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
				this.jp_prix.clear();
			} else {
				this.jp_prix.collect();
			}
			TrackableList<Pair<String, InfosModelParticipationObjectif>> objectifsRemportesA = this.jp_prix.getObjectifsRemportesA();
			TrackableList<Pair<String, InfosModelParticipationObjectif>> objectifsRemportesB = this.jp_prix.getObjectifsRemportesB();
			
			// Créer les informations de participation
			Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation> participationA = new Triple<String, TrackableList<Pair<String,InfosModelParticipationObjectif>>, InfosModelParticipation>(nomEquipeA, objectifsRemportesA, new InfosModelParticipation(resultatA));
			Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation> participationB = new Triple<String, TrackableList<Pair<String,InfosModelParticipationObjectif>>, InfosModelParticipation>(nomEquipeB, objectifsRemportesB, new InfosModelParticipation(resultatB));
			
			// Transmettre les données au collector
			this.collector.accept(new Triple<Triple<String,TrackableList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,TrackableList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, InfosModelMatchPhasesQualifs>(participationA , participationB, new InfosModelMatchPhasesQualifs(null,details)));
		}
	}
	
	// Implémentation de quit
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}

	// Implémentation de ItemListener
	@Override
	public void itemStateChanged (ItemEvent event) {
		if(event.getStateChange() == ItemEvent.SELECTED) {
			// Modifier la liste non modifiée si nécéssaire
			JComboBox jcb_event = event.getSource() == this.jcb_resultatA ? this.jcb_resultatA : this.jcb_resultatB;
			JComboBox jcb_other = event.getSource() == this.jcb_resultatA ? this.jcb_resultatB : this.jcb_resultatA;
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
