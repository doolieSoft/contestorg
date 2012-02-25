package org.contestorg.infos;

import java.util.Date;

/**
 * Conteneur d'informations pour la création ou la modification d'un match
 */
public abstract class InfosModelMatch extends InfosModelAbstract
{
	/** Date */
	private Date date;
	
	/** Détails */
	private String details;
	
	/**
	 * Constructeur
	 * @param date date
	 * @param details détails
	 */
	public InfosModelMatch(Date date,String details) {
		this.details = details;
	}
	
	/**
	 * Récupérer la date
	 * @return date
	 */
	public Date getDate() {
		return this.date;
	}
	
	/**
	 * Récupérer les détails
	 * @return détails
	 */
	public String getDetails() {
		return this.details;
	}
}
