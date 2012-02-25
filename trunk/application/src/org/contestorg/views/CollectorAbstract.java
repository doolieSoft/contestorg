package org.contestorg.views;

import java.awt.Window;

import org.contestorg.interfaces.ICollector;

/**
 * Classe d'aide à l'implémentation de ICollector
 * @param <T> classe des objets à collecter
 */
public abstract class CollectorAbstract<T> implements ICollector<T>
{
	/** Fenêtre associée */
	protected Window window;
	
	/**
	 * Définir la fenêtre associée
	 * @param window fenêtre associée
	 */
	public void setWindow(Window window) {
		this.window = window;
	}
	
	/**
	 * Fermer le collector
	 */
	public void close() {
		if(this.window != null) {
			// Masquer et détruire la fenêtre
			this.window.setVisible(false);
			this.window = null;
		}
	}

	/**
	 * @see ICollector#cancel()
	 */
	@Override
	public void cancel () {
		// Fermer le collector
		this.close();
	}
}
