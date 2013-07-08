package org.contestorg.views;

import java.awt.Window;

import org.contestorg.common.Quintuple;
import org.contestorg.infos.InfosConfiguration;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'une phase qualificative
 */
@SuppressWarnings("serial")
public class JDPhaseQualifCreer extends JDPhaseQualifAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de la phase qualificative
	 * @param nomCategorie nom de la catégorie de destination
	 * @param nomPoule nom de la poule de destination
	 */
	public JDPhaseQualifCreer(Window w_parent, ICollector<Quintuple<String,String,InfosConfiguration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une phase qualificative", collector, nomCategorie, nomPoule);		
	}
	
}
