package org.contestorg.views;

import java.awt.Window;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'un match des phases qualificatives
 */
@SuppressWarnings("serial")
public class JDMatchPhasesQualifsCreer extends JDMatchPhasesQualifsAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du match
	 * @param nomCategorie nom de la catégorie de destination
	 * @param nomPoule nom de la poule de destination
	 * @param numeroPhase numéro de la phase qualificative de destination
	 */
	public JDMatchPhasesQualifsCreer(Window w_parent, ICollector<Triple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, int numeroPhase) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un match", collector, nomCategorie, nomPoule, numeroPhase);
	}
	
}
