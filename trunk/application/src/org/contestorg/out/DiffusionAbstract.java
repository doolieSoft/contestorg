package org.contestorg.out;

import java.util.ArrayList;

import org.contestorg.interfaces.IOperation;
import org.contestorg.interfaces.IStartStopListener;

/**
 * Diffusion abstraite
 */
public abstract class DiffusionAbstract
{
	/** Liste des listeners */
	private ArrayList<IStartStopListener> listeners = new ArrayList<IStartStopListener>();
	
	/**
	 * Démarrer la diffusion
	 * @return opération de démarrage de la diffusion
	 */
	public abstract IOperation getStartOperation ();
	
	/**
	 * Arreter la diffusion
	 * @return opération d'arrêt de la diffusion
	 */
	public abstract IOperation getStopOperation ();
	
	// Signalements
	
	/**
	 * Signaler le démarrage de la diffusion
	 */
	protected void fireStart() {
		for(IStartStopListener listener : this.listeners) {
			listener.start();
		}
	}
	
	/**
	 * Signaler l'arrêt de la diffusion
	 */
	protected void fireStop() {
		for(IStartStopListener listener : this.listeners) {
			listener.stop();
		}
	}
	
	// Ajouter/Supprimer des listeners
	
	/**
	 * Ajouter un listener
	 * @param listener listener
	 */
	public void addListener(IStartStopListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Supprimer un listener
	 * @param listener
	 */
	public void removeListener(IStartStopListener listener) {
		this.listeners.remove(listener);
	}
	
	
}
