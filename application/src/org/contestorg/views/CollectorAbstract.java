package org.contestorg.views;


import java.awt.Window;

import org.contestorg.interfaces.ICollector;

public abstract class CollectorAbstract<T> implements ICollector<T>
{
	// fenêtre associée
	protected Window window;
	
	// Définir la fenêtre associée
	public void setWindow(Window window) {
		this.window = window;
	}

	// Implémentation de cancel
	@Override
	public void cancel () {
		// Fermer le collector
		this.close();
	}
	
	// Fermer le collector
	public void close() {
		if(this.window != null) {
			// Masquer et détruire la fenêtre
			this.window.setVisible(false);
			this.window = null;
		}
	}
}
