package views;

import infos.InfosModelMatchPhasesQualifs;
import infos.InfosModelParticipation;
import infos.InfosModelParticipationObjectif;
import interfaces.ICollector;

import java.awt.Window;

import common.TrackableList;
import common.Pair;
import common.Triple;

@SuppressWarnings("serial")
public class JDMatchPhasesQualifsCreer extends JDMatchPhasesQualifsAbstract
{

	// Constructeur
	public JDMatchPhasesQualifsCreer(Window w_parent, ICollector<Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, int numeroPhase) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un match", collector, nomCategorie, nomPoule, numeroPhase);
	}
	
}
