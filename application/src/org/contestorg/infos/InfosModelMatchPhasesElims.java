package org.contestorg.infos;

import java.util.Date;

/**
 * Conteneur d'informations pour la création ou la modification d'un match des phases éliminatoires
 */
public class InfosModelMatchPhasesElims extends InfosModelMatch
{

	/**
	 * Constructeur
	 * @param date date
	 * @param details détails
	 */
	public InfosModelMatchPhasesElims(Date date,String details) {
		// Appeller le constructeur parent
		super(date,details);
	}

}
