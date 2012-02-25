package org.contestorg.events;

import org.contestorg.common.ContestOrgException;

/**
 * Classe des exceptions soulevées par l'historique
 */
public class ContestOrgEventException extends ContestOrgException
{
	/** Numéro de version de la classe */
	private static final long serialVersionUID = 1;
	
	/**
	 * Constructeur
	 * @param message message de l'exception
	 */
	public ContestOrgEventException(String message) {
		// Appeller le constructeur du parent
		super(message);
	}
	
}
