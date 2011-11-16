package org.contestorg.common;

import org.contestorg.interfaces.IOperation;
import org.contestorg.interfaces.IOperationListener;

public abstract class OperationAbstract implements IOperation
{
	// Runnable associée à l'opération
	private OperationRunnableAbstract runnable;
	
	// Implémentation de IOperation
	@Override
	public void operationStart () {
		new Thread(this.getRunnableSafe()).start();
	}
	@Override
	public void operationStop () {
		this.getRunnableSafe().stop();
	}
	@Override
	public void addListener (IOperationListener listener) {
		this.getRunnableSafe().addListener(listener);
	}
	@Override
	public void removeListener (IOperationListener listener) {
		this.getRunnableSafe().removeListener(listener);
	}
	
	// Méthode à implémenter
	public abstract OperationRunnableAbstract getRunnable();
	
	// Méthode pour récupérer le runnable de manière unitaire
	public OperationRunnableAbstract getRunnableSafe() {
		if(this.runnable == null) {
			this.runnable = this.getRunnable();
		}
		return this.runnable;
	}
	
}
