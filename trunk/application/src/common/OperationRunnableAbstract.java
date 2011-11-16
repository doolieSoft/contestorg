package common;

import interfaces.IOperationListener;

import java.util.ArrayList;

public abstract class OperationRunnableAbstract implements Runnable
{
	// Listeners
	private ArrayList<IOperationListener> listeners = new ArrayList<IOperationListener>();
	
	// Arret demandé ?
	protected boolean arret = false;
	
	// Demander l'arret
	public void stop() {
		this.arret = true;
	}
	
	// Ajouter/Supprimer des listeners
	public void addListener(IOperationListener listener) {
		this.listeners.add(listener);
	}
	public void removeListener(IOperationListener listener) {
		this.listeners.remove(listener);
	}
	
	// Fire des listeners
	protected void fireMessage(String message) {
		for(IOperationListener listener : this.listeners) {
			listener.progressionMessage(message);
		}
	}
	protected void fireAvancement(double progression) {
		for(IOperationListener listener : this.listeners) {
			listener.progressionAvancement(progression);
		}
	}
	private void fireReussite() {
		for(IOperationListener listener : this.listeners) {
			listener.operationReussite();
			listener.progressionFin();
		}
	}
	private void fireEchec() {
		for(IOperationListener listener : this.listeners) {
			listener.operationEchec();
			listener.progressionFin();
		}
	}
	private void fireArret() {
		for(IOperationListener listener : this.listeners) {
			listener.operationArret();
			listener.progressionFin();
		}
	}
	
	// Signaler l'arret du test (en prenant soin de nettoyer l'opération)
	protected void arret () {
		// Nettoyer le test
		this.clean();
		
		// Prévenir de l'arret
		this.fireArret();
	}
	
	// Signaler la réussite du test (en prenant soin de nettoyer l'opération)
	protected void reussite (String message, boolean clean) {
		// Nettoyer le test
		if(clean) {
			this.clean();
		}
		
		// Envoyer un message au listener
		this.fireMessage(message);
		
		// Prévenir la réussite
		this.fireReussite(); 
	}
	protected void reussite (String message) {
		this.reussite(message, true);
	}
	
	// Signaler l'échec du test (en prenant soin de nettoyer l'opération)
	protected void echec (String message) {
		// Nettoyer le test
		this.clean();
		
		// Envoyer un message au listener
		this.fireMessage(message);
		
		// Prévenir le l'echec
		this.fireEchec();
	}
	
	// Nettoyer l'opération en vue de son arret
	protected abstract void clean();
	
}
