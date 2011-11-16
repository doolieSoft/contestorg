package org.contestorg.views;


import java.awt.Window;

import org.contestorg.common.Triple;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.ICollector;


@SuppressWarnings("serial")
public class JDPhaseQualifCreer extends JDPhaseQualifAbstract
{

	// Constructeur
	public JDPhaseQualifCreer(Window w_parent, ICollector<Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une phase qualificative", collector, nomCategorie, nomPoule);		
	}
	
}
