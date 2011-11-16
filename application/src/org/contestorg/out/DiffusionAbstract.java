package org.contestorg.out;


import java.util.ArrayList;

import org.contestorg.interfaces.IOperation;
import org.contestorg.interfaces.IStartStopListener;

public abstract class DiffusionAbstract
{
	// Listeners
	private ArrayList<IStartStopListener> listeners = new ArrayList<IStartStopListener>();
	
	// Démarrer la diffusion
	public abstract IOperation getStartOperation ();
	
	// Arreter la diffusion
	public abstract IOperation getStopOperation ();
	
	// Fire des listeners
	protected void fireStart() {
		for(IStartStopListener listener : this.listeners) {
			listener.start();
		}
	}
	protected void fireStop() {
		for(IStartStopListener listener : this.listeners) {
			listener.stop();
		}
	}
	
	// Ajouter/Supprimer des listeners
	public void addListener(IStartStopListener listener) {
		this.listeners.add(listener);
	}
	public void removeListener(IStartStopListener listener) {
		this.listeners.remove(listener);
	}
	
	
}
