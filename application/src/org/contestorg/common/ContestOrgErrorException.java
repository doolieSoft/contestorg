package org.contestorg.common;


/**
 * Classe des exceptions soulevées par ContestOrg concernant des erreurs
 */
public class ContestOrgErrorException extends ContestOrgException
{
	
	/** Numéro de version de la classe */
	private static final long serialVersionUID = 1;

	/**
	 * Constructeur
	 * @param message message
	 */
	public ContestOrgErrorException(String message) {
		// Appeller le constructeur du parent
		super(message);
	}
	
}
