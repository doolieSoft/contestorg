package org.contestorg.models;

import org.contestorg.common.ContestOrgException;

/**
 * Classe des exceptions soulevées par ContestOrg concernant des erreurs sur le package models
 */
public class ContestOrgModelException extends ContestOrgException
{
	
	/** Numéro de version de la classe */
	private static final long serialVersionUID = 1;

	/**
	 * Constructeur
	 * @param message message
	 */
	public ContestOrgModelException(String message) {
		// Appeller le constructeur du parent
		super(message);
	}
	
}
