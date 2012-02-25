package org.contestorg.out;

import org.contestorg.common.ContestOrgException;

/**
 * Classe des exceptions soulevées par ContestOrg concernant des erreurs sur le package out
 */
public class ContestOrgOutException extends ContestOrgException
{
	
	/** Numéro de version de la classe */
	private static final long serialVersionUID = 1;

	/**
	 * Constructeur
	 * @param message message
	 */
	public ContestOrgOutException(String message) {
		// Appeller le constructeur du parent 
		super(message);
	}
	
}
