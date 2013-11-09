package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue d'édition d'un match des phases qualificatives
 */
@SuppressWarnings("serial")
public class JDMatchPhasesQualifsEditer extends JDMatchPhasesQualifsAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du match
	 * @param nomCategorie nom de la catégorie de destinaton
	 * @param nomPoule nom de la poule de destination
	 * @param numeroPhase numéro de la phase qualificative de destination
	 * @param infos informations du match
	 */
	public JDMatchPhasesQualifsEditer(Window w_parent, ICollector<Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, int numeroPhase, Quadruple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String,String>, InfosModelMatchPhasesQualifs> infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un match", collector, nomCategorie, nomPoule, numeroPhase);
		
		// Rajouter les participants qui ne peuvent plus participer mais qui ont joué pour ce match
		ArrayList<String> participants = ContestOrg.get().getCtrlPhasesQualificatives().getListeParticipantsParticipants(nomCategorie, nomPoule);
		if(infos.getFirst().getFirst() != null && !participants.contains(infos.getFirst().getFirst())) {
			this.jcb_participantA.insertItemAt(infos.getFirst().getFirst(), 0);
			this.jcb_participantB.insertItemAt(infos.getFirst().getFirst(), 0);
		}
		if(infos.getSecond().getFirst() != null && !participants.contains(infos.getSecond().getFirst())) {
			this.jcb_participantA.insertItemAt(infos.getSecond().getFirst(), 0);
			this.jcb_participantB.insertItemAt(infos.getSecond().getFirst(), 0);
		}
		
		// Remplir la liste des objectifs remportés
		this.jp_objectifs.setObjectifsRemportesA(infos.getFirst().getSecond());
		this.jp_objectifs.setObjectifsRemportesB(infos.getSecond().getSecond());
		
		// Remplir les entrées avec les informations du match
		if(infos.getFirst().getFirst() != null) {
			this.jcb_participantA.setSelectedItem(infos.getFirst().getFirst());
		} else {
			this.jcb_participantA.setSelectedIndex(this.jcb_participantA.getItemCount()-1);
		}
		if(infos.getSecond().getFirst() != null) {
			this.jcb_participantB.setSelectedItem(infos.getSecond().getFirst());
		} else {
			this.jcb_participantB.setSelectedIndex(this.jcb_participantB.getItemCount()-1);
		}
		switch(infos.getFirst().getThird().getResultat()) {
			case InfosModelParticipation.RESULTAT_ATTENTE:
				this.jcb_resultatA.setSelectedIndex(this.index_attente);
				break;
			case InfosModelParticipation.RESULTAT_VICTOIRE:
				this.jcb_resultatA.setSelectedIndex(this.index_victoire);
				break;
			case InfosModelParticipation.RESULTAT_EGALITE:
				if(this.index_egalite == -1) {
					this.addResultatEgalite();
				}
				this.jcb_resultatA.setSelectedIndex(this.index_egalite);
				break;
			case InfosModelParticipation.RESULTAT_DEFAITE:
				this.jcb_resultatA.setSelectedIndex(this.index_defaite);
				break;
			case InfosModelParticipation.RESULTAT_FORFAIT:
				this.jcb_resultatA.setSelectedIndex(this.index_forfait);
				break;
		}
		switch(infos.getSecond().getThird().getResultat()) {
			case InfosModelParticipation.RESULTAT_ATTENTE:
				this.jcb_resultatB.setSelectedIndex(this.index_attente);
				break;
			case InfosModelParticipation.RESULTAT_VICTOIRE:
				this.jcb_resultatB.setSelectedIndex(this.index_victoire);
				break;
			case InfosModelParticipation.RESULTAT_EGALITE:
				if(this.index_egalite == -1) {
					this.addResultatEgalite();
				}
				this.jcb_resultatB.setSelectedIndex(this.index_egalite);
				break;
			case InfosModelParticipation.RESULTAT_DEFAITE:
				this.jcb_resultatB.setSelectedIndex(this.index_defaite);
				break;
			case InfosModelParticipation.RESULTAT_FORFAIT:
				this.jcb_resultatB.setSelectedIndex(this.index_forfait);
				break;
		}
		if(infos.getFourth().getDate() != null) {
			this.jcb_date.setSelected(true);
			this.jp_datePicker.setDate(infos.getFourth().getDate());
		} else {
			this.jcb_date.setSelected(false);
			this.setDateVisible(false);
		}
		if(infos.getThird() != null) {
			this.jp_lieuEmplacement.setNomLieu(infos.getThird().getFirst());
			this.jp_lieuEmplacement.setNomEmplacement(infos.getThird().getSecond());
		}
		this.jta_details.setText(infos.getFourth().getDetails());
		
		// Pack
		this.pack();
	}
	
}
