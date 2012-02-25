package org.contestorg.common;


import java.util.ArrayList;

import org.contestorg.interfaces.IMoody;
import org.contestorg.interfaces.IMoodyListener;

/**
 * Classe d'aide à l'implémentation de l'interface IMoody
 */
public abstract class MoodyAbstract implements IMoody
{

	/** Etats */
	private int states = 0;
	
	/** Liste des listeners */
	private ArrayList<IMoodyListener> listeners = new ArrayList<IMoodyListener>();

	// Implémentation de IMoody
	@Override
	public boolean is (int state) {
		return (this.states & state) == state;
	}
	@Override
	public void addListener(IMoodyListener listener) {
		this.listeners.add(listener);
	}
	@Override
	public void removeListener(IMoodyListener listener) {
		this.listeners.remove(listener);
	}
	
	/**
	 * Ajouter un état
	 * @param state état
	 */
	protected void addState(int state) {
		this.states = this.states | state;
		this.fire();
	}
	
	/**
	 * Ajouter plusieurs états
	 * @param states états
	 */
	protected void addStates(int... states) {
		for(int state : states) {
			this.states = this.states | state;
		}
		this.fire();
	}
	
	/**
	 * Retirer un état
	 * @param state état
	 */
	protected void removeState(int state) {
		this.states = this.is(state) ? this.states ^ state : this.states;
		this.fire();
	}
	
	/**
	 * Retirer plusieurs états
	 * @param states états
	 */
	protected void removeStates(int... states) {
		for(int state : states) {
			this.states = this.is(state) ? this.states ^ state : this.states;
		}
		this.fire();
	}
	
	/**
	 * Réinitialiser le statut
	 */
	protected void initStates() {
		this.states = 0;
		this.fire();
	}
	
	/**
	 * Réinitialiser le statut
	 * @param states états initiaux
	 */
	protected void initStates(int... states) {
		this.initStates();
		this.addStates(states);
	}
	
	/**
	 * Informer les listeners que l'état à changé
	 */
	protected void fire() {
		for(IMoodyListener listener : this.listeners) {
			listener.moodyChanged(this);
		}
	}

}
