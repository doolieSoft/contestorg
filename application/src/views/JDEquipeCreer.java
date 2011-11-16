package views;

import infos.InfosModelConcours;
import infos.InfosModelEquipe;
import infos.InfosModelProprieteEquipe;
import interfaces.ICollector;

import java.awt.Window;

import common.TrackableList;
import common.Pair;
import common.Quintuple;

import controlers.ContestOrg;

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
