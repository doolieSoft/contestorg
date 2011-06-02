package views;

import infos.Configuration;
import infos.InfosModelMatchPhasesQualifs;
import infos.InfosModelPhaseQualificative;
import interfaces.ICollector;

import java.awt.Window;

import common.Triple;

@SuppressWarnings("serial")
public class JDPhaseQualifCreer extends JDPhaseQualifAbstract
{

	// Constructeur
	public JDPhaseQualifCreer(Window w_parent, ICollector<Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une phase qualificative", collector, nomCategorie, nomPoule);		
	}
	
}
