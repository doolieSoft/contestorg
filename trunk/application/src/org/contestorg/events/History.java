package org.contestorg.events;


import java.util.ArrayList;
import java.util.Stack;

import org.contestorg.interfaces.IEventListener;
import org.contestorg.interfaces.IHistoryListener;
import org.contestorg.log.Log;

/**
 * Historique
 */
public class History implements IEventListener
{
	/** Action en cours */
	private String action;
	
	/** Pile des actions */
	private Stack<Action> actions = new Stack<Action>();
	
	/** Liste des listeners */
	private ArrayList<IHistoryListener> listeners = new ArrayList<IHistoryListener>();
	
	/**
	 * Initialiser l'historique
	 */
	public void init() {
		// Vérifier si aucune action n'est en cours
		if(this.action != null) {
			Log.getLogger().error("L'action \""+this.action+"\" n'a pas été fermée.");
		} else {
			// Initialiser les données
			this.action = null;
			this.actions = new Stack<Action>();
			
			// Fire des listeners
			for(IHistoryListener listener : new ArrayList<IHistoryListener>(this.listeners)) {
				listener.historyInit();
			}
		}
	}
	
	/**
	 * Démarrer la capture d'évenements
	 * @param action nom de l'action
	 */
	public void start(String action) {
		// Vérifier si l'action n'a pas déjà été démarrée
		if(this.action != null) {
			Log.getLogger().error("L'action \""+this.action+"\" n'a pas été fermée.");
		} else {
			// Démarrer la nouvelle action
			this.action = action;
			this.actions.push(new Action(action));
			
			// Log
			Log.getLogger().info("Action démarrée : \""+this.action+"\".");
		}
	}
	
	/**
	 * Fermer l'action en cours
	 */
	public void close() {
		// Vérifier si l'action n'est pas déjà fermée
		if(this.action == null) {
			Log.getLogger().error("L'action en cours est déjà fermée.");
		} else {
			// Log
			Log.getLogger().info("Action fermée : \""+this.action+"\".");
			
			// Fermer l'action en cours
			this.action = null;
			
			// Fire des listeners
			this.fire(this.actions.lastElement());
		}
	}

	// Implémentation de event
	@Override
	public void event (Event event) {
		// Vérifier si une action a été ouverte
		if(this.action == null) {
			Log.getLogger().error("Aucune action n'est démarrée.");
		} else {
			// Ajouter l'évenement à l'action en cours
			this.actions.lastElement().push(event);
		}
	}
	
	/**
	 * Récupérer la pile des actions
	 * @return pile des actions
	 */
	public Stack<Action> getActions() {
		return this.actions;
	}
	

	// Listeners

	/**
	 * Fire des listeners
	 * @param action action à envoyer aux listeners
	 */
	private void fire(Action action) {
		for(IHistoryListener listener : new ArrayList<IHistoryListener>(this.listeners)) {
			listener.historyActionPushed(action);
		}
	}
	
	/**
	 * Ajouter un listener
	 * @param listener listener à ajouter
	 */
	public void addListener(IHistoryListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Supprimer un listener
	 * @param listener listener à supprimer
	 */
	public void removeListener(IHistoryListener listener) {
		this.listeners.remove(listener);
	}
	
}
