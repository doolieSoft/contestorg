package org.contestorg.views;


import java.awt.Window;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelEquipe;
import org.contestorg.infos.InfosModelProprieteEquipe;
import org.contestorg.interfaces.ICollector;



@SuppressWarnings("serial")
public class JDEquipeCreer extends JDEquipeAbstract
{

	// Constructeur
	public JDEquipeCreer(Window w_parent, ICollector<Quintuple<String,String,InfosModelEquipe,TrackableList<Pair<String,InfosModelProprieteEquipe>>,TrackableList<String>>> collector, String nomCategorie, String nomPoule) {
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

	// Implémentation de checkNomEquipe
	@Override
	protected boolean checkNomEquipe () {
		// Retourner true s'il n'y a pas d'équipe qui a le meme nom
		return !ContestOrg.get().getCtrlEquipes().isEquipeExiste(this.jtf_nom.getText().trim());
	}
	
}
