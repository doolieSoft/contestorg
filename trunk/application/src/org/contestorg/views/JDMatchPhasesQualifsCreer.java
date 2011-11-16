package org.contestorg.views;


import java.awt.Window;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.interfaces.ICollector;


@SuppressWarnings("serial")
public class JDMatchPhasesQualifsCreer extends JDMatchPhasesQualifsAbstract
{

	// Constructeur
	public JDMatchPhasesQualifsCreer(Window w_parent, ICollector<Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, int numeroPhase) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un match", collector, nomCategorie, nomPoule, numeroPhase);
	}
	
}
