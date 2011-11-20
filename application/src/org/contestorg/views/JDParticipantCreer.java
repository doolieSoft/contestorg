package org.contestorg.views;


import java.awt.Window;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelProprieteParticipant;
import org.contestorg.interfaces.ICollector;



@SuppressWarnings("serial")
public class JDParticipantCreer extends JDParticipantAbstract
{

	// Constructeur
	public JDParticipantCreer(Window w_parent, ICollector<Quintuple<String,String,InfosModelParticipant,TrackableList<Pair<String,InfosModelProprieteParticipant>>,TrackableList<String>>> collector, String nomCategorie, String nomPoule) {
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

	// Implémentation de checkNomParticipant
	@Override
	protected boolean checkNomParticipant () {
		// Retourner true s'il n'y a pas de participant qui a le même nom
		return !ContestOrg.get().getCtrlParticipants().isParticipantExiste(this.jtf_nom.getText().trim());
	}
	
}
