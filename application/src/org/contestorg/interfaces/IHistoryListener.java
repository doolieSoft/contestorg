package org.contestorg.interfaces;

import org.contestorg.events.Action;

/**
 * Interface à implémenter si une classe souhaite etre tenue au courant des opérations effectuées sur l'historique
 */
public interface IHistoryListener
{

	// Recoit une notification comme quoi une nouvelle action a été ajoutée dans l'historique
	void historyActionPushed (Action action);
	
	// Recoit une notification comme quoi une nouvelle action a été ajoutée dans l'historique
	void historyActionPoped (Action action);
	
	// Recoit une notification comme quoi l'historique a été initialisé
	void historyInit();
	
}
