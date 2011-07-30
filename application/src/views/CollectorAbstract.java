package views;

import interfaces.ICollector;

import java.awt.Window;

public abstract class CollectorAbstract<T> implements ICollector<T>
{
	// Fenetre associée
	protected Window window;
	
	// Définir la fenetre associée
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
			// Masquer et détruire la fenetre
			this.window.setVisible(false);
			this.window = null;
		}
	}
}
