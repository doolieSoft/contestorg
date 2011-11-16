package common;

import interfaces.IMoody;
import interfaces.IMoodyListener;

import java.util.ArrayList;

public abstract class MoodyAbstract implements IMoody
{

	// Etats
	private int states = 0;
	
	// Listeners
	private ArrayList<IMoodyListener> listeners = new ArrayList<IMoodyListener>();

	// Implémentation de is
	@Override
	public boolean is (int state) {
		return (this.states & state) == state;
	}
	
	// Ajouter un état
	protected void addState(int state) {
		this.states = this.states | state;
		this.fire();
	}
	
	// Ajouter plusieurs états
	protected void addStates(int... states) {
		for(int state : states) {
			this.states = this.states | state;
		}
		this.fire();
	}
	
	// Retirer un état
	protected void removeState(int state) {
		this.states = this.is(state) ? this.states ^ state : this.states;
		this.fire();
	}
	
	// Retirer plusieurs états
	protected void removeStates(int... states) {
		for(int state : states) {
			this.states = this.is(state) ? this.states ^ state : this.states;
		}
		this.fire();
	}
	
	// Réinitialiser le statut
	protected void initStates() {
		this.states = 0;
		this.fire();
	}
	protected void initStates(int... states) {
		this.initStates();
		this.addStates(states);
	}
	
	// Fire des listeners
	protected void fire() {
		for(IMoodyListener listener : this.listeners) {
			listener.moodyChanged(this);
		}
	}
	
	// Ajouter/Enlever des listeners
	public void addListener(IMoodyListener listener) {
		this.listeners.add(listener);
	}
	public void removeListener(IMoodyListener listener) {
		this.listeners.remove(listener);
	}

}
