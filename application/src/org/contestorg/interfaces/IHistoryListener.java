package org.contestorg.interfaces;

import org.contestorg.events.Action;

/**
 * Interface à implémenter si une classe souhaite être tenue au courant des opérations effectuées sur l'historique
 */
public interface IHistoryListener
{

	/**
	 * Recevoir une notification comme quoi une nouvelle action a été ajoutée dans l'historique
	 * @param action action ajoutée dans l'historique
	 */
	void historyActionPushed (Action action);
	
	/**
	 * Recevoir une notification comme quoi une nouvelle action a été retirée de l'historique
	 * @param action action retirée de l'historique
	 */
	void historyActionPoped (Action action);
	
	/**
	 * Recevoir une notification comme quoi l'historique a été réinitialisé
	 */
	void historyInit();
	
}
