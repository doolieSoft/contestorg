package org.contestorg.common;

/**
 * Classe des exceptions soulevées par ContestOrg
 */
public abstract class ContestOrgException extends Exception
{
	/** Numéro de version de la classe */
	private static final long serialVersionUID = 1;

	/**
	 * Constructeur
	 * @param message message de l'exception
	 */
	public ContestOrgException(String message) {
		super(message);
	}

}
