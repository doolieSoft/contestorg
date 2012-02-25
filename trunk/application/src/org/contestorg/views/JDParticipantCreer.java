package org.contestorg.views;

import java.awt.Window;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelProprietePossedee;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'un participant
 */
@SuppressWarnings("serial")
public class JDParticipantCreer extends JDParticipantAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du participant
	 * @param nomCategorie nom de la catégorie de destination
	 * @param nomPoule nom de la poule de destination
	 */
	public JDParticipantCreer(Window w_parent, ICollector<Quintuple<String,String,InfosModelParticipant,TrackableList<Pair<String,InfosModelProprietePossedee>>,TrackableList<String>>> collector, String nomCategorie, String nomPoule) {
		// Appeller le constructeur du parent
		super(w_parent, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Ajouter une équipe" : "Ajouter un joueur",collector);
		
		// Séléctionner la catégorie et la poule si nécéssaire
		if(nomCategorie != null) {
			this.jp_categoriePoule.setCategorie(nomCategorie);
			if(nomPoule != null) {
				this.jp_categoriePoule.setPoule(nomPoule);
			}
		}
	}

	/**
	 * @see JDParticipantAbstract#checkNomParticipant()
	 */
	@Override
	protected boolean checkNomParticipant () {
		// Retourner true s'il n'y a pas de participant qui a le même nom
		return !ContestOrg.get().getCtrlParticipants().isParticipantExiste(this.jtf_nom.getText().trim());
	}
	
}
