package org.contestorg.infos;

import java.util.Date;

/**
 * Conteneur d'informations pour la création ou la modification d'un match des phases qualificatives
 */
public class InfosModelMatchPhasesQualifs extends InfosModelMatch
{

	/**
	 * Constructeur
	 * @param date date
	 * @param details détails
	 */
	public InfosModelMatchPhasesQualifs(Date date,String details) {
		// Appeller le constructeur parent
		super(date,details);
	}

}
